package com.zuehlke.sbdfx.utils;

import java.text.MessageFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Terminates the JVM after a given Timeout.
 */
public class TimeoutWatchdog {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeoutWatchdog.class);
    protected static final long SHUTDOWN_HOOKS_TIMEOUT_MILLIS = 60 * 1000;
    private static final double MILLISECONDS_PER_MINUTE = 60 * 1000;
    private static boolean watching = false;

    private final long timeoutMilliseconds;

    private TimeoutWatchdog(final long timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    public static void watchJVM(final long timeoutMilliseconds) {
        if (watching) {
            return;
        }
        final TimeoutWatchdog watchdog = new TimeoutWatchdog(timeoutMilliseconds);
        watchdog.start();
        watching = true;
    }

    private void start() {
        startDaemonThread("Watchdog-SystemExit", new Runnable() { //$NON-NLS-1$
            @Override
            public void run() {
                try {
                    Thread.sleep(timeoutMilliseconds);
                    LOGGER.warn("Timout exceeded. Exiting."); //$NON-NLS-1$
                    System.exit(-1);
                } catch (final InterruptedException e) {
                    final String msg = "Watchdog interruption detected. Not expected."; //$NON-NLS-1$
                    LOGGER.error(msg, e);
                }
            };
        });

        startDaemonThread("Watchdog-RuntimeHalt", new Runnable() { //$NON-NLS-1$
            @Override
            public void run() {
                try {
                    Thread.sleep(timeoutMilliseconds + SHUTDOWN_HOOKS_TIMEOUT_MILLIS);
                    Runtime.getRuntime().halt(-2);
                } catch (final InterruptedException e) {
                    final String msg = "Watchdog interruption detected. Not expected."; //$NON-NLS-1$
                    LOGGER.error(msg, e);
                }
            };
        });
        final Date latestExitTime = new Date(System.currentTimeMillis() + timeoutMilliseconds);
        final double timeoutMinutes = timeoutMilliseconds / MILLISECONDS_PER_MINUTE;
        LOGGER.info(MessageFormat
                .format("Timeout Watchdog started. Application will terminate latest in {0,number,#.###} minutes at {1,date,EEE, d MMM yyyy HH:mm:ss Z}.", //$NON-NLS-1$
                        timeoutMinutes, latestExitTime));
    }

    private void startDaemonThread(final String threadName, final Runnable runnable) {
        final Thread thread = new Thread(runnable, threadName);
        thread.setDaemon(true);
        thread.start();
    }

}
