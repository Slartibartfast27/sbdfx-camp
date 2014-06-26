package com.zuehlke.sbdfx.dataaccess.mongo;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.dataaccess.api.FindByAreaRequest;
import com.zuehlke.sbdfx.domain.City;

public class MongoCitiesDaoTest {

    private final CitiesDao dao = new MongoCitiesDao();

    @Test
    public void testPersistCityAndFindByGeonameId() {
        dao.clear();

        createAndPersistCity(41);
        final City city = createAndPersistCity(42);
        createAndPersistCity(43);

        final City found = dao.findByGeoNameId(42);
        assertEquals(city, found);
    }

    private City createAndPersistCity(final int geonameid) {
        final City city = createCity(geonameid);

        dao.persistCity(city);
        return city;
    }

    private City createCity(final int geonameid) {
        final City city = new City();
        city.setName("SomeCity-" + geonameid);
        city.setGeonameid(geonameid);
        return city;
    }

    @Test
    public void testFindByArea() {
        dao.clear();

        for (int latitude = 0; latitude < 10; latitude++) {
            for (int longitude = 20; longitude < 30; longitude++) {
                final int geonameid = latitude + 1000 * longitude;
                City city = createCity(geonameid);
                city.setLatitude(latitude);
                city.setLongitude(longitude);
                dao.persistCity(city);
            }
        }
        
        FindByAreaRequest req = new FindByAreaRequest();
        
        testFindByArea(req, 100);
        
        req.setLatitudeMin(5);
        req.setLatitudeMax(5);
        testFindByArea(req, 0);

        req.setLatitudeMax(6);
        testFindByArea(req, 10);
        
        req.setLatitudeMin(5);
        req.setLatitudeMax(7);
        req.setLongitudeMin(22);
        req.setLongitudeMax(25);
        testFindByArea(req, 6);

        req.setLongitudeMax(22);
        testFindByArea(req, 0);

    }

    private void testFindByArea(FindByAreaRequest req, int expectedNumberOfResults) {
        Collection<City> found = dao.findByArea( req );
        assertEquals(expectedNumberOfResults, found.size());
    }

    @Test
    public void printDbStatistics() {
        dao.printStatistics();
    }
}
