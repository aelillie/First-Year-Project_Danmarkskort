package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Multipolygon extends MapFeature {

    public Multipolygon(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("islet")) layer_value = 49;
        if (value.equals("building")) layer_value = 35;
        else layer_value = 0;
    }

    @Override
    public void setValueNames() {
        if(value.equals("building")) {
            isArea = true;
            setValueName(ValueName.BUILDING);
        }
        else if(value.equals("islet")) {
            isArea = true;
            setValueName(ValueName.ISLET);
        }
        else {
            isArea = false;
            setValueName(ValueName.PLACE);
        }
    }

}
