package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class MapIcon implements Serializable {
    public static final long serialVersionUID = 5;
    public static final URL metroIcon = MapIcon.class.getResource("/data/metroIcon.png");
    public static final URL STogIcon = MapIcon.class.getResource("/data/stogIcon.png");
    public static final URL parkingIcon = MapIcon.class.getResource("/data/parkingIcon.jpg");
    public static final URL busIcon = MapIcon.class.getResource("/data/busIcon.png");
    public static final URL pubIcon = MapIcon.class.getResource("/data/pubIcon.png");
    public static final URL atmIcon = MapIcon.class.getResource("/data/atmIcon.png");

    public static final URL standard = MapIcon.class.getResource("/data/standardMapImage.png");
    public static final URL colorblind = MapIcon.class.getResource("/data/colorblindMapImage.png");
    public static final URL transport = MapIcon.class.getResource("/data/transportMapImage.png");

    public static final URL startPointIcon = MapIcon.class.getResource("/data/startPointIcon.png");
    public static final URL endPointIcon = MapIcon.class.getResource("/data/endPointIcon.png");

    public static final URL fullscreenIcon = MapIcon.class.getResource("/data/fullscreenIcon.png");
    public static final URL minusIcon = MapIcon.class.getResource("/data/minusIcon.png");
    public static final URL plusIcon = MapIcon.class.getResource("/data/plusIcon.png");
    public static final URL searchIcon = MapIcon.class.getResource("/data/searchIcon.png");
    public static final URL optionsIcon = MapIcon.class.getResource("/data/optionsIcon.png");
    public static final URL layerIcon = MapIcon.class.getResource("/data/layerIcon.png");
    public static final URL chosenAddressIcon = MapIcon.class.getResource("/data/chosenAddressIcon.png");

    static ArrayList<URL> icons = addIcons();
    BufferedImage img;
    Shape shape;
    Point2D coord;
    URL imgPath;

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn in the middle of a shape specified by the parameter.
     * @param shape The Shape we want pinpointed by an Icon.
     * @param imgPath The path of the image file.
     */
    public MapIcon(Shape shape, URL imgPath){
        try {
            img = ImageIO.read(imgPath);
            this.shape = shape;
            this.imgPath = imgPath;
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
    public MapIcon(Point2D coord, URL imgPath){
        try {
            img = ImageIO.read(imgPath);
            this.coord = coord;
            this.imgPath = imgPath;
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

    private void writeObject(ObjectOutputStream stream)throws IOException{
        if(shape != null) stream.writeObject(shape);
        else stream.writeObject(coord);
        stream.writeObject(imgPath);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{

        Object type = stream.readObject();
        if(type.getClass() == Path2D.Float.class) shape = (Shape) type;
        else coord = (Point2D) type;
        URL imgPath = (URL) stream.readObject();
        img = ImageIO.read(imgPath);
        this.imgPath = imgPath;

    }

    private static ArrayList<URL> addIcons(){
        ArrayList iconsOne = new ArrayList<>();
        iconsOne.add(metroIcon);
        iconsOne.add(busIcon);
        return iconsOne;
    }
    public static ArrayList<URL> getIcons(){
        addIcons();
        return icons;
    }



}
