package MapFeatures;

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
        if (value.equals("wood")) setValueSpecs(DrawAttribute.lightgreen, -2.0);
        if (value.equals("scrub")) setValueSpecs(DrawAttribute.lightgreen, -1.5);
        if (value.equals("heath")) setValueSpecs(DrawAttribute.skincolor, -2.0);
        if (value.equals("grassland")) setValueSpecs(DrawAttribute.bluegreen, -2.0);
        if (value.equals("sand")) setValueSpecs(DrawAttribute.sand, -2.0);
        if (value.equals("scree")) setValueSpecs(DrawAttribute.pink, -2.0);
        if (value.equals("fell")) setValueSpecs(DrawAttribute.orange, -2.0);
        if (value.equals("water")) setValueSpecs(DrawAttribute.whiteblue, -2.0);
        if (value.equals("wetland")) setValueSpecs(DrawAttribute.greenblue, -2.0);
        if (value.equals("beach")) setValueSpecs(DrawAttribute.sand, -2.0);
        if (value.equals("coastline")) {
            setValueSpecs(DrawAttribute.lightblue, -1.0);
            isArea = false;
        }
    }


    private void setValueIcon() {

    }


}
