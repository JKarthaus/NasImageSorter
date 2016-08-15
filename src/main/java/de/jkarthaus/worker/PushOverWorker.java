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
	private boolean sendPushover;
	private String apiToken;
	private String userToken;

	/**
	 * 
	 * @param config
	 */
	public PushOverWorker(Properties config) {
		this.apiToken = config.getProperty("pushover.app.api.token", "");
		this.userToken = config.getProperty("pushover.user.id.token", "");
		if (apiToken.length() > 0 && userToken.length() > 0) {
			sendPushover = true;
		} else {
			sendPushover = false;
		}

	}

	/**
	 * 
	 * @param imageSortResult
	 * @return
	 */
	public boolean sendToPushOver(ImageSortResult imageSortResult) {
		if (!sendPushover) {
			logger.info("Send to Pushover is not configured !");
			return true;
		}
		logger.info("Send ImageSortResult To Pushover Client");
		boolean result = true;
		String message = imageSortResult.toString();
		try {
			client.pushMessage(
					PushoverMessage.builderWithApiToken(apiToken).setUserId(userToken).setMessage(message).build());
		} catch (PushoverException e) {
			logger.error(e);
			result = true;
		}
		return result;
	}

}
