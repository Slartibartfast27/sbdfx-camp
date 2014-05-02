package com.zuehlke.sbdfx.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.zuehlke.sbdfx.dataaccess.api.CitiesDao;
import com.zuehlke.sbdfx.dataaccess.impl1.DefaultCitiesDao;
import com.zuehlke.sbdfx.domain.City;
import com.zuehlke.sbdfx.domain.Country;
import com.zuehlke.sbdfx.domain.FeatureClass;
import com.zuehlke.sbdfx.domain.FeatureCode;

public class SourceFileImporter {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceFileImporter.class);

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final CitiesDao dao = new DefaultCitiesDao();

    public void importFile(final File srcFile) throws Exception {
        final ZipFile zipFile = new ZipFile(srcFile);
        InputStream inputStream = null;
        try {
            final String textFileName = FilenameUtils.getBaseName(srcFile.getName()) + ".txt";
            final ZipEntry entry = zipFile.getEntry(textFileName);
            inputStream = zipFile.getInputStream(entry);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                putLineInDatabase(line);
            }
        } finally {
            zipFile.close();
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * <pre>
     *         geonameid         : integer id of record in geonames database
     *         name              : name of geographical point (utf8) varchar(200)
     *         asciiname         : name of geographical point in plain ascii characters, varchar(200)
     *         alternatenames    : alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(8000)
     *         latitude          : latitude in decimal degrees (wgs84)
     *         longitude         : longitude in decimal degrees (wgs84)
     *         feature class     : see http://www.geonames.org/export/codes.html, char(1)
     *         feature code      : see http://www.geonames.org/export/codes.html, varchar(10)
     *         country code      : ISO-3166 2-letter country code, 2 characters
     *         cc2               : alternate country codes, comma separated, ISO-3166 2-letter country code, 60 characters
     *         admin1 code       : fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20)
     *         admin2 code       : code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) 
     *         admin3 code       : code for third level administrative division, varchar(20)
     *         admin4 code       : code for fourth level administrative division, varchar(20)
     *         population        : bigint (8 byte int) 
     *         elevation         : in meters, integer
     *         dem               : digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat.
     *         timezone          : the timezone id (see file timeZone.txt) varchar(40)
     *         modification date : date of last modification in yyyy-MM-dd format
     * </pre>
     * 
     * @throws ParseException
     */
    private void putLineInDatabase(final String line) throws ParseException {
        LOGGER.info("Processing Line {}", line);
        final String[] split = line.split("\\t");
        final City city = new City();
        int pos = 0;
        city.setGeonameid(Integer.parseInt(split[pos++]));
        city.setName(split[pos++]);
        city.setAsciiName(split[pos++]);
        city.setAlternatenames(parseList(split[pos++]));
        city.setLatitude(Double.parseDouble(split[pos++]));
        city.setLongitude(Double.parseDouble(split[pos++]));
        city.setFeatureClass(parseFeatureClass(split[pos++]));
        city.setFeatureCode(parseFeatureCode(split[pos++]));
        city.setCountry(parseCountry(split[pos++]));
        city.setAlternateCountryCodes(parseAlternateCountries(split[pos++]));
        city.setAdmin1Code(split[pos++]);
        city.setAdmin2Code(split[pos++]);
        city.setAdmin3Code(split[pos++]);
        city.setAdmin4Code(split[pos++]);
        city.setPopulation(Long.parseLong(split[pos++]));
        city.setElevation(parseInt(split[pos++]));
        city.setDem(split[pos++]);
        city.setTimezone(split[pos++]);
        city.setModificationDate(parseDate(split[pos++]));

    }

    private Integer parseInt(final String string) {
        if (string.length() == 0) {
            return null;
        }
        return Integer.parseInt(string);
    }

    private List<Country> parseAlternateCountries(final String string) {
        if (string.length() == 0) {
            return Collections.emptyList();
        }
        if (string.length() == 2) {
            return Collections.singletonList(parseCountry(string));
        }
        List<Country> result = Lists.newArrayList();
        final String[] split = string.split(",");
        for (String isoCode : split) {
            result.add(parseCountry(isoCode));
        }
        
        return result;
    }

    private Calendar parseDate(final String string) throws ParseException {
        final Date parsed = DATE_FORMAT.parse(string);
        final GregorianCalendar result = new GregorianCalendar();
        result.setTime(parsed);
        return result;
    }

    private Country parseCountry(final String isoCode) {
        return dao.getOrCreateCountry(isoCode);
    }

    private FeatureCode parseFeatureCode(final String string) {
        return dao.getOrCreateFeatureCode(string);
    }

    private FeatureClass parseFeatureClass(final String string) {
        return dao.getOrCreateFeatureClass(string);
    }

    private List<String> parseList(final String string) {
        if (string.length() == 0) {
            return Collections.emptyList();
        }
        final String[] split = string.split(",");
        return Arrays.asList(split);
    }

}
