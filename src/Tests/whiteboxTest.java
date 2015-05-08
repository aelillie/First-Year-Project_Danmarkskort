package Tests;

import Model.MapFeatures.Highway;
import Model.Model;
import Model.Path.Edge;
import Model.PathCreater;

import Model.MapCalculator;
import Model.Path.PathTree;
import Model.Path.Vertices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anders on 06-05-2015.
 */
public class whiteboxTest {
    private static final double DELTA = 1e-15;
    private Model m = Model.getModel();
    private Vertices V;



    @Before
    public void setUp() {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/data/PathTest.osm");
            m.loadFile("ways.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        V = m.getVertices();
    }
    @Test
    public void branch1case1() {
        PathTree PT = new PathTree(m.getDiGraph(), 1509, 1509);
        PT.useShortestPath();
        PT.useCarRoute();
        PT.initiate();

        Assert.assertTrue(checkForOcc(PT) == 2);
        Assert.assertEquals(MapCalculator.haversineDist(V.getVertex(1509), V.getVertex(932)), PT.getValueTo()[932], DELTA);
        Assert.assertEquals(MapCalculator.haversineDist(V.getVertex(1509), V.getVertex(1566)), PT.getValueTo()[1566], DELTA);
    }

    private int checkForOcc(PathTree PT) {
        int counter = 0;
        for (Edge e : PT.getEdgeTo()) {
            if (e != null) counter++;
        }
        return counter;
    }

    @Test
    public void shortest() {
        int s = 1509;
        int d = 841;
        PathTree shortPathTree = new PathTree(m.getDiGraph(), s, d);
        shortPathTree.useShortestPath();
        shortPathTree.useCarRoute();
        shortPathTree.initiate();

        //Computed using haversinedist
        double d0 = 0.01490535353407642588;
        double d1 = 0.010151537740917758;   //1509 -> 1566
        double d2 = 0.156346002534375;      //1566 -> 131
        double d3 = 0.032584261132448104;   //131 -> 132
        double d4 = 0.09822315895524689;    //132 -> 133
        double d5 = 0.10270560188920581;    //133 -> 841



    }

    @Test
    public void fast() {
        int s = 1509;
        int d = 841;
        PathTree fastPathTree = new PathTree(m.getDiGraph(), s, d);
        fastPathTree.useShortestPath();
        fastPathTree.useCarRoute();
        fastPathTree.initiate();

        //Computed using haversinedist
        double d1 = 0.010151537740917758;   //1509 -> 1566
        double d2 = 0.156346002534375;      //1566 -> 131
        double d3 = 0.10375198853577307;   //131 -> 839
        double d4 = 0.0517383369737396;    //839 -> 840
        double d5 = 0.09198325731698755;    //840 -> 841



    }



    /*
    @Test
    public void testPathTree() {
        Vertices vertices = new Vertices();
        HashMap<String, String> kv_map = new HashMap<>();
        kv_map.put("highway","primary");
        Point2D point1 = new Point2D.Float(10, 20);
        Point2D point2 = new Point2D.Float(20,30);
        Point2D point3 = new Point2D.Float(30,40);
        Point2D point4 = new Point2D.Float(40,50);
        Point2D point5 = new Point2D.Float(50,60);
        List<Point2D> wayCoords1 = new ArrayList<>();
        List<Point2D> wayCoords2 = new ArrayList<>();
        List<Point2D> wayCoords3 = new ArrayList<>();
        List<Point2D> wayCoords4 = new ArrayList<>();
        wayCoords1.add(point1);
        wayCoords1.add(point2);
        wayCoords2.add(point2);
        wayCoords2.add(point3);
        wayCoords3.add(point3);
        wayCoords3.add(point4);
        wayCoords4.add(point4);
        wayCoords4.add(point5);
        Path2D way1 = PathCreater.createWay(wayCoords1);
        Path2D way2 = PathCreater.createWay(wayCoords2);
        Path2D way3 = PathCreater.createWay(wayCoords3);
        Path2D way4 = PathCreater.createWay(wayCoords4);

        Highway highway1 = new Highway(way1, 2, "primary", false, "Højager", "50");
        highway1.setRouteType(kv_map);
        vertices.add(wayCoords1);
        highway1.assignEdges(wayCoords1, "no");
        Highway highway2 = new Highway(way2, 2, "primary", false, "Hundevej", "50");
        highway2.setRouteType(kv_map);
        vertices.add(wayCoords2);
        highway2.assignEdges(wayCoords2, "no");
        Highway highway3 = new Highway(way3, 2, "primary", false, "Yologade", "80");
        highway3.setRouteType(kv_map);
        vertices.add(wayCoords3);
        highway3.assignEdges(wayCoords3, "no");
        Highway highway4 = new Highway(way4, 2, "primary", false, "Motorvejen", "50");
        highway4.setRouteType(kv_map);
        vertices.add(wayCoords4);
        highway4.assignEdges(wayCoords4, "no");
        System.out.println("lol");
    } */
}
