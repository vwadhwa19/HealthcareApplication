import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Homepage extends JFrame {

	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JLabel usernameLabel, passwordLabel, infoLabel; // labels
	private JTextField usernameInput, passwordInput; // inputs
	private JButton loginButton, registerButton; // buttons
	private JRadioButton patient, doctor, nurse, admin;
	private String username, password;

	// Constructor
	public Homepage() {

		// Set the title bar text
		super("Healthcare Scheduling System");

		// Specify an action for the close button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setExtendedState(JFrame.MAXIMIZED_BOTH);

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
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 400, 10, 400));

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

	// Builds north panel
	private void buildNorthPanel() {

		JLabel welcomeLabel = new JLabel("Healthcare Scheduling System");
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 80));
		northPanel.add(welcomeLabel);
	}

	// Builds center panel
	public void buildCenterPanel() {

		JPanel innerCenterPanel = new JPanel();
		JPanel innerSouthPanel = new JPanel();

		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);

		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
		innerSouthPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 300, 0));

		centerPanel.setLayout(new BorderLayout());
		innerCenterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 300, 0));

		usernameLabel = new JLabel("Username: ");
		usernameLabel.setFont(new Font("Serif", Font.BOLD, 30));
		usernameInput = new JTextField(25);
		usernameInput.setPreferredSize(new Dimension(100, 24));
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(new Font("Serif", Font.BOLD, 30));
		passwordInput = new JTextField(25);
		passwordInput.setPreferredSize(new Dimension(100, 24));
		loginButton = new JButton("Login");
		loginButton.setPreferredSize(new Dimension(100, 50));
		registerButton = new JButton("Patient Register");
		registerButton.setPreferredSize(new Dimension(150, 50));

		loginButton.addActionListener(new LoginButtonListener());
		registerButton.addActionListener(new RegisterButtonListener());

		innerCenterPanel.add(usernameLabel);
		innerCenterPanel.add(usernameInput);
		innerCenterPanel.add(passwordLabel);
		innerCenterPanel.add(passwordInput);

		patient = new JRadioButton("Patient");
		nurse = new JRadioButton("Nurse");
		doctor = new JRadioButton("Doctor");
		admin = new JRadioButton("Admin");
		ButtonGroup userRole = new ButtonGroup();

		patient.setBackground(Color.white);
		doctor.setBackground(Color.white);
		nurse.setBackground(Color.white);
		admin.setBackground(Color.white);

		userRole.add(patient);
		userRole.add(nurse);
		userRole.add(doctor);
		userRole.add(admin);

		infoLabel = new JLabel("Select a role");
		infoLabel.setFont(new Font("Serif", Font.BOLD, 24));

		innerCenterPanel.add(infoLabel);
		innerCenterPanel.add(patient);
		innerCenterPanel.add(nurse);
		innerCenterPanel.add(doctor);
		innerCenterPanel.add(admin);

		innerSouthPanel.add(loginButton);
		innerSouthPanel.add(registerButton);

		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);

	}

	private class LoginButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			username = usernameInput.getText();
			password = passwordInput.getText();

			if (patient.isSelected()) {
				PatientDB patient = new PatientDB(username, password);
				if (patient.isExists()) {
					patient.closeDB();
					dispose();
					new PatientHomepage(username, password);
				} else
					JOptionPane.showMessageDialog(null, "Your username or password is incorrect.");
			} else if (nurse.isSelected()) {
				NurseDB nurse = new NurseDB(username, password);
				if (nurse.isExists()) {
					nurse.closeDB();
					dispose();
					new NurseHomepage(username, password);
				} else
					JOptionPane.showMessageDialog(null, "Your username or password is incorrect.");
			} else if (doctor.isSelected()) {
				DoctorDB doctor = new DoctorDB(username, password);
				if (doctor.isExists()) {
					doctor.closeDB();
					dispose();
					new DoctorHomepage(username, password);
				} else
					JOptionPane.showMessageDialog(null, "Your username or password is incorrect.");
			} else if (admin.isSelected()) {
				AdminDB admin = new AdminDB(username, password);
				if (admin.isExists()) {
					admin.closeDB();
					dispose();
					new AdminHomepage(username, password);
				} else
					JOptionPane.showMessageDialog(null, "Your username or password is incorrect.");
			} else {
				JOptionPane.showMessageDialog(null, "You must select a user role.");
			}
		}
	}

	private class RegisterButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			dispose();
			new Register();
		}
	}

	public static void main(String[] args) {

		new Homepage();
	}
}
