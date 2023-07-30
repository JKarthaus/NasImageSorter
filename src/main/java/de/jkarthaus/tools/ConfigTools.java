package de.jkarthaus.tools;

import de.jkarthaus.model.SortSource;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Singleton
public class ConfigTools {

	private final static Logger logger = LoggerFactory.getLogger(ConfigTools.class);

	Properties config = new Properties();

	public void loadFromFile(File configFile) throws IOException {
		config.load(new FileInputStream(configFile));
	}

	/**
	 * 
	 * @return
	 */
	public Properties getConfig() {
		return config;
	}

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
