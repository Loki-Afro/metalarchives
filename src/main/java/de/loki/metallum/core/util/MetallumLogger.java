package de.loki.metallum.core.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class MetallumLogger {

	static {
		final Logger logger = Logger.getRootLogger();
		logger.setLevel(Level.DEBUG);
		final PatternLayout layout = new PatternLayout("%d{dd.MM.yyyy HH:mm:ss,SSS} %-5p [%c] %m%n");
		final ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		consoleAppender.setLayout(layout);
		logger.addAppender(consoleAppender);
	}

	public static void setLogLevel(final Level newLevel) {
		Logger.getRootLogger().setLevel(newLevel);
	}

}
