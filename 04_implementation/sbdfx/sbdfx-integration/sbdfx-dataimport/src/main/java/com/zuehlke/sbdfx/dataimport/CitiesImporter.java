/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.dataimport;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.zuehlke.sbdfx.utils.CampApplBase;

/**
 * 
 * @author cbu
 */
public class CitiesImporter extends CampApplBase {

    private static final File TARGET_FOLDER = new File("build/geonames-dump");

    private final static Logger LOGGER = LoggerFactory.getLogger(CampApplBase.class);

    @SuppressWarnings("static-access")
    private static final Option O_FETCH_SOURCE_FILES = OptionBuilder.withLongOpt("fetch") //$NON-NLS-1$
            .withDescription("fetches files from the cities database into build directory").create("f"); //$NON-NLS-1$

    @SuppressWarnings("static-access")
    private static final Option O_IMPORT = OptionBuilder.withLongOpt("import").hasArgs()
            .withDescription("import given files, e.g. CH, DE, cities15000, ...").create("i"); //$NON-NLS-1$

    {
        OPTIONS.addOption(O_FETCH_SOURCE_FILES);
        OPTIONS.addOption(O_IMPORT);
    }

    public static void main(final String[] args) {
        final CitiesImporter app = new CitiesImporter();
        try {
            app.mainWithoutSystemExit(args);
        } catch (final Throwable ex) {
            LOGGER.error("Application failed by Exception.", ex);
        } finally {
            System.exit(app.getExitCode());
        }
    }

    @Override
    protected void doPerformActions() throws Exception {
        if (isOptionPresent(O_FETCH_SOURCE_FILES)) {
            final SourceFilesFetcher fetcher = new SourceFilesFetcher(TARGET_FOLDER);
            fetcher.fetchAll();
        }
        if (isOptionPresent(O_IMPORT)) {
            final SourceFileImporter importer = new SourceFileImporter();
            final List<String> filesToImport = getOptionValues(O_IMPORT);
            for (final String srcFileSpec : filesToImport) {
                final File srcFile = findSrcFile(srcFileSpec);
                importer.importFile(srcFile);
            }
        }
    }

    private File findSrcFile(final String srcFileSpec) {
        File result = new File(TARGET_FOLDER, srcFileSpec);
        if (result.exists()) {
            return result;
        }
        result = new File(TARGET_FOLDER, srcFileSpec + ".zip");
        Preconditions.checkArgument(result.exists(), "File mus exists for %s in %s. Is File downloaded?", srcFileSpec,
                TARGET_FOLDER);
        return result;
    }
}
