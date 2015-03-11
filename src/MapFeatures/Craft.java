package MapFeatures;

import Model.Drawable;
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
        setValueSpecs(Drawable.lightgrey, -.8);
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
