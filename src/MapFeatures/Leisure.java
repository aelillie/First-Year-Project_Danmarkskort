package MapFeatures;

import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Leisure extends MapFeature {

    public Leisure(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();

    }

    @Override
    public void setValueAttributes() {
        if(value.equals("garden"))setValueSpecs(DrawAttribute.whitegreen, -1.2);
        else if (value.equals("common")) setValueSpecs(DrawAttribute.neongreen, -1.2);
        else if(value.equals("park")) setValueSpecs(DrawAttribute.whitegreen, -1.0);
        else if(value.equals("pitch")) setValueSpecs(DrawAttribute.grey, -1.0);
        else if(value.equals("playground")) setValueSpecs(DrawAttribute.whitegreen, -1.0);

    }

}
