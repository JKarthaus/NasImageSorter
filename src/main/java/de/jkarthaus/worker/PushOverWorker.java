package de.jkarthaus.worker;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jkarthaus.model.ImageSortResult;
import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverException;
import net.pushover.client.PushoverMessage;
import net.pushover.client.PushoverRestClient;

public class PushOverWorker {

	private static final Logger logger = LogManager.getLogger(ImageSortWorker.class);
	private PushoverClient client = new PushoverRestClient();
	private Properties config;

	public PushOverWorker(Properties config) {
		this.config = config;

	}

	/**
	 * 
	 * @param imageSortResult
	 * @return
	 */
	public boolean sendToPushOver(ImageSortResult imageSortResult) {
		logger.info("Send ImageSortResult To Pushover Client");
		boolean result = true;
		String message = imageSortResult.getResultText();
		try {
			client.pushMessage(PushoverMessage.builderWithApiToken(config.getProperty("pushover.app.api.token"))
					.setUserId(config.getProperty("pushover.user.id.token")).setMessage(message).build());
		} catch (PushoverException e) {
			logger.error(e);
			result = true;
		}
		return result;
	}

}
