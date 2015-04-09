package Tests;

import Model.Model;
import Model.MapFeature;
import Model.PathCreater;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    public void setUp() {
        try {
            inputStream = Main.class.getResourceAsStream("/data/test_map.osm");
            m.loadFile("map.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapFeatures() {
        List<Point2D> wayCoords = new ArrayList<>();
        wayCoords.add(new Point2D.Float(55.6695228f, 12.5802146f));
        wayCoords.add(new Point2D.Float(55.6693366f, 12.5805524f));
        wayCoords.add(new Point2D.Float(55.6690488f, 12.5811529f));
        wayCoords.add(new Point2D.Float(55.6687563f, 12.5817373f));
        wayCoords.add(new Point2D.Float(55.6685247f, 12.5823799f));
        Path2D way = PathCreater.createWay(wayCoords);

        m.getVisibleData(new Rectangle2D.Float(0, 0, 500, 500));
        ArrayList<List<MapFeature>> mapFeatures = new ArrayList<>();

    }

}
