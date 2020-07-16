import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NurseDB {

	String username, password;
	Connection con = null;
	Statement statement = null;
	ResultSet rs;
	private String[][] tableData, tempTableData;

	public NurseDB() {
		this("", "");
	}

	public NurseDB(String username, String password) {

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
		String query = "SELECT * FROM Nurse WHERE Username = '" + username + "'";

		try {
			rs = statement.executeQuery(query);
			if (rs.next())
				confirmed = false;
			else {
				statement.executeUpdate(
						"INSERT INTO Nurse values(' " + name + "', '" + username + "', '" + password + "')");
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

		String sql = "SELECT * FROM Nurse WHERE Username = '" + username + "' AND Password = '" + password + "'";
		try {
			rs = statement.executeQuery(sql);
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

		String numRowsQuery = "SELECT COUNT(*) FROM FinalScheduling";
		String apptQuery = "SELECT PatientName, DoctorName, Date, Time, Comments, AppointmentID FROM FinalScheduling";
		int numRows;

		try {
			rs = statement.executeQuery(numRowsQuery);
			numRows = rs.getInt(1);
			tableData = new String[numRows][6];
			rs = statement.executeQuery(apptQuery);
			rs.next();

			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < 6; col++) {

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

		String numRowsQuery = "SELECT COUNT(*) FROM PreScheduling";
		String apptQuery = "SELECT PatientName, Date, Time, Comments, AppointmentID FROM PreScheduling";
		int numRows;

		try {
			rs = statement.executeQuery(numRowsQuery);
			numRows = rs.getInt(1);
			tempTableData = new String[numRows][5];
			rs = statement.executeQuery(apptQuery);
			rs.next();

			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < 5; col++) {

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

	public void confirmAppointment(String doctorUsername, String appointmentID) {

		String sql = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + appointmentID + "'";
		String drNameQuery = "SELECT Name FROM Doctor WHERE Username = '" + doctorUsername + "'";
		String patientName, date, time, comments, drName, patientUsername;

		try {
			rs = statement.executeQuery(drNameQuery);
			drName = rs.getString("Name");
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				patientName = rs.getString("PatientName");
				patientUsername = rs.getString("PatientUsername");
				date = rs.getString("Date");
				time = rs.getString("Time");
				comments = rs.getString("Comments");

				statement.executeUpdate("INSERT INTO FinalScheduling values('" + patientName + "', '" + patientUsername
						+ "', '" + drName + "', '" + doctorUsername + "', '" + date + "', '" + time + "', '" + comments
						+ "', '" + appointmentID + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void confirmAppointment(String appointmentID, String doctorUsername, String date, String time) {

		String sql = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + appointmentID + "'";
		String drNameQuery = "SELECT Name FROM Doctor WHERE Username = '" + doctorUsername + "'";
		String patientName, comments, drName, patientUsername;

		try {
			rs = statement.executeQuery(drNameQuery);
			drName = rs.getString("Username");
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				patientName = rs.getString("PatientName");
				patientUsername = rs.getString("PatientUsername");
				comments = rs.getString("Comments");

				statement.executeUpdate("INSERT INTO FinalScheduling values('" + patientName + "', '" + patientUsername
						+ "', '" + drName + "', '" + doctorUsername + "', '" + date + "', '" + time + "', '" + comments
						+ "', '" + appointmentID + "')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rescheduleAppointment(String apptID, String doctorUsername, String date, String time) {

		String patientName, patientUsername, doctorName, comments;
		String query1 = "SELECT * FROM PreScheduling WHERE AppointmentID = '" + apptID + "'";
		String query2 = "SELECT * FROM FinalScheduling WHERE AppointmentID = '" + apptID + "'";
		try {
			rs = statement.executeQuery(query1);

			if (rs.next()) {
				confirmAppointment(apptID, doctorUsername, date, time);
				removeAppointment(apptID);
			} else {
				rs = statement.executeQuery(query2);
				while (rs.next()) {
					patientName = rs.getString("PatientName");
					patientUsername = rs.getString("PatientUsername");
					doctorName = rs.getString("DoctorName");
					comments = rs.getString("Comments");

					statement.executeUpdate("INSERT INTO FinalScheduling values('" + patientName + "', '"
							+ patientUsername + "', " + doctorName + "'," + doctorUsername + "', '" + date + "', '"
							+ time + "', '" + comments + "', '" + apptID + "')");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void cancelAppointment(String appointmentID) {
		String query = "DELETE FROM FinalScheduling WHERE AppointmentID = '" + appointmentID + "'";

		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeAppointment(String appointmentID) {
		String query = "DELETE FROM PreScheduling WHERE AppointmentID = '" + appointmentID + "'";

		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
