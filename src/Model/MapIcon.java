package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapIcon implements Serializable, MapData {
    public static final long serialVersionUID = 5;

    //Hashmap containing references of paths to resource files.
    public static Map<String, URL> iconURLs = new HashMap<>();

    static{
        Map<String, URL> aMap = new HashMap<>();
        aMap.put("busIcon", MapIcon.class.getResource("/data/busIcon.png"));
        aMap.put("metroIcon", MapIcon.class.getResource("/data/metroIcon.png"));
        aMap.put("stogIcon", MapIcon.class.getResource("/data/stogIcon.png"));
        aMap.put("parkingIcon", MapIcon.class.getResource("/data/parkingIcon.jpg"));
        aMap.put("pubIcon", MapIcon.class.getResource("/data/pubIcon.png"));
        aMap.put("atmIcon", MapIcon.class.getResource("/data/atmIcon.png"));
        aMap.put("standardMapImage", MapIcon.class.getResource("/data/standardMapImage.png"));
        aMap.put("colorblindMapImage", MapIcon.class.getResource("/data/colorblindMapImage.png"));
        aMap.put("transportMapImage", MapIcon.class.getResource("/data/transportMapImage.png"));
        aMap.put("startPointIcon", MapIcon.class.getResource("/data/startPointIcon.png"));
        aMap.put("endPointIcon", MapIcon.class.getResource("/data/endPointIcon.png"));
        aMap.put("fullscreenIcon", MapIcon.class.getResource("/data/fullscreenIcon.png"));
        aMap.put("minusIcon", MapIcon.class.getResource("/data/minusIcon.png"));
        aMap.put("plusIcon", MapIcon.class.getResource("/data/plusIcon.png"));
        aMap.put("searchIcon", MapIcon.class.getResource("/data/searchIcon.png"));
        aMap.put("optionsIcon", MapIcon.class.getResource("/data/optionsIcon.png"));
        aMap.put("layerIcon", MapIcon.class.getResource("/data/layerIcon.png"));
        aMap.put("chosenAddressIcon", MapIcon.class.getResource("/data/chosenAddressIcon.png"));
        aMap.put("restaurantIcon", MapIcon.class.getResource("/data/restaurantIcon.png"));
        aMap.put("7elevenIcon", MapIcon.class.getResource("/data/7elevenIcon.jpg"));
        aMap.put("hotelIcon", MapIcon.class.getResource("/data/hotelIcon.png"));
        aMap.put("hospitalIcon", MapIcon.class.getResource("/data/hospitalIcon.png"));
        MapIcon.iconURLs = aMap;
    }


    static HashMap<URL, Boolean> hashIcon = addIcon();
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
        this.type = type.intern();
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
        this.type = type.intern();
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
            if(img == null)
                img = ImageIO.read(imgPath);
        } catch(IOException e){
            e.printStackTrace();
        }
        double x = 0;
        double y = 0;
        if(imgPath.getPath().equals(iconURLs.get("chosenAddressIcon").getPath())){

            double height = (img.getHeight()/transform.getScaleY());
            double width = (img.getWidth()/transform.getScaleX())/2;
            x = coord.getX() - width;
            y = coord.getY()- height;


        }else {
            x = coord.getX();
            y = coord.getY();
        }

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
        this.type = stream.readUTF().intern();

        imgPath = iconURLs.get(type);
    }
    public URL getURL()
    {
        return this.imgPath;
    }


    public static void setIconState(URL url, boolean state)
    {
        hashIcon.put(url, state);
    }

    public static boolean getIconState(URL url)
    {
        return hashIcon.get(url);
    }


    /**
     * Used to check wether MapFeature or MapIcon when static type is MapData.
     * @return What class this is
     */
    public Class getClassType(){
        return this.getClass();
    }

    public Point2D getPosition(){

        return coord;
    }

    /**
     * Returns a list of the paths to icon files needed
     * @return List of URL to icon files
     */
    public static ArrayList<URL> getIcons(){
        ArrayList<URL> iconsOne = new ArrayList<>();
        iconsOne.add(MapIcon.iconURLs.get("metroIcon"));
        iconsOne.add(MapIcon.iconURLs.get("busIcon"));
        iconsOne.add(MapIcon.iconURLs.get("stogIcon"));
        iconsOne.add(MapIcon.iconURLs.get("parkingIcon"));
        iconsOne.add(MapIcon.iconURLs.get("atmIcon"));
        iconsOne.add(MapIcon.iconURLs.get("pubIcon"));
        iconsOne.add(MapIcon.iconURLs.get("restaurantIcon"));
        iconsOne.add(MapIcon.iconURLs.get("7elevenIcon"));
        iconsOne.add(MapIcon.iconURLs.get("hotelIcon"));
        iconsOne.add(MapIcon.iconURLs.get("hospitalIcon"));
        return iconsOne;
    }

    private static HashMap<URL,Boolean> addIcon(){
        HashMap<URL, Boolean> hashIcon = new HashMap<>();
        hashIcon.put(MapIcon.iconURLs.get("metroIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("busIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("stogIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("parkingIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("atmIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("pubIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("restaurantIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("7elevenIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("hotelIcon"),false);
        hashIcon.put(MapIcon.iconURLs.get("hospitalIcon"), false);
        return hashIcon;
    }


    /**
     * Returns true or false, whether the icon is currently visible or not
     * @return boolean
     */
    public Boolean isVisible() {
        return getIconState(this.imgPath);
    }

    public int getLayerVal(){
        return 100;
    }
}

