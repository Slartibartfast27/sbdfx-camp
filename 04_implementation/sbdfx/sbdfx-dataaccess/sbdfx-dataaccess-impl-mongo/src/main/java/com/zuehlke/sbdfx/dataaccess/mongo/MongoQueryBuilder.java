package com.zuehlke.sbdfx.dataaccess.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoQueryBuilder {

    private static final String LESS_THAN = "$lt";
    private static final String GREATER_THAN_OR_EQUAL = "$gte";

    private final BasicDBObject rootElement = new BasicDBObject();
    private final BasicDBObject currentElement = rootElement;

    public DBObject create() {
        return rootElement;
    }

    public void numberBetween(final Number from, final String attributeName, final Number to) {
        final BasicDBObject critera = createBetween(from, to);
        addCriteria(attributeName, critera);
    }

    private void addCriteria(final String attributeName, final BasicDBObject critera) {
        if (critera == null) {
            return;
        }
        currentElement.put(attributeName, critera);
    }

    private BasicDBObject createBetween(final Number from, final Number to) {
        if (from == null && to == null) {
            return null;
        }
        final BasicDBObject result = new BasicDBObject();
        if (from != null) {
            result.put(GREATER_THAN_OR_EQUAL, from);
        }
        if (to != null) {
            result.put(LESS_THAN, to);
        }
        return result;
    }

}
