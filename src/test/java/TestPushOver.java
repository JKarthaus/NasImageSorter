import net.pushover.client.MessagePriority;
import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverException;
import net.pushover.client.PushoverMessage;
import net.pushover.client.PushoverRestClient;
import net.pushover.client.Status;

public class TestPushOver {

	public static void main(String[] args) throws PushoverException {

		PushoverClient client = new PushoverRestClient();

		Status result = client.pushMessage(PushoverMessage.builderWithApiToken("aii7mhdizknpmu8kw8x7sbvwijcsnt")
				.setUserId("uESQjtZEDFW91MPL1FdQVS1t4473rc")
				.setMessage("<b>testing!</b>").setPriority(MessagePriority.HIGH) // HIGH|NORMAL|QUIET
				.setTitle("title").setUrl("https://github.com/sps/pushover4j").setTitleForURL("pushover4j github repo")
				.setSound("magic").build());
	}

}
