package View;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by Kevin on 17-03-2015.
 */
public class MapMenu extends JComboBox<Icon> {

    private HashMap<Icon, String> mapNameMap = new HashMap<>();

    /**
     * Sets up ComboBox with different types of maps.
     */
    public MapMenu(){
        loadImagesAndOptions();
        initialize();
    }


    private void initialize(){
        Dimension prefered = getPreferredSize();
        setEditable(false);

        setBorder(BorderFactory.createRaisedBevelBorder());
        setBounds((int) prefered.getWidth() - 160, (int) (prefered.getHeight() - prefered.getHeight() / 3 * 2 - 50), 130, 30);
        setBackground(Color.white);
        setFocusable(false);
        setActionCommand("maptype");
        MapTypeBoxRenderer maptypeRend = new MapTypeBoxRenderer(mapNameMap);
        maptypeRend.setPreferredSize(new Dimension(300, 50));
        setRenderer(maptypeRend);
    }

    private void loadImagesAndOptions(){
        ImageIcon standardMapImage = new ImageIcon("data//standardMapImage.png");
        ImageIcon colorblindMapImage = new ImageIcon("data//colorblindMapImage.png");
        ImageIcon transportMapImage = new ImageIcon("data//transportMapImage.png");
        mapNameMap.put(standardMapImage, "Standard");
        mapNameMap.put(colorblindMapImage, "Colorblind map");
        mapNameMap.put(transportMapImage, "Transport map");
        addItem(standardMapImage);
        addItem(colorblindMapImage);
        addItem(transportMapImage);
    }
}
