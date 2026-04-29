package application;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ForgotPassword extends JFrame implements ActionListener {

	JLabel l1, l2;
	JTextField t1;
	JPasswordField t2;
	JButton reset;

	ForgotPassword() {
		setTitle("Reset Password");

		l1 = new JLabel("Username");
		l2 = new JLabel("New Password");

		t1 = new JTextField();
		t2 = new JPasswordField();

		reset = new JButton("Reset Password");

		setLayout(null);

		l1.setBounds(50, 50, 120, 30);
		l2.setBounds(50, 100, 120, 30);

		t1.setBounds(170, 50, 150, 30);
		t2.setBounds(170, 100, 150, 30);

		reset.setBounds(120, 160, 150, 30);

		add(l1);
		add(l2);
		add(t1);
		add(t2);
		add(reset);

		reset.addActionListener(this);

		setSize(400, 250);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String username = t1.getText();
		String password = new String(t2.getPassword());

		try {
			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con.prepareStatement("update login set password=? where username=?");

			ps.setString(1, password);
			ps.setString(2, username);

			int i = ps.executeUpdate();

			if (i > 0) {
				JOptionPane.showMessageDialog(this, "Password Updated");
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "User not found");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
