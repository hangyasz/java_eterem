package megjelenites.raktar;

import raktar.raktar;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class raktar_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Név", "Mértékegység", "Mennyiség","Hozzadás", "Törlés"};
    private final List<raktar> raktars;

    public raktar_Tabla(List<raktar> raktars) {
        this.raktars = raktars;
    }

    @Override
    public int getRowCount() {
        return raktars.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        raktar raktar = raktars.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return raktar.getNev();
            case 1:
                return raktar.getMertekegyseg();
            case 2:
                return raktar.getMennyiseg();
            case 3:
                return null;
            case 4:
                return "Törlés";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return String.class;
            case 2:
                return Double.class;
            case 3:
                return Double.class;
            case 4:
                return JButton.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3 || columnIndex == 4;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        raktar raktar = raktars.get(rowIndex);
        switch (columnIndex) {
            case 3:
                raktar.addMennyiseg((Double) value);
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}