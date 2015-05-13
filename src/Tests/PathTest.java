package Tests;

import Model.MapCalculator;
import Model.Model;
import Model.Path.Edge;
import Model.Path.Graph;
import Model.Path.PathTree;
import Model.Path.Vertices;
import Model.ValueName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

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

        Stack<Edge> path = (Stack<Edge>) pathTree.pathTo(338);

        //Two most obvious paths from the test map
        //Route1 : 202 -> 201 -> 200 -> 338
        double d1 = MapCalculator.haversineDist(v.getVertex(202), v.getVertex(201)); //Maxspeed: 50
        double d2 = MapCalculator.haversineDist(v.getVertex(201), v.getVertex(200)); //Maxspeed: 50
        double d3 = MapCalculator.haversineDist(v.getVertex(200), v.getVertex(338)); //Maxspeed: 60
        double t1 = (d1/50)*60;
        double t2 = (d2/50)*60;
        double t3 = (d3/60)*60;
        double time = t1 + t2 + t3;

        //Route2 : 202 -> 256 -> 257 -> 258 -> 269 -> 176 -> 339 -> 338
        double dd1 = MapCalculator.haversineDist(v.getVertex(202), v.getVertex(256));//Maxspeed: 50
        double dd2 = MapCalculator.haversineDist(v.getVertex(256), v.getVertex(257));//Maxspeed: 50
        double dd3 = MapCalculator.haversineDist(v.getVertex(257), v.getVertex(258));//Maxspeed: 50
        double dd4 = MapCalculator.haversineDist(v.getVertex(258), v.getVertex(269));//Maxspeed: 50
        double dd5 = MapCalculator.haversineDist(v.getVertex(269), v.getVertex(176));//Maxspeed: 50
        double dd6 = MapCalculator.haversineDist(v.getVertex(176), v.getVertex(339));//Maxspeed: 50
        double dd7 = MapCalculator.haversineDist(v.getVertex(339), v.getVertex(338));//Maxspeed: 50
        double tt1 = (dd1/50)*60;
        double tt2 = (dd2/50)*60;
        double tt3 = (dd3/50)*60;
        double tt4 = (dd4/50)*60;
        double tt5 = (dd5/50)*60;
        double tt6 = (dd6/50)*60;
        double tt7 = (dd7/50)*60;
        double time1 = tt1 + tt2 + tt3 + tt4 + tt5 + tt6 + tt7;

        Assert.assertNotEquals(time, time1, DELTA); //Make sure they are not the same
        double expMinTime = Math.min(time, time1); //find the actual minimum time for the two routes
        double actualTime = pathTree.timeTo(338); //actual time for the route

        Assert.assertEquals(expMinTime, actualTime, DELTA);
    }

    @Test
    public void shortestPath(){
        PathTree shortest = new PathTree(m.getGraph(), 244, 241);
        shortest.useCarRoute();
        shortest.useShortestPath();
        shortest.initiate();
        Vertices v = m.getVertices();
        Assert.assertTrue(shortest.hasPathTo(241));

        //Manually compute the length of the two paths

        //1. option
        double d244to239 = MapCalculator.haversineDist(v.getVertex(244), v.getVertex(239));
        double d239to240 = MapCalculator.haversineDist(v.getVertex(239), v.getVertex(240));
        double d240to241 = MapCalculator.haversineDist(v.getVertex(240), v.getVertex(241));
        Double option1 = d244to239 + d239to240 + d240to241;
        //2. option
        double d241to760 = MapCalculator.haversineDist(v.getVertex(241), v.getVertex(760));
        double d760to761 = MapCalculator.haversineDist(v.getVertex(760), v.getVertex(761));
        double d761to244 = MapCalculator.haversineDist(v.getVertex(761), v.getVertex(244));
        Double option2 = d241to760 + d760to761 + d761to244;

        double shortestLength = Math.min(option1, option2);

        Assert.assertEquals(shortestLength, shortest.distTo(241), 1e-15);
        Assert.assertEquals(d244to239, shortest.distTo(239), 1e-15);
        Assert.assertEquals(d761to244 + d760to761 , shortest.distTo(760), 1e-15);

    }
}
