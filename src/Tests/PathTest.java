package Tests;

import Model.MapFeatures.Highway;
import Model.Model;
import Model.Path.Edge;
import Model.Path.Graph;
import Model.Path.PathTree;
import Model.Path.Vertices;
import Model.PathCreater;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;
import Model.MapCalculator;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kevin on 29-04-2015.
 */
public class PathTest {
    private static final double DELTA = 1e-15;
    private Model m = Model.getModel();
    private Vertices v = m.getVertices();
    private Graph g;
    private int start = 933, end = 595;

    @Before
    public void loadMap(){
        //Test map with known vertices for test use.
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/data/PathTest.osm");
            m.loadFile("map.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g = m.getDiGraph();
    }


    @Test
    public void findPath(){
        PathTree pS = new PathTree(g,start, end);
        pS.useShortestPath();
        pS.initiate();

        Assert.assertEquals(true, pS.hasPathTo(end));

        Assert.assertNotNull(pS.pathTo(end));


    }

    @Test
    public void testFastestPath(){
        Graph g = m.getDiGraph();

        //Create one pathTree for shortest route and for fastest
        PathTree shortestTree = new PathTree(g, start, end);
        shortestTree.useCarRoute();
        shortestTree.useShortestPath();
        shortestTree.initiate();

        PathTree fastestTree = new PathTree(g, start, end);
        fastestTree.useCarRoute();
        fastestTree.useFastestPath();
        fastestTree.initiate();

        //can they both find a path?
        Assert.assertTrue(fastestTree.hasPathTo(end));
        Assert.assertTrue(shortestTree.hasPathTo(end));


        Iterable<Edge> edges = shortestTree.pathTo(end);
        double time = 0;
        for(Edge e : edges)
            time += e.driveTime();

        //Fastest path should find a faster path
        Assert.assertTrue(fastestTree.timeTo(end) < time);

        //The two paths should be different.
        Assert.assertNotEquals(fastestTree.pathTo(end), shortestTree.pathTo(end));
    }

    @Test
    public void testTravelInfo() {
        Graph g = Model.getModel().getDiGraph();
        PathTree fastestTree = new PathTree(g, 595, 60);
        fastestTree.useCarRoute();
        fastestTree.useShortestPath();
        fastestTree.initiate();

        Assert.assertTrue(fastestTree.hasPathTo(60));

        double distance1 = MapCalculator.haversineDist(v.getVertex(595), v.getVertex(596));
        double distance2 = MapCalculator.haversineDist(v.getVertex(596), v.getVertex(1504));
        double distance3 = MapCalculator.haversineDist(v.getVertex(1504), v.getVertex(60));
        double expDistance = distance1 + distance2 + distance3;

        Iterable<Edge> edges = fastestTree.pathTo(60);
        double realDistance = 0;
        for (Edge e : edges) {
            realDistance += e.distance();
        }

        Assert.assertEquals(expDistance, realDistance, DELTA);
    }

}
