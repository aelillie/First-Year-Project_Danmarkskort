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
import java.util.HashMap;
import java.util.Map;

public class MapIcon implements Serializable, MapData {
    public static final long serialVersionUID = 5;

    public static Map<String, URL> iconURLs = new HashMap<>();

    static{
        iconURLs.put("busIcon", MapIcon.class.getResource("/data/busIcon.png"));
        iconURLs.put("metroIcon", MapIcon.class.getResource("/data/metroIcon.png"));
        iconURLs.put("stogIcon", MapIcon.class.getResource("/data/stogIcon.png"));
        iconURLs.put("parkingIcon", MapIcon.class.getResource("/data/parkingIcon.jpg"));
        iconURLs.put("pubIcon", MapIcon.class.getResource("/data/pubIcon.png"));
        iconURLs.put("atmIcon", MapIcon.class.getResource("/data/atmIcon.png"));
        iconURLs.put("standardMapImage", MapIcon.class.getResource("/data/standardMapImage.png"));
        iconURLs.put("colorblindMapImage", MapIcon.class.getResource("/data/colorblindMapImage.png"));
        iconURLs.put("transportMapImage", MapIcon.class.getResource("/data/transportMapImage.png"));
        iconURLs.put("startPointIcon", MapIcon.class.getResource("/data/startPointIcon.png"));
        iconURLs.put("endPointIcon", MapIcon.class.getResource("/data/endPointIcon.png"));
        iconURLs.put("fullscreenIcon", MapIcon.class.getResource("/data/fullscreenIcon.png"));
        iconURLs.put("minusIcon", MapIcon.class.getResource("/data/minusIcon.png"));
        iconURLs.put("plusIcon", MapIcon.class.getResource("/data/plusIcon.png"));
        iconURLs.put("searchIcon", MapIcon.class.getResource("/data/searchIcon.png"));
        iconURLs.put("optionsIcon", MapIcon.class.getResource("/data/optionsIcon.png"));
        iconURLs.put("layerIcon", MapIcon.class.getResource("/data/layerIcon.png"));
        iconURLs.put("chosenAddressIcon", MapIcon.class.getResource("/data/chosenAddressIcon.png"));
    }
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
        imgPath = iconURLs.get(type);
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
        imgPath = iconURLs.get(type);


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

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

        Object co = stream.readObject();
        coord = (Point2D) co;
        this.type = stream.readUTF();

        imgPath = iconURLs.get(type);
    }

    public Class getClassType(){
        return this.getClass();
    }

    public Point2D getPosition(){

        return coord;
    }

}
