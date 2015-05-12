package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;


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
    public void setValueName() {
        if (value.equals("island")) {
            isArea = true;
            setValueName(ValueName.ISLAND);
        }
        else if (value.equals("islet")) {
            isArea = true;
            setValueName(ValueName.ISLET);
        }
        else {
            isArea = false;
            setValueName(ValueName.PLACE);
        }
    }

}
