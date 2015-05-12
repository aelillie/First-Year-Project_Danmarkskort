package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Leisure extends MapFeature {

    public Leisure(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("pitch")) layer_value = 32;
        else if (value.equals("playground")) layer_value = 31;
        else if (value.equals("common")) layer_value = 5;

    }

    @Override
    public void setValueName() {
        if(value.equals("garden")) setValueName(ValueName.GARDEN);
        else if (value.equals("common")) setValueName(ValueName.COMMON);
        else if(value.equals("park")) setValueName(ValueName.PARK);
        else if(value.equals("pitch")) setValueName(ValueName.PITCH);
        else if(value.equals("playground")) setValueName(ValueName.PLAYGROUND);
        else setValueName(ValueName.LEISURE);
    }

}
