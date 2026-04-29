package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

// iText7 Imports
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class LowStockAlert extends JFrame implements ActionListener {

	JTable table;
	DefaultTableModel model;

	JButton loadBtn, pdfBtn, closeBtn; // ✅ closeBtn add kelay

	LowStockAlert() {

		setTitle("Low Stock Report");
		setLayout(null);

		JLabel title = new JLabel("Low Stock Medicines");
		title.setBounds(300, 10, 200, 30);
		add(title);

		loadBtn = new JButton("Load Low Stock");
		pdfBtn = new JButton("Generate PDF");
		closeBtn = new JButton("Close"); // ✅ button create

		loadBtn.setBounds(150, 50, 150, 30);
		pdfBtn.setBounds(320, 50, 150, 30);
		closeBtn.setBounds(490, 50, 150, 30); // ✅ position

		add(loadBtn);
		add(pdfBtn);
		add(closeBtn); // ✅ add to frame

		loadBtn.addActionListener(this);
		pdfBtn.addActionListener(this);
		closeBtn.addActionListener(this); // ✅ action

		// Table
		model = new DefaultTableModel();
		table = new JTable(model);

		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Company");
		model.addColumn("Stock");
		model.addColumn("Price");

		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 100, 750, 400);
		add(sp);

		setSize(800, 600);
		setVisible(true);
	}

	void loadLowStock() {
		try {
			model.setRowCount(0);

			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con
					.prepareStatement("SELECT id,name,company,stock,price FROM medicine WHERE stock < 10");

			ResultSet rs = ps.executeQuery();

			String lowStockNames = "";

			while (rs.next()) {

				String name = rs.getString("name");

				model.addRow(new Object[] { rs.getInt("id"), name, rs.getString("company"), rs.getInt("stock"),
						rs.getDouble("price") });

				lowStockNames += name + "\n";
			}

			if (!lowStockNames.equals("")) {
				JOptionPane.showMessageDialog(this, "⚠ Low Stock Medicines:\n\n" + lowStockNames);
			} else {
				JOptionPane.showMessageDialog(this, "✅ No Low Stock Medicines");
			}

			con.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	void generatePDF() {
		try {
			PdfWriter writer = new PdfWriter("C:\\Users\\Admin\\OneDrive\\Desktop\\LowStockReport.pdf");
			PdfDocument pdf = new PdfDocument(writer);
			Document doc = new Document(pdf);

			doc.add(new Paragraph("Low Stock Report\n\n"));

			float[] col = { 50, 150, 150, 80, 80 };
			Table tablePdf = new Table(col);

			tablePdf.addCell("ID");
			tablePdf.addCell("Name");
			tablePdf.addCell("Company");
			tablePdf.addCell("Stock");
			tablePdf.addCell("Price");

			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < model.getColumnCount(); j++) {
					tablePdf.addCell(model.getValueAt(i, j).toString());
				}
			}

			doc.add(tablePdf);
			doc.close();

			JOptionPane.showMessageDialog(this, "✅ PDF Generated!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loadBtn) {
			loadLowStock();
		}

		if (e.getSource() == pdfBtn) {
			generatePDF();
		}

		if (e.getSource() == closeBtn) { // ✅ close logic
			dispose(); // window close
		}
	}

	public static void main(String[] args) {
		new LowStockAlert();
	}
}