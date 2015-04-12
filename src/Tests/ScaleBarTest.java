package Tests;

import Controller.Controller;
import Model.MapCalculator;
import Model.Model;
import View.Scalebar;
import View.View;
import javafx.scene.transform.Affine;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jar.Main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Created by Nicoline on 11-04-2015.
 */
public class ScaleBarTest {

    private Model m = Model.getModel();
    private InputStream inputStream;
    private View v;

   /* @Before
    public void setUp(){
        try {
            inputStream = Main.class.getResourceAsStream("/data/test_map.osm");
            m.loadFile("/data/test_map.osm", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        v = new View(m);

    }*/

    /*@Test
    public void getDesiredDistanceTest(){
        Scalebar scalebar = v.getScaleBar();

        double lineWidth = 100.0;
        double desiredDistance = 1.0; //1 km
        try {
            Point2D startPoint = callGetDesiredDistance(lineWidth, desiredDistance,scalebar); //Nullpointerexception?!
            Point2D endPoint = scalebar.getLineEnd();
            Point2D transformedStart = new Point2D.Double();
            Point2D transformedEnd = new Point2D.Double();

            AffineTransform transform = scalebar.getTransform();
            transform.inverseTransform(startPoint, transformedStart);
            transform.inverseTransform(endPoint, transformedEnd);
            double dist = MapCalculator.haversineDist(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());

        } catch (Exception e){
            e.printStackTrace();
        }

    }*/

    public Point2D callGetDesiredDistance(double lineWidth, double desiredDistance, Scalebar scalebar) throws Exception{
        //private Point2D getDesiredDistance(double lineWidth, double desiredDistance)
        Method method = Scalebar.class.getDeclaredMethod("getDesiredDistance",double.class,double.class);
        method.setAccessible(true);
        return (Point2D) method.invoke(scalebar,lineWidth,desiredDistance);
    }
}
