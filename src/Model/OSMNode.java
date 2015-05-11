package Model;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Created by Anders on 11-05-2015.
 */
public class OSMNode extends Point2D.Float implements Serializable {

    public boolean hasTrafficSignal;

    public OSMNode(float x, float y) {
        super(x, y);
    }
}
