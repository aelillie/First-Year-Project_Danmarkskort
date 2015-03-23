import Controller.Controller;
import Model.Model;
import View.View;

import java.net.URL;


public class Main {
    public static void main(String[] args) {
        Model m = Model.getModel();
        URL input = Main.class.getResource("/data/newSmall.osm");
        m.loadFile(input.getPath());
        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }
}
