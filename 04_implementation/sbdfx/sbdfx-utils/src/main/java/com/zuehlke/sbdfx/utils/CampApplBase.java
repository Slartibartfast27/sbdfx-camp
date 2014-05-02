/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zuehlke.sbdfx.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author cbu
 */
public abstract class CampApplBase {

    private final static Logger LOGGER = LoggerFactory.getLogger(CampApplBase.class);
    
    protected static final Options OPTIONS = new Options();
    
    private static int exitCode = -1;
    
    @SuppressWarnings("static-access")
    private static final Option O_CONFIG_FILE = OptionBuilder.withLongOpt("config") //$NON-NLS-1$
            .withDescription("config file for string options (keys are long option names). " //$NON-NLS-1$
                    + "Values of config file can be overridden as they " //$NON-NLS-1$
                    + "have less priority than command line args. (default: config.txt").hasArg(true) //$NON-NLS-1$
            .create("c"); //$NON-NLS-1$

    
    @SuppressWarnings("static-access")
    private static final Option O_TIMEOUT_MINUTES = OptionBuilder
            .withLongOpt("timeout-minutes") //$NON-NLS-1$
            .withDescription(
                    "timeout for the whole application in minutes. If exceeded, the System will terminate unconditionally (default: 30 minutes).") //$NON-NLS-1$
            .hasArg(true).create("t");
    
    
    private static CommandLine commandLine;
    private static Exception applicationException;
    private static int defaultTimeoutMinutes = 30;
    static final Properties propertiesFromConfigFile = new Properties();
    
    private final static double MILLISECONDS_PER_MINUTE = 1000*60;

    static {
            LoggingInitializer.assertLoggingInitialized();
            OPTIONS.addOption(O_CONFIG_FILE);
            OPTIONS.addOption(O_TIMEOUT_MINUTES);
    }
    
    public static void setDefaultTimeoutMinutes(final int defaultTimeoutMinutes) {
        CampApplBase.defaultTimeoutMinutes = defaultTimeoutMinutes;
    }

    private static void startWatchdog() {
        final double timeoutMinutes = Double.parseDouble(getOptionValue(O_TIMEOUT_MINUTES,
                String.valueOf(defaultTimeoutMinutes)));
        TimeoutWatchdog.watchJVM((long) (timeoutMinutes * MILLISECONDS_PER_MINUTE));
    }

