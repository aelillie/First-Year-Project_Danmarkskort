package MapFeatures;

import Model.ValueName;
import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

public class Natural extends MapFeature {

    public Natural(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
        setValueIcon();

    }

    @Override
    public void setValueAttributes() {
        if (value.equals("wood")) setValueSpecs(ValueName.WOOD);
        if (value.equals("scrub")) setValueSpecs(ValueName.SCRUB);
        if (value.equals("heath")) setValueSpecs(ValueName.HEATH);
        if (value.equals("grassland")) setValueSpecs(ValueName.GRASSLAND);
        if (value.equals("sand")) setValueSpecs(ValueName.SAND);
        if (value.equals("scree")) setValueSpecs(ValueName.SCREE);
        if (value.equals("fell")) setValueSpecs(ValueName.FELL);
        if (value.equals("water")) setValueSpecs(ValueName.WATER);
        if (value.equals("wetland")) setValueSpecs(ValueName.WETLAND);
        if (value.equals("beach")) setValueSpecs(ValueName.BEACH);
        if (value.equals("coastline"))setValueSpecs(ValueName.COASTLINE);

    }


    private void setValueIcon() {

    }


}
