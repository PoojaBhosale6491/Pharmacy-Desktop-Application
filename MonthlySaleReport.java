package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

// ✅ iText7 Imports
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class MonthlySaleReport extends JFrame implements ActionListener {

	JTable table;
	DefaultTableModel model;

	JComboBox<String> monthBox;
	JTextField yearField;

	JButton loadBtn, pdfBtn, closeBtn;

	MonthlySaleReport() {

		setTitle("Monthly Sale Report");
		setLayout(null);

		JLabel title = new JLabel("Monthly Sale Report");
		title.setBounds(350, 10, 200, 30);
		add(title);

		// Month
		JLabel l1 = new JLabel("Select Month:");
		l1.setBounds(50, 60, 100, 30);
		add(l1);

		String months[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		monthBox = new JComboBox<>(months);
		monthBox.setBounds(150, 60, 100, 30);
		add(monthBox);

		// Year
		JLabel l2 = new JLabel("Year:");
		l2.setBounds(280, 60, 50, 30);
		add(l2);

		yearField = new JTextField();
		yearField.setBounds(330, 60, 100, 30);
		add(yearField);

		// Buttons
		loadBtn = new JButton("Load Report");
		pdfBtn = new JButton("Generate PDF");
		closeBtn = new JButton("Close");

		loadBtn.setBounds(460, 60, 130, 30);
		pdfBtn.setBounds(610, 60, 150, 30);
		closeBtn.setBounds(770, 60, 100, 30);

		add(loadBtn);
		add(pdfBtn);
		add(closeBtn);

		loadBtn.addActionListener(this);
		pdfBtn.addActionListener(this);
		closeBtn.addActionListener(this);

		// Table
		model = new DefaultTableModel();
		table = new JTable(model);

		model.addColumn("ID");
		model.addColumn("Medicine");
		model.addColumn("Quantity");
		model.addColumn("Total Price");
		model.addColumn("Date");

		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 120, 850, 400);
		add(sp);

		setSize(920, 600); // ✅ increased size
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// ✅ LOAD DATA
	void loadMonthlyData(String month, String year) {
		try {
			model.setRowCount(0);

			Connection con = DBConnection.getConnection();

			PreparedStatement ps = con.prepareStatement("SELECT * FROM sales WHERE MONTH(date)=? AND YEAR(date)=?");

			ps.setInt(1, Integer.parseInt(month));
			ps.setInt(2, Integer.parseInt(year));

			ResultSet rs = ps.executeQuery();

			double totalSales = 0;

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("medicine"), rs.getInt("qty"),
						rs.getDouble("total"), rs.getTimestamp("date") });

				totalSales += rs.getDouble("total");
			}

			JOptionPane.showMessageDialog(this, "Total Monthly Sales = ₹" + totalSales);

			con.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	// ✅ PDF GENERATION
	void generatePDF() {
		try {
			PdfWriter writer = new PdfWriter("C:\\Users\\Admin\\OneDrive\\Desktop\\MonthlySale.pdf");
			PdfDocument pdf = new PdfDocument(writer);
			Document doc = new Document(pdf);

			doc.add(new Paragraph("Monthly Sales Report\n\n"));

			float[] col = { 50, 100, 80, 100, 120 };
			Table pdfTable = new Table(col);

			// Header
			pdfTable.addCell("ID");
			pdfTable.addCell("Medicine");
			pdfTable.addCell("Qty");
			pdfTable.addCell("Total");
			pdfTable.addCell("Date");

			// Data
			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < model.getColumnCount(); j++) {
					pdfTable.addCell(model.getValueAt(i, j).toString());
				}
			}

			doc.add(pdfTable);
			doc.close();

			JOptionPane.showMessageDialog(this, "PDF Generated on Desktop!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loadBtn) {
			String month = monthBox.getSelectedItem().toString();
			String year = yearField.getText().trim();

			if (year.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Enter Year!");
				return;
			}

			loadMonthlyData(month, year);
		}

		if (e.getSource() == pdfBtn) {
			generatePDF();
		}

		if (e.getSource() == closeBtn) {
			dispose(); // ✅ close window
		}
	}

	public static void main(String[] args) {
		new MonthlySaleReport();
	}
}