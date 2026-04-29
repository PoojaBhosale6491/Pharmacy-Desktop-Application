package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

// iText7 Imports
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class InventoryReport extends JFrame implements ActionListener {

	JTable table;
	DefaultTableModel model;

	JTextField tSearch;
	JButton searchBtn, refreshBtn, pdfBtn, closeBtn; // ✅ closeBtn add

	InventoryReport() {

		setTitle("Inventory Report");
		setLayout(null);

		JLabel title = new JLabel("Inventory Report");
		title.setBounds(300, 10, 200, 30);
		add(title);

		JLabel lsearch = new JLabel("Search Medicine:");
		lsearch.setBounds(20, 50, 120, 30);
		add(lsearch);

		tSearch = new JTextField();
		tSearch.setBounds(140, 50, 150, 30);
		add(tSearch);

		searchBtn = new JButton("Search");
		refreshBtn = new JButton("Refresh");
		pdfBtn = new JButton("Generate PDF");
		closeBtn = new JButton("Close"); // ✅ create button

		searchBtn.setBounds(300, 50, 80, 30);
		refreshBtn.setBounds(410, 50, 80, 30);
		pdfBtn.setBounds(520, 50, 130, 30);
		closeBtn.setBounds(690, 50, 70, 30); // ✅ position

		add(searchBtn);
		add(refreshBtn);
		add(pdfBtn);
		add(closeBtn); // ✅ add to frame

		searchBtn.addActionListener(this);
		refreshBtn.addActionListener(this);
		pdfBtn.addActionListener(this);
		closeBtn.addActionListener(this); // ✅ action

		model = new DefaultTableModel();
		table = new JTable(model);

		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Company");
		model.addColumn("Stock");
		model.addColumn("Price");
		model.addColumn("Expiry");

		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 100, 750, 400);
		add(sp);

		setSize(850, 600); // ✅ size thoda increase kelay
		setVisible(true);

		loadData();
	}

	void loadData() {
		try {
			model.setRowCount(0);

			Connection con = DBConnection.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT id,name,company,stock,price,expiry FROM medicine");

			Date today = new Date(System.currentTimeMillis());

			String lowStockList = "";
			String expiredList = "";

			while (rs.next()) {
				int stock = rs.getInt("stock");
				Date expiry = rs.getDate("expiry");
				String name = rs.getString("name");

				if (stock < 50) {
					lowStockList += name + "\n";
				}

				if (expiry.before(today)) {
					expiredList += name + "\n";
				}

				model.addRow(new Object[] { rs.getInt("id"), name, rs.getString("company"), stock,
						rs.getDouble("price"), expiry });
			}

			if (!lowStockList.isEmpty() && !expiredList.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"⚠ Low Stock Medicines:\n" + lowStockList + "\n⚠ Expired Medicines:\n" + expiredList);
			} else if (!lowStockList.isEmpty()) {
				JOptionPane.showMessageDialog(this, "⚠ Low Stock Medicines:\n" + lowStockList);
			} else if (!expiredList.isEmpty()) {
				JOptionPane.showMessageDialog(this, "⚠ Expired Medicines:\n" + expiredList);
			}

			con.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	void searchData(String name) {
		try {
			model.setRowCount(0);

			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con.prepareStatement(
					"SELECT id,name,company,stock,price,expiry FROM medicine WHERE LOWER(name) LIKE LOWER(?)");

			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("company"),
						rs.getInt("stock"), rs.getDouble("price"), rs.getDate("expiry") });
			}

			con.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	void generatePDF() {
		try {
			PdfWriter writer = new PdfWriter("C:\\Users\\Admin\\OneDrive\\Desktop\\InventoryReport.pdf");
			PdfDocument pdf = new PdfDocument(writer);
			Document doc = new Document(pdf);

			doc.add(new Paragraph("Inventory Report\n\n"));

			float[] col = { 50, 100, 100, 50, 50, 100 };
			Table tablePdf = new Table(col);

			tablePdf.addCell("ID");
			tablePdf.addCell("Name");
			tablePdf.addCell("Company");
			tablePdf.addCell("Stock");
			tablePdf.addCell("Price");
			tablePdf.addCell("Expiry");

			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < model.getColumnCount(); j++) {
					tablePdf.addCell(model.getValueAt(i, j).toString());
				}
			}

			doc.add(tablePdf);
			doc.close();

			JOptionPane.showMessageDialog(this, "PDF Generated Successfully!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == searchBtn) {
			searchData(tSearch.getText().trim());
		}

		if (e.getSource() == refreshBtn) {
			tSearch.setText("");
			loadData();
		}

		if (e.getSource() == pdfBtn) {
			generatePDF();
		}

		if (e.getSource() == closeBtn) { // ✅ close logic
			dispose();
		}
	}

	public static void main(String[] args) {
		new InventoryReport();
	}
}