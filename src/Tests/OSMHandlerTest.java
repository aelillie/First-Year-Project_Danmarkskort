package Tests;

import MapFeatures.Barrier;
import MapFeatures.Highway;
import MapFeatures.Leisure;
import Model.Model;
import Model.MapData;
import Model.MapCalculator;
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
    }

    @Test
    public void MapFeatureWayTest() {
        List<Point2D> highwayCoords = new ArrayList<>();
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6695228), 12.5802146f));
        highwayCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6693366), 12.5805524f));
        List<Point2D> barrierCoords = new ArrayList<>();
        barrierCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6677049), 12.5853856f));
        barrierCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6676617), 12.5853626f));
        List<Point2D> leisureCoords = new ArrayList<>();
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6653196), 12.5818835f));
        leisureCoords.add(new Point2D.Float((float) MapCalculator.latToY(55.6654239), 12.5820209f));

        //The map features we want
        Highway highway = null;
        Barrier barrier = null;
        Leisure leisure = null;


        assert streetList != null;
        for (MapData mapData : streetList) {
            if (mapData instanceof Highway && highway.getValue().equals("tertiary"))
                highway = (Highway) mapData;
            else if (mapData instanceof Barrier && barrier.getValue().equals("hedge"))
                barrier = (Barrier) mapData;
            else if (mapData instanceof Leisure && leisure.getValue().equals("garden"))
                leisure = (Leisure) mapData;
        }

        if(highway != null && barrier != null && leisure != null) {
            PathIterator highwayPath = highway.getShape().getPathIterator(new AffineTransform()); //object that iterates along a shape
            PathIterator barrierPath = barrier.getShape().getPathIterator(new AffineTransform());
            PathIterator leisurePath = leisure.getShape().getPathIterator(new AffineTransform());
            pathIterate(highwayPath, highwayCoords);
            pathIterate(barrierPath, barrierCoords);
            pathIterate(leisurePath, leisureCoords);
        }
    }

    private void pathIterate(PathIterator path, List<Point2D> wayCoords) {
        int i = 0;
        while(!path.isDone()) {
            float[] points = new float[6];
            path.currentSegment(points);
            Assert.assertEquals(points[0], wayCoords.get(i).getY(), DELTA); //point[0] is y-coordinate
            Assert.assertEquals(points[1], wayCoords.get(i).getX(), DELTA); //point[1] is x-coordinate
            path.next();
            i++;
        }
    }

}
