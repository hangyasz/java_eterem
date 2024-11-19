package megjelenites.raktar;

import megjelenites.ButtonEditor;
import megjelenites.ButtonRenderer;
import menu.menu;
import raktar.raktar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class m_raktar extends JFrame {
    private final List<raktar> raktars;
    private final List<menu> menus;
    private final JTextField filterField;
    private final JTable table;

    public m_raktar(List<raktar> raktars, List<menu> menus) {
        this.raktars = raktars;
        this.menus = menus;
        // Ablak beállítása
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Raktár");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        // Felső panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Raktár"));
        add(topPanel, BorderLayout.NORTH);

        // Középső panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        // Szűrő panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.add(new JLabel("Szűrés:"));
        filterField = new JTextField(40);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        // Táblázat
        table = new JTable(new raktar_Tabla(raktars));
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        // Hozzáadás panel
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addPanel.add(new JLabel("Név:"));
        JTextField addField = new JTextField(10);
        addPanel.add(addField);
        addPanel.add(new JLabel("Mértékegység:"));
        JTextField addField2 = new JTextField(10);
        addPanel.add(addField2);
        addPanel.add(new JLabel("Mennyiség:"));
        JTextField addField3 = new JTextField(10);
        addPanel.add(addField3);
        JButton addButton = new JButton("Hozzáad");
        addPanel.add(addButton);
        centerPanel.add(addPanel, BorderLayout.SOUTH);

        // Szűrő mező eseménykezelője
        filterField.getDocument().addDocumentListener(new FilterFieldListener());

        // Alsó panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        // Gomb renderer és editor beállítása
        setButtonEditorAndRenderer(table);

        // Hozzáadás gomb eseménykezelője
        addButton.addActionListener(e -> {
            // Ellenőrzés, hogy a kötelező mezők ki vannak-e töltve
            if (addField.getText().isEmpty() || addField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(m_raktar.this, "Minden mező kitöltése kötelező!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nev = addField.getText();
            String mertekegyseg = addField2.getText();
            double mennyiseg;
            try {
                // Mennyiség ellenőrzése
                if (addField3.getText().isEmpty()) {
                    mennyiseg = 0;
                } else {
                    mennyiseg = Double.parseDouble(addField3.getText());
                    if (mennyiseg < 0) {
                        JOptionPane.showMessageDialog(m_raktar.this, "A mennyiség nem lehet negatív!", "Hiba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(m_raktar.this, "Érvénytelen mennyiség!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Ellenőrzés, hogy létezik-e már ilyen nevű termék
            for (raktar r : raktars) {
                if (r.getNev().equalsIgnoreCase(nev)) {
                    JOptionPane.showMessageDialog(m_raktar.this, "Már létezik ilyen nevű termék a raktárban!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            // Raktár hozzáadása
            raktars.add(new raktar(nev, mertekegyseg, mennyiseg));
            // Táblázat frissítése
            filterTable();
            // Mezők ürítése
            addField.setText("");
            addField2.setText("");
            addField3.setText("");

        });
    }

    // Táblázat frissítése szűrés után
    private void filterTable() {
        String filterText = filterField.getText();
        List<raktar> filteredRaktars = raktars.stream()
                .filter(r -> r.getNev().toLowerCase().contains(filterText.toLowerCase()))
                .collect(Collectors.toList());
        table.setModel(new raktar_Tabla(filteredRaktars));
        setButtonEditorAndRenderer(table);
    }

    // Torlés gomb beállítása
    private void setButtonEditorAndRenderer(JTable table) {
        TableColumn buttonColumn = table.getColumnModel().getColumn(4);
        buttonColumn.setCellRenderer(new ButtonRenderer());
        // Törlés gomb eseménykezelője
        buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            // Kiválasztott sor lekérése
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            raktar raktarToRemove = raktars.get(row);
            // Ellenőrzés, hogy az összetevő szerepel-e valamelyik menüben
            List<String> containingMenus = menus.stream()
                    .filter(m -> m.getOszetevok().stream().anyMatch(o -> o.getNev().equals(raktarToRemove.getNev())))
                    .map(menu::getNev)
                    .collect(Collectors.toList());

            // Ha szerepel valamelyik menüben, akkor nem lehet törölni és hibaüzenetet jelenítünk
            if (!containingMenus.isEmpty()) {
                //kiírjuk az ételeket
                JTextArea textArea = new JTextArea(String.join("\n", containingMenus));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(300, 200));
                JOptionPane.showMessageDialog(this, scrollPane, "Nem lehet törölni, mert az alábbi ételek tartalmazzák:", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Törlés
            raktars.remove(raktarToRemove);
            filterTable();
        }));
    }



    // Szűrő mező eseménykezelője
    private class FilterFieldListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filterTable();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filterTable();
        }
    }
}