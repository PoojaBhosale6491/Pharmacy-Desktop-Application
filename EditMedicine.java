
package application;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class EditMedicine extends JFrame implements ActionListener {

	JLabel l1, l2, l3;
	JTextField t1, t3;
	JComboBox cb;
	JButton b1;

	EditMedicine() {

		setTitle("Edit Medicine");

		l1 = new JLabel("Medicine ID");
		l2 = new JLabel("Select Field");
		l3 = new JLabel("New Value");

		t1 = new JTextField();
		t3 = new JTextField();

		String fields[] = { "name", "company", "batchId", "stock", "price", "category", "expiry" };
		cb = new JComboBox(fields);

		b1 = new JButton("Update Medicine");

		l1.setBounds(50, 50, 150, 30);
		t1.setBounds(200, 50, 150, 30);

		l2.setBounds(50, 100, 150, 30);
		cb.setBounds(200, 100, 150, 30);

		l3.setBounds(50, 150, 150, 30);
		t3.setBounds(200, 150, 150, 30);

		b1.setBounds(100, 220, 200, 30);

		add(l1);
		add(t1);
		add(l2);
		add(cb);
		add(l3);
		add(t3);
		add(b1);

		b1.addActionListener(this);

		setSize(400, 350);
		setLayout(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		try {

			int id = Integer.parseInt(t1.getText());
			String field = cb.getSelectedItem().toString();
			String value = t3.getText();

			Connection con = DBConnection.getConnection();

			String sql = "UPDATE medicine SET " + field + "=? WHERE id=?";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, value);
			ps.setInt(2, id);

			int rows = ps.executeUpdate();

			if (rows > 0) {
				JOptionPane.showMessageDialog(this, "Medicine Updated Successfully");
			} else {
				JOptionPane.showMessageDialog(this, "Invalid ID");
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex);
		}
	}

	public static void main(String[] args) {
		new EditMedicine();
	}
}