import Controller.Controller;
import Model.Model;
import Model.MapIcon;
import View.View;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) {

        Model m = Model.getModel();
        try {
            String filename = "newSmall.bin"; //Is used to get filename in order to determine the file format
            InputStream inputStream = Main.class.getResourceAsStream(filename);
            setIconResources();
            m.loadFile(filename, inputStream);
            m.setCurrentFilename(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }

    private static void setIconResources() {
        MapIcon.metroIcon = Main.class.getResource("/data/metroIcon.png");
        MapIcon.STogIcon = Main.class.getResource("/data/stogIcon.png");
        MapIcon.parkingIcon = Main.class.getResource("/data/parkingIcon.jpg");
        MapIcon.busIcon = Main.class.getResource("/data/busIcon.png");
        MapIcon.pubIcon = Main.class.getResource("/data/pubIcon.png");
        MapIcon.atmIcon = Main.class.getResource("/data/atmIcon.png");

        MapIcon.standard = Main.class.getResource("/data/standardMapImage.png");
        MapIcon.colorblind = Main.class.getResource("/data/colorblindMapImage.png");
        MapIcon.transport = Main.class.getResource("/data/transportMapImage.png");

        MapIcon.startPointIcon = Main.class.getResource("/data/startPointIcon.png");
        MapIcon.endPointIcon = Main.class.getResource("/data/endPointIcon.png");

        MapIcon.fullscreenIcon = Main.class.getResource("/data/fullscreenIcon.png");
        MapIcon.minusIcon = Main.class.getResource("/data/minusIcon.png");
        MapIcon.plusIcon = Main.class.getResource("/data/plusIcon.png");
        MapIcon.searchIcon = Main.class.getResource("/data/searchIcon.png");
        MapIcon.optionsIcon = Main.class.getResource("/data/optionsIcon.png");
        MapIcon.layerIcon = Main.class.getResource("/data/layerIcon.png");
        MapIcon.chosenAddressIcon = Main.class.getResource("/data/chosenAddressIcon.png");
    }
}
