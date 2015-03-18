package MapFeatures;

import Model.ValueName;
import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Multipolygon extends MapFeature {

    public Multipolygon(Shape way, int layer_value, String value) {

        //TODO this is still not done at all!
        super(way, layer_value, value);
        setValueAttributes();
        isArea = true;
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("building")) setValueSpecs(ValueName.BUILDING);
    }

}
