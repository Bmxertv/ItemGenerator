package de.bmxertv.itemgenerator.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleUtil {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void info(String message) {
        LOGGER.log(Level.INFO, ColorUtils.colorize(message));
    }

    public static void error(String message) {
        LOGGER.log(Level.ERROR, ColorUtils.colorize(message));
    }

    public static void debug(String message) {
        LOGGER.log(Level.DEBUG, ColorUtils.colorize(message));
    }

}
