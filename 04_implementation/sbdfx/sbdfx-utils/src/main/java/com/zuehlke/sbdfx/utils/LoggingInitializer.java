package com.zuehlke.sbdfx.utils;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author cbu
 */
@Startup
@Singleton
@LocalBean
public class LoggingInitializer {

    @PostConstruct
    public void init() {
        assertLoggingInitialized();
    }

    private static boolean initialized = false;

    public static synchronized void assertLoggingInitialized() {
        if (!initialized) {
            initializeLogging();
            initialized = true;
        }
    }

    private static void initializeLogging() {

        final String specificLogFile = System.getProperty("logback.config"); //$NON-NLS-1$
        if (StringUtils.isNotBlank(specificLogFile)) {
            final File f = new File(specificLogFile);
            if (f.exists()) {
                configureWithJoran(f);
                System.out.println("Using logback configuration from " + f.getAbsolutePath()); //$NON-NLS-1$
                return;
            }
        }

        final String homeDir = System.getProperty("user.home"); //$NON-NLS-1$
        final File logConfigFileInHomedir = new File(homeDir, "logback.xml"); //$NON-NLS-1$
        if (logConfigFileInHomedir.exists()) {
            configureWithJoran(logConfigFileInHomedir);
            System.out.println("Using logback configuration from " + logConfigFileInHomedir.getAbsolutePath()); //$NON-NLS-1$
        } else {
            System.out
                    .println("Logging configuration not changed. To personalize logging, create " + logConfigFileInHomedir.getAbsolutePath()); //$NON-NLS-1$
        }
    }

    private static void configureWithJoran(final File logConfigFileInHomedir) {
        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            // the context was probably already configured by default
            // configuration rules
            lc.reset();
            configurator.doConfigure(logConfigFileInHomedir);
        } catch (final JoranException je) {
            je.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    }

}
