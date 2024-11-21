package megjelenites.Eterem;

import asztal.asztal;
import megjelenites.login.login_side;
import menu.menu;
import menu.MenuType;
import role.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static role.exes.magasab;

public class m_Etterem extends JFrame {
    private final List<asztal> asztalok;
    private final List<menu> menus;
    private final JPanel ordersPanel;
    private double x_term = 50;
    private double y_term = 50;
    private List<User> users;
    private User authenticatedUser;
    private MenuType selectedType;

    public m_Etterem(List<asztal> asztalok, double x_term, double y_term, List<menu> menus, List<User> users) {
        this.asztalok = asztalok;
        this.x_term = x_term;
        this.y_term = y_term;
        this.menus = menus;
        this.users = users;

        // Beálitja az ablak tulajdonságait
        setTitle("Étterem");
        setSize(900, 700);
        setMinimumSize(new Dimension(900, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Felső panel
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);

        //Terem panel
        TeremPanel_asztal panel = new TeremPanel_asztal(asztalok, x_term, y_term,this);
        add(panel, BorderLayout.CENTER);

        //Rendelések panel
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        //orderPanel nem látható
        ordersPanel.setVisible(false);
        add(ordersPanel, BorderLayout.EAST);
        setVisible(true);

    }

    //Beállítja az ablak méretét
    @Override
    public void doLayout() {
        super.doLayout();
        Dimension size = getSize();
        ordersPanel.setPreferredSize(new Dimension(size.width / 3, size.height));
        ordersPanel.revalidate();
    }

    //Visszaállítja az eredeti elrendezést
    private void restoreOriginalLayout() {
        getContentPane().removeAll();
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);
        TeremPanel_asztal panel = new TeremPanel_asztal(asztalok, x_term, y_term, this);
        add(panel, BorderLayout.CENTER);
        ordersPanel.removeAll();
        add(ordersPanel, BorderLayout.EAST);
        ordersPanel.setVisible(false);
        revalidate();
        repaint();
    }

    //ő jeleniti meg a rendeláseket
    void showOrders(asztal asztal) {
        //törli a korábbi tatartalmat a panelről
        ordersPanel.removeAll();
        ordersPanel.setLayout(new BorderLayout());

        //ki írja az aszal nevét és a bezár gombot
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        //elrejti a panelt
        closeButton.addActionListener(e -> ordersPanel.setVisible(false));
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        ordersPanel.add(titlePanel, BorderLayout.NORTH);
        //alsó panel ahol az új rendelés hozzáadása, összeg és fizet gombok vannak
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        JButton payButton = new JButton("Fizet");
        JButton addOrderButton = new JButton("Új rendelés hozzáadása");
        addOrderButton.addActionListener(e -> {
            //hitelsités hasikerült akkor megjelenik az új rendelés panel
            login_side login = new login_side(users);
            authenticatedUser = login.showLoginDialog(this);
            if (authenticatedUser == null) {
                return;
            }
            if (exes.asztalex(authenticatedUser)) {
                showNewOrderPanel(asztal);
            } else {
                JOptionPane.showMessageDialog(this, "Nincs hozzáférése", "Hitelesitás", JOptionPane.ERROR_MESSAGE);
            }
        });
        payButton.addActionListener(e -> {
            //hitelsités ha sikerült akkor akkor ki írja a vég öszeget és törli a rendeléseket
            if (!exes.ratar_menuex(authenticatedUser)) {
                if (!magasab(users, this)) {
                    return;
                }
            }
            asztal.getRendelesek().clear();
            ordersPanel.setVisible(false);
            JOptionPane.showMessageDialog(null, "Fizetendő összeg: " + asztal.getEretke() + " Ft", "Fizetés", JOptionPane.INFORMATION_MESSAGE);
            asztal.setEretke(0);
        });

        //rendelések panel
        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        int totlawidth = ordersPanel.getWidth();
        for (menu item : asztal.getRendelesek()) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BorderLayout());
            orderPanel.setMaximumSize(new Dimension(totlawidth, 40));
            orderPanel.setMinimumSize(new Dimension(totlawidth, 40));

            JLabel name = new JLabel(item.getNev());
            orderPanel.add(name, BorderLayout.WEST);

