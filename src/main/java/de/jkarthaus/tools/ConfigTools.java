package de.jkarthaus.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jkarthaus.model.SortSource;

public class ConfigTools {

	private static final Logger logger = LogManager.getLogger(ConfigTools.class);

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static List<SortSource> getSources(Properties config) {
		ArrayList<SortSource> result = new ArrayList<SortSource>();
		for (int i = 1; i < 5; i++) {
			String source = config.getProperty("source." + i);

			if (source != null && source.trim().length() > 0) {
				File testFile = new File(source);
				if (testFile.exists() && testFile.isDirectory()) {
					SortSource sortSource = new SortSource();
					sortSource.setSourceDir(testFile);
					sortSource
							.setPreString(StringUtils.defaultString(config.getProperty("source." + i + ".prefix", "")));
					result.add(sortSource);
				} else {
					logger.error("Source " + i + " :" + testFile.getAbsolutePath() + " not found - or no Directory");
				}
			} else {
				logger.info("Ignoring Source " + i);
			}
		}

		return result;
	}

}
