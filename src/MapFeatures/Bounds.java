package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Nicoline on 30-03-2015.
 */
public class Bounds extends MapFeature {

    public Bounds(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
    }

    @Override
    public void setValueNames() {
        setValueName(ValueName.BOUNDS);
    }
}
