package View;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by Kevin on 16-03-2015.
 */
class MapTypeBoxRenderer extends DefaultListCellRenderer {
    private Map<Icon, String> map;


    public MapTypeBoxRenderer(Map<Icon, String> map) {
        this.map = map;
        setOpaque(false);
    }


    public Component getListCellRendererComponent( JList list, Object value,int index,boolean isSelected, boolean cellHasFocus ) {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        ImageIcon item = (ImageIcon)value;
        String s = map.get(value);

        if (index == -1)
        {
            setText(s );
            setIcon( null );
        }
        else if (isSelected) {
            setText(s);
            setIcon(null); }

        else {
            setText(s);
            setIcon(item);
        }

        return this;
    }

}