package Tests;

import Model.MapFeatures.Highway;
import Model.MapFeature;
import Model.OSMNode;
import Model.PathCreater;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kevin on 08-04-2015.
 */
public class FindNearestTest {
    ArrayList<MapFeature> mapFeatures;

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

    private MapFeature findNearest(Point2D position){
        //Rectangle2D rec = new Rectangle2D.Double(position.getX(), position.getY(),0,0);


        MapFeature champion = null;
        Line2D championLine = null;

        for (MapFeature mp : mapFeatures) {
            if (mp instanceof Highway) {
                float[] points = new float[6];
                PathIterator pI = mp.getWay().getPathIterator(new AffineTransform());
                pI.currentSegment(points);
                Point2D p1 = new OSMNode(points[0], points[1]);
                pI.next();
                while(!pI.isDone()) {
                    pI.currentSegment(points);
                    Point2D p2 = new OSMNode(points[0], points[1]);

                    pI.next();

                    Line2D path = new Line2D.Double(p1,p2);
                    p1 = p2;
                    if(championLine == null) {
                        championLine = path;
                        champion = mp;
                    }

                    else if(path.ptSegDist(position) < championLine.ptSegDist(position)){
                        champion = mp;
                        championLine = path;

                    }
                }
            }

        }


        return champion;

    }


    @Test
    public void findNearest(){

        MapFeature champion = findNearest(new Point(8,5));

        assertEquals("H1", champion.getValue());


        champion = findNearest(new Point(70,75));

        assertEquals("H2", champion.getValue());
    }


}
