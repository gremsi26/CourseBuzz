package coursebuzzserver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

public class OscarScraper {
	static String searchLink = "";

	final static String SPRINGURL = Calendar.getInstance().get(Calendar.YEAR)
			+ "08";
	final static String SUMMERURL = Calendar.getInstance().get(Calendar.YEAR)
			+ "05";
	final static String FALLURL = Calendar.getInstance().get(Calendar.YEAR)
			+ "02";
	private static String currentURL = SPRINGURL;

	public OscarScraper() {

	}

	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		String url = "https://oscar.gatech.edu/pls/bprod/bwckschd.p_disp_detail_sched?term_in="
				+ currentURL + "&crn_in=81478";

		WebClient webClient = OscarScraper.startWebClient();
		getRemainingSeats("81478", webClient);
	}

	public static ArrayList<String[]> getRemainingSeats(String crn,
			WebClient webClient) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		ArrayList<String[]> info = new ArrayList<String[]>();
		String url = "https://oscar.gatech.edu/pls/bprod/bwckschd.p_disp_detail_sched?term_in="
				+ currentURL + "&crn_in=" + crn;
		System.out.println(url);
		HtmlPage page = getPage(url, webClient);
		List<?> table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[2]/td[1]"); // /.getHtmlElementById("datadisplaytable");
		// System.out.println(table.get(0).toString());
		String capacity = ((HtmlTableDataCell) table.get(0)).getTextContent();

		table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[2]/td[2]"); // /.getHtmlElementById("datadisplaytable");
		String actual = ((HtmlTableDataCell) table.get(0)).getTextContent();

		table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[2]/td[3]"); // /.getHtmlElementById("datadisplaytable");
		String remaining = ((HtmlTableDataCell) table.get(0)).getTextContent();

		String[] array = { capacity, actual, remaining };

		info.add(array);
		System.out.println("SEATS\nCapacity: " + capacity + "\nActual: "
				+ actual + "\nRemaining: " + remaining);

		//
		table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[3]/td[1]"); // /.getHtmlElementById("datadisplaytable");
		// System.out.println(table.get(0).toString());
		String waitlistCapacity = ((HtmlTableDataCell) table.get(0))
				.getTextContent();

		table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[3]/td[2]"); // /.getHtmlElementById("datadisplaytable");
		String waitlistActual = ((HtmlTableDataCell) table.get(0))
				.getTextContent();

		table = page
				.getByXPath("//table[@summary='This layout table is used to present the seating numbers.']//tr[3]/td[3]"); // /.getHtmlElementById("datadisplaytable");
		String waitlistRemaining = ((HtmlTableDataCell) table.get(0))
				.getTextContent();

		System.out.println("\nWAITLIST SEATS\nCapacity: " + waitlistCapacity
				+ "\nActual: " + waitlistActual + "\nRemaining: "
				+ waitlistRemaining);
		String[] array2 = { waitlistCapacity, waitlistActual, waitlistRemaining };
		info.add(array2);
		return info;
	}

	public static WebClient startWebClient() {
		System.out.println("Starting Web Client...");
		WebClient webClient = new WebClient();
		return webClient;
	}

	public static void stopWebClient(WebClient webClient) {
		System.out.println("Started HtmlPage. Closing All Windows..");
		webClient.closeAllWindows();
	}

	public static HtmlPage getPage(String url, WebClient webClient)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		System.out.println("Started Web Client. Starting HtmlPage..");
		HtmlPage page = webClient.getPage(url);
		return page;
	}
}
