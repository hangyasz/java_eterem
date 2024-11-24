package megjelenites.raktar;

import raktar.raktar;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * A raktár táblázatot megjelenítő osztály
 */

public class raktar_Tabla extends AbstractTableModel {
    private final String[] columnNames = {"Név", "Mértékegység", "Mennyiség","Hozzadás", "Törlés"};
    private  List<raktar> raktars;


    /**
     * Konstruktor
     * @param raktars a raktárak listája
     */
    public raktar_Tabla(List<raktar> raktars) {
        this.raktars = raktars;
    }

    /**
     * visszaadja a raktárak listáj méretét
     */
    @Override
    public int getRowCount() {
        return raktars.size();
    }

    /**
     * visszaadja a raktárak listáj méretét
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * visszaadja a táblázat egy cellájának értékét
     * @param rowIndex        the row whose value is to be queried
     * @param columnIndex     the column whose value is to be queried
     * @return a cella értéke
     */
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

    /**
     * visszaadja az oszlopok nevét
     * @param column az oszlop sorszáma
     * @return az oszlop neve
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * visszaadja az oszlopok típusát
     * @param columnIndex az oszlop sorszáma
     * @return az oszlop típusa
     */
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

    /**
     * visszaadja, hogy az adott cella szerkeszthető-e
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * @return csak a hozzáadás és a törlés oszlop szerkeszthető
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3 || columnIndex == 4;
    }

    /**
     * a táblázat celláinak értékét beállító metódus
     * @param value az új érték
     * @param rowIndex a sor sorszáma
     * @param columnIndex az oszlop sorszáma
     * beállítja a raktár mennyiségét a kapott értékr alpján
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        raktar raktar = raktars.get(rowIndex);
        switch (columnIndex) {
            case 3:
                //hozzáadás és ha minusz akkor kivonás
                raktar.addMennyiseg((Double) value);
                break;
            default:
                break;
        }
        //frissítjük a táblát
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}