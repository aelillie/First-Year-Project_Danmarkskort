package MapFeatures;

import Model.ValueName;
import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Kevin on 11-03-2015.
 */
public class ManMade extends MapFeature{

    public ManMade(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(ValueName.MANMADE);
    }

}
