package MapFeatures;

import Model.DrawAttributes;
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
        if (value.equals("wood")) setValueSpecs(DrawAttributes.lightgreen, -2.0);
        if (value.equals("scrub")) setValueSpecs(DrawAttributes.lightgreen, -1.5);
        if (value.equals("heath")) setValueSpecs(DrawAttributes.skincolor, -2.0);
        if (value.equals("grassland")) setValueSpecs(DrawAttributes.bluegreen, -2.0);
        if (value.equals("sand")) setValueSpecs(DrawAttributes.sand, -2.0);
        if (value.equals("scree")) setValueSpecs(DrawAttributes.pink, -2.0);
        if (value.equals("fell")) setValueSpecs(DrawAttributes.orange, -2.0);
        if (value.equals("water")) setValueSpecs(DrawAttributes.whiteblue, -2.0);
        if (value.equals("wetland")) setValueSpecs(DrawAttributes.greenblue, -2.0);
        if (value.equals("beach")) setValueSpecs(DrawAttributes.sand, -2.0);
        if (value.equals("coastline")) {
            setValueSpecs(DrawAttributes.lightblue, -1.0);
            isArea = false;
        }
    }


    private void setValueIcon() {

    }


}
