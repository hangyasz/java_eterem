// m_Etterem.java
package megjelenites.Eterem;

import asztal.asztal;
import megjelenites.kezd;
import megjelenites.login.login_side;
import megjelenites.raktar.m_raktar;
import menu.menu;
import menu.MenuType;
import oszetevok.oszetevok;
import role.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class m_Etterem extends JFrame {
    private final List<asztal> asztalok;
    private final List<menu> menus;
    private final JPanel ordersPanel;
    private double x_term = 50;
    private double y_term = 50;
    private List<User> users;
    private  User authenticatedUser;

    public m_Etterem(List<asztal> asztalok, double x_term, double y_term, List<menu> menus, List<User> users) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;
        this.menus = menus;
        this.users = users;

        setTitle("Étterem");
        setSize(900, 700);
        setMinimumSize(new Dimension(900, 700)); // Minimális méret beállítása
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);

        TeremPanel panel = new TeremPanel();
        add(panel, BorderLayout.CENTER);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setVisible(false);
        add(ordersPanel, BorderLayout.EAST);

        setVisible(true);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        Dimension size = getSize();
        ordersPanel.setPreferredSize(new Dimension(size.width / 3, size.height));
        ordersPanel.revalidate();
    }

    private void restoreOriginalLayout() {
        getContentPane().removeAll();
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);
        TeremPanel panel = new TeremPanel();
        add(panel, BorderLayout.CENTER);
        ordersPanel.removeAll();
        add(ordersPanel, BorderLayout.EAST);
        ordersPanel.setVisible(false);
        revalidate();
        repaint();
    }

    private class TeremPanel extends JPanel {
        private double zoomFactorX = 1.0;
        private double zoomFactorY = 1.0;

        public TeremPanel() {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension size = getSize();
                    zoomFactorX = size.width / (x_term * 10);
                    zoomFactorY = size.height / (y_term * 10);
                    revalidate();
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (asztal asztal : asztalok) {
                        if (e.getX() >= asztal.getX() * zoomFactorX && e.getX() <= (asztal.getX() + 50) * zoomFactorX &&
                                e.getY() >= asztal.getY() * zoomFactorY && e.getY() <= (asztal.getY() + 50) * zoomFactorY) {
                            showOrders(asztal);
                            break;
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(zoomFactorX, zoomFactorY);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, (int) (x_term * 10), (int) (y_term * 10));
            for (asztal asztal : asztalok) {
                if (asztal.getRendelesek().isEmpty()) {
                    g2d.setColor(Color.GREEN);
                } else {
                    g2d.setColor(Color.RED);
                }
                g2d.fillRect(asztal.getX(), asztal.getY(), 50, 50); // Fill the rectangle with the color
                g2d.setColor(Color.BLACK);
                g2d.drawRect(asztal.getX(), asztal.getY(), 50, 50); // Draw the border of the rectangle
                g2d.drawString(asztal.getNev(), asztal.getX() + 5, asztal.getY() + 25);
            }
        }
    }

    private void showOrders(asztal asztal) {
        ordersPanel.removeAll();
        ordersPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> ordersPanel.setVisible(false));
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        ordersPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        JButton payButton = new JButton("Fizet");
        JButton addOrderButton = new JButton("Új rendelés hozzáadása");
        addOrderButton.addActionListener(e ->
                {
                    login_side login = new login_side(users);
                    authenticatedUser = login.showLoginDialog(this);
                    if (authenticatedUser == null) {
                        return;
                    }
                    if (exes.asztalex(authenticatedUser)) {
                        showNewOrderPanel(asztal);
                    } else {
                        JOptionPane.showMessageDialog(this, "Hincsen hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
        payButton.addActionListener(e -> {
            if (!exes.ratar_menuex(authenticatedUser)) {
                if (!magasab()) {
                    return;
                }
            }
            asztal.getRendelesek().clear();
            ordersPanel.setVisible(false);
            JOptionPane.showMessageDialog(null, "Fizetendő oszeg: "+ asztal.getEretke()+ " Ft", "fizetés", JOptionPane.INFORMATION_MESSAGE);
            asztal.setEretke(0);
        });
        // Módosított rendelések lista panel
        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));

        // Beállítjuk a maximális szélességet a panelnek
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (menu item : asztal.getRendelesek()) {
            // Minden rendeléshez új panel, ami a teljes szélességet kitölti
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BorderLayout());
            orderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Fix magasság
            orderPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40)); // Fix magasság
            orderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Margók

            // Bal oldalra a szöveg
            JLabel name = new JLabel(item.getNev());
            orderPanel.add(name, BorderLayout.WEST);

            // Jobb oldalra a törlés gomb
            JLabel arr = new JLabel(item.getAr() + " Ft");
            orderPanel.add(arr, BorderLayout.EAST);

            // Adjunk hozzá egy elválasztó vonalat
            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            ordersListPanel.add(orderPanel);
        }

        // ScrollPane a görgetéshez
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);


        bottomPanel.add(addOrderButton);
        bottomPanel.add(totalLabel);
        bottomPanel.add(payButton);
        ordersPanel.add(bottomPanel, BorderLayout.SOUTH);

        ordersPanel.setVisible(true);
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void showNewOrderPanel(asztal asztal) {
        JPanel newOrderPanel = new JPanel(new BorderLayout());

        // Top panel - asztal neve
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tableNameLabel = new JLabel(asztal.getNev());
        topPanel.add(tableNameLabel);
        newOrderPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - menü elemek
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        newOrderPanel.add(centerPanel, BorderLayout.CENTER);

        // Right panel - rendelési információk
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        updateOrdersPanel(rightPanel, asztal, centerPanel);
        newOrderPanel.add(rightPanel, BorderLayout.EAST);

        // Left panel - menü típus gombok
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Nincs vízszintes görgetés
        newOrderPanel.add(leftScrollPane, BorderLayout.WEST);

        // Kiszámoljuk a bal panel teljes magasságát
        int totalHeight = getHeight()-topPanel.getHeight(); // Panel teljes magassága
        int buttonHeight = totalHeight / MenuType.values().length; // A gombok magassága a panel magasságának függvényében

        for (MenuType type : MenuType.values()) {
            JButton typeButton = new JButton(type.name());
            typeButton.addActionListener(event -> showMenuItems(type, asztal, centerPanel, rightPanel));

            // Gomb magasságának beállítása
            typeButton.setPreferredSize(new Dimension(150, buttonHeight)); // Fix szélesség és számolt magasság
            typeButton.setMaximumSize(new Dimension(150, buttonHeight));
            typeButton.setMinimumSize(new Dimension(150, buttonHeight));

            // Gomb hozzáadása a panelhez
            leftPanel.add(typeButton);
        }

        // A fő panel frissítése
        getContentPane().removeAll();
        getContentPane().add(newOrderPanel);
        revalidate();
        repaint();
    }

    private MenuType selectedType=null;

    // Update the selectedType variable in the showMenuItems method
    private void showMenuItems(MenuType type, asztal asztal, JPanel centerPanel, JPanel rightPanel) {
        selectedType = type; // Update the selected type
        centerPanel.removeAll();

        // Fix parameters
        final int COLUMNS = 3; // Number of columns
        final int MIN_ROWS = 6; // Minimum number of rows

        // Get the items
        List<menu> items = getMenuItemsByType(type);

        // Calculate the number of rows based on the number of items
        int totalRows = (int) Math.ceil((double) items.size() / COLUMNS);

        // Ensure at least MIN_ROWS rows
        totalRows = Math.max(totalRows, MIN_ROWS);

        // Panel to hold the buttons
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(totalRows, COLUMNS, 10, 10)); // Dynamic rows and columns

        // Add buttons
        int itmeDb = 0;
        for (menu item : items) {
            // Szöveg beállítása
            String itemTextName = item.getNev(); // Termék neve
            String itemTextPrice = item.getAr() + " Ft"; // Termék ára
            itmeDb = item.getOszetevok().stream()
                    .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                    .min()
                    .orElse(0); // Ha nincs elem, alapértelmezett érték 0

            // Egyedi gomb kinézet létrehozása
            JPanel itemButton = new JPanel();
            itemButton.setLayout(new BorderLayout()); // Határozza meg az elrendezést
            itemButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Opcionális keret
            itemButton.setBackground(itmeDb == 0 ? Color.RED : Color.GREEN); // Szín beállítása

            // Névre vonatkozó szöveg bal felső sarokban
            JLabel nameLabel = new JLabel(itemTextName);
            nameLabel.setHorizontalAlignment(SwingConstants.LEFT); // Balra igazítva
            nameLabel.setVerticalAlignment(SwingConstants.CENTER); // Középre igazítva
            itemButton.add(nameLabel, BorderLayout.CENTER);

            // Ár szöveg jobb alsó sarokban
            JLabel priceLabel = new JLabel(itemTextPrice);
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT); // Jobbra igazítva
            priceLabel.setVerticalAlignment(SwingConstants.BOTTOM); // Alulra igazítva
            itemButton.add(priceLabel, BorderLayout.SOUTH);

            // Ha jobb felső sarokra
            JLabel topRightPanel = new JLabel(String.valueOf(itmeDb));
            topRightPanel.setHorizontalAlignment(SwingConstants.RIGHT);
            topRightPanel.setVerticalAlignment(SwingConstants.TOP);
            itemButton.add(topRightPanel, BorderLayout.EAST);

            // Eseménykezelő hozzáadása az egyedi "gombhoz"
            int finalItmeDb = itmeDb;
            itemButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (finalItmeDb == 0) {
                        JOptionPane.showMessageDialog(null, "Nincs elég összetevő a rendeléshez", "Hiba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    asztal.addRendeles(item); // Rendelés hozzáadása
                    updateOrdersPanel(rightPanel, asztal, centerPanel); // Frissíti a jobb oldali panelt
                    showMenuItems(selectedType, asztal, centerPanel, rightPanel); // Újrarendeli a középső részt
                }
            });
            itemButton.setPreferredSize(new Dimension(150, 100)); // Gomb szélessége fix, magassága dinamikus
            itemButton.setMinimumSize(new Dimension(150, 100));
            itemButton.setMaximumSize(new Dimension(150, 100));
            gridPanel.add(itemButton);
        }

        // Add empty panels to fill the grid
        for (int i = items.size(); i < totalRows * COLUMNS; i++) {
            gridPanel.add(new JPanel());
        }

        // Scrollable panel
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Update the center panel
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // Use the selectedType variable in the updateOrdersPanel method
    private void updateOrdersPanel(JPanel rightPanel, asztal asztal, JPanel centerPanel) {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        // Fejléc panel változatlan
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> {
            ordersPanel.setVisible(false);
            restoreOriginalLayout();
        });
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        rightPanel.add(titlePanel, BorderLayout.NORTH);

        // Módosított rendelések lista panel
        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));

        // Beállítjuk a maximális szélességet a panelnek
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (menu item : asztal.getRendelesek()) {
            // Minden rendeléshez új panel, ami a teljes szélességet kitölti
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BorderLayout());
            orderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Fix magasság
            orderPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40)); // Fix magasság
            orderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Margók

            // Bal oldalra a szöveg
            JLabel orderLabel = new JLabel(item.getNev() + " - " + item.getAr() + " Ft");
            orderPanel.add(orderLabel, BorderLayout.WEST);

            // Jobb oldalra a törlés gomb
            JButton deleteButton = new JButton("Törlés");
            deleteButton.addActionListener(e -> {
                if (!exes.ratar_menuex(authenticatedUser)) {
                    if (!magasab()) {
                        return;
                    }
                }
                asztal.removeRendeles(item);
                updateOrdersPanel(rightPanel, asztal, centerPanel); // Frissíti a jobb oldali panelt
                showMenuItems(selectedType, asztal, centerPanel, rightPanel); // Újrarendeli a középső részt
            });
            orderPanel.add(deleteButton, BorderLayout.EAST);

            // Adjunk hozzá egy elválasztó vonalat
            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            ordersListPanel.add(orderPanel);
        }

        // ScrollPane a görgetéshez
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Összeg panel az alján
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD));
        totalPanel.add(totalLabel);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private List<menu> getMenuItemsByType(MenuType type) {
        List<menu> itemsByType = new ArrayList<>();
        for (menu item : menus) {
            if (item.getType() == type && item.isEnabled()) {
                itemsByType.add(item);
            }
        }
        return itemsByType;
    }

    private boolean magasab() {
        login_side login = new login_side(users);
        User  magasab = login.showLoginDialog(this);
        if (magasab == null) {
            return false;
        }
        if (!exes.ratar_menuex(magasab)) {
            JOptionPane.showMessageDialog(this, "Hincsen hozzáférése", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}