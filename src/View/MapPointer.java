package View;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicoline on 19-04-2015.
 */
public class MapPointer {

    private URL imgPath;
    private List<Path2D> streetLocation;
    private Point2D addressLocation;
    private Path2D boundaryLocation;
    private BufferedImage img;
    private String type;

    private Point2D pointerLocation;

    public static Map<String, URL> pointerIconURLs = new HashMap<>();

    static {
        Map<String, URL> aMap = new HashMap<>();
        aMap.put("chosenAddressIcon", MapPointer.class.getResource("/data/chosenAddressIcon.png"));
        aMap.put("endPointIcon", MapPointer.class.getResource("/data/endPointIcon.png"));
        aMap.put("startPointIcon", MapPointer.class.getResource("/data/startPointIcon.png"));
        pointerIconURLs = aMap;
    }


    public MapPointer(List<Path2D> streetLocation, String type){
        this.streetLocation = streetLocation;
        imgPath = pointerIconURLs.get(type);
        pointerLocation = getMiddlePoint(streetLocation);
        this.type = type;
    }

    public MapPointer(Point2D addressLocation, String type){
        this.addressLocation = addressLocation;
        imgPath = pointerIconURLs.get(type);
        pointerLocation = addressLocation;
        this.type = type;
    }

    public MapPointer(Path2D boundaryLocation, String type){
        this.boundaryLocation = boundaryLocation;
        imgPath = pointerIconURLs.get(type);
        this.type = type;
    }

    private static Point2D getMiddlePoint(List<Path2D> street){
        int pathCount = 0;
        double xCoordinateMean = 0;
        double yCoordinateMean = 0;
        for(Path2D path: street){
            Rectangle2D rect = path.getBounds2D();
            xCoordinateMean += rect.getX();
            yCoordinateMean += rect.getY();
            pathCount++;
        }

        xCoordinateMean = xCoordinateMean/pathCount;
        yCoordinateMean = yCoordinateMean/pathCount;
        return new Point2D.Double(xCoordinateMean,yCoordinateMean);
    }

    public void draw(Graphics2D g, AffineTransform transform){
        if(streetLocation != null) drawStreetLocation(g);
        else drawPointer(g,transform);
    }


    private void drawStreetLocation(Graphics2D g){
        for(Path2D street : streetLocation) {
            g.setStroke(new BasicStroke(0.000035f)); //TODO: Varying stroke and color according to drawattribute
            g.setColor(DrawAttribute.cl_red);
            g.draw(street);
        }
    }

    private void drawPointer(Graphics2D g, AffineTransform transform){
        try{
            if(img == null)
                img = ImageIO.read(imgPath);
        } catch(IOException e){
            e.printStackTrace();
        }

        double height = (img.getHeight()/transform.getScaleY());
        double width = (img.getWidth()/transform.getScaleX())/2;
        double x = pointerLocation.getX() - width;
        double y = pointerLocation.getY()- height;

        AffineTransform it = AffineTransform.getTranslateInstance(x, y);
        it.scale((1 / transform.getScaleX()), (1 / transform.getScaleY())); //Sets off against the transform of the context, scaling the transform of the icon accordingly.
        g.drawImage(img, it, null);
    }

    public String getType(){
        return type;
    }


}
