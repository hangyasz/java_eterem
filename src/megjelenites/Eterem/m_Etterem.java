package megjelenites.Eterem;

import asztal.asztal;
import megjelenites.login.login_side;
import menu.MenuType;
import menu.menu;
import role.User;
import role.exes;
import terem.terem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static role.exes.magasab;

/**
 * Az étterem ablakot reprezentáló osztály
 */

public class m_Etterem extends JFrame {
    private final List<asztal> asztalok;
    private final List<menu> menus;
    private final JPanel ordersPanel;
    private terem terem;
    private List<User> users;
    private User authenticatedUser;
    private MenuType selectedType;

    /**
     * Konstruktor lértehoz egy étterem ablakot
     * @param asztalok asztalok listája
     * @param terem terem meretei
     * @param menus menü elemek listája
     * @param users felhasználók listája
     */
    public m_Etterem(List<asztal> asztalok,terem terem , List<menu> menus, List<User> users) {
        this.asztalok = asztalok;
        this.terem = terem;
        this.menus = menus;
        this.users = users;

        // Beálitja az ablak tulajdonságait
        setTitle("Étterem");
        setSize(1050, 700);
        setMinimumSize(new Dimension(1050, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Felső panel
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);

        //Terem panel
        TeremPanel_asztal panel = new TeremPanel_asztal(asztalok, terem,this);
        add(panel, BorderLayout.CENTER);

        //Rendelések panel
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        //orderPanel nem látható
        ordersPanel.setVisible(false);
        add(ordersPanel, BorderLayout.EAST);
        setVisible(true);

    }

    /**
     * Az ablak újra elrendezése
     *
     */
    @Override
    public void doLayout() {
        super.doLayout();
        Dimension size = getSize();
        ordersPanel.setPreferredSize(new Dimension(size.width / 3, size.height));
        ordersPanel.revalidate();
    }

    /**
     * Az ablak újra elrendezése a korábbi elrendezésre az orderPanel eltávolításával
     *
     */
    private void restoreOriginalLayout() {
        getContentPane().removeAll();
        JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
        up.add(new JLabel("Étterem"));
        add(up, BorderLayout.NORTH);
        TeremPanel_asztal panel = new TeremPanel_asztal(asztalok, terem, this);
        add(panel, BorderLayout.CENTER);
        ordersPanel.removeAll();
        add(ordersPanel, BorderLayout.EAST);
        ordersPanel.setVisible(false);
        revalidate();
        repaint();
    }

    /**
     * Az asztalhoz tartozó rendelések megjelenítése
     * @param asztal az asztal
     * a rendelések megjelenítése egymás alatt tornik a panelen
     * a fizetés gombra kattintva a rendelések törlődnek és megjelenik a fizetendő összeg mégegyszer
     */
    void showOrders(asztal asztal) {
        //beállítja az orderPanel méretét
        ordersPanel.setSize(new Dimension(getWidth() / 3, getHeight()));

        //törli az előző tartalmat
        ordersPanel.removeAll();
        ordersPanel.setLayout(new BorderLayout());
        //mehjelení az asztal nevét és a bezár gombot
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> ordersPanel.setVisible(false));
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        ordersPanel.add(titlePanel, BorderLayout.NORTH);

        //ki listázza a rendeléseket
        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension size = new Dimension(ordersPanel.getWidth(), 40);
        for (menu item : asztal.getRendelesek()) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BorderLayout());
            orderPanel.setMaximumSize(size);
            orderPanel.setMinimumSize(size);

            String nameText = item.getNev();
            if (nameText.length() > 35)
                nameText = nameText.substring(0, 33) + "..";

            JLabel name = new JLabel(nameText);
            name.setToolTipText(item.getNev());
            orderPanel.add(name, BorderLayout.WEST);

            JLabel arr = new JLabel(item.getAr() + " Ft");
            orderPanel.add(arr, BorderLayout.EAST);
            // Border
            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

