package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
*/
public class Aeroway extends MapFeature {

    public Aeroway(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        layer_value = 26;
    }

    @Override
    public void setValueName() {
        if (value.equals("terminal")) {
            isArea = true;
            setValueName(ValueName.TERMINAL);
        }
        else if (value.equals("runway")) {
            isArea = true;
            setValueName(ValueName.RUNWAY);
        }
        else if (value.equals("taxiway")) {
            isArea = false;
            setValueName(ValueName.TAXIWAY);
        }
        else {
            isArea = true;
            setValueName(ValueName.AEROWAY);
        }
    }

}
