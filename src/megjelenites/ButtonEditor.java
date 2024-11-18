package megjelenites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private ActionListener actionListener;

    public ButtonEditor(JCheckBox checkBox, ActionListener actionListener) {
        super(checkBox);
        this.actionListener = actionListener;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            actionListener.actionPerformed(e);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}