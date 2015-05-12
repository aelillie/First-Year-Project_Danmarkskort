package Tests;

import Model.*;
import Model.MapFeatures.Highway;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kevin on 08-04-2015.
 */
public class FindNearestTest {
    ArrayList<MapData> mapFeatures;

    @Before
    public void setUp(){
        mapFeatures = new ArrayList<>();

        ArrayList<OSMNode> points = new ArrayList<>();
        points.add(new OSMNode(0,0));
        points.add(new OSMNode(10,10));
        points.add(new OSMNode(15,10));
        points.add(new OSMNode(50,60));
        points.add(new OSMNode(70,80));
        points.add(new OSMNode(75,80));
        points.add(new OSMNode(20,30));
        points.add(new OSMNode(30,40));
        points.add(new OSMNode(40,40));
        points.add(new OSMNode(15,75));
        points.add(new OSMNode(25,50));



        mapFeatures.add(new Highway(PathCreater.createWay(points.subList(0, 2)), 0 , "H1", false, "vej1", null));
        mapFeatures.add(new Highway(PathCreater.createWay(points.subList(3, 5)), 0 , "H2", false, "vej2", null));
        mapFeatures.add(new Highway(PathCreater.createWay(points.subList(6, 8)), 0 , "H3", false, "vej3", null));
        mapFeatures.add(new Highway(PathCreater.createWay(points.subList(9, 10)), 0 , "H4", false, "vej4", null));


    }


    @Test
    public void findNearest(){

        MapFeature champion = RouteFinder.findNearestHighway(new Point(8, 5), mapFeatures);

        assertEquals("H1", champion.getValue());


        champion = RouteFinder.findNearestHighway(new Point2D.Double(70, 75), mapFeatures);

        assertEquals("H2", champion.getValue());
    }


    @Test (expected = NullPointerException.class)
    public void noClosestPath(){

        MapFeature champion = RouteFinder.findNearestHighway(new Point(3,5), new ArrayList<>());

        if(champion == null)
            throw new NullPointerException("no ways given");
    }

    @Test
    public void twoEqualClose(){
        ArrayList<OSMNode> points = new ArrayList<>();
        points.add(new OSMNode(0,0));
        points.add(new OSMNode(0,10));
        points.add(new OSMNode(10,0));
        points.add(new OSMNode(10,10));

        ArrayList<MapData> twoPaths = new ArrayList<>();

        twoPaths.add(new Highway(PathCreater.createWay(points.subList(0, 1)), 0 , "H1", false, "vej1", null));

        twoPaths.add(new Highway(PathCreater.createWay(points.subList(2,3)), 0 , "H2", false, "vej2", null));

        MapFeature champion = RouteFinder.findNearestHighway(new Point2D.Double(5,5), twoPaths);



        assertTrue(champion != null);
        String value = champion.getValue();

        assertTrue(value.equals("H1") || value.equals("H2"));


    }


}
