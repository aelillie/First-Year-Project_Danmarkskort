import Controller.Controller;
import Model.Model;
import View.View;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;


public class Main {
    public static void main(String[] args) {
        Model m = Model.getModel();
        try {
            URL input = Main.class.getResource("/data/newSmall.osm");
            InputSource inputSource = new InputSource(input.openStream());
            m.loadFile(input.getFile(),inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }
}
