package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

public class MapIcon implements Serializable, MapData {
    public static long serialVersionUID = 5;

    public static URL metroIcon, STogIcon, parkingIcon, busIcon, pubIcon, atmIcon, standard, colorblind, transport, layerIcon;
    public static URL startPointIcon, endPointIcon, fullscreenIcon, minusIcon, plusIcon, searchIcon, optionsIcon, chosenAddressIcon;

    private BufferedImage img;
    private Point2D coord;
    private URL imgPath;

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn in the middle of a shape specified by the parameter.
     * @param shape The Shape we want pinpointed by an Icon.
     * @param imgPath The path of the image file.
     */
    public MapIcon(Shape shape, URL imgPath){

        coord = new Point2D.Float((float)shape.getBounds2D().getCenterX(), (float)shape.getBounds2D().getCenterY());
        this.imgPath = imgPath;

    }

    /**
     * Creates a new Icon instance.
     * This constructor is used when the icon should be drawn at the coordinate specified by the parameter.
     * @param coord The coordinate of the point of orientation we want to pinpoint using an Icon.
     * @param imgPath The path of the image file.
     */
    public MapIcon(Point2D coord, URL imgPath){


            this.coord = coord;
            this.imgPath = imgPath;

    }

    public static void setIconResources() {
        metroIcon = MapIcon.class.getResource("/data/metroIcon.png");
        STogIcon = MapIcon.class.getResource("/data/stogIcon.png");
        parkingIcon = MapIcon.class.getResource("/data/parkingIcon.jpg");
        busIcon = MapIcon.class.getResource("/data/busIcon.png");
        pubIcon = MapIcon.class.getResource("/data/pubIcon.png");
        atmIcon = MapIcon.class.getResource("/data/atmIcon.png");

        standard = MapIcon.class.getResource("/data/standardMapImage.png");
        colorblind = MapIcon.class.getResource("/data/colorblindMapImage.png");
        transport = MapIcon.class.getResource("/data/transportMapImage.png");

        startPointIcon = MapIcon.class.getResource("/data/startPointIcon.png");
        endPointIcon = MapIcon.class.getResource("/data/endPointIcon.png");

        fullscreenIcon = MapIcon.class.getResource("/data/fullscreenIcon.png");
        minusIcon = MapIcon.class.getResource("/data/minusIcon.png");
        plusIcon = MapIcon.class.getResource("/data/plusIcon.png");
        searchIcon = MapIcon.class.getResource("/data/searchIcon.png");
        optionsIcon = MapIcon.class.getResource("/data/optionsIcon.png");
        layerIcon = MapIcon.class.getResource("/data/layerIcon.png");
        chosenAddressIcon = MapIcon.class.getResource("/data/chosenAddressIcon.png");
    }

    /**
     * Draws the Icon taking the AffineTransform into account.
     * @param g The graphics context.
     * @param transform The AffineTransform context.
     */
    public void draw(Graphics2D g, AffineTransform transform){
        try{
            if(img == null)
                img = ImageIO.read(imgPath);
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
        stream.writeObject(imgPath);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{

        Object type = stream.readObject();
        coord = (Point2D) type;
        URL imgPath = (URL) stream.readObject();
        this.imgPath = imgPath;

    }

    public Class getType(){
        return this.getClass();
    }

    public Point2D getPosition(){

        return coord;
    }

}
