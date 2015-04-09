package Tests;

import MapFeatures.Highway;
import Model.Model;
import Model.MapData;
import Model.MapFeature;
import Model.PathCreater;
import Model.MapCalculator;
import Model.ValueName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

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
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6695228), 12.5802146f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6693366), 12.5805524f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6690488), 12.5811529f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6687563), 12.5817373f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6685247), 12.5823799f));
        Path2D way1 = PathCreater.createWay(wayCoords);
        MapFeature highway = new Highway(way1, 13, "teriary", false);
        highway.setValueName(ValueName.TERTIARY);

        ArrayList<List<MapData>> queryList = m.getVisibleData(new Rectangle2D.Float(0, 0, 500, 500));
        List<MapData> mapFeatures = null;
        MapData mapData = null;
        MapFeature mapFeature = null;
        outerloop:
        for (int s = 0; s < queryList.size() ; s++) {
            mapFeatures = queryList.get(s);
            for (int i = 0 ; i < mapFeatures.size() ; i++) {
                mapData = mapFeatures.get(i);
                if (mapData instanceof Highway) {
                    mapFeature = (MapFeature) mapData;
                    if (mapFeature.getValue().equals("tertiary")) break outerloop;
                }
            }

        }

        for (Point2D wayCoord : wayCoords) {
            if (mapFeature != null) {
                Assert.assertTrue(mapFeature.getWay().contains(wayCoord));
            }
        }
        if (mapFeature != null) {
            Assert.assertEquals(highway.toString(), mapFeature.toString());
        }
    }

}
