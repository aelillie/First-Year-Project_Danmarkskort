package Tests;

import Model.MapCalculator;
import Model.Model;
import Model.Path.Edge;
import Model.Path.PathTree;
import Model.Path.Vertices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Anders on 06-05-2015.
 */
public class whiteboxTest {
    private static final double DELTA = 1e-15;
    private Model m = Model.getModel();
    private Vertices V;
    double d0; //1509 -> 932
    double d1; //1509 -> 1566
    double d2; //1566 -> 131
    double d3; //131 -> 132
    double d4; //132 -> 133
    double d5; //133 -> 841
    double d6; //133 -> 78
    double d7; //133 -> 134
    double d8; //841 -> 842
    double d9; //131 -> 839
    double d10; //840 -> 841
    double d11; //839 -> 840




    @Before
    public void setUp() {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/data/PathTest.osm");
            m.loadFile("ways.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        V = m.getVertices();
        d0 = MapCalculator.haversineDist(V.getVertex(1509), V.getVertex(932));  //1509 -> 932
        d1 = MapCalculator.haversineDist(V.getVertex(1509), V.getVertex(1566)); //1509 -> 1566
        d2 = MapCalculator.haversineDist(V.getVertex(1566), V.getVertex(131));  //1566 -> 131
        d3 = MapCalculator.haversineDist(V.getVertex(131), V.getVertex(132));   //131 -> 132
        d4 = MapCalculator.haversineDist(V.getVertex(132), V.getVertex(133));   //132 -> 133
        d5 = MapCalculator.haversineDist(V.getVertex(133), V.getVertex(841));   //133 -> 841
        d6 = MapCalculator.haversineDist(V.getVertex(133), V.getVertex(78));    //133 -> 78
        d7 = MapCalculator.haversineDist(V.getVertex(133), V.getVertex(134));   //133 -> 134
        d8 = MapCalculator.haversineDist(V.getVertex(841), V.getVertex(842));   //841 -> 842
        d9 = MapCalculator.haversineDist(V.getVertex(131), V.getVertex(839));   //131 -> 839
        d10 = MapCalculator.haversineDist(V.getVertex(840), V.getVertex(841));  //840 -> 841
        d11 = MapCalculator.haversineDist(V.getVertex(839), V.getVertex(840));  //839 -> 840
    }

    private int checkForOcc(PathTree PT) {
        int counter = 0;
        for (Edge e : PT.getEdgeTo()) {
            if (e != null)
                counter++;
        }
        return counter;
    }

    /**
     * Whiteboxtest with source vertex and destination vertex being the same - 0 iterations
     */
    @Test
    public void branch1case1() {
        PathTree PT = new PathTree(m.getDiGraph(), 1509, 1509);
        PT.useShortestPath();
        PT.useCarRoute();
        PT.initiate();

        Assert.assertTrue(checkForOcc(PT) == 0);
        //Assert.assertEquals(d0, PT.getValueTo()[932], DELTA);
        //Assert.assertEquals(d1, PT.getValueTo()[1566], DELTA);
    }
    /**
     *
     */
    @Test
    public void branch1case2() {
        PathTree PT = new PathTree(m.getDiGraph(), 1509, 1566);
        PT.useShortestPath();
        PT.useCarRoute();
        PT.initiate();

        Assert.assertTrue(checkForOcc(PT) == 2);
        Assert.assertEquals(d0, PT.getValueTo()[932], DELTA);
        Assert.assertEquals(d1, PT.getValueTo()[1566], DELTA);
        //Assert.assertEquals(d1 + d2, PT.getValueTo()[131], DELTA);
    }

    @Test
    public void branch1case3() {
        PathTree PT = new PathTree(m.getDiGraph(), 1509, 131);
        PT.useShortestPath();
        PT.useCarRoute();
        PT.initiate();
        int actualNum = checkForOcc(PT);

        Assert.assertTrue(3 == actualNum);
        Assert.assertEquals(d0, PT.getValueTo()[932], DELTA);
        Assert.assertEquals(d1, PT.getValueTo()[1566], DELTA);
        Assert.assertEquals(d1 + d2, PT.getValueTo()[131], DELTA);
       //Assert.assertEquals(d1 + d2 + d3, PT.getValueTo()[132], DELTA);
        //Assert.assertEquals(d1 + d2 + d9, PT.getValueTo()[839], DELTA);
    }

    @Test
    public void branch2case1(){


        PathTree zeroAdjacent= new PathTree(m.getDiGraph(), 636, 635);
        zeroAdjacent.useShortestPath();
        zeroAdjacent.useCarRoute();
        zeroAdjacent.initiate();

        //no adjacent  == 0 iterations
        Assert.assertTrue(0 == checkForOcc(zeroAdjacent));

        Assert.assertEquals(Double.POSITIVE_INFINITY, zeroAdjacent.distTo(635), DELTA);

    }

    @Test
    public void branch2case2(){
        PathTree oneAdjacent = new PathTree(m.getDiGraph(), 635, 636);
        oneAdjacent.useShortestPath();
        oneAdjacent.useCarRoute();
        oneAdjacent.initiate();

        //Only needs to check one adjacent edge
        Assert.assertEquals(1, checkForOcc(oneAdjacent));

        Assert.assertTrue(oneAdjacent.hasPathTo(636));

        Assert.assertEquals(Double.POSITIVE_INFINITY, oneAdjacent.distTo(634), DELTA);

    }

    @Test
    public void branch2case3(){
        PathTree moreAdjacent = new PathTree(m.getDiGraph(), 510, 511);

        moreAdjacent.useShortestPath();
        moreAdjacent.useCarRoute();
        moreAdjacent.initiate();

        //Vertex 510 has 3 adjacent edges
        Assert.assertEquals(3, checkForOcc(moreAdjacent));

        Assert.assertTrue(moreAdjacent.hasPathTo(511));

        Assert.assertTrue(moreAdjacent.hasPathTo(689));
        Assert.assertTrue(moreAdjacent.hasPathTo(509));


    }

    @Test
    public void branch3case1(){
        PathTree PT = new PathTree(m.getDiGraph(), 1120, 393);
        PT.useShortestPath();
        PT.useCarRoute();
        PT.initiate();

        //Due to no path the distance to should be infinite

        Assert.assertEquals(Double.POSITIVE_INFINITY, PT.distTo(393), DELTA);



    }

    @Test
    public void branch4case1and2(){
        PathTree shortest = new PathTree(m.getDiGraph(), 1509, 841);
        PathTree fastest = new PathTree(m.getDiGraph(), 1509, 841);

        shortest.useCarRoute();
        shortest.useShortestPath();
        shortest.initiate();


        fastest.useCarRoute();
        fastest.useFastestPath();
        fastest.initiate();
        //Chosen a path were fastest and shortest shouldn't be the same.

        Assert.assertNotEquals(shortest.pathTo(841), fastest.pathTo(841));

        HashSet<Integer> actualShortestVertices = new HashSet<>();
        for(Edge e : shortest.pathTo(841)){
            actualShortestVertices.add(e.either());
            actualShortestVertices.add(e.other(e.either()));
        }
        HashSet<Integer> expectedShortestVertices = new HashSet<>();
        expectedShortestVertices.add(1509);
        expectedShortestVertices.add(1566);
        expectedShortestVertices.add(131);
        expectedShortestVertices.add(132);
        expectedShortestVertices.add(133);
        expectedShortestVertices.add(841);


        Assert.assertEquals(expectedShortestVertices, actualShortestVertices);

        HashSet<Integer> actualFastestVertices = new HashSet<>();
        for(Edge e : shortest.pathTo(841)){
            actualShortestVertices.add(e.either());
            actualShortestVertices.add(e.other(e.either()));
        }
        HashSet<Integer> expectedFastestVertices = new HashSet<>();
        expectedShortestVertices.add(1509);
        expectedShortestVertices.add(1566);
        expectedShortestVertices.add(131);
        expectedShortestVertices.add(839);
        expectedShortestVertices.add(840);
        expectedShortestVertices.add(841);

        Assert.assertEquals(expectedFastestVertices, actualFastestVertices);

        double lengthShortest = 0;
        double lengthFastest = 0;

        for(Edge e: shortest.pathTo(841))
            lengthShortest += e.distance();


        for(Edge e: fastest.pathTo(841))
            lengthFastest += e.distance();


        Assert.assertTrue(lengthShortest <= lengthFastest);

        double timeShortest = 0;
        double timeFastest = 0;

        for(Edge e: shortest.pathTo(841))
            timeShortest += e.driveTime();


        for(Edge e: fastest.pathTo(841))
            timeFastest += e.driveTime();

        Assert.assertTrue(timeShortest >= timeFastest);

    }

    @Test
    public void branch5case1(){
        //End found, break of search

        PathTree destinationPossible = new PathTree(m.getDiGraph(), 1509, 131);

        destinationPossible.useCarRoute();
        destinationPossible.useShortestPath();
        destinationPossible.initiate();

        Assert.assertTrue(destinationPossible.hasPathTo(131));
        //Assert.assertTrue(!destinationPossible.hasPathTo(839));

        Assert.assertEquals(Double.POSITIVE_INFINITY , destinationPossible.distTo(839), DELTA);
        Assert.assertEquals(Double.POSITIVE_INFINITY, destinationPossible.distTo(132), DELTA);

    }

    @Test
    public void branch5case2(){
        //End not found, check all possibilities

        PathTree destinationNotPossible = new PathTree(m.getDiGraph(), 95, 654);
        destinationNotPossible.useCarRoute();
        destinationNotPossible.useShortestPath();
        destinationNotPossible.initiate();

        Assert.assertTrue(!destinationNotPossible.hasPathTo(654));

        //Check that it tries all possibilities before leaving while loop

        ArrayList<Integer> possibleVertices = new ArrayList<>();

        possibleVertices.add(84);
        possibleVertices.add(85);
        possibleVertices.add(86);
        possibleVertices.add(87);
        possibleVertices.add(88);
        possibleVertices.add(89);
        possibleVertices.add(90);
        possibleVertices.add(91);
        possibleVertices.add(92);
        possibleVertices.add(93);
        possibleVertices.add(94);
        possibleVertices.add(95);
        possibleVertices.add(96);

        for(Integer vertex : possibleVertices)
            Assert.assertTrue(destinationNotPossible.hasPathTo(vertex));
    }

}
