package de.jkarthaus;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.jkarthaus.tools.ConfigTools;
import de.jkarthaus.worker.ImageSortWorker;

@SpringBootApplication
public class MediaSorterApplication implements CommandLineRunner {

	private final static Logger logger = LoggerFactory.getLogger(MediaSorterApplication.class);

	Date startTime = new Date();
	
	@Autowired
	ConfigTools configTools;

	@Autowired
	ImageSortWorker imageSortWorker;

	@Override
	public void run(String... args) throws Exception {
		if (args == null || args.length != 1) {
			logger.error("I want a config File at Parameter 1");
			throw new ExitException();
		}
		File configFile = new File(args[0]);
		if (!configFile.exists() || !configFile.canRead()) {
			logger.error("Config File:" + args[0] + " not found or cannot read.");
			throw new ExitException();
		}
		logger.info("--------------------------------------------------------------------------");

		configTools.loadFromFile(configFile);
		imageSortWorker.sortImages();
		Date endTime = new Date();
		long diff = endTime.getTime() - startTime.getTime();
		logger.info("Working Time : " + (diff / 1000 % 60) + " Seconds.");
		logger.info("--------------------------------------------------------------------------");

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(MediaSorterApplication.class, args);
	}

}
