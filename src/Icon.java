import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Icon {
    BufferedImage img;
    Shape shape;
    Point2D coord;

    public Icon(Shape shape, String imgPath){
        try {
            img = ImageIO.read(new File(imgPath));
            this.shape = shape;
        } catch(IOException e){
            e.printStackTrace(); //Try to load again?
        }
    }

    public Icon(Point2D coord, String imgPath){
        try {
            img = ImageIO.read(new File(imgPath));
            this.coord = coord;
        } catch(IOException e){
            e.printStackTrace(); //Try to load again?
        }
    }

    public void draw(Graphics2D g, AffineTransform transform){
        double x;
        double y;
        if(shape != null) {
            x = shape.getBounds2D().getCenterX();
            y = shape.getBounds2D().getCenterY();
        } else {
            x = coord.getX();
            y = coord.getY();
        }
        AffineTransform it = AffineTransform.getTranslateInstance(x, y);
        it.scale((1 / transform.getScaleX()), (1 / transform.getScaleY()));
        g.drawImage(img, it, null);
    }

    public void scaleDraw(Graphics2D g, AffineTransform transform, Double scale){}

}
