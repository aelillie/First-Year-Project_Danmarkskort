package Tests;

import Model.Model;
import Model.PathCreater;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Test class for the OSMHandler class
 */
public class OSMHandlerTest {
    Model m = Model.getModel();
    InputStream inputStream;

    /**
     * Sets up an osm test file, which enables us to cover all the functions the OSMHandler invokes
     */
    @Before
    public void loadFile() {
        try {
            inputStream = Main.class.getResourceAsStream("/data/test_map.osm");
            m.loadFile("map.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapFeatures() {
        Point2D coord1 = new Point2D.Double(55.6695228, 12.5802146);
        Point2D coord2 = new Point2D.Double(55.6693366, 12.5805524);
        Point2D coord3 = new Point2D.Double(55.6690488, 12.5811529);
        Point2D coord4 = new Point2D.Double(55.6687563, 12.5817373);
        Point2D coord5 = new Point2D.Double(55.6685247, 12.5823799);
        List<Point2D> wayCoords = new ArrayList<>();
        wayCoords.add(coord1);
        wayCoords.add(coord2);
        wayCoords.add(coord3);
        wayCoords.add(coord4);
        wayCoords.add(coord5);
        Path2D way = PathCreater.createWay(wayCoords);
        //Assert.assertEquals(way, );
    }

}
