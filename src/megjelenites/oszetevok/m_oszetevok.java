package megjelenites.oszetevok;

import megjelenites.ButtonEditor;
import megjelenites.ButtonRenderer;
import oszetevok.oszetevok;
import raktar.raktar;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class m_oszetevok extends JFrame {

    private final List<oszetevok> oszetevok_lista;

    public m_oszetevok(List<oszetevok> oszetevok_lista, List<raktar> raktars) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.oszetevok_lista = oszetevok_lista;
        setTitle("Összetevők");
        setSize(800, 400);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Összetevők"));
        add(topPanel, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        JTable table = new JTable(new oszetevok_Tabla(oszetevok_lista));
        JScrollPane scrollPane = new JScrollPane(table);
        center.add(scrollPane, BorderLayout.CENTER);

        // Panel for adding new ingredient
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JComboBox<raktar> ingredientComboBox;
        if (raktars.isEmpty()) {
            ingredientComboBox = new JComboBox<>();
        } else {
            ingredientComboBox = new JComboBox<>(new DefaultComboBoxModel<>(raktars.toArray(new raktar[0])));
        }
        ingredientComboBox.setEditable(true);
        JTextField quantityField = new JTextField(5);
        JLabel quantityLabel = new JLabel(raktars.isEmpty() ? "" : String.valueOf(raktars.get(0).getMertekegyseg())); // Set the quantity label to the first element's quantity
        JButton addButton = new JButton("Hozzáadás");
        addPanel.add(new JLabel("Összetevő:"));
        addPanel.add(ingredientComboBox);
        addPanel.add(new JLabel("Mertek egység"));
        addPanel.add(quantityLabel);
        addPanel.add(new JLabel("Mennyiség:"));
        addPanel.add(quantityField);
        addPanel.add(addButton);
        center.add(addPanel, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);

        // Gomb renderer és editor beállítása
        setButtonEditorAndRenderer(table);

        // Add button action
        addButton.addActionListener(e -> {
            raktar selectedIngredient = (raktar) ingredientComboBox.getSelectedItem();
            if (selectedIngredient == null) {
                JOptionPane.showMessageDialog(this, "Nincs kiválasztva összetevő!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double quantity;
            try {
                quantity = Double.parseDouble(quantityField.getText());
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Érvénytelen mennyiség!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (oszetevok_lista.stream().anyMatch(oszetevok -> oszetevok.getNev().equals(selectedIngredient.getNev()))) {
                JOptionPane.showMessageDialog(this, "Az összetevő már szerepel a listában!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            oszetevok newOszetevok = new oszetevok(selectedIngredient, quantity);
            oszetevok_lista.add(newOszetevok);
            table.setModel(new oszetevok_Tabla(oszetevok_lista));
            setButtonEditorAndRenderer(table); // Újra beállítjuk a gombokat
        });

        // Add filtering functionality to the combo box
        JTextField editorComponent = (JTextField) ingredientComboBox.getEditor().getEditorComponent();
        editorComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = editorComponent.getText();
                List<raktar> filteredItems = raktars.stream()
                        .filter(item -> item.getNev().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
                ingredientComboBox.setModel(new DefaultComboBoxModel<>(filteredItems.toArray(new raktar[0])));
                editorComponent.setText(text);
                ingredientComboBox.showPopup();
            }
        });

        // Update quantity label when a new item is selected
        ingredientComboBox.addActionListener(e -> {
            raktar selectedIngredient = (raktar) ingredientComboBox.getSelectedItem();
            if (selectedIngredient != null) {
                quantityLabel.setText(String.valueOf(selectedIngredient.getMertekegyseg()));
            }
        });
    }

    private void setButtonEditorAndRenderer(JTable table) {
        TableColumn buttonColumn = table.getColumnModel().getColumn(3);
        buttonColumn.setCellRenderer(new ButtonRenderer());
        buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            oszetevok oszetevok1 = oszetevok_lista.get(table.getSelectedRow());
            oszetevok_lista.remove(oszetevok1);
            table.setModel(new oszetevok_Tabla(oszetevok_lista));
            setButtonEditorAndRenderer(table); // Újra beállítjuk a gombokat
        }));
    }
}