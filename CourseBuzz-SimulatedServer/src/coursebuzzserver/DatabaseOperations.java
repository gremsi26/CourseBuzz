package coursebuzzserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseOperations {

	private String url;
	private Connection con;
	private Log log;
	private Statement st;

	public DatabaseOperations(Log log, String url, String dbname,
			String username, String password) {
		this.log = log;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://" + url + "/" + dbname;
			con = DriverManager.getConnection(url, username, password);
			st = con.createStatement();
			this.log.log("Connected to Server [DatabaseOperations() in DatabaseOperations.java]");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			ResultSet rs = st.executeQuery(query);
			log.log("Execute Query: " + query
					+ " [executeQuery() in DatabaseOperations.java]");
			return rs;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public void execute(String query) {
		try {
			st.execute(query);
			log.log("Execute Query: " + query
					+ " [execute() in DatabaseOperations.java]");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void createTable(String query) {
		try {
			st.execute(query);
			log.log("Table Created [createTable() in DatabaseOperations.java]");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void clearAllTables(String[] tableNames) {
		try {
			for (int i = 0; i < tableNames.length; i++) {
				st.execute("DELETE FROM " + tableNames[i]);
			}
			log.log("All Tables Emptied [clearAllTables() in DatabaseOperations.java]");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean isEmpty() {
		String query = "SELECT count(*) AS countnum FROM information_schema.tables WHERE table_type = 'BASE TABLE' and table_schema = 'erms'";
		ResultSet rs = executeQuery(query);
		try {
			if (rs.next())
				if (rs.getInt("countnum") == 0)
					return true;
		} catch (Exception e) {
			System.out.println(e);
			return true;
		}
		return false;
	}

}