    protected boolean parseAndApplyArgs(final String[] args) {
        boolean argumentsParsedSuccessfully = false;

        try {
            setExitCode(-1);
            LOGGER.debug("Given Options: {}", Joiner.on(" ").join(args)); //$NON-NLS-1$ //$NON-NLS-2$
            final CommandLineParser parser = new PosixParser();
            commandLine = parser.parse(OPTIONS, args);
            parseConfigFile();
            applyOptions();
            startWatchdog();
            argumentsParsedSuccessfully = true;
        } catch (final Exception e) {
            LOGGER.error("parseAndApplyArgs failed", e);
            LOGGER.error("\n\nInvalid Command Line: " + e.getMessage() + "\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
            printUsage();
        }
        return argumentsParsedSuccessfully;
    }

    /**
     * @return the {@link Exception} thrown by the application, typically
     *         causing an exit code of -1
     */
    public static Exception getApplicationException() {
        return applicationException;
    }

    protected final void performActions() {
        try {
            setExitCode(-1);
            doPerformActions();
            setExitCode(0);
            LOGGER.info("Finished Successfully."); //$NON-NLS-1$
        } catch (final Exception e) {
            applicationException = e;
            LOGGER.error("Failed to perform actions", e); //$NON-NLS-1$
        }
    }

    private static void parseConfigFile() throws FileNotFoundException, IOException {
        final String configFileLocation = getOptionValue(O_CONFIG_FILE, "config.txt").trim(); //$NON-NLS-1$
        final File configFile = new File(configFileLocation);
        if (configFile.exists()) {
            LOGGER.info("Reading options from config file " + configFile.getAbsolutePath()); //$NON-NLS-1$
            FileReader reader = null;
            try {
                reader = new FileReader(configFile);
                propertiesFromConfigFile.load(reader);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    protected abstract void doPerformActions() throws Exception;

    protected static List<String> getOptionValues(final Option option) {
        final String[] optionValues = commandLine.getOptionValues(option.getLongOpt());
        if (optionValues == null) {
            return Collections.emptyList();
        }
        final List<String> result = Lists.newArrayList();
        for (final String optionValue : optionValues) {
            if (StringUtils.isNotBlank(optionValue)) {
                result.add(optionValue);
            }
        }
        return result;
    }

    protected void applyOptions() {
        applyApplicationSpecificOptions();
    }

    protected void applyApplicationSpecificOptions() {
        // intentionally left empty. To be implemented by subclasses for
        // additional options.
    }

    protected static String getOptionValue(final Option option) {
        return getOptionValue(option, null);
    }

    protected static boolean hasOption(final Option option) {
        return commandLine.hasOption(option.getLongOpt());
    }

    protected static String getOptionValue(final Option option, final String defaultValue) {

        String result = commandLine.getOptionValue(option.getLongOpt(), "").trim(); //$NON-NLS-1$
        if (result.length() > 0) {
            return result;
        }

        final String longOpt = option.getLongOpt();
        result = propertiesFromConfigFile.getProperty(longOpt, "").trim(); //$NON-NLS-1$
        if (result.length() > 0) {
            return result;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new IllegalArgumentException("Missing value for required option " + longOpt); //$NON-NLS-1$
    }

    protected static boolean isOptionPresent(final Option option) {
        return commandLine.hasOption(option.getLongOpt());
    }

    protected static Long getOptionValueAsLong(final Option option, final Long defaultValue) {
        final String optionValueString = getOptionValue(option, defaultValue != null ? String.valueOf(defaultValue)
                : ""); //$NON-NLS-1$
        if (StringUtils.isNotBlank(optionValueString)) {
            try {
                return Long.parseLong(optionValueString);
            } catch (final Exception e) {
                throw new IllegalArgumentException(MessageFormat.format("Failed to parse option {0} as integer: {1}", //$NON-NLS-1$
                        option.getLongOpt(), optionValueString));
            }
        } else {
            return defaultValue;
        }
    }

    protected static File getOptionValueAsFile(final Option option, final String defaultValue, final boolean forceExists) {
        final String optionValueString = getOptionValue(option, defaultValue != null ? String.valueOf(defaultValue)
                : ""); //$NON-NLS-1$
        File result = null;
        if (StringUtils.isNotBlank(optionValueString)) {
            result = new File(optionValueString);
        } else if (StringUtils.isNotBlank(defaultValue)) {
            result = new File(defaultValue);
        } else {
            throw new IllegalArgumentException("Missing option or default value: " + option.getOpt());
        }
        if (forceExists && !result.exists()) {
            throw new IllegalArgumentException("File does not exist for option : " + option.getOpt() + " - "
                    + result.getAbsolutePath());
        }
        return result;
    }

    private static void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.printHelp("\n", OPTIONS); //$NON-NLS-1$
    }

    public static void setExitCode(final int exitCode) {
        CampApplBase.exitCode = exitCode;
    }

    public static int getExitCode() {
        return exitCode;
    }

    protected Option getSelectedOption(final Set<Option> exclusiveOptions) {
        Option selectedMode = null;
        for (final Option o : exclusiveOptions) {
            if (isOptionPresent(o)) {
                if (selectedMode == null) {
                    selectedMode = o;
                } else {
                    final Function<Option, String> optionToLongArgs = new Function<Option, String>() {
                        @Override
                        public String apply(final Option from) {
                            return from.getLongOpt();
                        }
                    };
                    throw new IllegalArgumentException("Only one of the following options allowed: "
                            + Joiner.on(", ").join(
                                    Lists.transform(Lists.newArrayList(exclusiveOptions), optionToLongArgs)));
                }
            }
        }
        return selectedMode;
    }

}
