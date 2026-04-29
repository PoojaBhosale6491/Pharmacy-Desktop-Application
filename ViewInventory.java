package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class ViewInventory extends JFrame implements ActionListener {

	JTable table;
	DefaultTableModel model;
	JTextField searchField;
	JButton searchBtn;

	ViewInventory() {

		setTitle("View Inventory");

		model = new DefaultTableModel();

		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Company");
		model.addColumn("Batch ID");
		model.addColumn("Stock");
		model.addColumn("Price");
		model.addColumn("Category");
		model.addColumn("Expiry Date");

		table = new JTable(model);

		JScrollPane sp = new JScrollPane(table);

		searchField = new JTextField();
		searchBtn = new JButton("Search");

		setLayout(null);

		searchField.setBounds(200, 20, 200, 30);
		searchBtn.setBounds(420, 20, 100, 30);

		sp.setBounds(20, 70, 850, 300);

		add(searchField);
		add(searchBtn);
		add(sp);

		searchBtn.addActionListener(this);

		setSize(900, 450);
		setLocationRelativeTo(null);
		setVisible(true);

		loadData();
	}

	void loadData() {

		try {
			model.setRowCount(0);

			Connection con = DBConnection.getConnection();
			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM medicine");

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("company"),
						rs.getString("batchId"), rs.getInt("stock"), rs.getDouble("price"), rs.getString("category"),
						rs.getDate("expiry")

				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			model.setRowCount(0);

			String search = searchField.getText();

			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM medicine WHERE name LIKE ?");

			ps.setString(1, "%" + search + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("company"),
						rs.getString("batchId"), rs.getInt("stock"), rs.getDouble("price"), rs.getString("category"),
						rs.getDate("expiry") });
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}