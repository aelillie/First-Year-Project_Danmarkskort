package MapFeatures;

import Model.MapFeature;

import java.awt.*;

public class Waterway extends MapFeature {

    public Waterway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("stream")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("canal")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("river")) setValueSpecs(Color.blue, -1.0);
        else if (value.equals("dam")) setValueSpecs(Color.blue, -1.0);
    }



    @Override
    public void setValueIcon() {

    }

    @Override
    public void setColorBlind() {

    }

    @Override
    public void setStandard() {

    }
}
