package gui;

import GUI.Inicio;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agents.BookSellerAgent;
import jade.core.AID;

public class BookSellerGui extends JFrame {

    private BookSellerAgent myAgent;
    private JTextField titleField, priceField;

    public BookSellerGui(BookSellerAgent a, Inicio in) {
        super(a.getLocalName());
        myAgent = a;
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.add(new JLabel("Título del libro:"));
        titleField = new JTextField(15);
        p.add(titleField);
        p.add(new JLabel("Precio:"));
        priceField = new JTextField(15);
        p.add(priceField);
        getContentPane().add(p, BorderLayout.CENTER);

        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    String title = titleField.getText().trim();
                    String price = priceField.getText().trim();
                    JOptionPane.showConfirmDialog(null, "\"Libro [\" + title + \"] insertado con el precio $\" + price");
                    myAgent.updateCatalogue(title, Integer.parseInt(price));
                    titleField.setText("");
                    priceField.setText("");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(BookSellerGui.this, "Valores inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        p = new JPanel();
        p.add(addButton);
        getContentPane().add(p, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });

        setResizable(false);
    }

    public BookSellerGui(AID name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;

        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
