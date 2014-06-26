package com.zuehlke.sbdfx.dataaccess.mongo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongoQueryBuilder {

    private final JsonObject rootElement = new JsonObject();
    private final JsonElement currentElement = rootElement;

    public DBObject create() {
        final Gson gson = new Gson();
        final String json = gson.toJson(rootElement);
        return (DBObject) JSON.parse(json);
    }

    public void numberBetween(final Number from, final String attributeName, final Number to) {
        final JsonObject critera = createBetween(from, to);
        addCriteria(attributeName, critera);
    }

    private void addCriteria(final String attributeName, final JsonObject critera) {
        if (critera == null) {
            return;
        }
        ((JsonObject) currentElement).add(attributeName, critera);
    }

    private JsonObject createBetween(final Number from, final Number to) {
        if (from == null && to == null) {
            return null;
        }
        final JsonObject result = new JsonObject();
        if (from != null) {
            result.addProperty("$gte", from);
        }
        if (to != null) {
            result.addProperty("$lt", to);
        }
        return result;
    }

}
