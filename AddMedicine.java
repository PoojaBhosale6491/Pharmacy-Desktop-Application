package application;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AddMedicine extends JFrame implements ActionListener {

	JTextField name, company, batchId, stock, price, category, expiry;
	JButton addBtn;

	AddMedicine() {
		setTitle("Add Medicine");

		JLabel l1 = new JLabel("Name");
		JLabel l2 = new JLabel("Company");
		JLabel l3 = new JLabel("Batch ID");
		JLabel l4 = new JLabel("Stock");
		JLabel l5 = new JLabel("Price");
		JLabel l6 = new JLabel("Category");
		JLabel l7 = new JLabel("Expiry Date (YYYY-MM-DD)");

		name = new JTextField();
		batchId = new JTextField();
		expiry = new JTextField();
		stock = new JTextField();
		price = new JTextField();
		category = new JTextField();
		company = new JTextField();

		addBtn = new JButton("Add Medicine");

		setLayout(null);

		l1.setBounds(50, 30, 150, 25);
		name.setBounds(200, 30, 200, 25);

		l2.setBounds(50, 70, 150, 25);
		company.setBounds(200, 70, 200, 25);

		l3.setBounds(50, 110, 150, 25);
		batchId.setBounds(200, 110, 200, 25);

		l4.setBounds(50, 150, 150, 25);
		stock.setBounds(200, 150, 200, 25);

		l5.setBounds(50, 190, 150, 25);
		price.setBounds(200, 190, 200, 25);

		l6.setBounds(50, 230, 150, 25);
		category.setBounds(200, 230, 200, 25);

		l7.setBounds(50, 270, 150, 25);
		expiry.setBounds(200, 270, 200, 25);

		addBtn.setBounds(150, 360, 150, 30);
		add(l1);
		add(name);
		add(l2);
		add(company);
		add(l3);
		add(batchId);
		add(l4);
		add(stock);
		add(l5);
		add(price);
		add(l6);
		add(category);
		add(l7);
		add(expiry);

		add(addBtn);

		addBtn.addActionListener(this);

		setSize(500, 450);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO medicine(name,Company,batchId,stock,price,category,expiry) VALUES(?,?,?,?,?,?,?)");

			ps.setString(1, name.getText());
			ps.setString(2, company.getText());
			ps.setString(3, batchId.getText());
			ps.setInt(4, Integer.parseInt(stock.getText()));
			ps.setDouble(5, Double.parseDouble(price.getText()));
			ps.setString(6, category.getText());
			ps.setString(7, expiry.getText());

			int i = ps.executeUpdate();

			if (i > 0) {
				JOptionPane.showMessageDialog(this, "Medicine Added Successfully");
			}

		}

		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}

	}
}
