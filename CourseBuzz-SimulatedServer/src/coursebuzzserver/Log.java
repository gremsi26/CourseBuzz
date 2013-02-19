package coursebuzzserver;

public class Log {
	public void log(String stmt) {
		print("LOG", stmt);
	}

	public void print(String title, String stmt) {
		System.out.println(title + ": " + stmt);
	}
}