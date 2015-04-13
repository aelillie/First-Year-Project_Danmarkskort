package View;

import Controller.IconController;
import Model.MapIcon;
import Model.MapIcon;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AmaliePalmund on 25/03/15.
 */
public class IconPanel extends JScrollPane {
    private JPanel panel;
    private static ArrayList<IconController> controllers;

    public IconPanel() {
        super();
        controllers = new ArrayList<>();

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
            String ikoner = icons.get(i).getFile();
            JLabel l1 = new JLabel(new ImageIcon(ikoner));
            panel.add(l1);
            JCheckBox checkbox = new JCheckBox("",true);
            IconController controller = new IconController(icons.get(i));
            MapIcon.setIconState(icons.get(i), true);
            //icons.get(i).setController(controller);
            checkbox.addItemListener(controller);
            checkbox.addComponentListener(controller);
            panel.add(checkbox);
            panel.addComponentListener(controller);
            controllers.add(controller);
        }
        this.setViewportView(panel);
    }

    public static ArrayList<IconController> getControllers() {
        return controllers;
    }

    //for all IconControllers adjust the view
    public void addObserverToIcons(View v){
        for(IconController con : controllers){
            con.setView(v);
        }

    }

    public void showIconPanel(){
        boolean isVisible = isVisible();
        setVisible(!isVisible);
    }

}

