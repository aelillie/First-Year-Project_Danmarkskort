package MapFeatures;

import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Shop extends MapFeature {
    public Shop(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
        isArea = true;
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(DrawAttribute.lightgrey, -1.0);
    }

}
