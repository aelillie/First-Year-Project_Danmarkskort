package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Waterway extends MapFeature {

    public Waterway(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        if (!this.isArea) //if area isn't declared true in setValueAttr
            this.isArea = isArea; //set area to the the value specified in the parameter
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        layer_value = 19;
    }

    @Override
    public void setValueName() {
        if (value.equals("riverbank")) {
            isArea = true;
            setValueName(ValueName.RIVERBANK);
        }
        else if (value.equals("stream")) setValueName(ValueName.STREAM);
        else if (value.equals("canal")) setValueName(ValueName.CANAL);
        else if (value.equals("river")) setValueName(ValueName.RIVER);
        else if (value.equals("dam")) setValueName(ValueName.DAM);
        else setValueName(ValueName.WATERWAY);
    }
}
