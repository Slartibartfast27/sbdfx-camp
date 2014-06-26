package com.zuehlke.sbdfx.dataaccess.mongo;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoUtilsFromMin {

//    public static void main(final String[] args) {
//
//        // DB-Verbindung aufbauen
//        final MongoClient dbConnection = MongoDbUtils.connect();
//        final DBCollection dbCollection = MongoDbUtils.createDbAndDbCollection(dbConnection, "RequestLogDB",
//                "RequestLogEntries");
//
//        final boolean connected = (dbConnection != null && dbCollection != null);
//        if (!connected) {
//
//            System.err.println("can't connect to mongo db");
//            return;
//        }
//
//        printDbObjects(performQuery(dbCollection, attribute("method", oneOf("loadStructureSets", "checkPermission"))));
//
//        // Nach Request ID
//        printDbObjects(performQuery(dbCollection, attribute("reqid", is(7398250715052238391L))));
//        printDbObjects(performQuery(dbCollection,
//                attribute("reqid", oneOf(7398250715052238391L, -1418390196945674304L))));
//
//        //
//        printDbObjects(performQuery(dbCollection, attribute("user", is("abc@SNB.CH")))); // Executing
//                                                                                         // query
//                                                                                         // {
//                                                                                         // "user":
//                                                                                         // {
//                                                                                         // $not:
//                                                                                         // "xin@SNB.CH"
//                                                                                         // }
//                                                                                         // }
//        printDbObjects(performQuery(dbCollection, attribute("user", notIs("xin@SNB.CH"))));
//
//        // RANGE-FILTER
//        // ************
//        printDbObjects(performQuery(dbCollection, attribute("tid", between(40, 50))));
//        printDbObjects(performQuery(dbCollection, attribute("method", between("echo", "getZ"))));
//
//        // Nach Type
//        printDbObjects(performQuery(dbCollection, attribute("type", is("REQ_STARTED"))));
//        printDbObjects(performQuery(dbCollection, attribute("type", oneOf("REQ_FINISHED", "A_REQ_FINISHED"))));
//
//        // Nach Datum + Uhrzeit
//        printDbObjects(performQuery(dbCollection, attribute("time", day("2013-05-28"))));
//        printDbObjects(performQuery(dbCollection, attribute("time", day("2013-05-31"))));
//        printDbObjects(performQuery(dbCollection, attribute("time", timeContains("12:40"))));
//        printDbObjects(performQuery(dbCollection, attribute("time", timeContains("09:49"))));
//
//        printDbObjects(performQuery(dbCollection, attribute("time", dayAndTime("2013-05-31", "18:09"))));
//
//        printDbObjects(performQuery(dbCollection, attribute("time", daysBetween("2013-05-29", "2013-05-31"))));
//        printDbObjects(performQuery(
//                dbCollection,
//                and(attribute("method", oneOf("echo")),
//                        and(attribute("type", is("REQ_FINISHED")),
//                                attribute("time", daysBetween("2013-05-29", "2013-06-01"))))));
//    }

    private static String day(final String dateValue) {

        return attribute("time", likeIgnorecase(dateValue));
    }

    private static String timeContains(final String timeValue) {

        return attribute("time", likeIgnorecase(timeValue));
    }

    private static String dayAndTime(final String dateValue, final String timeValue) {

        return likeIgnorecase(dateValue + "T" + timeValue);
    }

    public static String daysBetween(final String fromIncluded, final String toIncluded) {

        return "{ $gte: \"" + fromIncluded + "\", $lte: \"" + toIncluded + "\" }";
    }

    public static String like(final String searchValue) {

        return "{ $regex: '" + searchValue + "' }";
    }

    public static String likeIgnorecase(final String searchValue) {

        return "{ $regex: '" + searchValue + "', $options: 'i' }";
    }

    public static String attribute(final String attributeName, final String condition) {

        return "{ \"" + attributeName + "\": " + condition + " }";
    }

    // Arbeitet NICHT, wie man erwartet auf Ausdrücken, sondern muss vor die
    // einzelne Bedinung gesetzt werden
    public static String not(final String condition) {

        return "{ $not: " + condition + " }";
    }

    public static String between(final long fromIncluded, final long toIncluded) {

        return "{ $gte: " + fromIncluded + ", $lte: " + toIncluded + " }";
    }

    public static String between(final String fromIncluded, final String toIncluded) {

        return "{ $gte: \"" + fromIncluded + "\", $lte: \"" + toIncluded + "\" }";
    }

    public static String is(final long value) {

        return Long.toString(value);
    }

    public static String is(final String value) {

        return "\"" + value + "\"";
    }

    public static String notIs(final long value) {

        return "{ $ne: " + value + " }";
    }

    public static String notIs(final String value) {

        return "{ $ne: \"" + value + "\" }";
    }

    public static String oneOf(final long... searchValues) {

        String result = "";
        for (int i = 0; i < searchValues.length; i++) {

            final long searchValue = searchValues[i];

            result += Long.toString(searchValue);
            if (i < searchValues.length - 1) {
                result += ", ";
            }
        }

        return "{ $in : [ " + result + " ] }";
    }

    public static String oneOf(final String... searchValues) {

        String result = "";
        for (int i = 0; i < searchValues.length; i++) {

            final String searchValue = searchValues[i];

            result += "\"" + searchValue + "\"";
            if (i < searchValues.length - 1) {
                result += ", ";
            }
        }

        return "{ $in : [ " + result + " ] }";
    }

    public static String or(final String... conditions) {

        String result = "";
        for (int i = 0; i < conditions.length; i++) {

            final String condition = conditions[i];

            result += condition;
            if (i < conditions.length - 1) {
                result += ", ";
            }
        }

        return "{ $or: [ " + result + " ] }";
    }

    public static String and(final String... conditions) {

        String result = "";
        for (int i = 0; i < conditions.length; i++) {

            final String condition = conditions[i];

            result += condition;
            if (i < conditions.length - 1) {
                result += ", ";
            }
        }

        return "{ $and: [ " + result + " ] }";
    }

//    private static List<DBObject> performQuery(final DBCollection dbCollection) {
//        final int limit = 50;
//        final DBCursor dbCursor = dbCollection.find();
//        final List<DBObject> logEntries = MongoDbUtils.retrieveNextNObjects(dbCollection, dbCursor, limit);
//        return logEntries;
//    }
//
//    private static List<DBObject> performQuery(final DBCollection dbCollection, final String queryAsJson) {
//        System.out.println("Executing query " + queryAsJson);
//
//        final BasicDBObject query = (BasicDBObject) JSON.parse(queryAsJson);
//
//        final int limit = 20;
//        final DBCursor dbCursor = dbCollection.find(query);
//        final List<DBObject> logEntries = MongoDbUtils.retrieveNextNObjects(dbCollection, dbCursor, limit);
//        return logEntries;
//    }
//
//    private static List<DBObject> performQueryUnlimited(final DBCollection dbCollection, final String queryAsJson) {
//        System.out.println("Executing query " + queryAsJson);
//
//        final BasicDBObject query = (BasicDBObject) JSON.parse(queryAsJson);
//
//        final DBCursor dbCursor = dbCollection.find(query);
//        final List<DBObject> logEntries = MongoDbUtils.retrieveNextNObjects(dbCollection, dbCursor, -1);
//        return logEntries;
//    }

    private static void printDbObjects(final List<DBObject> dbObjects) {
        for (final DBObject dbObject : dbObjects) {

            System.out.println(dbObject);
        }
    }

    public static DBCursor executeQuery(final String queryAsJson, final DBCollection dbCollection) {
        System.out.println("Executing query " + queryAsJson);

        final BasicDBObject query = (BasicDBObject) JSON.parse(queryAsJson);
        final DBCursor cursor = dbCollection.find(query);

        return cursor;
    }
}
