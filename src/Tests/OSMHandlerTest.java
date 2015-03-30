package Tests;

import Model.Model;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Anders on 23-03-2015.
 */
public class OSMHandlerTest {
    Model m = Model.getModel();

    @Before
    public void loadFile(){
        try {
            InputStream stream = Main.class.getResourceAsStream("/data/map.osm");
            m.loadFile("map.osm", stream);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testNumberOfWays() {
       // assertEquals(4,m.getMapFeatures().size());
    }

    @Test
    public void testTagName(){
       /* assertEquals("unclassified", m.getMapFeatures().get(0).getValue());
        assertEquals("service", m.getMapFeatures().get(1).getValue());
        assertEquals("service", m.getMapFeatures().get(2).getValue());
        assertEquals("service", m.getMapFeatures().get(3).getValue());*/
    }


}
