package View;

import Controller.IconController;
import Model.MapIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;


public class IconPanel extends JScrollPane {
    private JPanel panel;
    private static ArrayList<IconController> controllers;
    private ArrayList<URL> icons;


    public IconPanel() {
        super();
        controllers = new ArrayList<>();
        Dimension preferred = getPreferredSize();
        GridLayout gridLayout = new GridLayout(0, 2);
        setBounds((int) (preferred.getWidth() - 214), (int) (preferred.getHeight() - preferred.getHeight() * 0.98 + 70), 120, 180);
        setBounds(593, 80, 120, 180);
        this.setBackground(Color.BLACK);
        panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(gridLayout);
        addIcons();
        this.setViewportView(panel);
        panel.setVisible(true);
        showIconPanel();
    }

    public void addIcons() {
        icons = MapIcon.getIcons();
        for (int i = 0; i < icons.size(); i++) {
            URL iconPath = icons.get(i);
            JLabel l1 = new JLabel(new ImageIcon(iconPath));
            panel.add(l1);
            IconController controller = new IconController(icons.get(i));
            if (icons.get(i).equals(MapIcon.getIconURLs().get("restaurantIcon")) ||
                    icons.get(i).equals(MapIcon.getIconURLs().get("pubIcon"))||
                    icons.get(i).equals(MapIcon.getIconURLs().get("toiletIcon")) ||
                    icons.get(i).equals(MapIcon.getIconURLs().get("7elevenIcon")) ||
                    icons.get(i).equals(MapIcon.getIconURLs().get("cafeIcon"))) {
                MapIcon.setIconState(icons.get(i), false);
                JCheckBox checkboxx = new JCheckBox("", false);
                checkboxx.addItemListener(controller);
                checkboxx.addComponentListener(controller);
                panel.add(checkboxx);
                panel.addComponentListener(controller);
                controllers.add(controller);
            } else {
                JCheckBox checkbox = new JCheckBox("", true);
                MapIcon.setIconState(icons.get(i), true);
                checkbox.addItemListener(controller);
                checkbox.addComponentListener(controller);
                panel.add(checkbox);
                panel.addComponentListener(controller);
                controllers.add(controller);

            }
        }
    }

    //for all IconControllers adjust the view
    public void addObserverToIcons(View v) {
        for (IconController con : controllers) {
            con.setView(v);
        }

    }

    public void showIconPanel() {
        boolean isVisible = isVisible();
        setVisible(!isVisible);
    }
}

