package com.zuehlke.sbdfx.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceFileImporter {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceFileImporter.class);

    public void importFile(final File srcFile) throws ZipException, IOException {
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
     */
    private void putLineInDatabase(final String line) {
        LOGGER.info("Processing Line {}", line);

    }

}
