package Tests;

import MapFeatures.Amenity;
import MapFeatures.Highway;
import Model.Model;
import Model.MapData;
import Model.MapIcon;
import Model.MapFeature;
import Model.PathCreater;
import Model.MapCalculator;
import Model.ValueName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.awt.geom.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Test class for the OSMHandler class
 */
public class OSMHandlerTest {
    private static final double DELTA = 1e-15;
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
    public void MapFeatureWayTest() {
        List<Point2D> wayCoords = new ArrayList<>();
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6695228), 12.5802146f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6693366), 12.5805524f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6690488), 12.5811529f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6687563), 12.5817373f));
        wayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6685247), 12.5823799f));
        Path2D way1 = PathCreater.createWay(wayCoords); //first way

        List<MapData> queryList = m.getVisibleStreets(new Rectangle2D.Float(0, 0, 500, 500));

        List<MapData> mapDataList = null;
        mapDataList = queryList; //throw all map features and icons into one list


        Highway highway_tertiary = null; //The map feature we want

        assert mapDataList != null;
        for (int i = 0 ; i < mapDataList.size() ; i++) {
            MapData mapData = mapDataList.get(i);
            if (mapData instanceof Highway && ((Highway) mapData).getValue().equals("tertiary")) {
                highway_tertiary = (Highway) mapData;
                
            }
        }



        if (highway_tertiary != null) {
            PathIterator pI = highway_tertiary.getShape().getPathIterator(new AffineTransform());
            int i = 0;
            while(!pI.isDone()){
                float[] points = new float[6];

                pI.currentSegment(points);


                Assert.assertEquals(points[0], wayCoords.get(i).getY(), DELTA);
                Assert.assertEquals(points[1], wayCoords.get(i).getX(), DELTA);

                pI.next();
                i++;

            }

        }

    }

    @Test
    public void addingIconsTest(){
        List<MapData> icons = m.getVisibleIcons(new Rectangle2D.Float(0, 0, 500, 500));

        Assert.assertEquals(5, icons.size());
        MapIcon icon = (MapIcon) icons.get(0);
        Assert.assertEquals(new Point2D.Float(12.5818120f , (float) MapCalculator.latToY(55.6653496)), icon.getPosition());

    }

    @Test
    public void addingBuildingsTest(){
        List<MapData> buildings = m.getVisibleBuildings(new Rectangle2D.Float(0, 0, 500, 500));

        Assert.assertTrue(!buildings.isEmpty());

        Assert.assertNotNull(buildings.get(0));

        Assert.assertEquals(5, buildings.size());

        Assert.assertTrue(buildings.get(0) instanceof Amenity);



    }


    @Test
    public void addingNaturalsTest(){
        List<MapData> naturals = m.getVisibleNatural(new Rectangle2D.Float(0, 0, 500, 500));

        Assert.assertTrue(!naturals.isEmpty());
        Assert.assertEquals(3, naturals.size());
        Assert.assertNotNull(naturals.get(1));



    }

}
