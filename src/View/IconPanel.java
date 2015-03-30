package View;

import Model.MapIcon;
import Model.MapIcon;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AmaliePalmund on 25/03/15.
 */
    public class IconPanel extends JScrollPane {
        private JPanel panel;
        private MapIcon mapIcon;



        public IconPanel() {
            super();


            GridLayout gridLayout = new GridLayout(0,2);
            setBounds(50, 100, 150, 180);
            //  setOpaque(true);
            //setBorder(new MatteBorder(100, 100, 10, 10, new Color(161, 161, 161)));
            this.setBackground(Color.WHITE);
            panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setLayout(gridLayout);
            panel.setVisible(true);
            ArrayList<URL> icons = MapIcon.getIcons();
            for (int i= 0; i < icons.size(); i++)
            {
                JButton b1 = new JButton("Hej");
                panel.add(b1);
                JCheckBox checkbox = new JCheckBox();
                panel.add(checkbox);
            }
            this.setViewportView(panel);

        }

}
