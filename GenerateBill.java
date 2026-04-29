package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class GenerateBill extends JFrame implements ActionListener {

	JTable table, finalTable;
	DefaultTableModel model, finalModel;

	JTextField tQty, tName, tContact, tMed;
	JButton addBtn, billBtn;

	ArrayList<String> medList = new ArrayList<>();
	ArrayList<Integer> qtyList = new ArrayList<>();
	ArrayList<Double> priceList = new ArrayList<>();
	ArrayList<Double> totalList = new ArrayList<>();

	GenerateBill() {

		setTitle("Generate Bill");

		JLabel cname = new JLabel("Customer Name");
		JLabel cno = new JLabel("Contact No");
		JLabel mname = new JLabel("Medicine Name");
		JLabel lqty = new JLabel("Quantity");

		cname.setBounds(500, 50, 120, 30);
		cno.setBounds(500, 90, 120, 30);
		mname.setBounds(500, 130, 120, 30);
		lqty.setBounds(50, 270, 100, 30);

		tName = new JTextField();
		tContact = new JTextField();
		tMed = new JTextField();
		tQty = new JTextField();

		tName.setBounds(650, 50, 150, 30);
		tContact.setBounds(650, 90, 150, 30);
		tMed.setBounds(650, 130, 150, 30);
		tQty.setBounds(150, 270, 100, 30);

		add(cname);
		add(cno);
		add(mname);
		add(lqty);
		add(tName);
		add(tContact);
		add(tMed);
		add(tQty);

		// Inventory Table
		model = new DefaultTableModel();
		table = new JTable(model);

		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Stock");
		model.addColumn("Price");

		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 50, 450, 200);
		add(sp);

		loadData();

		addBtn = new JButton("Add Medicine");
		billBtn = new JButton("Generate Bill");

		addBtn.setBounds(270, 270, 150, 30);
		billBtn.setBounds(100, 320, 150, 30);

		add(addBtn);
		add(billBtn);

		addBtn.addActionListener(this);
		billBtn.addActionListener(this);

		// Final Table (Saved Records)
		finalModel = new DefaultTableModel();
		finalTable = new JTable(finalModel);

		finalModel.addColumn("Customer");
		finalModel.addColumn("Medicine");
		finalModel.addColumn("Qty");
		finalModel.addColumn("Price");
		finalModel.addColumn("Total");

		JScrollPane sp3 = new JScrollPane(finalTable);
		sp3.setBounds(500, 200, 320, 370);
		add(sp3);

		loadSalesData(); // 🔥 IMPORTANT

		setSize(850, 650);
		setLayout(null);
		setVisible(true);
	}

	// Load medicine
	void loadData() {
		try {
			Connection con = DBConnection.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM medicine");

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getInt("stock"),
						rs.getDouble("price") });
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	// 🔥 LOAD OLD SALES (IMPORTANT)
	void loadSalesData() {
		try {
			Connection con = DBConnection.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM sales");

			while (rs.next()) {
				finalModel.addRow(new Object[] { rs.getString("customer_name"), rs.getString("medicine"),
						rs.getInt("qty"), rs.getDouble("price"), rs.getDouble("total") });
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	public void actionPerformed(ActionEvent e) {

		// ADD MEDICINE
		if (e.getSource() == addBtn) {
			try {
				String name = tMed.getText().trim();
				int qty = Integer.parseInt(tQty.getText().trim());

				Connection con = DBConnection.getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT price, stock FROM medicine WHERE LOWER(name)=LOWER(?)");
				ps.setString(1, name);

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					double price = rs.getDouble("price");
					int stock = rs.getInt("stock");

					if (qty > stock) {
						JOptionPane.showMessageDialog(this, "Not enough stock!");
						return;
					}

					double total = price * qty;

					medList.add(name);
					qtyList.add(qty);
					priceList.add(price);
					totalList.add(total);

					JOptionPane.showMessageDialog(this, "Medicine Added!");

				} else {
					JOptionPane.showMessageDialog(this, "Medicine not found!");
				}

				tMed.setText("");
				tQty.setText("");

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex);
			}
		}

		// GENERATE BILL
		if (e.getSource() == billBtn) {
			try {
				Connection con = DBConnection.getConnection();

				double grandTotal = 0;
				String customer = tName.getText();

				String message = "------ MEDICAL BILL ------\n";
				message += "Customer Name: " + customer + "\n";
				message += "Contact No: " + tContact.getText() + "\n";
				message += "----------------------------------------\n";

				for (int i = 0; i < medList.size(); i++) {

					String med = medList.get(i);
					int qty = qtyList.get(i);
					double price = priceList.get(i);
					double total = totalList.get(i);

					// ✅ SAVE TO DATABASE
					PreparedStatement ps = con.prepareStatement(
							"INSERT INTO sales(customer_name,contact,medicine,qty,price,total) VALUES(?,?,?,?,?,?)");

					ps.setString(1, customer);
					ps.setString(2, tContact.getText());
					ps.setString(3, med);
					ps.setInt(4, qty);
					ps.setDouble(5, price);
					ps.setDouble(6, total);
					ps.executeUpdate();

					// table show
					finalModel.addRow(new Object[] { customer, med, qty, price, total });

					message += "Medicine : " + med + "\n";
					message += "Qty      : " + qty + "\n";
					message += "Price    : " + price + "\n";
					message += "Total    : " + total + "\n";

					grandTotal += total;
				}

				message += "----------------------------------------\n";
				message += "Grand Total: " + grandTotal;

				JOptionPane.showMessageDialog(this, message);

				medList.clear();
				qtyList.clear();
				priceList.clear();
				totalList.clear();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex);
			}
		}
	}

	public static void main(String[] args) {
		new GenerateBill();
	}
}