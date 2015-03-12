package MapFeatures;

import Model.Drawable;
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
        if(value.equals("garden"))setValueSpecs(Drawable.whitegreen, -1.2);
        else if (value.equals("common")) setValueSpecs(Drawable.neongreen, -1.2);
        else if(value.equals("park")) setValueSpecs(Drawable.whitegreen, -1.0);
    }

    @Override
    public void setValueIcon() {

    }

    @Override
    public void setColorBlind() {

    }

    @Override
    public void setStandard() {

    }
}
