package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {

    JButton b1,b2,b3,b4,b5,b6,b7,logout;

    Dashboard() {
        setTitle("Pharmacy Desktop Application");
        setLayout(null);

        // ✅ Top Title
        JLabel title = new JLabel("Pharmacy Desktop Application", JLabel.CENTER);
        title.setBounds(0,0,900,50);
        title.setOpaque(true);
        title.setBackground(new Color(0,102,102));
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial",Font.BOLD,20));
        add(title);

        // ✅ Left Panel (Buttons)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8,1,5,5));
        panel.setBounds(0,50,200,411);
        panel.setBackground(Color.WHITE);

        b1=new JButton("View Inventory");
        b2=new JButton("Add Medicine");
        b3=new JButton("Edit Medicine");
        b4=new JButton("Delete Medicine");
        b5=new JButton("Show Alerts");
        b6=new JButton("Generate Bill");
        b7=new JButton("Generate Reports");
        logout=new JButton("Logout");

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(b5);
        panel.add(b6);
        panel.add(b7);
        panel.add(logout);

        add(panel);

        // ✅ Right Side Image
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("bg.jpg"));
        Image img = imgIcon.getImage().getScaledInstance(700,450,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBounds(200,50,700,450);
        add(imageLabel);

        // ✅ Text over image
        JLabel text = new JLabel("Generic Plus Pharmacy");
        text.setBounds(350,220,400,50);
        text.setFont(new Font("Arial",Font.BOLD,30));
        text.setForeground(Color.BLACK);
        imageLabel.add(text);

        // Actions
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);
        logout.addActionListener(this);

        setSize(900,500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==b1) new ViewInventory();
        if(e.getSource()==b2) new AddMedicine();
        if(e.getSource()==b3) new EditMedicine();
        if(e.getSource()==b4) new DeleteMedicine();
        if(e.getSource()==b5) MedicineAlert.checkAlerts();
        if(e.getSource()==b6) new GenerateBill();
        if(e.getSource()==b7) new ReportMenu();
        if(e.getSource()==logout){
            new Login();
            dispose();
        }
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}