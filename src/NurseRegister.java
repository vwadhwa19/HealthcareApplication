import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class NurseRegister extends JFrame {

	// To reference panels
	private JPanel northPanel, southPanel, eastPanel, westPanel, centerPanel;
	private JTextField firstName, lastName, username, password;
	private JButton confirmButton, cancelButton;
	private NurseDB nurse;

	// Constructor
	public NurseRegister() {

		// Set the title bar text
		super("Nurse Registration");

		nurse = new NurseDB();

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

		buildCenterPanel();
		buildNorthPanel();

		northPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
		southPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 750, 50, 750));

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

	public void buildCenterPanel() {

		firstName = new JTextField(25);
		lastName = new JTextField(25);
		username = new JTextField(25);
		password = new JTextField(25);

		firstName.setText("First name");
		lastName.setText("Last name");
		username.setText("Username");
		password.setText("Password");

		JPanel innerCenterPanel = new JPanel();
		JPanel innerSouthPanel = new JPanel();

		// Panel layouts
		centerPanel.setLayout(new BorderLayout());
		innerCenterPanel.setLayout(new GridLayout(10, 1, 0, 10));
		innerSouthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 25));

		innerCenterPanel.setBackground(Color.white);
		innerSouthPanel.setBackground(Color.white);

		innerCenterPanel.add(firstName);
		innerCenterPanel.add(lastName);
		innerCenterPanel.add(username);
		innerCenterPanel.add(password);

		confirmButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");

		confirmButton.addActionListener(new ConfirmButtonListener());
		cancelButton.addActionListener(new CancelButtonListener());

		innerSouthPanel.add(confirmButton);
		innerSouthPanel.add(cancelButton);

		centerPanel.add(innerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(innerSouthPanel, BorderLayout.SOUTH);

	}

	// Builds north panel
	private void buildNorthPanel() {

		JLabel welcomeLabel = new JLabel("Nurse Registration");
		northPanel.add(welcomeLabel);
	}

	private class ConfirmButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String getFirstName = firstName.getText();
			String getLastName = lastName.getText();
			String getUsername = username.getText();
			String getPassword = password.getText();
			String name = getFirstName + " " + getLastName;

			boolean done = nurse.Register(name, getUsername, getPassword);

			if (done) {
				JOptionPane.showMessageDialog(null, "Registration Completed");
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Registration Error. Username Already Taken!");
			}

		}
	}

	private class CancelButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			JOptionPane.showMessageDialog(null, "Registration Canceled");
			dispose();
		}
	}
}
