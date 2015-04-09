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
        super.setPreDefValues();
        setValueAttributes();
    }

    @Override
    public void setPreDefValues() {
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("garden")) setValueName(ValueName.GARDEN);
        else if (value.equals("common")) setValueName(ValueName.COMMON);
        else if(value.equals("park")) setValueName(ValueName.PARK);
        else if(value.equals("pitch")) setValueName(ValueName.PITCH);
        else if(value.equals("playground")) setValueName(ValueName.PLAYGROUND);
        else setValueName(ValueName.LEISURE);
    }

}
