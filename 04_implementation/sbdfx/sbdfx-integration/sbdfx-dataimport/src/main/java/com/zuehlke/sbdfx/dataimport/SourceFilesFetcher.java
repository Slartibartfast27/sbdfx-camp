/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.dataimport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.collect.Sets;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * 
 * @author cbu
 */
class SourceFilesFetcher {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceFilesFetcher.class);

    private final static String SOURCE_FOLDER = "http://download.geonames.org/export/dump/";

    private final static Set<String> EXTENSIONS_TO_DOWNLOAD = Sets.newTreeSet(Arrays.asList("zip", "txt"));

    private final File targetFolder;
    private final WebConversation wc = new WebConversation();

    SourceFilesFetcher(final File targetFolder) {
        this.targetFolder = targetFolder;
    }

    void fetchAll() throws IOException, SAXException {
        HttpUnitOptions.setScriptingEnabled(false);
        final WebRequest req = new GetMethodWebRequest(SOURCE_FOLDER);
        final WebResponse resp = wc.getResponse(req);
        final WebLink[] links = resp.getLinks();
        targetFolder.mkdirs();
        for (final WebLink webLink : links) {
            downloadLink(webLink);
        }
    }

    private void downloadLink(final WebLink webLink) throws IOException {
        final String urlString = webLink.getURLString();
        OutputStream output = null;
        InputStream input = null;
        try {
            if (isLinkToDownload(urlString)) {
                final File targetFile = new File(targetFolder, urlString);
                if (targetFile.exists()) {
                    FileUtils.forceDelete(targetFile);
                }
                final File tempFile = new File(targetFolder, "currentDownload");
                if (tempFile.exists()) {
                    FileUtils.forceDelete(tempFile);
                }
                LOGGER.info("Downloading {} to {}", urlString, targetFile.getAbsolutePath());
                URL srcUrl = new URL(new URL(SOURCE_FOLDER), urlString);
                input = srcUrl.openStream();
                output = new FileOutputStream(tempFile);
                IOUtils.copyLarge(input, output);
                output.close();
                FileUtils.moveFile(tempFile, targetFile);
            } else {
                LOGGER.info("NOT Downloading {}", urlString);
            }
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
    }

    private boolean isLinkToDownload(final String urlString) {
        if (urlString.contains("/")) {
            return false;
        }
        for (final String extension : EXTENSIONS_TO_DOWNLOAD) {
            if (urlString.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
