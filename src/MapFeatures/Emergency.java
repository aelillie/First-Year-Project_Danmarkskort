package MapFeatures;

import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Emergency extends MapFeature {
    public Emergency(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(DrawAttribute.lightgrey, -1.0);
    }

}
