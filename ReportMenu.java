package application;

import javax.swing.*;
import java.awt.event.*;

public class ReportMenu extends JFrame implements ActionListener {

	JButton b1, b2, b3, b4, close;

	ReportMenu() {

		setTitle("Reports Menu");
		setSize(400, 400);
		setLayout(null);

		JLabel title = new JLabel("Select Report");
		title.setBounds(130, 20, 200, 30);
		add(title);

		b1 = new JButton("Inventory Report");
		b2 = new JButton("Monthly Sale Report");
		b3 = new JButton("Expired Medicine Report");
		b4 = new JButton("Low Stock Report");
		close = new JButton("Close");

		b1.setBounds(80, 70, 220, 40);
		b2.setBounds(80, 120, 220, 40);
		b3.setBounds(80, 170, 220, 40);
		b4.setBounds(80, 220, 220, 40);
		close.setBounds(130, 280, 120, 35);

		add(b1);
		add(b2);
		add(b3);
		add(b4);
		add(close);

		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		close.addActionListener(this);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == b1) {
			new InventoryReport();
		}

		if (e.getSource() == b2) {
			new MonthlySaleReport();
		}

		if (e.getSource() == b3) {
			new ExpiredMedicineReport();
		}

		if (e.getSource() == b4) {
			new LowStockAlert(); // 👉 ensure this class exists
		}

		if (e.getSource() == close) {
			dispose();
		}
	}
}