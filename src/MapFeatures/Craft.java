package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Craft extends MapFeature {
    public Craft(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        setValueName(ValueName.CRAFT);
    }

}