            ordersListPanel.add(orderPanel);
        }

        //gorgethető panel
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);

        //alsó panel ahol a fizetés és új rendelés hozzáadása gomb van
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        JButton payButton = new JButton("Fizet");
        JButton addOrderButton = new JButton("Új rendelés hozzáadása");
        addOrderButton.addActionListener(e -> {
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
            if (!exes.ratar_menuex(authenticatedUser)) {
                if (!magasab(users, this)) {
                    return;
                }
            }

            JOptionPane.showMessageDialog(null, "Fizetendő összeg: " + asztal.getEretke() + " Ft", "Fizetés", JOptionPane.INFORMATION_MESSAGE);
            asztal.pay();
            ordersPanel.setVisible(false);
        });

       //gombok hozzáadása a panelhez
        bottomPanel.add(addOrderButton);
        bottomPanel.add(totalLabel);
        bottomPanel.add(payButton);
        ordersPanel.add(bottomPanel, BorderLayout.SOUTH);

        //panel megjelenítése
        ordersPanel.setVisible(true);
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    /**
     * Az új rendelés panel megjelenítése
     * @param asztal az asztal
     * az új rendelés panelen megjelenik a menü elemek listája és a rendelések listája
     * a menü elemek közül lehet választani és a rendelések listájához hozzáadni
     * a rendelések listájából lehet törölni
     */

    private void showNewOrderPanel(asztal asztal) {
        JPanel newOrderPanel = new JPanel(new BorderLayout());

        //felső panel ahol az asztal neve van
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tableNameLabel = new JLabel(asztal.getNev());
        tableNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.setPreferredSize(new Dimension(900, 50));
        topPanel.setMinimumSize(new Dimension(900, 50));
        topPanel.setMaximumSize(new Dimension(900, 50));
        topPanel.add(tableNameLabel);
        newOrderPanel.add(topPanel, BorderLayout.NORTH);

        //középső panel ahol a menü elemek vannak
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setPreferredSize(new Dimension(600, 600));
        centerPanel.setMinimumSize(new Dimension(600, 600));
        centerPanel.setMaximumSize(new Dimension(600, 600));
        newOrderPanel.add(centerPanel, BorderLayout.CENTER);

        //jobb panel ahol a rendelések vannak
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 600));
        rightPanel.setMinimumSize(new Dimension(300, 600));
        rightPanel.setMaximumSize(new Dimension(300, 600));
        updateOrdersPanel(rightPanel, asztal, centerPanel);
        newOrderPanel.add(rightPanel, BorderLayout.EAST);

        //bal panel ahol a menü típusok vannak
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(150, 600));
        leftPanel.setMinimumSize(new Dimension(150, 600));
        leftPanel.setMaximumSize(new Dimension(150, 600));
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        newOrderPanel.add(leftScrollPane, BorderLayout.WEST);

        //panel mérete
        int totalHeight = getHeight() - topPanel.getHeight();
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

    /**
     * Az rendelések panel frissítése itt lehet törölni a rendeléseket és ujakat hozzáadni
     * @param rightPanel a jobb panel
     * @param asztal az asztal
     * @param centerPanel a középső panel
     */

    private void updateOrdersPanel(JPanel rightPanel, asztal asztal, JPanel centerPanel) {
        //torli az előző tartalmat
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        //mehjelení az asztal nevét és a bezár gombot
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel(asztal.getNev() + " asztalnál:");
        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> {
            ordersPanel.setVisible(false);
            restoreOriginalLayout();
            authenticatedUser = null;
        });
        titlePanel.add(titleLabel);
        titlePanel.add(closeButton);
        rightPanel.add(titlePanel, BorderLayout.NORTH);

        //megjeleníti a rendeléseket
        JPanel ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        ordersListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension size = new Dimension(rightPanel.getWidth(), 40);
        for (menu item : asztal.getRendelesek()) {
            JPanel orderPanel = new JPanel(new GridBagLayout());
            orderPanel.setMaximumSize(size);
            orderPanel.setMinimumSize(size);
            orderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 0, 5);

            //név
            String nameText = item.getNev();
            if (nameText.length() > 17) {
                nameText = nameText.substring(0, 17) + "...";
            } else {
                nameText = String.format("%-20s", nameText);
            }
            JLabel nameLabel = new JLabel(nameText);
            gbc.gridx = 0;
            gbc.weightx = 0.5; // 50%  használja a rendelkezésre álló helyet
            orderPanel.add(nameLabel, gbc);

            // Price Label
            JLabel priceLabel = new JLabel(item.getAr() + " Ft");
            gbc.gridx = 1;
            gbc.weightx = 0.3; // 30%  használja a rendelkezésre álló helyet
            orderPanel.add(priceLabel, gbc);

            // Delete Button
            JButton deleteButton = new JButton("Törlés");
            deleteButton.addActionListener(e -> {
                if (!exes.ratar_menuex(authenticatedUser)) {
                    if (!magasab(users, this)) {
                        return;
                    }
                }
                asztal.removeRendeles(item);
                updateOrdersPanel(rightPanel, asztal, centerPanel);
                showMenuItems(asztal, centerPanel, rightPanel);
            });
            gbc.gridx = 2;
            gbc.weightx = 0.2; // 20%  használja a rendelkezésre álló helyet
            orderPanel.add(deleteButton, gbc);

            orderPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            ordersListPanel.add(orderPanel);
        }

        //gorgethető panel
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        //alsó panel ahol az összeg van
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Összeg: " + asztal.getEretke() + " Ft");
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD));
        totalPanel.add(totalLabel);
        rightPanel.add(totalPanel, BorderLayout.SOUTH);

        //panel megjelenítése
        rightPanel.revalidate();
        rightPanel.repaint();
    }


    /**
     * A menü elemek megjelenítése
     * @param asztal az asztal
     * @param centerPanel a középső panel
     * @param rightPanel a jobb panel
     */
    private void showMenuItems( asztal asztal, JPanel centerPanel, JPanel rightPanel) {
        centerPanel.removeAll();
        //a menü elemek megjelenítése minimálosan 6 sorban és 3 oszlopban
        final int COLUMNS = 3;
        final int MIN_ROWS = 6;

        //a kiválasztott menü típus alapján kiválasztja a menü elemeket
        List<menu> items = getMenuItemsByType(selectedType);

        //a menü elemek számából kiszámolja a sorok számát
        int totalRows = (int) Math.ceil((double) items.size() / COLUMNS);
        totalRows = Math.max(totalRows, MIN_ROWS);

        //a menü elemek megjelenítése a gridPanelen
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(totalRows, COLUMNS, 10, 10));

        for (menu item : items) {
            JPanel itemButton = new JPanel();
            itemButton.setLayout(new BorderLayout());
            itemButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            //a háttérszín piros ha nincs elég összetevő a rendeléshez, egyébként zöld
            itemButton.setBackground(item.getOszetevok().stream()
                    .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                    .min()
                    .orElse(0) == 0 ? Color.RED : Color.GREEN);

            //név
            JLabel nameLabel = new JLabel(item.getNev());
            nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            nameLabel.setVerticalAlignment(SwingConstants.CENTER);
            itemButton.add(nameLabel, BorderLayout.CENTER);

            //ár
            JLabel priceLabel = new JLabel(item.getAr() + " Ft");
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            itemButton.add(priceLabel, BorderLayout.SOUTH);

            //a jobb felső sarokban megjeleníti a rendelkezésre álló menyiség alapján a minimális rendelhető mennyiséget
            int minAmount = item.getOszetevok().stream()
                    .mapToInt(oszetevok -> (int) Math.floor(oszetevok.getRaktar().getMennyiseg() / oszetevok.getMennyiseg()))
                    .min()
                    .orElse(0);
            JLabel topRightPanel = new JLabel(String.valueOf(minAmount));
            topRightPanel.setHorizontalAlignment(SwingConstants.RIGHT);
            topRightPanel.setVerticalAlignment(SwingConstants.TOP);
            itemButton.add(topRightPanel, BorderLayout.EAST);

            //a menü elemre kattintva hozzáadja a rendeléshez ha van elég összetevő
            itemButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (minAmount == 0) {
                        JOptionPane.showMessageDialog(null, "Nincs elég összetevő a rendeléshez", "Hiba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    asztal.addRendeles(item);
                    updateOrdersPanel(rightPanel, asztal, centerPanel);
                    showMenuItems( asztal, centerPanel, rightPanel);
                }
            });
            //a menü elem gomb mérete
            itemButton.setPreferredSize(new Dimension(150, 100));
            gridPanel.add(itemButton);
        }

        //a menü elemek számának kiegeszítése üres panellel
        for (int i = items.size(); i < totalRows * COLUMNS; i++) {
            gridPanel.add(new JPanel());
        }

        //gorgethető teszi a panelt ha több a menü elem mint amennyi elfér
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //panel megjelenítése
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /**
     * A menü elemek listáját adja vissza a megadott típus alapján
     * @param type a menü elem típusa
     * @return a menü elemek listája
     */
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