package Tests;



import Model.MapCalculator;
import View.Scalebar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Created by i5-4670K on 12-04-2015.
 */
public class ScaleBarTest {
    private Point2D lineStart;
    private Point2D lineEnd;
    private double lineWidth;
    private AffineTransform transform;



    @Before
    public void setUp(){

        lineStart = new Point2D.Double(200, 10);
        lineEnd = new Point2D.Double(100,10);

        transform = new AffineTransform();
        //transform.scale(7667.5, -7667.5);
        transform.scale(20, 20);
        lineWidth = lineEnd.getX()-lineStart.getX();

    }


    @Test
    public void TestStartPoint() {
        assertEquals(new Point2D.Double(150,10), getDesiredDistance(lineWidth, 1.0));

    }




    private Point2D getDesiredDistance(double lineWidth, double desiredDistance){
        Point2D.Float transformedStart = new Point2D.Float();
        Point2D.Float transformedEnd = new Point2D.Float();

        try {
            transform.inverseTransform(lineStart, transformedStart); //Use inverse transform to calculate the points to their corresponding lat and lon according to our transform
            transform.inverseTransform(lineEnd,transformedEnd);
        } catch (NoninvertibleTransformException e){
            e.printStackTrace();
        }


        double distance = 2.0; //Testing of Calculation of real distance is done in MapCalculator test


        double lineWidthPerKm = lineWidth/distance; //Used to calculate the desired distance in pixels
        return new Point2D.Float((float) (lineEnd.getX()-desiredDistance*lineWidthPerKm),(float)lineStart.getY()); //Change the x coordinate to form the desired distance.
    }




}
