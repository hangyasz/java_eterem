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

import static role.exes.magasab;

public class m_menu extends JFrame {
    private List<menu> menus;
    private final List<raktar> raktars;
    private final JTextField filterField;
    private final JTable table;
    private List<menu> filteredMenus;
    private User authenticatedUser;
    private List<User> users;
    private JFrame frame;

    public m_menu(List<menu> menus, List<raktar> raktars, List<User> users, User authenticatedUser) {
        //privát változók beállítása
        frame = this;
        this.menus = menus;
        this.raktars = raktars;
        this.filteredMenus = menus;
        this.authenticatedUser = authenticatedUser;
        this.users = users;
        //ablak beállítása
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Menü");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Menü"));
        add(topPanel, BorderLayout.NORTH);
        setVisible(true);

        //szűrő mező
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterPanel.add(new JLabel("Szűrés:"));
        filterField = new JTextField(40);
        filterPanel.add(filterField);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        //tábla létrehozása
        table = new JTable(new menu_Tabla(filteredMenus));
        setButtonEditorAndRenderer(table);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //menü hozzáadása panel
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

        //vissza gomb
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        //szűrő mező figyelése és változás esetén a tábla frissítése
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

        //menü hozzáadása gomb lenyomás esemény feltételek ellenőrzése
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
                //menü hozzáadása a listához alapanyagok még nincsenek hozzáadva
                menu newMenu = new menu(name, price, new ArrayList<>(), type);
                menus.add(newMenu);
                //tábla frissítése
                filterTable();
                //mezők ürítése
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                //hiba esetén hibaüzenet
                JOptionPane.showMessageDialog(this, "Érvénytelen ár!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
    }

    //tábla frissítése szűrés alapján
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

    //gomb használata táblázatbol
    private void setButtonEditorAndRenderer(JTable table) {
        // dropbox szerkesztő
        TableColumn typeColumn = table.getColumnModel().getColumn(1);
        JComboBox<MenuType> menuTypeCombo = new JComboBox<>(MenuType.values());

        //chekbox szerkesztő
        DefaultCellEditor menuTypeEditor = new DefaultCellEditor(menuTypeCombo) {
            //hozzáférés ellenőrzése
            @Override
            public boolean isCellEditable(EventObject e) {
                if (!exes.ratar_menuex(authenticatedUser)) {
                    if (!magasab(users, frame)) {
                        return false;
                    }
                }
                return super.isCellEditable(e);
            }
        };
        //beállijuk váltotathatová
        typeColumn.setCellEditor(menuTypeEditor);
        //cella megjelenítő és érték beállítása
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

        //Törlés gomb
        TableColumn buttonTorles = table.getColumnModel().getColumn(5);
        buttonTorles.setCellRenderer(new ButtonRenderer());
        buttonTorles.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            //hozzáférés ellenőrzése
            if (!exes.ratar_menuex(authenticatedUser)) {
                if (!magasab(users, frame)) {
                    return;
                }
            }
            //lekérjük a kiválasztott sort
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            menu menuToRemove = filteredMenus.get(row);
            //töröljük a menüt
            menus.remove(menuToRemove);
            //frissítjük a táblát
            filterTable();
        }));

        // Összetevők gomb
        TableColumn buttonoszetevok = table.getColumnModel().getColumn(4);
        buttonoszetevok.setCellRenderer(new ButtonRenderer());
        buttonoszetevok.setCellEditor(new ButtonEditor(new JCheckBox(), e -> {
            //megjelenítjük az adot menü összetevőit
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            menu menu = filteredMenus.get(row);
            new m_oszetevok(menu.getOszetevok(), raktars);
            //frissítjük a táblát gombjait
            setButtonEditorAndRenderer(table);
        }));
    }


}