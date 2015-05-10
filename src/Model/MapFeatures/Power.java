package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Created by Anders on 10-05-2015.
 */
public class Power extends MapFeature {

    public Power(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("plant") || value.equals("substation"))
            layer_value = 9;
    }

    @Override
    public void setValueName() {
        if (value.equals("plant") || value.equals("substation")) {
            isArea = true;
            setValueName(ValueName.POWER_AREA);
        }
        else
            setValueName(ValueName.POWER);
    }
}
