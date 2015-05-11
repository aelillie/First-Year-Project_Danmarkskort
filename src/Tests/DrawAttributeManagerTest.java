package Tests;

import Model.MapFeature;
import Model.MapFeatures.Building;
import Model.MapFeatures.Highway;
import Model.MapFeatures.Leisure;
import Model.MapFeatures.Natural;
import Model.PathCreater;
import Model.OSMNode;
import View.DrawAttribute;
import View.DrawAttributeManager;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by i5-4670K on 09-04-2015.
 */
public class DrawAttributeManagerTest {
    private ArrayList<MapFeature> mapFeatures;
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();


    @Before
    public void setUp(){
        mapFeatures = new ArrayList<>();

        mapFeatures.add(new Highway(createRandomPath(), -5, "tertiary", false, "vej312312", null));
        mapFeatures.add(new Building(createRandomPath(), 0, "building"));
        mapFeatures.add(new Natural(createRandomPath(), 0, "wood"));
        mapFeatures.add(new Leisure(createRandomPath(), -1, "park"));
    }


    private Path2D createRandomPath(){
        ArrayList<OSMNode> points = new ArrayList<>();
        Random r = new Random();
        points.clear();
        points.add(new OSMNode(r.nextFloat(), r.nextFloat()));
        points.add(new OSMNode(r.nextFloat(), r.nextFloat()));
        points.add(new OSMNode(r.nextFloat(), r.nextFloat()));
        return PathCreater.createWay(points);
    }


    @Test
    public void testManager(){

        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(0).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightyellow);
        assertEquals(drawAttribute.getZoomLevel(), 6);
        drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(2).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightgreen);
        assertEquals(drawAttribute.isDashed(), false);

    }

    @Test
    public void TestToggle(){
        drawAttributeManager.toggleTransportView();

        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(0).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.lightergrey);
        assertEquals(drawAttribute.getZoomLevel(),6);
        drawAttribute = drawAttributeManager.getDrawAttribute(mapFeatures.get(2).getValueName());

        assertEquals(drawAttribute.getColor(), DrawAttribute.whitegreen);
        assertEquals(drawAttribute.isDashed(), false);

    }
}
