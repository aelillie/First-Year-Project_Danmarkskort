package MapFeatures;

import Model.Drawable;
import Model.Line;
import Model.MapFeature;

import java.awt.*;

public class Highway extends MapFeature {
    public Highway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("motorway") || value.equals("motorway_link")) setValueSpecs(Drawable.lightblue, -1.0);
        if (value.equals("trunk") || value.equals("trunk_link")) setValueSpecs(Drawable.lightgreen, -1.0);
        if (value.equals("primary") || value.equals("primay_link")) setValueSpecs(Drawable.babyred, -1.0);
        if (value.equals("secondary") || value.equals("secondary_link")) setValueSpecs(Drawable.lightred, -1.0);
        if (value.equals("tertiary") || value.equals("tertiary_link")) setValueSpecs(Drawable.lightyellow, -0.8);
        if (value.equals("unclassified")) setValueSpecs(Color.WHITE, -0.8);
        if (value.equals("residential")) setValueSpecs(Color.DARK_GRAY, -1.0);
        if (value.equals("living_street")) setValueSpecs(Drawable.grey, -1.0);
        if (value.equals("pedestrian")) setValueSpecs(Drawable.white, -0.1);
        if (value.equals("track")) setValueSpecs(Drawable.bloodred, -0.0);
        if (value.equals("bus_guideway")) setValueSpecs(Drawable.darkblue, -0.4);
        if (value.equals("raceway")) setValueSpecs(Drawable.white, -0.4);
        if (value.equals("road")) setValueSpecs(Drawable.grey, -0.4);
        if (value.equals("footway")) setValueDashedSpecs(Drawable.red, -0.1);
        if (value.equals("cycleway")) setValueDashedSpecs(Drawable.lightblue, -0.18);
        if (value.equals("bridleway")) setValueDashedSpecs(Drawable.lightgreen, -0.1);
        if (value.equals("steps")) setValueDashedSpecs(Drawable.red, -0.1);
        if (value.equals("path")) setValueDashedSpecs(Drawable.red, -0.1);
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
