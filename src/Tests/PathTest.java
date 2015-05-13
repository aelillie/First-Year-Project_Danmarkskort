package Tests;

import Model.Model;
import Model.Path.Edge;
import Model.Path.Graph;
import Model.Path.PathTree;
import Model.Path.Vertices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;
import Model.MapCalculator;
import Model.ValueName;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class PathTest {
    private static final double DELTA = 1e-15;
    private Model m = Model.getModel();
    private Vertices v;
    private Graph g;
    private int start = 933, end = 595;

    @Before
    public void loadMap(){
        //Test map with known vertices for test use.
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/data/PathTest.osm");
            m.loadFile("data/PathTest.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        v = m.getVertices();
        g = m.getGraph();
    }


    @Test
    public void findPath(){
        PathTree shortestTree = new PathTree(g,start, end);
        shortestTree.useShortestPath();
        shortestTree.initiate();
        Assert.assertEquals(true, shortestTree.hasPathTo(end));
        Assert.assertNotNull(shortestTree.pathTo(end));

        PathTree fastestTree = new PathTree(g,start, end);
        fastestTree.useFastestPath();
        fastestTree.initiate();
        Assert.assertEquals(true, fastestTree.hasPathTo(end));
        Assert.assertNotNull(fastestTree.pathTo(end));
    }

    @Test
    public void testPathDifferences(){
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
        double SPtime = 0;
        for(Edge e : edges)
            SPtime += e.driveTime();

        Iterable<Edge> edges1 = fastestTree.pathTo(end);
        double FPdist = 0;
        for (Edge e : edges1)
                FPdist += e.distance();

        //The two paths should be different.
        Assert.assertNotEquals(fastestTree.pathTo(end), shortestTree.pathTo(end));

        //Fastest path should find a faster path
        Assert.assertTrue(fastestTree.timeTo(end) < SPtime);
        //Shortest path should find a shorter path
        Assert.assertTrue(shortestTree.distTo(end) < FPdist);

    }

    @Test
    public void testDistance() {
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

    @Test
    public void testTime(){
        PathTree pathTree = new PathTree(g, 1512, 131);
        pathTree.useFastestPath();
        pathTree.useCarRoute();
        pathTree.initiate();
        Stack<Edge> path = (Stack<Edge>) pathTree.pathTo(131);

        //Expected distances
        double d1 = MapCalculator.haversineDist(v.getVertex(1512), v.getVertex(637));
        double d2 = MapCalculator.haversineDist(v.getVertex(637), v.getVertex(1509));
        double d3 = MapCalculator.haversineDist(v.getVertex(1509), v.getVertex(1566));
        double d4 = MapCalculator.haversineDist(v.getVertex(1566), v.getVertex(131));
        int maxSpeed = 60; //Roskildevej

        Assert.assertTrue(pathTree.hasPathTo(131));
        Assert.assertNotNull(pathTree.pathTo(131));

        //Expected travel time
        double t1 = (d1/maxSpeed)*60;
        double t2 = (d2/maxSpeed)*60;
        double t3 = (d3/maxSpeed)*60;
        double t4 = (d4/maxSpeed)*60;
        double time = t1 + t2 + t3 + t4;

        Assert.assertEquals(t1, pathTree.timeTo(637), DELTA);
        Assert.assertEquals(t1 + t2, pathTree.timeTo(1509), DELTA);
        Assert.assertEquals(t1 + t2 + t3, pathTree.timeTo(1566), DELTA);
        Assert.assertEquals(time, pathTree.timeTo(131), DELTA);

        //Travel time shown in GUI will take traffic signals into account
        Assert.assertTrue(v.getVertex(path.get(2).w()).trafficSignal == ValueName.TRAFFICSIGNAL);
        Assert.assertTrue(v.getVertex(path.get(3).w()).trafficSignal == ValueName.TRAFFICSIGNAL);
        double actualTime = pathTree.timeTo(131) + path.get(2).trafficSignal() + path.get(3).trafficSignal();
        Assert.assertEquals(time + 0.6, actualTime, DELTA);
    }

    @Test
    public void testFastestPath() {
        PathTree pathTree = new PathTree(g, 202, 338);
        pathTree.useFastestPath();
        pathTree.useCarRoute();
        pathTree.initiate();

        //Other route : 202 -> 201 -> -> 200 -> 339 -> 338

        //Fastest route : 202 -> 256 -> 257 -> 258 -> 269 -> 176 -> 339 -> 338
    }
}
