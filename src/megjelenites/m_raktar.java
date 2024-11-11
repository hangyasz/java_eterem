package megjelenites;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.stream.Collectors;

public class m_raktar extends JFrame {
    private JTextField filterField;
    private List<raktar.raktar> raktars;  // Store the original list
    private JPanel centerPanel;

    public m_raktar(List<raktar.raktar> raktars) {
        this.raktars = raktars;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Raktár");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(true);

        // Felső panel a címkével és szűrőmezővel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new JLabel("Raktár"));

        filterField = new JTextField(20);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterItems(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterItems(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterItems(); }
        });
        topPanel.add(new JLabel("Szűrés: "));
        topPanel.add(filterField);
        JButton pluszButen=new JButton("+");
        pluszButen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new add_raktar(raktars);
                filterItems();
            }
        });
        topPanel.add(pluszButen);
        add(topPanel, BorderLayout.NORTH);

        // Középső panel a kártyákkal (BoxLayout, hogy egymás alatt jelenjenek meg)
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Stack items vertically
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Alsó panel a "Vissza" gombbal
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton button = new JButton("Vissza");
        button.addActionListener(e -> dispose());
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.SOUTH);

        // Eseménykezelő a méretváltozáshoz
        centerPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    filterItems(); // Update cards based on filter
                    scrollPane.revalidate();
                    scrollPane.repaint();
                });
            }
        });

        updateCards(raktars);  // Initial update to show all items
        setVisible(true);
    }

    private void filterItems() {
        String filterText = filterField.getText().toLowerCase();
        List<raktar.raktar> filteredRaktars = raktars.stream()
                .filter(r -> r.getNev().toLowerCase().contains(filterText))
                .collect(Collectors.toList());

        updateCards(filteredRaktars);
    }

    private void updateCards(List<raktar.raktar> filteredRaktars) {
        centerPanel.removeAll();

        int cardWidth = 100;
        int cardHeight = 100;

        for (raktar.raktar raktar : filteredRaktars) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel nameLabel = new JLabel(raktar.getNev() + " " + raktar.getMennyiseg() + " " + raktar.getMertekegyseg());
            namePanel.add(nameLabel);
            card.add(namePanel, BorderLayout.NORTH);

            JTextField textField = new JTextField("0", 10);
            textField.addActionListener(e -> {
                try {
                    double newQuantity = Double.parseDouble(textField.getText());
                    raktar.addMennyiseg(newQuantity);
                    nameLabel.setText(raktar.getNev() + " " + raktar.getMennyiseg() + " " + raktar.getMertekegyseg());
                    textField.setText("0");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Kérem, érvényes számot adjon meg!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            });
            card.add(textField, BorderLayout.CENTER);

            centerPanel.add(card);  // Add card vertically
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }
}
