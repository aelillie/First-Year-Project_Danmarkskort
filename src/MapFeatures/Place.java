package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 27-04-2015.
 */
public class Place extends MapFeature {

    public Place(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        layer_value = 49;
    }

    @Override
    public void setValueNames() {
        if (value.equals("island")) {
            isArea = true;
            setValueName(ValueName.ISLAND);
        }
        else {
            isArea = false;
            setValueName(ValueName.PLACE);
        }
    }

}
