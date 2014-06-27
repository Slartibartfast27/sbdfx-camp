/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.dataaccess.mongo;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;

import javax.ejb.Stateless;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.GroupCommand;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.zuehlke.sbdfx.dataaccess.api.BoundingBox;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.dataaccess.api.ListResult;
import com.zuehlke.sbdfx.dataaccess.api.FindByAreaRequest;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.Country;
import com.zuehlke.sbdfx.domain.FeatureClass;
import com.zuehlke.sbdfx.domain.FeatureCode;

/**
 * 
 * @author cbu
 */
@Stateless
public class MongoCitiesDao implements CitiesDao {

    private static final String COLLECTION_NAME_CITIES = "cities";
    private final Map<String, FeatureClass> featureClassCache = Maps.newLinkedHashMap();
    private final Map<String, FeatureCode> featureCodeCache = Maps.newLinkedHashMap();
    private final Map<String, Country> countryCache = Maps.newLinkedHashMap();

    private final MongoClient mongoClient;
    private final DB db;
    private final DBCollection cities;

    public MongoCitiesDao() {
        try {
            mongoClient = new MongoClient("localhost");
            db = mongoClient.getDB("mydb");
            cities = getOrCreateCitiesCollection();
        } catch (final UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private DBCollection getOrCreateCitiesCollection() {
        final DBCollection result = db.getCollectionFromString(COLLECTION_NAME_CITIES);
        return result;
    }

    @Override
    public City findZuerich() {
        final City zuerich = new City();
        zuerich.setCountry(Country.SWITZERLAND);
        zuerich.setName("Zuerich");
        zuerich.setPopulation(383_707);
        return zuerich;
    }

    @Override
    public FeatureClass getOrCreateFeatureClass(final String id) {
        FeatureClass result = featureClassCache.get(id);
        if (result == null) {
            result = new FeatureClass(id);
            featureClassCache.put(id, result);
        }
        return result;
    }

    @Override
    public FeatureCode getOrCreateFeatureCode(final String id) {
        FeatureCode result = featureCodeCache.get(id);
        if (result == null) {
            result = new FeatureCode(id);
            featureCodeCache.put(id, result);
        }
        return result;
    }

    @Override
    public Country getOrCreateCountry(final String isoCode) {
        Country result = countryCache.get(isoCode);
        if (result == null) {
            result = new Country(isoCode);
            countryCache.put(isoCode, result);
        }
        return result;
    }

    @Override
    public void persistCity(final City city) {
        final DBObject cityDbOject = createDbObject(city);
        cities.insert(cityDbOject);
    }

    private DBObject createDbObject(final City city) {
        final Gson gson = new Gson();
        final String json = gson.toJson(city);
        final Object result = JSON.parse(json);
        return (DBObject) result;
    }

    @Override
    public void clear() {
        db.dropDatabase();
    }

    @Override
    public City findByGeoNameId(final int geonameid) {
        final DBObject query = BasicDBObjectBuilder.start().add("geonameid", geonameid).get();
        final DBObject found = cities.findOne(query);
        return toCity(found);
    }

    private City toCity(final DBObject found) {
        if (found == null) {
            return null;
        }
        final Gson gson = new Gson();
        final String json = JSON.serialize(found);
        final City city = gson.fromJson(json, City.class);
        return city;
    }

    @Override
    public ListResult<City> findByArea(final FindByAreaRequest req) {
        
        final MongoQueryBuilder b = new MongoQueryBuilder();
        
        b.numberBetween(req.getLatitudeMin(), "latitude", req.getLatitudeMax());
        b.numberBetween(req.getLongitudeMin(), "longitude", req.getLongitudeMax());
        
        final DBCursor cursor = cities.find(b.create()).limit(req.getMaxResults());
        final ListResult<City> result = ListResult.create( cursor.count() );
        for (final DBObject dbObject : cursor) {
            result.add(toCity(dbObject));
        }
        return result;
    }

    @Override
    public void printStatistics() {
        final CommandResult stats = db.getStats();
        System.out.println(stats);
    }

    @Override
    public BoundingBox getGlobalBoundingBox() {
        
        // example taken from: http://stackoverflow.com/questions/4762980/getting-the-highest-value-of-a-column-in-mongodb
        
        DBObject keys = null;
        DBObject condition = null;
        DBObject initial = new BasicDBObject();
        initial.put("maxLongitude", Double.MIN_VALUE);
        initial.put("maxLatitude", Double.MIN_VALUE);
        initial.put("minLongitude", Double.MAX_VALUE);
        initial.put("minLatitude", Double.MAX_VALUE);
        
        String reduce = "function(obj,prev) { "
                + "if ( prev.maxLongitude < obj.longitude ) prev.maxLongitude = obj.longitude; "
                + "if ( prev.minLongitude > obj.longitude ) prev.minLongitude = obj.longitude; "
                + "if ( prev.maxLatitude < obj.latitude ) prev.maxLatitude = obj.latitude; "
                + "if ( prev.minLatitude > obj.latitude ) prev.minLatitude = obj.latitude; "
                + "}";
        String finalize = null;
        
        GroupCommand cmd = new GroupCommand(cities, keys, condition, initial, reduce, finalize);
        DBObject resultObjectList = cities.group(cmd);
        
        BoundingBox result = new BoundingBox();
        DBObject resultObject = (DBObject) resultObjectList.get("0");
        result.setLongitudeMax( (double) resultObject.get("maxLongitude"));
        result.setLongitudeMin( (double) resultObject.get("minLongitude"));
        result.setLatitudeMax( (double) resultObject.get("maxLatitude"));
        result.setLatitudeMin( (double) resultObject.get("minLatitude"));
        return result;
    }

}
