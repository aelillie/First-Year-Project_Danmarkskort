package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Building extends MapFeature {

    public Building(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        super.setPreDefValues();
        setValueAttributes();
    }

    @Override
    public void setPreDefValues() {
        if (value.equals("yes")) layer_value = 45;
    }

    @Override
    public void setValueAttributes() {
        setValueName(ValueName.BUILDING);
    }

}
