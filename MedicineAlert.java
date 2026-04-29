package application;

import java.sql.*;
import javax.swing.*;
import java.util.Date;

public class MedicineAlert {

	public static void checkAlerts() {

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM medicine";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			String message = "";

			while (rs.next()) {

				String name = rs.getString("name");
				int stock = rs.getInt("stock");
				Date expiry = rs.getDate("expiry");

				// Low stock
				if (stock < 10) {
					message += "⚠ Low Stock: " + name + " (Stock: " + stock + ")\n";
				}

				// Expired
				if (expiry.before(new Date())) {
					message += "❌ Expired: " + name + " (Date: " + expiry + ")\n";
				}

				// Expiring in next 7 days
				long diff = expiry.getTime() - new Date().getTime();
				long days = diff / (1000 * 60 * 60 * 24);

				if (days >= 0 && days <= 7) {
					message += "⏳ Expiring Soon: " + name + " (Date: " + expiry + ")\n";
				}
			}

			// Show popup
			if (!message.equals("")) {
				JOptionPane.showMessageDialog(null, message);
			} else {
				JOptionPane.showMessageDialog(null, "All medicines are safe ✅");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}