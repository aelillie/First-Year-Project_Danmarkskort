package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class MapIcon implements Serializable, MapData {
    public static final long serialVersionUID = 5;

    private BufferedImage img;
    private Point2D coord;
    private URL imgPath;
    private String type;


    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn in the middle of a shape specified by the parameter.
     * @param shape The Shape we want pinpointed by an Icon.
     * @param type The path of the image file.
     */
    public MapIcon(Shape shape, String type){
        this.type = type;
        coord = new Point2D.Float((float)shape.getBounds2D().getCenterX(), (float)shape.getBounds2D().getCenterY());
        imgPath = MapIcon.class.getResource("/data/" + type + ".png");
        if (imgPath == null)
            imgPath = MapIcon.class.getResource("/data/" + type + ".jpg");

    }

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn at the coordinate specified by the parameter.
     * @param coord The coordinate of the point of orientation we want to pinpoint using an Icon.
     * @param type The path of the image file.
     */
    public MapIcon(Point2D coord, String type){
        this.type = type;
        this.coord = coord;
        imgPath = MapIcon.class.getResource("/data/" + type + ".png");
        if (imgPath == null)
            imgPath = MapIcon.class.getResource("/data/" + type + ".jpg");

    }

    /**
     * Draws the Icon taking the AffineTransform into account.
     * @param g The graphics context.
     * @param transform The AffineTransform context.
     */
    public void draw(Graphics2D g, AffineTransform transform){
        try{
            if(img == null) {
                img = ImageIO.read(imgPath);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        double x;
        double y;

            x = coord.getX();
            y = coord.getY();

        AffineTransform it = AffineTransform.getTranslateInstance(x, y);
        it.scale((1 / transform.getScaleX()), (1 / transform.getScaleY())); //Sets off against the transform of the context, scaling the transform of the icon accordingly.
        g.drawImage(img, it, null);
    }

    private void writeObject(ObjectOutputStream stream)throws IOException{
        stream.writeObject(coord);
        stream.writeUTF(type);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{

        Object co = stream.readObject();
        coord = (Point2D) co;
        this.type = stream.readUTF();

        imgPath = MapIcon.class.getResource("/data/" + type + ".png");
        if (imgPath == null)
            imgPath = MapIcon.class.getResource("/data/" + type + ".jpg");

    }

    public Class getType(){
        return this.getClass();
    }

    public Point2D getPosition(){

        return coord;
    }

}
