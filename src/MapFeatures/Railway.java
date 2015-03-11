package MapFeatures;

import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Railway extends MapFeature {

    public Railway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
        dashed = true;
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(Color.DARK_GRAY, -1.9);
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
