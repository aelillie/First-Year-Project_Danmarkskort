package MapFeatures;

import Model.Drawable;
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
        if (value.equals("wood")) setValueSpecs(Drawable.lightgreen, -2.0);
        if (value.equals("scrub")) setValueSpecs(Drawable.lightgreen, -1.5);
        if (value.equals("heath")) setValueSpecs(Drawable.skincolor, -2.0);
        if (value.equals("grassland")) setValueSpecs(Drawable.bluegreen, -2.0);
        if (value.equals("sand")) setValueSpecs(Drawable.sand, -2.0);
        if (value.equals("scree")) setValueSpecs(Drawable.pink, -2.0);
        if (value.equals("fell")) setValueSpecs(Drawable.orange, -2.0);
        if (value.equals("water")) setValueSpecs(Drawable.whiteblue, -2.0);
        if (value.equals("wetland")) setValueSpecs(Drawable.greenblue, -2.0);
        if (value.equals("beach")) setValueSpecs(Drawable.sand, -2.0);
        if (value.equals("coastline")) {
            setValueSpecs(Drawable.lightblue, -1.0);
            isArea = false;
        }
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
