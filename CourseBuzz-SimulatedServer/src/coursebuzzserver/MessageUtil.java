package coursebuzzserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MessageUtil {
	private final static String AUTH = "authentication";

	private static final String UPDATE_CLIENT_AUTH = "Update-Client-Auth";

	public static final String PARAM_REGISTRATION_ID = "registration_id";

	public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";

	public static final String PARAM_COLLAPSE_KEY = "collapse_key";

	private static final String UTF8 = "UTF-8";

	public static int sendMessage(String auth_token, String registrationId,
			String message) throws IOException {

		StringBuilder postDataBuilder = new StringBuilder();
		postDataBuilder.append(PARAM_REGISTRATION_ID).append("=")
				.append(registrationId);
		postDataBuilder.append("&").append(PARAM_COLLAPSE_KEY).append("=")
				.append("0");
		postDataBuilder.append("&").append("data.payload").append("=")
				.append(URLEncoder.encode(message, UTF8));

		byte[] postData = postDataBuilder.toString().getBytes(UTF8);

		// Hit the dm URL.
		System.out.println("1");
		URL url = new URL("https://android.clients.google.com/c2dm/send");
		// HttpURLConnection
		// .setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		conn.setRequestProperty("Content-Length",
				Integer.toString(postData.length));
		conn.setRequestProperty("Authorization", "GoogleLogin auth="
				+ auth_token);

		OutputStream out = conn.getOutputStream();
		out.write(postData);
		out.close();

		int responseCode = conn.getResponseCode();

		System.out.println(responseCode);
		String responseLine = new BufferedReader(new InputStreamReader(
				conn.getInputStream())).readLine();

		System.out.println("Got " + responseCode
				+ " response from Google C2DM endpoint.");

		System.out.println("Response Line:" + responseLine);

		if (responseLine == null || responseLine.equals("")) {
			throw new IOException(
					"Got empty response from Google C2DM endpoint.");
		}

		String[] responseParts = responseLine.split("=", 2);
		if (responseParts.length != 2) {
			System.out.println("Invalid message from google: " + responseCode
					+ " " + responseLine);
			throw new IOException("Invalid response from Google "
					+ responseCode + " " + responseLine);
		}

		if (responseParts[0].equals("id")) {
			System.out.println("Successfully sent data message to device: "
					+ responseLine);
		}

		else if (responseParts[0].equals("Error")) {
			String err = responseParts[1];
			System.out.println("Got error response from Google C2DM endpoint: "
					+ err);
			// No retry.
			// TODO(costin): show a nicer error to the user.
			throw new IOException("Server error: " + err);
		} else {
			// 500 or unparseable response - server error, needs to retry
			System.out.println("Invalid response from google " + responseLine
					+ " " + responseCode);
		}

		return responseCode;
	}

	/*
	 * private static class CustomizedHostnameVerifier implements
	 * HostnameVerifier { public boolean verify(String hostname, SSLSession
	 * session) { return true; } }
	 */
}
