package com.zuehlke.sbdfx.dataaccess.mongo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.zuehlke.sbdfx.dataaccess.api.BoundingBox;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.dataaccess.api.FindByAreaRequest;
import com.zuehlke.sbdfx.dataaccess.api.ListResult;
import com.zuehlke.sbdfx.domain.City;

public class MongoCitiesDaoTest {

    private static final double DEFAULT_PRECISION = 1e-10;
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
        createSampleCitites();

        final FindByAreaRequest req = new FindByAreaRequest();

        testFindByArea(req, 100);
        req.setMaxResults(50);
        ListResult<City> result = testFindByArea(req, 50);
        assertEquals(100, result.getTotalCount());

        req.setLatitudeMin(5.0);
        req.setLatitudeMax(5.0);
        testFindByArea(req, 0);

        req.setLatitudeMax(6.0);
        result = testFindByArea(req, 10);
        assertEquals(10, result.getTotalCount());
        
        req.setLatitudeMin(5.0);
        req.setLatitudeMax(7.0);
        req.setLongitudeMin(22.0);
        req.setLongitudeMax(25.0);
        testFindByArea(req, 6);

        req.setLongitudeMax(22.0);
        testFindByArea(req, 0);

    }

    private void createSampleCitites() {
        for (int latitude = 0; latitude < 10; latitude++) {
            for (int longitude = 20; longitude < 30; longitude++) {
                final int geonameid = latitude + 1000 * longitude;
                final City city = createCity(geonameid);
                city.setLatitude(latitude);
                city.setLongitude(longitude);
                dao.persistCity(city);
            }
        }
    }

    private ListResult<City> testFindByArea(final FindByAreaRequest req, final int expectedNumberOfResults) {
        final ListResult<City> found = dao.findByArea(req);
        assertEquals(expectedNumberOfResults, found.getFetchedCount());
        return found;
    }

    @Test
    public void printDbStatistics() {
        dao.printStatistics();
    }

    @Test
    public void testGlobalBoundingBox() {
        dao.clear();
        createSampleCitites();
        final BoundingBox globalBoundingBox = dao.getGlobalBoundingBox();
        assertEquals(20, globalBoundingBox.getLongitudeMin(), DEFAULT_PRECISION);
        assertEquals(29, globalBoundingBox.getLongitudeMax(), DEFAULT_PRECISION);
        assertEquals(0, globalBoundingBox.getLatitudeMin(), DEFAULT_PRECISION);
        assertEquals(9, globalBoundingBox.getLatitudeMax(), DEFAULT_PRECISION);
    }
}
