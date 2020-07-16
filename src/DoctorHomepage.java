import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;

public class DoctorHomepage extends JFrame {
	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JLabel welcomeLabel;
	private JButton viewRecordButton, logoutButton;
	private String username;
	private DoctorDB doctor;
	private String[][] tableData;
	private String[] tableHeader = { "Patient", "Date", "Time", "Comments", "Appointment ID" };
	private JTable table;
	private JScrollPane scrollPane;

	// Constructor
	public DoctorHomepage() {
		super("Doctor Portal");
	}

	public DoctorHomepage(String username, String password) {

		this();
		this.username = username;
		doctor = new DoctorDB(username, password);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

		tableData = doctor.getAppointment();

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

		innerCenterPanel.setLayout(new GridLayout(1, 1, 0, 25));

		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);

		table = new JTable(tableData, tableHeader);
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(
				BorderFactory.createTitledBorder(null, "Upcoming Appointments", TitledBorder.CENTER, TitledBorder.TOP));

		innerCenterPanel.add(scrollPane);

		viewRecordButton = new JButton("View Medical Records");
		logoutButton = new JButton("Logout");

		logoutButton.addActionListener(new LogoutButtonListener());

		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
		innerSouthPanel.add(logoutButton);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);

	}

	private class LogoutButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			doctor.closeDB();
			dispose();
			new Homepage();
		}
	}

}
