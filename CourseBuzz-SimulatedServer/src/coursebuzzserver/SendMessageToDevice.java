package coursebuzzserver;

import java.io.IOException;

public class SendMessageToDevice {

	public static void main(String[] args) throws IOException {
		// "Message to your device." is the message we will send to the Android
		// app
		int responseCode = MessageUtil.sendMessage(
				ServerConfiguration.AUTHENTICATION_TOKEN,
				ServerConfiguration.REGISTRATION_ID, "I'm Watching You...");
		System.out.println(responseCode);
	}

	public static void sendMessage(String regId, String msg) throws IOException {
		int responseCode = MessageUtil.sendMessage(
				ServerConfiguration.AUTHENTICATION_TOKEN, regId, msg);
		System.out.println(responseCode);
	}
}
