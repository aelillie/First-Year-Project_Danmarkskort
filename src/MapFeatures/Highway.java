package MapFeatures;

import Model.Drawable;
import Model.MapFeature;

import java.awt.*;

public class Highway extends MapFeature {
    public Highway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("motorway") || value.equals("motorway_link"))setLineSpecs(Drawable.lightblue, -1.0, 3);
        else if (value.equals("trunk") || value.equals("trunk_link")) setLineSpecs(Drawable.lightgreen, -1.0, 2);
        else if (value.equals("primary") || value.equals("primay_link")) setLineSpecs(Drawable.babyred, -1.0, 3);
        else if (value.equals("secondary") || value.equals("secondary_link")) setLineSpecs(Drawable.lightred, -1.0, 2);
        else if (value.equals("tertiary") || value.equals("tertiary_link")) setLineSpecs(Drawable.lightyellow, -0.8, 1);
        else if (value.equals("unclassified")) setLineSpecs(Color.WHITE, -0.8, 1);
        else if (value.equals("residential")) setLineSpecs(Color.DARK_GRAY, -1.0, 2);
        else if (value.equals("living_street")) setLineSpecs(Drawable.grey, -1.0, 2);
        else if (value.equals("pedestrian")) setLineSpecs(Drawable.white, -0.5, 1);
        else if (value.equals("track")) setLineSpecs(Drawable.bloodred, -0.4, 1);
        else if (value.equals("bus_guideway")) setLineSpecs(Drawable.darkblue, -0.4, 1);
        else if (value.equals("raceway")) setLineSpecs(Drawable.white, -0.4, 1);
        else if (value.equals("road")) setLineSpecs(Drawable.grey, -0.4, 1);
        else if (value.equals("footway")) setValueDashedSpecs(Drawable.red, -0.1, 1);
        else if (value.equals("cycleway")) setValueDashedSpecs(Drawable.lightblue, -0.18, 2);
        else if (value.equals("bridleway")) setValueDashedSpecs(Drawable.lightgreen, -0.1, 1);
        else if (value.equals("steps")) setValueDashedSpecs(Drawable.red, -0.1, 3);
        else if (value.equals("path")) setValueDashedSpecs(Drawable.red, -0.1, 1);
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
