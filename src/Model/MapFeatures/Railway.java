package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Railway extends MapFeature {

    public Railway(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
    }

    @Override
    public void setValueName() {
        if (value.equals("rail")) setValueName(ValueName.RAIL);
        else if (value.equals("light_rail")) setValueName(ValueName.LIGHT_RAIL);
        else if (value.equals("subway")) setValueName(ValueName.SUBWAY);
        else if (value.equals("tram")) setValueName(ValueName.TRAM);
        else setValueName(ValueName.RAILWAY);
    }

}
