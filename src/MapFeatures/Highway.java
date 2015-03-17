package MapFeatures;

import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

public class Highway extends MapFeature {
    public Highway(Shape way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;
        if (layer_value == 0) setPreDefValues();
        setValueAttributes();
    }

    private void setPreDefValues() {
        if (value.equals("motorway") || value.equals("motorway_link")) layer_value = 7; //TODO: What if there's a bridge? (Value max 5)
        else if (value.equals("trunk") || value.equals("trunk_link")) layer_value = 6;
        else if (value.equals("primary") || value.equals("primay_link")) layer_value = 5;
        else if (value.equals("secondary") || value.equals("secondary_link")) layer_value = 4;
        else if (value.equals("tertiary") || value.equals("tertiary_link")) layer_value = 3;
        else if (value.equals("residential")) layer_value = 2;
        else layer_value = 1;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("motorway") || value.equals("motorway_link"))setLineSpecs(DrawAttribute.lightblue, -1.0, 5);
        else if (value.equals("trunk") || value.equals("trunk_link")) setLineSpecs(DrawAttribute.lightgreen, -1.0, 3);
        else if (value.equals("primary") || value.equals("primay_link")) setLineSpecs(DrawAttribute.babyred, -1.0, 4);
        else if (value.equals("secondary") || value.equals("secondary_link")) setLineSpecs(DrawAttribute.lightred, -1.0, 3);
        else if (value.equals("tertiary") || value.equals("tertiary_link")) setLineSpecs(DrawAttribute.lightyellow, -0.8, 3);
        else if (value.equals("unclassified")) setLineSpecs(Color.WHITE, -0.8, 1);
        else if (value.equals("residential")) setLineSpecs(Color.white, -1.0, 2);
        else if (value.equals("service")) setLineSpecs(Color.white, -1.0, 2);
        else if (value.equals("living_street")) setLineSpecs(DrawAttribute.grey, -1.0, 2);
        else if (value.equals("pedestrian")) setLineSpecs(DrawAttribute.white, -0.5, 1);
        else if (value.equals("track")) setValueDashedSpecs(DrawAttribute.brown, -0.4, 0);
        else if (value.equals("bus_guideway")) setLineSpecs(DrawAttribute.darkblue, -0.4, 1);
        else if (value.equals("raceway")) setLineSpecs(DrawAttribute.white, -0.4, 1);
        else if (value.equals("road")) setLineSpecs(DrawAttribute.grey, -0.4, 1);
        else if (value.equals("footway")) {
            if(isArea) {
                setValueSpecs(DrawAttribute.white, -0.5);
            } else setValueDashedSpecs(DrawAttribute.red, -0.1, 0);
        }
        else if (value.equals("cycleway")) setValueDashedSpecs(DrawAttribute.lightblue, -0.18, 1);
        else if (value.equals("bridleway")) setValueDashedSpecs(DrawAttribute.lightgreen, -0.1, 0);
        else if (value.equals("steps")) setValueDashedSpecs(DrawAttribute.red, -0.1, 3);
        else if (value.equals("path")) setValueDashedSpecs(DrawAttribute.red, -0.1, 0);
    }


}
