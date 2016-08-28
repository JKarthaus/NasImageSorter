package de.jkarthaus.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.jkarthaus.model.ImageSortResult;
import de.jkarthaus.tools.ConfigTools;
import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverException;
import net.pushover.client.PushoverMessage;
import net.pushover.client.PushoverRestClient;

@Component
public class PushOverWorker {

	private final static Logger logger = LoggerFactory.getLogger(PushOverWorker.class);
	private PushoverClient client = new PushoverRestClient();

	@Autowired
	ConfigTools configTools;

	/**
	 * 
	 * @param imageSortResult
	 * @return
	 */
	public boolean sendToPushOver(ImageSortResult imageSortResult) {
		String apiToken = configTools.getConfig().getProperty("pushover.app.api.token", "");
		String userToken = configTools.getConfig().getProperty("pushover.user.id.token", "");

		if (apiToken.length() == 0 || userToken.length() == 0) {
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
			logger.error(e.getMessage());
			result = true;
		}
		return result;
	}

}
