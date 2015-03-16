package MapFeatures;

import Model.DrawAttributes;
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
        if (value.equals("motorway") || value.equals("motorway_link"))setLineSpecs(DrawAttributes.lightblue, -1.0, 5);
        else if (value.equals("trunk") || value.equals("trunk_link")) setLineSpecs(DrawAttributes.lightgreen, -1.0, 3);
        else if (value.equals("primary") || value.equals("primay_link")) setLineSpecs(DrawAttributes.babyred, -1.0, 4);
        else if (value.equals("secondary") || value.equals("secondary_link")) setLineSpecs(DrawAttributes.lightred, -1.0, 3);
        else if (value.equals("tertiary") || value.equals("tertiary_link")) setLineSpecs(DrawAttributes.lightyellow, -0.8, 3);
        else if (value.equals("unclassified")) setLineSpecs(Color.WHITE, -0.8, 1);
        else if (value.equals("residential")) setLineSpecs(Color.white, -1.0, 2);
        else if (value.equals("service")) setLineSpecs(Color.white, -1.0, 2);
        else if (value.equals("living_street")) setLineSpecs(DrawAttributes.grey, -1.0, 2);
        else if (value.equals("pedestrian")) setLineSpecs(DrawAttributes.white, -0.5, 1);
        else if (value.equals("track")) setValueDashedSpecs(DrawAttributes.brown, -0.4, 0);
        else if (value.equals("bus_guideway")) setLineSpecs(DrawAttributes.darkblue, -0.4, 1);
        else if (value.equals("raceway")) setLineSpecs(DrawAttributes.white, -0.4, 1);
        else if (value.equals("road")) setLineSpecs(DrawAttributes.grey, -0.4, 1);
        else if (value.equals("footway")) {
            if(isArea) {
                setValueSpecs(DrawAttributes.white, -0.5);
            } else setValueDashedSpecs(DrawAttributes.red, -0.1, 0);
        }
        else if (value.equals("cycleway")) setValueDashedSpecs(DrawAttributes.lightblue, -0.18, 1);
        else if (value.equals("bridleway")) setValueDashedSpecs(DrawAttributes.lightgreen, -0.1, 0);
        else if (value.equals("steps")) setValueDashedSpecs(DrawAttributes.red, -0.1, 3);
        else if (value.equals("path")) setValueDashedSpecs(DrawAttributes.red, -0.1, 0);
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
