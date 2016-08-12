package de.jkarthaus;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jkarthaus.model.ImageSortResult;
import de.jkarthaus.worker.ImageSortWorker;
import de.jkarthaus.worker.PushOverWorker;

public class NasImageSorter {

	private static final Logger logger = LogManager.getLogger(NasImageSorter.class);

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.out.println("give me a config File");
			logger.error("No config given.");
			System.exit(100);
		}
		File configFile = new File(args[0]);
		if (!configFile.exists() || !configFile.canRead()) {
			System.out.println("Config File:" + args[0] + " not found or cannot read.");
			logger.error("Config File:" + args[0] + " not found or cannot read.");
			System.exit(100);

		}
		Properties config = new Properties();
		try {
			config.load(new FileInputStream(configFile));
		} catch (Exception e) {
			logger.error(e);
			System.exit(100);
		}
		ImageSortWorker worker = new ImageSortWorker(config);
		ImageSortResult imageSortResult = worker.sortImages();
		if (imageSortResult.getCountImages() > 0) {
			PushOverWorker pushOverWorker = new PushOverWorker(config);
			if (!pushOverWorker.sendToPushOver(imageSortResult)) {
				logger.error("Send to Pushover failed");
			}
		} else {
			logger.info("No Files found for Import");
		}
	}
}
