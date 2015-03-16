package MapFeatures;

import Model.DrawAttributes;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Historic extends MapFeature {
    public Historic(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
        isArea = true;
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("archaeological_site")) setValueSpecs(DrawAttributes.lightgreen, -1.0);
    }

}
