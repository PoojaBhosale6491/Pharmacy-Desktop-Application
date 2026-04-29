package application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

// ✅ iText 7 Imports
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

public class ExpiredMedicineReport extends JFrame implements ActionListener {

	JTable table;
	DefaultTableModel model;

	JComboBox<String> monthBox;
	JTextField yearField;

	JButton loadBtn, closeBtn, pdfBtn;

	public ExpiredMedicineReport() {

		setTitle("Expired Medicines Report");
		setSize(950, 520);
		setLayout(null);

		JLabel title = new JLabel("Expired Medicines Report");
		title.setBounds(350, 10, 300, 30);
		add(title);

		JLabel mLabel = new JLabel("Select Month:");
		mLabel.setBounds(50, 50, 120, 30);
		add(mLabel);

		String months[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		monthBox = new JComboBox<>(months);
		monthBox.setBounds(160, 50, 80, 30);
		add(monthBox);

		JLabel yLabel = new JLabel("Year:");
		yLabel.setBounds(260, 50, 50, 30);
		add(yLabel);

		yearField = new JTextField();
		yearField.setBounds(310, 50, 100, 30);
		add(yearField);

		loadBtn = new JButton("Load Report");
		loadBtn.setBounds(430, 50, 130, 30);
		loadBtn.addActionListener(this);
		add(loadBtn);

		pdfBtn = new JButton("Generate PDF");
		pdfBtn.setBounds(580, 50, 150, 30);
		pdfBtn.addActionListener(this); // ✅ IMPORTANT
		add(pdfBtn);

		closeBtn = new JButton("Close");
		closeBtn.setBounds(750, 50, 100, 30);
		closeBtn.addActionListener(this);
		add(closeBtn);

		model = new DefaultTableModel();
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 100, 900, 350);
		add(sp);

		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Company");
		model.addColumn("Batch");
		model.addColumn("Stock");
		model.addColumn("Price");
		model.addColumn("Category");
		model.addColumn("Expiry Date");

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loadBtn) {
			loadExpiredData();
		}

		// ✅ PDF BUTTON ACTION
		if (e.getSource() == pdfBtn) {
			generatePDF();
		}

		if (e.getSource() == closeBtn) {
			dispose();
		}
	}

	void loadExpiredData() {

		try {
			model.setRowCount(0);

			String month = (String) monthBox.getSelectedItem();
			String year = yearField.getText();

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root",
					"poojabhosale");

			String query = "SELECT * FROM medicine WHERE expiry < CURDATE() AND MONTH(expiry)=? AND YEAR(expiry)=?";
			PreparedStatement pst = con.prepareStatement(query);

			pst.setString(1, month);
			pst.setString(2, year);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("id"), rs.getString("name"), rs.getString("company"),
						rs.getString("batchId"), rs.getInt("stock"), rs.getDouble("price"), rs.getString("category"),
						rs.getDate("expiry") });
			}

			con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading data");
		}
	}

	// ✅ iText 7 PDF METHOD
	void generatePDF() {
		try {

			String path = System.getProperty("user.home") + "\\OneDrive\\Desktop\\ExpiredMedicineReport.pdf";

			PdfWriter writer = new PdfWriter(path);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			// Title
			document.add(new Paragraph("Expired Medicines Report").setBold().setFontSize(18));
			document.add(new Paragraph("\n"));

			int cols = table.getColumnCount();
			Table pdfTable = new Table(cols);

			// Headers
			for (int i = 0; i < cols; i++) {
				pdfTable.addCell(new Cell().add(new Paragraph(table.getColumnName(i))));
			}

			// Data
			for (int rows = 0; rows < table.getRowCount(); rows++) {
				for (int c = 0; c < cols; c++) {
					Object value = table.getValueAt(rows, c);
					pdfTable.addCell(new Cell().add(new Paragraph(value == null ? "" : value.toString())));
				}
			}

			document.add(pdfTable);
			document.close();

			JOptionPane.showMessageDialog(this, "PDF Generated Successfully!");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error generating PDF");
		}
	}

	public static void main(String[] args) {
		new ExpiredMedicineReport();
	}
}