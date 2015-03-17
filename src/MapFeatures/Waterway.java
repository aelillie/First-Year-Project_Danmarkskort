package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.*;

public class Waterway extends MapFeature {

    public Waterway(Shape way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        setValueAttributes();
        this.isArea = isArea;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setValueSpecs(ValueName.RIVERBANK, -1.0);
        else if (value.equals("stream")) setValueSpecs(ValueName.STREAM, -1.0);
        else if (value.equals("canal")) setValueSpecs(ValueName.CANAL, -1.0);
        else if (value.equals("river")) setValueSpecs(ValueName.RIVER, -1.0);
        else if (value.equals("dam")) setValueSpecs(ValueName.DAM, -1.0);
    }
}
