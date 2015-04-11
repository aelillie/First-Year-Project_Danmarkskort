package View;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Kevin on 26-03-2015.
 */
public class CanvasBounds {

    private Rectangle2D bounds;
    private AffineTransform transform;


    /**
     * stores a slightly bigger copy of the rectangle given, and a
     * reference to the AffineTransformer used
     * @param bounds    Rectangle2D to be stored
     * @param transform AffineTransform used to draw
     */
    public CanvasBounds(Rectangle2D bounds, AffineTransform transform){
        this.bounds = new Rectangle2D.Double(
                bounds.getX() - bounds.getWidth()/4,
                bounds.getY() + bounds.getHeight()/4,
                bounds.getWidth()* 1.40,
                bounds.getHeight() *1.40);
        this.transform = transform;
    }

    /**
     * Updates the values of the Rectangle2D
     * @param viewRect  New Rectangle.
     */
    public void updateBounds(Rectangle2D viewRect){
        AffineTransform inverser = new AffineTransform();
        try {
            inverser = transform.createInverse();
        }catch (NoninvertibleTransformException e){}

        Path2D.Double tmp = (Path2D.Double) inverser.createTransformedShape(viewRect);

        Rectangle2D tmpRect = tmp.getBounds2D();

        bounds = new Rectangle2D.Double(
                tmpRect.getX() - tmpRect.getWidth()/4,
                tmpRect.getY() - tmpRect.getHeight()/4,
                tmpRect.getWidth()* 1.40,
                tmpRect.getHeight() * 1.40);
    }

    public Rectangle2D getBounds(){return bounds;}


}