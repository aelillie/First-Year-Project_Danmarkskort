package Tests;

import MapFeatures.*;
import Model.MapFeature;
import Model.PathCreater;
import View.DrawAttribute;
import View.DrawAttributeManager;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by i5-4670K on 09-04-2015.
 */
public class DrawAttributeManagerTest {
    private ArrayList<MapFeature> mapFeatures;
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();


    @Before
    public void setUp(){
        mapFeatures = new ArrayList<>();

        mapFeatures.add(new Highway(createRandomPath(), -5, "tertiary", false));
        mapFeatures.add(new Building(createRandomPath(), 0, "building"));
        mapFeatures.add(new Natural(createRandomPath(), 0, "wood"));
        mapFeatures.add(new Leisure(createRandomPath(), -1, "park"));


    }


    private Path2D createRandomPath(){
        ArrayList<Point2D> points = new ArrayList<>();
        Random r = new Random();
        points.clear();
        points.add(new Point2D.Float(r.nextFloat(), r.nextFloat()));
        points.add(new Point2D.Float(r.nextFloat(), r.nextFloat()));
        points.add(new Point2D.Float(r.nextFloat(), r.nextFloat()));
        return PathCreater.createWay(points);
    }


    @Test
    public void testManager(){

        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(0).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightyellow);
        assertEquals(drawAttribute.getZoomLevel(), 8);
        drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(2).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightgreen);
        assertEquals(drawAttribute.isDashed(), false);

    }

    @Test
    public void TestToggle(){
        drawAttributeManager.toggleTransportView();

        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(0).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightergrey);
        assertEquals(drawAttribute.getZoomLevel(),5);
        drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(2).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.whitegreen);
        assertEquals(drawAttribute.isDashed(), false);

    }
}
