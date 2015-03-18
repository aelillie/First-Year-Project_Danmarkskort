package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Railway extends MapFeature {

    public Railway(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("rail")) setValueSpecs(ValueName.RAIL);
        else if (value.equals("light_rail")) setValueSpecs(ValueName.LIGHT_RAIL);
        else if (value.equals("subway")) setValueSpecs(ValueName.SUBWAY);
        else if (value.equals("tram")) setValueSpecs(ValueName.TRAM);
        else setValueSpecs(ValueName.RAILWAY);
    }

}
