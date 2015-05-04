package Tests;

import Model.Model;
import Model.ShortestPath.Edge;
import Model.ShortestPath.Graph;
import Model.ShortestPath.PathTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kevin on 29-04-2015.
 */
public class ShortestPathTest {
    Model m = Model.getModel();
    InputStream inputStream;
    int start = 933, end = 595;

    @Before
    public void loadMap(){
        //Test map with known vertices for test use.
        try {
            inputStream = Main.class.getResourceAsStream("/data/PathTest.osm");
            m.loadFile("map.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void findPath(){
        Graph g = m.getDiGraph();

        PathTree pS = new PathTree(g,start, end);
        pS.useShortestPath(true);
        pS.initiate();

        Assert.assertEquals(true, pS.hasPathTo(end));

        Assert.assertNotNull(pS.pathTo(end));


    }

    @Test
    public void testFastestPath(){
        Graph g = m.getDiGraph();

        //Create one pathTree for shortest route and for fastest
        PathTree pS = new PathTree(g, start, end);
        pS.useCarRoute();
        pS.useShortestPath(true);
        pS.initiate();
        PathTree pF = new PathTree(g, start, end);
        pF.useCarRoute();
        pF.useShortestPath(false);
        pF.initiate();
        //can they both find a path?
        Assert.assertTrue(pF.hasPathTo(end));
        Assert.assertTrue(pS.hasPathTo(end));


        Iterable<Edge> edges = pS.pathTo(end);
        double time = 0;
        for(Edge e : edges)
            time += e.driveTime();

        //Fastest path should find a faster path
        Assert.assertTrue(pF.timeTo(end) < time);

        //The two paths should be different.
        Assert.assertNotEquals(pF.pathTo(end), pS.pathTo(end));



    }

}
