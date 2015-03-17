package MapFeatures;

import Model.ValueName;
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
        if (value.equals("motorway") || value.equals("motorway_link"))setValueSpecs(ValueName.MOTORWAY);
        else if (value.equals("trunk") || value.equals("trunk_link")) setValueSpecs(ValueName.TRUNK);
        else if (value.equals("primary") || value.equals("primay_link")) setValueSpecs(ValueName.PRIMARY);
        else if (value.equals("secondary") || value.equals("secondary_link")) setValueSpecs(ValueName.SECONDARY);
        else if (value.equals("tertiary") || value.equals("tertiary_link")) setValueSpecs(ValueName.TERTIARY);
        else if (value.equals("unclassified")) setValueSpecs(ValueName.UNCLASSIFIED);
        else if (value.equals("residential")) setValueSpecs(ValueName.RESIDENTIAL);
        else if (value.equals("service")) setValueSpecs(ValueName.SERVICE);
        else if (value.equals("living_street")) setValueSpecs(ValueName.LIVING_STREET);
        else if (value.equals("pedestrian")) setValueSpecs(ValueName.PEDESTRIAN);
        else if (value.equals("track")) setValueSpecs(ValueName.TRACK);
        else if (value.equals("bus_guideway")) setValueSpecs(ValueName.BUS_GUIDEWAY);
        else if (value.equals("road")) setValueSpecs(ValueName.ROAD);
        else if (value.equals("footway")) setValueSpecs(ValueName.FOOTWAY); //TODO: FOOTWAY_LINE, FOOTWAY_AREA
        else if (value.equals("cycleway")) setValueSpecs(ValueName.CYCLEWAY);
        else if (value.equals("bridleway")) setValueSpecs(ValueName.BRIDLEWAY);
        else if (value.equals("steps")) setValueSpecs(ValueName.STEPS);
        else if (value.equals("path")) setValueSpecs(ValueName.PATH);
    }


}
