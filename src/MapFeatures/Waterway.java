package MapFeatures;

import Model.DefaultView;
import Model.MapFeature;

import java.awt.*;

public class Waterway extends MapFeature {

    public Waterway(Shape way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        setValueAttributes();
        this.isArea = isArea;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("stream")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("canal")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("river")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("dam")) setValueSpecs(Color.blue, -1.0);
    }


}
