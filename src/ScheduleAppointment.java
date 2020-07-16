import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ScheduleAppointment extends JFrame {

	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JLabel dateLabel, timeLabel, welcomeLabel;
	private JButton confirmButton, cancelButton;
	private JTextField date, time;
	private JTextArea comment;
	private String username, password;
	PatientDB patient;

	// Constructor
	public ScheduleAppointment() {
		super("Appointment");

	}

	public ScheduleAppointment(String username, String password) {

		// Set the title bar text
		this.username = username;
		this.password = password;

		patient = new PatientDB(username, password);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

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

		welcomeLabel = new JLabel("Schedule Appointment " + username);
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 40));
		northPanel.add(welcomeLabel);
	}

	public void buildCenterPanel() {

		JPanel innerNorthPanel = new JPanel();
		JPanel innerCenterPanel = new JPanel();
		JPanel innerSouthPanel = new JPanel();

		innerNorthPanel.setLayout(new GridLayout(4, 1, 0, 15));
		innerCenterPanel.setLayout(new GridLayout(1, 1, 0, 5));
		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 25));

		innerNorthPanel.setBackground(Color.white);
		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);

		innerCenterPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 100, 0));

		dateLabel = new JLabel("Appointment Date:");
		timeLabel = new JLabel("Appointment Time:");
		date = new JTextField(10);
		date.setText("YYYY-MM-DD");
		time = new JTextField(5);
		time.setText("HH:MM");
		comment = new JTextArea(5, 50);
		comment.setText("You can write any comments for the doctor here");

		comment.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ConfirmButtonListener());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelButtonListener());

		innerSouthPanel.add(confirmButton);
		innerSouthPanel.add(cancelButton);

		innerNorthPanel.add(dateLabel);
		innerNorthPanel.add(date);
		innerNorthPanel.add(timeLabel);
		innerNorthPanel.add(time);

		innerCenterPanel.add(comment);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(innerNorthPanel, BorderLayout.NORTH);
		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);
	}

	private class ConfirmButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String getDate = date.getText();
			String getTime = time.getText();
			String getComments = comment.getText();

			patient.scheduleAppointment(getDate, getTime, getComments);
			JOptionPane.showMessageDialog(null, "Your appointment has been scheduled");
			patient.closeDB();
			dispose();
			new PatientHomepage(username, password);
		}
	}

	private class CancelButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			JOptionPane.showMessageDialog(null, "Appointment scheduling canceled");
			patient.closeDB();
			dispose();
			new PatientHomepage(username, password);

		}
	}
}
