package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Waterway extends MapFeature {

    public Waterway(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        setValueAttributes();
        this.isArea = isArea;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setValueSpecs(ValueName.RIVERBANK);
        else if (value.equals("stream")) setValueSpecs(ValueName.STREAM);
        else if (value.equals("canal")) setValueSpecs(ValueName.CANAL);
        else if (value.equals("river")) setValueSpecs(ValueName.RIVER);
        else if (value.equals("dam")) setValueSpecs(ValueName.DAM);
        else setValueSpecs(ValueName.WATERWAY);
    }
}
