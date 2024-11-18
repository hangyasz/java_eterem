package megjelenites.menu;

import megjelenites.ButtonEditor;
import megjelenites.ButtonRenderer;
import megjelenites.login.login_side;
import megjelenites.oszetevok.m_oszetevok;
import menu.menu;
import menu.MenuType;
import raktar.raktar;
import role.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.EventObject;

public class m_menu extends JFrame {
    private List<menu> menus;
    private final List<raktar> raktars;
    private final JTextField filterField;
    private final JTable table;
    private List<menu> filteredMenus;
    private User authenticatedUser;
    private List<User> users;

    public m_menu(List<menu> menus, List<raktar> raktars, List<User> users, User authenticatedUser) {
        this.menus = menus;
        this.raktars = raktars;
        this.filteredMenus = menus; // Initialize filteredMenus to menus
        this.authenticatedUser = authenticatedUser;
        this.users = users;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Menü");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Menü"));
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.add(new JLabel("Szűrés:"));
        filterField = new JTextField(40);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        table = new JTable(new menu_Tabla(filteredMenus));
        setButtonEditorAndRenderer(table);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panel for adding new menu items
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addPanel.add(new JLabel("Típus:"));
        JComboBox<MenuType> typeComboBox = new JComboBox<>(MenuType.values());
        addPanel.add(typeComboBox);
        addPanel.add(new JLabel("Név:"));
        JTextField nameField = new JTextField(20);
        addPanel.add(nameField);
        addPanel.add(new JLabel("Ár:"));
        JTextField priceField = new JTextField(20);
        addPanel.add(priceField);
        JButton addButton = new JButton("Hozzáadás");
        addPanel.add(addButton);
        centerPanel.add(addPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String priceText = priceField.getText();
            MenuType type = (MenuType) typeComboBox.getSelectedItem();
            if (name.isEmpty() || priceText.isEmpty() || type == null) {
                JOptionPane.showMessageDialog(this, "Minden mezőt ki kell tölteni!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int price = Integer.parseInt(priceText);
                if (price <= 0) {
                    throw new NumberFormatException();
                }
                menu newMenu = new menu(name, price, new ArrayList<>(), type);
                menus.add(newMenu);
                filterTable(); // Update the table after adding a new item
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Érvénytelen ár!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
        pack(); // Adjust the frame size to fit the preferred sizes of its components
    }

    private void filterTable() {
        String filterText = filterField.getText();
        if (filterText.isEmpty()) {
            filteredMenus = menus;
        } else {
            filteredMenus = menus.stream()
                    .filter(menu -> menu.getNev().toLowerCase().contains(filterText.toLowerCase()))
                    .collect(Collectors.toList());
        }
        table.setModel(new menu_Tabla(filteredMenus));
        setButtonEditorAndRenderer(table);
    }

    private void setButtonEditorAndRenderer(JTable table) {
        // Column 1 (MenuType) - Add permission check
        TableColumn typeColumn = table.getColumnModel().getColumn(1);
        JComboBox<MenuType> menuTypeCombo = new JComboBox<>(MenuType.values());

        // Custom cell editor with permission check
        DefaultCellEditor menuTypeEditor = new DefaultCellEditor(menuTypeCombo) {
            @Override
            public boolean isCellEditable(EventObject e) {
                if (!exes.ratar_menuex(authenticatedUser)) {
                    if (!magasab()) {
                        return false;
                    }
                }
                return super.isCellEditable(e);
            }
        };

        typeColumn.setCellEditor(menuTypeEditor);
        typeColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof MenuType) {
                    setText(((MenuType) value).name());
                } else {
                    super.setValue(value);
                }
            }
        });

        // Column 5 (Delete button) - Add permission check
        TableColumn buttonTorles = table.getColumnModel().getColumn(5);
        buttonTorles.setCellRenderer(new ButtonRenderer());
        buttonTorles.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            if (!exes.ratar_menuex(authenticatedUser)) {
                if (!magasab()) {
                    return;
                }
            }

            int row = table.convertRowIndexToModel(table.getSelectedRow());
            menu menuToRemove = filteredMenus.get(row);
            menus.remove(menuToRemove);
            filterTable();
        }));

        // Column 4 (Ingredients button) - No permission check needed
        TableColumn buttonoszetevok = table.getColumnModel().getColumn(4);
        buttonoszetevok.setCellRenderer(new ButtonRenderer());
        buttonoszetevok.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            menu menu = filteredMenus.get(row);
            new m_oszetevok(menu.getOszetevok(), raktars);
            setButtonEditorAndRenderer(table);
        }));
    }

    private boolean magasab() {
        login_side login = new login_side(users);
        User user = login.showLoginDialog(this);
        if (user == null) {
            return false;
        }
        if (!exes.ratar_menuex(user)) {
            JOptionPane.showMessageDialog(this, "Nincs hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}