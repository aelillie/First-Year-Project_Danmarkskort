package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Waterway extends MapFeature {

    public Waterway(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;
    }

    @Override
    public void setPreDefValues() {
        super.setPreDefValues();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setValueName(ValueName.RIVERBANK);
        else if (value.equals("stream")) setValueName(ValueName.STREAM);
        else if (value.equals("canal")) setValueName(ValueName.CANAL);
        else if (value.equals("river")) setValueName(ValueName.RIVER);
        else if (value.equals("dam")) setValueName(ValueName.DAM);
        else setValueName(ValueName.WATERWAY);
    }
}
