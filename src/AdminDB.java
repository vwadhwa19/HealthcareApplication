import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminDB {

	private String username, password;
	private Connection con = null;
	private Statement statement = null;
	private ResultSet rs;

	public AdminDB() {

	}

	public AdminDB(String username, String password) {

		this.username = username;
		this.password = password;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:HealthcareDB.db");
			statement = con.createStatement();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public boolean isExists() {
		boolean exists = false;

		String sql = "SELECT * FROM Admin WHERE Username = '" + username + "' AND Password = '" + password + "'";
		try {
			rs = statement.executeQuery(sql);
			if (rs.next())
				exists = true;
			else
				exists = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	public void closeDB() {
		try {
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateDoctorPassword(String username, String password) {

		this.password = password;

		String sql = "UPDATE Doctor SET Password = '" + password + "' WHERE Username = '" + username + "'";

		try {
			rs = statement.executeQuery(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	public boolean updateNursePassword(String username, String password) {

		this.password = password;

		String sql = "UPDATE Nurse SET Password = '" + password + "' WHERE Username = '" + username + "'";

		try {
			rs = statement.executeQuery(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean doctorUsernameExists(String username) {
		boolean exists = false;

		String sql = "SELECT * FROM Doctor WHERE Username = '" + username + "'";
		try {
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				exists = true;
			else
				exists = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}

	public boolean nurseUsernameExists(String username) {
		boolean exists = false;

		String sql = "SELECT * FROM Nurse WHERE Username = '" + username + "'";
		try {
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next())
				exists = true;
			else
				exists = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
	}
}
