package View;

import Controller.MapMenuController;
import Model.MapIcon;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by Nicoline on 30-03-2015.
 */
public class MapTypePanel extends JPanel {

    private JButton standardButton = new JButton();
    private JButton colorblindButton = new JButton();
    private JButton transportButton = new JButton();
    private MapMenuController controller;
    private View view;

    private HashMap<Icon, String> mapNameMap = new HashMap<>();

    public MapTypePanel(View view){
        this.view = view;
        setVisible(false);
        setBounds(view.getWidth()-300, view.getHeight() - view.getHeight() / 3 * 2 - 45, 280, 200);
        setOpaque(true);
        setBackground(DrawAttribute.fadeblack);
        setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK),
                        new EmptyBorder(0,20,20,20))
                );
        setLayout(new GridLayout(4,1,5,10));
        init();
    }

    private void init(){

        JLabel mapTypeLabel = new JLabel("Map types");
        mapTypeLabel.setFont(new Font("Arial",Font.BOLD,20));
        mapTypeLabel.setForeground(Color.WHITE);


        ImageIcon standardMapImage = new ImageIcon(MapIcon.standard);
        standardButton.setIcon(standardMapImage);
        standardButton.setBackground(Color.WHITE);
        standardButton.setActionCommand("standardMap");

        ImageIcon colorblindMapImage = new ImageIcon(MapIcon.colorblind);
        colorblindButton.setIcon(colorblindMapImage);
        colorblindButton.setBackground(Color.WHITE);
        ImageIcon transportMapImage = new ImageIcon(MapIcon.transport);
        colorblindButton.setActionCommand("colorblindMap");

        transportButton.setIcon(transportMapImage);
        transportButton.setBackground(Color.WHITE);
        transportButton.setActionCommand("transportMap");

        mapNameMap.put(standardMapImage, "Standard");
        mapNameMap.put(colorblindMapImage, "Colorblind map");
        mapNameMap.put(transportMapImage, "Transport map");

        add(mapTypeLabel);
        add(standardButton);
        add(colorblindButton);
        add(transportButton);
        controller = new MapMenuController(this);
    }

    public void showMapTypePanel(){
        boolean isVisible = isVisible();
        setVisible(!isVisible);
    }

    public View getView(){ return view;}
    public JButton getStandardButton(){ return standardButton;}
    public JButton getColorblindButton(){ return colorblindButton;}
    public JButton getTransportButton(){ return transportButton;}
}
