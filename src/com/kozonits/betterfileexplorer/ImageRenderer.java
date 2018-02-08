
package com.kozonits.betterfileexplorer;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//@author Marcel Kozonits, 3CHIF
//@date 08.02.2018
//@url -

class ImageRenderer extends DefaultTableCellRenderer {
    JLabel lbl = new JLabel();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //lbl.setText((String) value);
        ImageIcon icon = new ImageIcon(getClass().getResource((String)value));
        lbl.setIcon(icon);
        return lbl;
    }
}