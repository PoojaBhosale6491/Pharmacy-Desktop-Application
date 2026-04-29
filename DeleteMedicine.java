package application;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteMedicine extends JFrame implements ActionListener {

	JLabel l1;
	JTextField t1;
	JButton b1;

	DeleteMedicine() {

		setTitle("Delete Medicine");

		l1 = new JLabel("Enter Medicine ID");
		t1 = new JTextField();
		b1 = new JButton("Delete");

		l1.setBounds(50, 50, 150, 30);
		t1.setBounds(200, 50, 150, 30);
		b1.setBounds(120, 120, 100, 30);

		add(l1);
		add(t1);
		add(b1);

		b1.addActionListener(this);

		setSize(400, 250);
		setLayout(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		try {

			// Validation
			if (t1.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter ID");
				return;
			}

			int id = Integer.parseInt(t1.getText());

			Connection con = DBConnection.getConnection();

			String sql = "DELETE FROM medicine WHERE id=?";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			int rows = ps.executeUpdate();

			if (rows > 0) {
				JOptionPane.showMessageDialog(this, "Medicine Deleted Successfully");
				t1.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "Invalid ID");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex);
		}
	}

	public static void main(String[] args) {
		new DeleteMedicine();
	}
}
