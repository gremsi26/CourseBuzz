package coursebuzzserver;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.WebClient;

public class Server {
	private DatabaseOperations dbop;
	Log log = new Log();

	public Server() {
		dbop = new DatabaseOperations(log, "ipaddress", "databasename",
				"databasename", "databasepassword");
	}

	public void checkAllAlerts() {
		ResultSet rs = dbop
				.executeQuery("SELECT * FROM ALERTS NATURAL JOIN USERS");
		ArrayList<String[]> alertsToDelete = new ArrayList<String[]>();
		try {
			WebClient webClient = OscarScraper.startWebClient();
			while (rs.next()) {
				String id = rs.getString("id");
				String registrationid = rs.getString("registrationid");
				String crn = rs.getString("crn");
				System.out.println(id + "\n" + registrationid + "\n" + crn);
				String[] array = { id, crn };

				ArrayList<String[]> info = OscarScraper.getRemainingSeats(crn,
						webClient);
				int waitlistActual = Integer.parseInt(info.get(1)[1]);
				if (waitlistActual == 0) {
					int remainingSeats = Integer.parseInt(info.get(0)[2]);
					if (remainingSeats > 0) {
						SendMessageToDevice.sendMessage(registrationid, crn
								+ "!!!Remaining Seats for " + crn + ": "
								+ remainingSeats);
						alertsToDelete.add(array);

					}
				} else {
					SendMessageToDevice.sendMessage(registrationid,
							"crn!!!Waitlist exists for this CRN: " + crn
									+ ". Deleting alert request.");
					alertsToDelete.add(array);

				}

			}
			rs.close();

			for (int i = 0; i < alertsToDelete.size(); i++) {
				dbop.execute("DELETE FROM ALERTS WHERE id='"
						+ alertsToDelete.get(i)[0] + "' AND crn='"
						+ alertsToDelete.get(i)[1] + "'");
			}
			OscarScraper.stopWebClient(webClient);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Server server = new Server();
		server.checkAllAlerts();

	}

}
