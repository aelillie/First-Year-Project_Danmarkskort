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

    public CanvasBounds(Rectangle2D bounds, AffineTransform transform){
        this.bounds = new Rectangle2D.Double(
                bounds.getX()- bounds.getWidth()/4,
                bounds.getY()- bounds.getHeight()/4,
                bounds.getWidth()* 1.25,
                bounds.getHeight() * 1.25);
        this.transform = transform;
    }

    public void updateBounds(Rectangle2D viewRect){
        AffineTransform inverser = new AffineTransform();
        try {
            inverser = transform.createInverse();
        }catch (NoninvertibleTransformException e){}

        Path2D.Double tmp = (Path2D.Double) inverser.createTransformedShape(viewRect);

        bounds = tmp.getBounds2D();
    }

    public Rectangle2D getBounds(){return bounds;}


}