import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DoctorDB {

	String username, password;
	Connection con = null;
	Statement statement = null;
	ResultSet rs;
	private String[][] tableData;

	public DoctorDB() {
		this("", "");
	}

	public DoctorDB(String username, String password) {

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

	public boolean Register(String name, String username, String password) {

		boolean confirmed = false;
		String query = "SELECT * FROM Doctor WHERE Username = '" + username + "'";

		try {
			rs = statement.executeQuery(query);
			if (rs.next())
				confirmed = false;
			else {
				statement.executeUpdate(
						"INSERT INTO Doctor values(' " + name + "', '" + username + "', '" + password + "')");
				confirmed = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confirmed;
	}

	public boolean isExists() {
		boolean exists = false;

		String sql = "SELECT * FROM Doctor WHERE Username = '" + username + "' AND Password = '" + password + "'";
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

	public String[][] getAppointment() {

		String numRowsQuery = "SELECT COUNT(*) FROM FinalScheduling WHERE DoctorUsername = '" + username + "'";
		String apptQuery = "SELECT PatientName, Date, Time, Comments, AppointmentID FROM FinalScheduling WHERE DoctorUsername = '"
				+ username + "' ORDER BY Date, Time";
		int numRows;

		try {
			rs = statement.executeQuery(numRowsQuery);
			numRows = rs.getInt(1);
			tableData = new String[numRows][5];
			rs = statement.executeQuery(apptQuery);
			rs.next();

			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < 5; col++) {

					tableData[row][col] = rs.getString(col + 1);
				}
				rs.next();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableData;
	}

	public void closeDB() {
		try {
			statement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
