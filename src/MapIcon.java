import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapIcon {
    BufferedImage img;
    Shape shape;
    Point2D coord;

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn in the middle of a shape specified by the parameter.
     * @param shape The Shape we want pinpointed by an Icon.
     * @param imgPath The path of the image file.
     */
    public MapIcon(Shape shape, String imgPath){
        try {
            img = ImageIO.read(new File(imgPath));
            this.shape = shape;
        } catch(IOException e){
            e.printStackTrace(); //Try to load again?
        }
    }

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn at the coordinate specified by the parameter.
     * @param coord The coordinate of the point of orientation we want to pinpoint using an Icon.
     * @param imgPath The path of the image file.
     */
    public MapIcon(Point2D coord, String imgPath){
        try {
            img = ImageIO.read(new File(imgPath));
            this.coord = coord;
        } catch(IOException e){
            e.printStackTrace(); //Try to load again?
        }
    }

    /**
     * Draws the Icon taking the AffineTransform into account.
     * @param g The graphics context.
     * @param transform The AffineTransform context.
     */
    public void draw(Graphics2D g, AffineTransform transform){
        double x;
        double y;
        if(shape != null) { //If the Icon is created using a shape (e.g. a parking lot)
            x = shape.getBounds2D().getCenterX();
            y = shape.getBounds2D().getCenterY();
        } else { //If the Icon is created using a coordinate (e.g. a bus station)
            x = coord.getX();
            y = coord.getY();
        }
        AffineTransform it = AffineTransform.getTranslateInstance(x, y);
        it.scale((1 / transform.getScaleX()), (1 / transform.getScaleY())); //Sets off against the transform of the context, scaling the transform of the icon accordingly.
        g.drawImage(img, it, null);
    }

   // Work in progress....
    public void scaleDraw(Graphics2D g, AffineTransform transform, Double scale){}

}
