package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Route extends MapFeature{

    public Route(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("ferry"))setValueName(ValueName.FERRY);
        else setValueName(ValueName.ROUTE);
    }

}
