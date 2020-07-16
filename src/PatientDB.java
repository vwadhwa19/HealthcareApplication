import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class PatientDB {

	private String username, password;
	Connection con = null;
	Statement statement = null;
	private ResultSet rs;
	int appointmentID;
	private String[][] tableData, tempTableData;

	public PatientDB() {
		this("", "");
	}

	public PatientDB(String username, String password) {

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

	public boolean Register(String[] patientData) {

		String name, username, password, email, dob, schoolID, cellNumber, gender;
		name = patientData[0];
		username = patientData[1];
		password = patientData[2];
		email = patientData[3];
		dob = patientData[4];
		schoolID = patientData[5];
		cellNumber = patientData[6];
		gender = patientData[7];

		boolean confirmed = false;
		String query = "SELECT * FROM Patient WHERE Username = '" + username + "'";

		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
				confirmed = false;
			else {
				statement.executeUpdate(
						"INSERT INTO Patient values('" + name + "', '" + username + "', '" + password + "', '" + email
								+ "', '" + dob + "', '" + schoolID + "', '" + cellNumber + "', '" + gender + "')");
				confirmed = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return confirmed;
	}

	public boolean isExists() {
		boolean exists = false;

		String sql = "SELECT * FROM Patient WHERE Username = '" + username + "' AND Password = '" + password + "'";
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

	public void closeDB() {
		try {
			statement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[][] getAppointment() {

		String numRowsQuery = "SELECT COUNT(*) FROM FinalScheduling WHERE PatientUsername = '" + username + "'";
		String apptQuery = "SELECT DoctorName, Date, Time, Comments, AppointmentID FROM FinalScheduling WHERE PatientUsername = '"
				+ username + "'";
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

	public String[][] getTempAppointment() {

		String numRowsQuery = "SELECT COUNT(*) FROM PreScheduling WHERE PatientUsername = '" + username + "'";
		String apptQuery = "SELECT Date, Time, Comments, AppointmentID FROM PreScheduling WHERE PatientUsername = '"
				+ username + "'";
		int numRows;

		try {
			rs = statement.executeQuery(numRowsQuery);
			numRows = rs.getInt(1);
			rs.close();
			tempTableData = new String[numRows][4];
			rs = statement.executeQuery(apptQuery);
			rs.next();

			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < 4; col++) {

					tempTableData[row][col] = rs.getString(col + 1);
				}
				rs.next();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempTableData;
	}

	void scheduleAppointment(String date, String time, String comments) {

		String query, name;
		query = "SELECT Name FROM Patient WHERE Username = '" + username + "'";
		String apptID = getApptID();
		try {
			rs = statement.executeQuery(query);
			rs.next();
			name = rs.getString("Name");

			statement.executeUpdate("INSERT INTO PreScheduling values('" + name + "', '" + username + "', '" + date
					+ "', '" + time + "', '" + comments + "', '" + apptID + "')");
		} catch (SQLException e1) {
			System.err.println(e1.getMessage());
		}
	}

	void cancelAppointment(String apptID) {
		String query1 = "DELETE FROM PreScheduling WHERE AppointmentID = '" + apptID + "' AND PatientUsername = '"
				+ username + "'";
		String query2 = "DELETE FROM FinalScheduling WHERE AppointmentID = '" + apptID + "' AND PatientUsername = '"
				+ username + "'";
		String check = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + apptID + "' AND PatientUsername = '"
				+ username + "'";
		try {
			rs = statement.executeQuery(check);

			if (rs.next())
				statement.executeUpdate(query1);
			else
				statement.executeUpdate(query2);
			// statement.executeUpdate(query1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getApptID() {
		Random random = new Random();
		int apptID = 0;
		String id;
		do {
			apptID = random.nextInt(900) + 100;

		} while (checkID(apptID));

		id = Integer.toString(apptID);
		return id;
	}

	private boolean checkID(int apptID) {
		String id = Integer.toString(apptID);
		String sql = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + id + "'";
		try {
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				return true;
			} else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean checkEditAppt(int apptID) {
		String id = Integer.toString(apptID);
		String sql = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + id + "'";
		String sql2 = "SELECT * FROM FinalScheduling WHERE AppointmentID = '" + id + "'";
		try {
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				return true;
			} else {
				rs = statement.executeQuery(sql2);
				if (rs.next()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean updatePassword(String password) {

		this.password = password;

		String sql = "UPDATE Patient SET Password = '" + password + "' WHERE Username = '" + username + "'";

		try {
			rs = statement.executeQuery(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

}
