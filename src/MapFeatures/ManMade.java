package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.*;

/**
 * Created by Kevin on 11-03-2015.
 */
public class ManMade extends MapFeature{

    public ManMade(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        setValueSpecs(ValueName.MANMADE);
    }

}
