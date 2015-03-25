import Controller.Controller;
import Model.Model;
import View.View;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Main {
    public static void main(String[] args) {
        Model m = Model.getModel();
        try {
            String filePath = "/data/newSmall.osm";
            InputStream inputStream = Main.class.getResourceAsStream(filePath);
            m.loadFile(filePath, inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }
}
