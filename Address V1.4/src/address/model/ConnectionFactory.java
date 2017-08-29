package address.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost/address?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
	public static final String USERNAME = "address";
	public static final String PASSWORD = "address";
	
	
	public Connection getConnection() {
		Connection connection;
		
		try {
			Class.forName(DRIVER).newInstance();
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
		
		} catch(Exception e) {
			throw new RuntimeException("Database Connection Error!");
		}
		
		return connection;
	}

}
