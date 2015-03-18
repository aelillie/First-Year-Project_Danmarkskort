package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Leisure extends MapFeature {

    public Leisure(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();

    }

    @Override
    public void setValueAttributes() {
        if(value.equals("garden"))setValueSpecs(ValueName.GARDEN);
        else if (value.equals("common")) setValueSpecs(ValueName.COMMON);
        else if(value.equals("park")) setValueSpecs(ValueName.PARK);
        else if(value.equals("pitch")) setValueSpecs(ValueName.PITCH);
        else if(value.equals("playground")) setValueSpecs(ValueName.PLAYGROUND);
        else setValueSpecs(ValueName.LEISURE);
    }

}
