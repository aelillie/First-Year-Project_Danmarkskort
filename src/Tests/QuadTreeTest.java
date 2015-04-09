package Tests;

import MapFeatures.Building;
import MapFeatures.Highway;
import Model.MapFeature;
import Model.PathCreater;
import QuadTree.QuadTree;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kevin on 08-04-2015.
 */
public class QuadTreeTest {
    private QuadTree quadTree;
    private ArrayList<Path2D> paths;

    @Before
    public void CreateQuadTree(){
        paths = new ArrayList<>();
        ArrayList<Point2D> points = new ArrayList<>();
        Rectangle2D testBox = new Rectangle2D.Float(0,0,100,100);
        quadTree = new QuadTree(testBox);
        points.add(new Point2D.Float(0,0));
        points.add(new Point2D.Float(10,10));
        points.add(new Point2D.Float(50,60));
        points.add(new Point2D.Float(70,80));
        points.add(new Point2D.Float(20,30));
        points.add(new Point2D.Float(30,40));
        points.add(new Point2D.Float(40,40));
        points.add(new Point2D.Float(15,75));
        points.add(new Point2D.Float(25,50));

        paths.add(PathCreater.createWay(points.subList(0, 1)));
        paths.add(PathCreater.createWay(points.subList(2,3)));
        paths.add(PathCreater.createWay(points.subList(4,6)));
        paths.add(PathCreater.createWay(points.subList(7,8)));

        MapFeature mf1 = new Highway(paths.get(0), 0, "road", false);
        MapFeature mf2 = new Highway(paths.get(1), 0, "trunk", false);
        MapFeature mf3 = new Building(paths.get(2), 0, "building");
        MapFeature mf4 = new Highway(paths.get(3), 0, "footway", false);


        quadTree.insert(mf1);
        quadTree.insert(mf2);
        quadTree.insert(mf3);
        quadTree.insert(mf4);

    }


    @Test
    public void TestQuerySearch(){

        Rectangle2D window = new Rectangle2D.Float(60,30,60,30);

        ArrayList<List<MapFeature>> visible = quadTree.query2D(window);

        assertEquals( 1,visible.size() );

        List<MapFeature> list1 = visible.get(0);
        assertEquals(list1.size(), 1);


    }

    @Test
    public void TestSubdivide(){
        ArrayList<Point2D> points = new ArrayList<>();

        for(int i = 0; i < 1500; i++){
            points.clear();
            points.add(new Point2D.Float((i*4%50) + 50, i%50));
            points.add(new Point2D.Float((i*10/2%50) + 50, i/2%50));
            quadTree.insert(new Highway(PathCreater.createWay(points), 0, "road", false));
        }

        List<List<MapFeature>> Data = quadTree.query2D(new Rectangle2D.Float(51,1,100,49));

        assertEquals(4, Data.size());

        assertTrue(!Data.get(0).isEmpty());
        assertTrue(!Data.get(1).isEmpty());
        assertTrue(!Data.get(2).isEmpty());
        assertTrue(!Data.get(3).isEmpty());

    }

}
