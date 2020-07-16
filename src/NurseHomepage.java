import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;

public class NurseHomepage extends JFrame {
	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JLabel welcomeLabel;
	private JButton rescheduleButton, confirmButton, cancelButton, logoutButton;
	private String username, password;
	private NurseDB nurse;
	private String[][] tableData, tempTableData;
	private String[] tableHeader = { "Patient", "Doctor", "Date", "Time", "Comments", "Appointment ID" };
	private String[] tempTableHeader = { "Patient", "Date", "Time", "Comments", "Appointment ID" };

	private JTable table, tempTable;
	private JScrollPane scrollPane, tempScrollPane;

	// Constructor
	public NurseHomepage() {
		super("Welcome");
	}

	public NurseHomepage(String username, String password) {

		this();
		this.username = username;
		this.password = password;
		nurse = new NurseDB(username, password);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

		tableData = nurse.getAppointment();
		tempTableData = nurse.getTempAppointment();

		// Specify an action for the close button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add a BorderLayout manager to the content pane
		setLayout(new BorderLayout());

		// Create five panels
		northPanel = new JPanel();
		southPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		centerPanel = new JPanel();

		northPanel.setBackground(Color.cyan);
		centerPanel.setBackground(Color.white);
		southPanel.setBackground(Color.cyan);
		eastPanel.setBackground(Color.cyan);
		westPanel.setBackground(Color.cyan);

		buildNorthPanel();
		buildCenterPanel();

		northPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
		southPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 500, 50, 500));

		// Add the five panels to the content pane
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		add(eastPanel, BorderLayout.EAST);
		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);

		// Pack and display the window
		pack();
		setVisible(true);
	}

	public void buildNorthPanel() {

		welcomeLabel = new JLabel("Welcome " + username);
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 40));
		northPanel.add(welcomeLabel);
	}

	public void buildCenterPanel() {

		JPanel innerCenterPanel = new JPanel();
		JPanel innerSouthPanel = new JPanel();

		innerSouthPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);
		innerCenterPanel.setLayout(new GridLayout(2, 1, 0, 5));

		table = new JTable(tableData, tableHeader);
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(null, "Confirmed Appointments", TitledBorder.CENTER,
				TitledBorder.TOP));

		tempTable = new JTable(tempTableData, tempTableHeader);
		tempScrollPane = new JScrollPane(tempTable);
		tempScrollPane.setBorder(
				BorderFactory.createTitledBorder(null, "Pending Appointments", TitledBorder.CENTER, TitledBorder.TOP));

		innerCenterPanel.add(scrollPane);
		innerCenterPanel.add(tempScrollPane);

		rescheduleButton = new JButton("Reschedule Appointment");
		confirmButton = new JButton("Confirm Appointment");
		cancelButton = new JButton("Cancel Appointment");
		logoutButton = new JButton("Logout");

		rescheduleButton.addActionListener(new RescheduleButtonListener());
		confirmButton.addActionListener(new ConfirmButtonListener());
		cancelButton.addActionListener(new CancelButtonListener());
		logoutButton.addActionListener(new LogoutButtonListener());

		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));

		innerSouthPanel.add(rescheduleButton);
		innerSouthPanel.add(confirmButton);
		innerSouthPanel.add(cancelButton);
		innerSouthPanel.add(logoutButton);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);

	}

	private class RescheduleButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String apptID, doctorUsername, date, time;
			apptID = JOptionPane.showInputDialog("Enter the ID for the appointment to edit");
			if (apptID == null) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
				return;
			}
			doctorUsername = JOptionPane.showInputDialog("Enter any comment for the doctor");
			if (doctorUsername == null) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
				return;
			}
			date = JOptionPane.showInputDialog("Enter the appointment date");
			if (date == null) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
				return;
			}
			time = JOptionPane.showInputDialog("Enter the appointment time");
			if (time == null) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
				return;
			}
			int value;
			value = JOptionPane.showConfirmDialog(null, "Are you sure you want to reschedule this appointment?");
			if (value == JOptionPane.YES_OPTION) {
				nurse.rescheduleAppointment(apptID, doctorUsername, date, time);
				JOptionPane.showMessageDialog(null, "Appointment reschedule confirmed");
				nurse.closeDB();
				dispose();
				new NurseHomepage(username, password);
			} else if (value == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
			} else if (value == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment reschedule canceled");
			}
		}
	}

	private class ConfirmButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String doctorUsername = JOptionPane.showInputDialog("Enter the doctor's ID (username)");
			if (doctorUsername == null) {
				JOptionPane.showMessageDialog(null, "Appointment confirmation canceled");
				return;
			}
			String appointmentID = JOptionPane.showInputDialog("Enter the Appointment ID");
			if (appointmentID == null) {
				JOptionPane.showMessageDialog(null, "Appointment confirmation canceled");
				return;
			}

			int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to confirm this appointment?");
			if (value == JOptionPane.YES_OPTION) {
				nurse.confirmAppointment(doctorUsername, appointmentID);
				nurse.removeAppointment(appointmentID);
				JOptionPane.showMessageDialog(null, "Appointment Confirmed");
				nurse.closeDB();
				dispose();
				new NurseHomepage(username, password);
			} else if (value == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment confirmation canceled");
			} else if (value == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment confirmation canceled");
			}

		}
	}

	private class CancelButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String appointmentID = JOptionPane.showInputDialog("Enter the appointmentID");
			if (appointmentID == null) {
				JOptionPane.showMessageDialog(null, "Appointment cancelation halted");
				return;
			}

			int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this appointment?");
			if (value == JOptionPane.YES_OPTION) {
				nurse.removeAppointment(appointmentID);
				nurse.cancelAppointment(appointmentID);
				JOptionPane.showMessageDialog(null, "Appointment is cancelled");
				nurse.closeDB();
				dispose();
				new NurseHomepage(username, password);
			} else if (value == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment cancelation halted");
			} else if (value == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "Appointment cancelation halted");
			}
		}
	}

	private class LogoutButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			nurse.closeDB();
			dispose();
			new Homepage();
		}
	}

}
