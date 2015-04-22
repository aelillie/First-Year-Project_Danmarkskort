package Tests;

import MapFeatures.*;
import Model.Model;
import Model.MapData;
import Model.MapIcon;
import Model.MapFeature;
import Model.MapCalculator;
import org.junit.After;
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
    List<MapData> streetList;
    List<MapData> buildingList;
    List<MapData> iconList;
    List<MapData> naturalList;
    List<MapData> mapFeatureList = new ArrayList<>();

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
        streetList = m.getVisibleStreets(new Rectangle2D.Float(0, 0, 500, 500));
        buildingList = m.getVisibleBuildings(new Rectangle2D.Float(0, 0, 500, 500));
        iconList = m.getVisibleIcons(new Rectangle2D.Float(0, 0, 500, 500));
        naturalList = m.getVisibleNatural(new Rectangle2D.Float(0, 0, 500, 500));
        mapFeatureList.addAll(streetList);
        mapFeatureList.addAll(m.getVisibleRailways(new Rectangle2D.Float(0,0,500,500)));
        mapFeatureList.addAll(buildingList);
        mapFeatureList.addAll(naturalList);
    }

    @After
    public void tearDown() {
        streetList.clear();
        buildingList.clear();
        iconList.clear();
        naturalList.clear();
    }

    @Test
    public void MapFeatureWayTest() {
        List<Point2D> highwayCoords = new ArrayList<>();
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6695228), 12.5802146f));
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6693366), 12.5805524f));
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6690488), 12.5811529f));
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6687563), 12.5817373f));
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6685247), 12.5823799f));
        List<Point2D> barrierCoords = new ArrayList<>();
        barrierCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6677049), 12.5853856f));
        barrierCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6676617), 12.5853626f));
        List<Point2D> leisureCoords = new ArrayList<>();
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6653496), 12.5818120f));
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6653196), 12.5818835f));
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6654239), 12.5820209f));
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6654538), 12.5819494f));
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6653496), 12.5818120f));


        //The map features we want
        MapFeature highway = null;
        MapFeature barrier = null;
        MapFeature leisure = null;

        //Will 100% be of type MapFeature, since it's from buildingList
        //It is therefore safe to assume that it can be cast to MapFeature
        for (MapData mapData : streetList) {
            highway = (MapFeature) mapData;
            if (highway.getValue().equals("tertiary"))
                break;
        }
        for (MapData mapData : buildingList) {
            barrier = (MapFeature) mapData;
            if (barrier.getValue().equals("hedge"))
                break;
        }
        for (MapData mapData : buildingList) {
            leisure = (MapFeature) mapData;
            if (leisure.getValue().equals("garden"))
                break;
        }

        PathIterator highwayPath = highway.getWay().getPathIterator(new AffineTransform()); //object that iterates along a shape
        PathIterator barrierPath = barrier.getWay().getPathIterator(new AffineTransform());
        PathIterator leisurePath = leisure.getWay().getPathIterator(new AffineTransform());
        pathIterate(highwayPath, highwayCoords);
        pathIterate(barrierPath, barrierCoords);
        pathIterate(leisurePath, leisureCoords);

    }

    private void pathIterate(PathIterator path, List<Point2D> wayCoords) {
        int i = 0;
        while(!path.isDone()) {
            float[] points = new float[6]; //creates space to store coordinates
            path.currentSegment(points); //put coordinates from path's current segment into the array
            Assert.assertEquals(points[0], wayCoords.get(i).getY(), DELTA); //point[0] is y-coordinate
            Assert.assertEquals(points[1], wayCoords.get(i).getX(), DELTA); //point[1] is x-coordinate
            path.next(); //next segment in the path
            i++;
        }
    }

    @Test
    public void addingIconsTest(){
        Assert.assertEquals(5, iconList.size());
        MapIcon icon = (MapIcon) iconList.get(0);
        Assert.assertTrue(icon.getClassType() == MapIcon.class);
    }

    @Test
    public void addingBuildingsTest(){
        Assert.assertTrue(!buildingList.isEmpty());
        Assert.assertNotNull(buildingList.get(0));
        Assert.assertEquals(5, buildingList.size());
        Assert.assertTrue(buildingList.get(0) instanceof MapFeature);
    }


    @Test
    public void addingNaturalsTest(){
        Assert.assertTrue(!naturalList.isEmpty());
        Assert.assertEquals(3, naturalList.size());
        Assert.assertNotNull(naturalList.get(1));
        Assert.assertTrue(naturalList.get(0) instanceof MapFeature);
    }

    @Test
    public void addingStreetTest(){
        Assert.assertTrue(!streetList.isEmpty());
        Assert.assertEquals(3, streetList.size());
        Assert.assertNotNull(streetList.get(1));
        Assert.assertTrue(streetList.get(0) instanceof Highway);
    }

    @Test
    public void fetchOSMLayerTest() {
        int tertiary_layerVal = 0;
        int service_layerVal = 0;
        int subway_layerVal = 0;
        int building_layerVal = 0;
        int amenity_layerVal = 0;
        for (MapData feature : mapFeatureList) {
            MapFeature mapFeature = (MapFeature) feature;
            switch (mapFeature.getValue()) {
                case "tertiary":
                    tertiary_layerVal = mapFeature.getLayerVal();
                    break;
                case "service":
                    service_layerVal = mapFeature.getLayerVal();
                    break;
                case "subway":
                    subway_layerVal = mapFeature.getLayerVal();
                    break;
                case "yes":
                    building_layerVal = mapFeature.getLayerVal();
                    break;
                case "parking":
                    amenity_layerVal = mapFeature.getLayerVal();
                    break;
            }
        }
        Assert.assertEquals(13, tertiary_layerVal);
        Assert.assertEquals(10, service_layerVal);
        Assert.assertEquals(-10, subway_layerVal);
        Assert.assertEquals(19, building_layerVal);
        Assert.assertEquals(0, amenity_layerVal);
    }

}
