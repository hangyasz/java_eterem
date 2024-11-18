package megjelenites;

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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Raktár");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Raktár"));
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.add(new JLabel("Szűrés:"));
        filterField = new JTextField(40);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

        table = new JTable(new raktar_Tabla(raktars));
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

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

        addButton.addActionListener(new AddButtonListener(addField, addField2, addField3));

        filterField.getDocument().addDocumentListener(new FilterFieldListener());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        setButtonEditorAndRenderer(table);
    }

    private void filterTable() {
        String filterText = filterField.getText();
        List<raktar> filteredRaktars = raktars.stream()
                .filter(r -> r.getNev().toLowerCase().contains(filterText.toLowerCase()))
                .collect(Collectors.toList());
        table.setModel(new raktar_Tabla(filteredRaktars));
        setButtonEditorAndRenderer(table);
    }

    private void setButtonEditorAndRenderer(JTable table) {
        TableColumn buttonColumn = table.getColumnModel().getColumn(4);
        buttonColumn.setCellRenderer(new ButtonRenderer());
        buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            raktar raktarToRemove = raktars.get(row);
            List<String> containingMenus = menus.stream()
                    .filter(m -> m.getOszetevok().stream().anyMatch(o -> o.getNev().equals(raktarToRemove.getNev())))
                    .map(menu::getNev)
                    .collect(Collectors.toList());

            if (!containingMenus.isEmpty()) {
                JTextArea textArea = new JTextArea(String.join("\n", containingMenus));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(300, 200));
                JOptionPane.showMessageDialog(this, scrollPane, "Nem lehet törölni, mert az alábbi ételek tartalmazzák:", JOptionPane.ERROR_MESSAGE);
                return;
            }

            raktars.remove(raktarToRemove);
            filterTable();
        }));
    }

    private class AddButtonListener implements ActionListener {
        private final JTextField addField;
        private final JTextField addField2;
        private final JTextField addField3;

        public AddButtonListener(JTextField addField, JTextField addField2, JTextField addField3) {
            this.addField = addField;
            this.addField2 = addField2;
            this.addField3 = addField3;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (addField.getText().isEmpty() || addField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(m_raktar.this, "Minden mező kitöltése kötelező!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String nev = addField.getText();
            String mertekegyseg = addField2.getText();
            double mennyiseg;
            try {
                if (addField3.getText().isEmpty()) {
                    mennyiseg = 0;
                } else {
                    mennyiseg = Double.parseDouble(addField3.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(m_raktar.this, "Érvénytelen mennyiség!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (raktar r : raktars) {
                if (r.getNev().equalsIgnoreCase(nev)) {
                    JOptionPane.showMessageDialog(m_raktar.this, "Már létezik ilyen nevű termék a raktárban!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            raktars.add(new raktar(nev, mertekegyseg, mennyiseg));
            filterTable();
            addField.setText("");
            addField2.setText("");
            addField3.setText("");
        }
    }

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