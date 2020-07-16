import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AdminHomepage extends JFrame {
	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JLabel welcomeLabel, infoLabel;
	private JButton createDoctorButton, createNurseButton, resetNursePasswordButton, resetDoctorPasswordButton,
			logoutButton;
	private AdminDB admin;
	private String username;

	// Constructor
	public AdminHomepage() {
		super("Welcome");
	}

	// Constructor
	public AdminHomepage(String username, String password) {

		this.username = username;

		admin = new AdminDB(username, password);

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

		welcomeLabel = new JLabel("Welcome " + username);
		welcomeLabel.setFont(new Font("Serif", Font.BOLD, 40));
		northPanel.add(welcomeLabel);
	}

	public void buildCenterPanel() {

		infoLabel = new JLabel("Hi " + username + ", your assistance is required");
		infoLabel.setFont(new Font("Serif", Font.BOLD, 24));

		JPanel innerCenterPanel = new JPanel();
		JPanel innerSouthPanel = new JPanel();

		innerSouthPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 150, 100));

		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);

		innerCenterPanel.add(infoLabel);

		createDoctorButton = new JButton("Create Doctor Account");
		resetDoctorPasswordButton = new JButton("Reset Doctor Password");
		createNurseButton = new JButton("Create Nurse Account");
		resetNursePasswordButton = new JButton("Reset Nurse Password");

		logoutButton = new JButton("Logout");

		createDoctorButton.addActionListener(new CreateDoctorButtonListener());
		resetDoctorPasswordButton.addActionListener(new ResetDoctorPasswordButtonListener());
		createNurseButton.addActionListener(new CreateNurseButtonListener());
		resetNursePasswordButton.addActionListener(new ResetNursePasswordButtonListener());

		logoutButton.addActionListener(new LogoutButtonListener());

		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));

		innerSouthPanel.add(createDoctorButton);
		innerSouthPanel.add(resetDoctorPasswordButton);
		innerSouthPanel.add(createNurseButton);
		innerSouthPanel.add(resetNursePasswordButton);
		innerSouthPanel.add(logoutButton);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);

	}

	private class CreateDoctorButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			new DoctorRegister();

		}
	}

	private class ResetDoctorPasswordButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String username;
			String password;
			username = JOptionPane.showInputDialog("Enter the doctor's username");
			if (!admin.doctorUsernameExists(username)) {
				JOptionPane.showMessageDialog(null, "That username does not exist");
				return;
			}
			password = JOptionPane.showInputDialog("Enter the new password you would like to use");
			if (username != null && password != null) {
				int value;
				value = JOptionPane.showConfirmDialog(null, "Are you sure you want to change your password?");
				if (value == JOptionPane.YES_OPTION) {
					admin.updateDoctorPassword(username, password);
					JOptionPane.showMessageDialog(null, "Password has been changed");
				} else if (value == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Password has not been reset");
				} else if (value == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null, "Password reset canceled");
				}
			} else
				JOptionPane.showMessageDialog(null, "Password reset canceled");

		}
	}

	private class CreateNurseButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			new NurseRegister();

		}
	}

	private class ResetNursePasswordButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String username;
			String password;
			username = JOptionPane.showInputDialog("Enter the nurse's username");
			if (!admin.nurseUsernameExists(username)) {
				JOptionPane.showMessageDialog(null, "That username does not exist");
				return;
			}
			password = JOptionPane.showInputDialog("Enter the new password you would like to use");
			if (username != null && password != null) {
				int value;
				value = JOptionPane.showConfirmDialog(null, "Are you sure you want to change your password?");
				if (value == JOptionPane.YES_OPTION) {
					admin.updateNursePassword(username, password);
					JOptionPane.showMessageDialog(null, "Password has been changed");
				} else if (value == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Password has not been reset");
				} else if (value == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null, "Password reset canceled");
				}
			} else
				JOptionPane.showMessageDialog(null, "Password reset canceled");

		}
	}

	private class LogoutButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			admin.closeDB();
			dispose();
			new Homepage();
		}
	}

}
