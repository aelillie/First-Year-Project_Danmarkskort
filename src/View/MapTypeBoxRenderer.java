package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kevin on 16-03-2015.
 */
class MapTypeBoxRenderer extends DefaultListCellRenderer {
    private View v;


    public MapTypeBoxRenderer(View v) {
        this.v = v;
        setOpaque(false);
    }


    public Component getListCellRendererComponent( JList list, Object value,int index,boolean isSelected, boolean cellHasFocus ) {
        super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        ImageIcon item = (ImageIcon)value;
        String s = v.getMapNameMap().get(value);

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