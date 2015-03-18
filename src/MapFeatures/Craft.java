package MapFeatures;

import Model.ValueName;
import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Craft extends MapFeature {
    public Craft(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(ValueName.CRAFT);
    }

}