            JLabel arr = new JLabel(item.getAr() + " Ft");
            orderPanel.add(arr, BorderLayout.EAST);
            //szegély
            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            ordersListPanel.add(orderPanel);
        }
        //görgetősáv
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);

        //gombok hozzáadása a panelhez
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

        //felső panel ahol az asztal neve van
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tableNameLabel = new JLabel(asztal.getNev());
        tableNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(tableNameLabel);
        newOrderPanel.add(topPanel, BorderLayout.NORTH);

        //középső panel ahol a menü elemek vannak
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        newOrderPanel.add(centerPanel, BorderLayout.CENTER);

        //jobb panel ahol a rendelések vannak
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        updateOrdersPanel(rightPanel, asztal, centerPanel);
        newOrderPanel.add(rightPanel, BorderLayout.EAST);

        //bal panel ahol a menü típusok vannak
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        newOrderPanel.add(leftScrollPane, BorderLayout.WEST);

        //panel mérete
        int totalHeight = getHeight()-topPanel.getHeight();
        int buttonHeight = totalHeight / 6; //egyszere 6 gomb jelenik meg
        //gombok hozzáadása a panelhez
        for (MenuType type : MenuType.values()) {
            JButton typeButton = new JButton(type.name());
            typeButton.setMaximumSize(new Dimension(150, buttonHeight));
            typeButton.setMinimumSize(new Dimension(150, buttonHeight));

            typeButton.addActionListener(event -> {
                selectedType = type;
                showMenuItems(asztal, centerPanel, rightPanel);
            });

            leftPanel.add(typeButton);
        }

        getContentPane().removeAll();
        getContentPane().add(newOrderPanel);
        revalidate();
        repaint();
    }


    private void updateOrdersPanel(JPanel rightPanel, asztal asztal, JPanel centerPanel) {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> {
            ordersPanel.setVisible(false);
            restoreOriginalLayout();
            authenticatedUser=null;
        });
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        rightPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (menu item : asztal.getRendelesek()) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BorderLayout());
            orderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            orderPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
            orderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JLabel orderLabel = new JLabel(item.getNev() + " - " + item.getAr() + " Ft");
            orderPanel.add(orderLabel, BorderLayout.WEST);

            JButton deleteButton = new JButton("Törlés");
            deleteButton.addActionListener(e -> {
                if (!exes.ratar_menuex(authenticatedUser)) {
                    if (!magasab(users, this)) {
                        return;
                    }
                }
                asztal.removeRendeles(item);
                updateOrdersPanel(rightPanel, asztal, centerPanel);
                showMenuItems( asztal, centerPanel, rightPanel);
            });
            orderPanel.add(deleteButton, BorderLayout.EAST);

            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            ordersListPanel.add(orderPanel);
        }

        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD));
        totalPanel.add(totalLabel);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void showMenuItems( asztal asztal, JPanel centerPanel, JPanel rightPanel) {
        centerPanel.removeAll();

        final int COLUMNS = 3;
        final int MIN_ROWS = 6;

        List<menu> items = getMenuItemsByType(selectedType);

        int totalRows = (int) Math.ceil((double) items.size() / COLUMNS);
        totalRows = Math.max(totalRows, MIN_ROWS);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(totalRows, COLUMNS, 10, 10));

        for (menu item : items) {
            JPanel itemButton = new JPanel();
            itemButton.setLayout(new BorderLayout());
            itemButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            itemButton.setBackground(item.getOszetevok().stream()
                    .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                    .min()
                    .orElse(0) == 0 ? Color.RED : Color.GREEN);

            JLabel nameLabel = new JLabel(item.getNev());
            nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            nameLabel.setVerticalAlignment(SwingConstants.CENTER);
            itemButton.add(nameLabel, BorderLayout.CENTER);

            JLabel priceLabel = new JLabel(item.getAr() + " Ft");
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            itemButton.add(priceLabel, BorderLayout.SOUTH);

            JLabel topRightPanel = new JLabel(String.valueOf(item.getOszetevok().stream()
                    .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                    .min()
                    .orElse(0)));
            topRightPanel.setHorizontalAlignment(SwingConstants.RIGHT);
            topRightPanel.setVerticalAlignment(SwingConstants.TOP);
            itemButton.add(topRightPanel, BorderLayout.EAST);

            itemButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (item.getOszetevok().stream()
                            .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                            .min()
                            .orElse(0) == 0) {
                        JOptionPane.showMessageDialog(null, "Nincs elég összetevő a rendeléshez", "Hiba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    asztal.addRendeles(item);
                    updateOrdersPanel(rightPanel, asztal, centerPanel);
                    showMenuItems( asztal, centerPanel, rightPanel);
                }
            });
            itemButton.setPreferredSize(new Dimension(150, 100));
            itemButton.setMinimumSize(new Dimension(150, 100));
            itemButton.setMaximumSize(new Dimension(150, 100));
            gridPanel.add(itemButton);
        }

        for (int i = items.size(); i < totalRows * COLUMNS; i++) {
            gridPanel.add(new JPanel());
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.revalidate();
        centerPanel.repaint();
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

}