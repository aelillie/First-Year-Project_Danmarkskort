package Model;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * A Point2D object
 */
public class OSMNode extends Point2D.Float implements Serializable {
    public static final long serialVersionUID = 1;

    public ValueName trafficSignal;

    public OSMNode(float x, float y) {
        super(x, y);
    }
}
