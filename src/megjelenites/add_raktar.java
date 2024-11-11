package megjelenites;


import javax.swing.*;
import java.util.List;

public class add_raktar extends JFrame {
    public add_raktar(List<raktar.raktar> raktars) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Raktár hozzáadása");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField nevField = new JTextField(20);
        JTextField mertekegysegField = new JTextField(20);
        JTextField mennyisegField = new JTextField(20);

        panel.add(new JLabel("Név:"));
        panel.add(nevField);
        panel.add(new JLabel("Mértékegység:"));
        panel.add(mertekegysegField);
        panel.add(new JLabel("Mennyiség:"));
        panel.add(mennyisegField);

        JButton addButton = new JButton("Hozzáadás");
        addButton.addActionListener(e -> {
            String nev = nevField.getText();
            String mertekegyseg = mertekegysegField.getText();
            double mennyiseg = Double.parseDouble(mennyisegField.getText());
            raktars.add(new raktar.raktar(nev, mertekegyseg, mennyiseg));
            JOptionPane.showMessageDialog(null, "Raktár hozzáadva");
            dispose();
        });
        panel.add(addButton);

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
