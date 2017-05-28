import Controller.Controller;
import Model.Model;
import View.View;

import java.io.IOException;
import java.io.InputStream;


public class Main {

    public static void main(String[] args) {

        Model m = Model.getModel();
        try {
            String filename = "data/indreby2.osm"; //Is used to get filename in order to determine the file format
            InputStream inputStream = Main.class.getResourceAsStream(filename);
            m.loadFile(filename, inputStream);
            m.setCurrentFilename(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }

}
