package application;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

	JLabel l1, l2;
	JTextField t1;
	JPasswordField t2;
	JButton b1, forgot;

	Login() {
		setTitle("Pharmacy Login");

		l1 = new JLabel("Username");
		l2 = new JLabel("Password");

		t1 = new JTextField();
		t2 = new JPasswordField();

		b1 = new JButton("Login");
		forgot = new JButton("Forgot Password");

		setLayout(null);

		l1.setBounds(50, 50, 100, 30);
		l2.setBounds(50, 100, 100, 30);

		t1.setBounds(150, 50, 150, 30);
		t2.setBounds(150, 100, 150, 30);

		b1.setBounds(80, 160, 100, 30);
		forgot.setBounds(190, 160, 150, 30);

		add(l1);
		add(l2);
		add(t1);
		add(t2);
		add(b1);
		add(forgot);

		b1.addActionListener(this);
		forgot.addActionListener(this);

		setSize(400, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			String username = t1.getText();
			String password = new String(t2.getPassword());

			try {
				Connection con = DBConnection.getConnection();

				PreparedStatement ps = con.prepareStatement("select * from login where username=? and password=?");

				ps.setString(1, username);
				ps.setString(2, password);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					JOptionPane.showMessageDialog(this, "Login Successful");
					new Dashboard();
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Invalid Login");
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (e.getSource() == forgot) {
			new ForgotPassword();
		}
	}

	public static void main(String args[]) {
		new Login();
	}
}
