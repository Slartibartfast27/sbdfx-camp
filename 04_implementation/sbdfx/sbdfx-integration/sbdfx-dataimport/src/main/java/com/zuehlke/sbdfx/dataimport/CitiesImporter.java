/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.sbdfx.dataimport;

import java.io.File;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zuehlke.sbdfx.utils.CampApplBase;

/**
 * 
 * @author cbu
 */
public class CitiesImporter extends CampApplBase {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(CampApplBase.class);

	@SuppressWarnings("static-access")
	private static final Option O_FETCH_SOURCE_FILES = OptionBuilder
			.withLongOpt("fetch") //$NON-NLS-1$
			.withDescription(
					"fetches files from the cities database into build directory")
			.create("f"); //$NON-NLS-1$

	{
		OPTIONS.addOption(O_FETCH_SOURCE_FILES);
	}

	public static void main(final String[] args) {
		final CitiesImporter app = new CitiesImporter();
		try {
			app.mainWithoutSystemExit(args);
		} catch (Throwable ex) {
			LOGGER.error("Application failed by Exception.", ex);
		} finally {
			System.exit(app.getExitCode());
		}
	}

	@Override
	protected void doPerformActions() throws Exception {
		if (isOptionPresent(O_FETCH_SOURCE_FILES)) {
			SourceFilesFetcher fetcher = new SourceFilesFetcher(new File(
					"build/geonames-dump"));
			fetcher.fetchAll();
		}
	}
}
