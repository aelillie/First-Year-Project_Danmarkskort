package MapFeatures;

import Model.Drawable;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Barrier extends MapFeature{

    private boolean isArea = false;

    public Barrier(Shape way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;

    }

    @Override
    public void setValueAttributes() {
        if(value.equals("hence")) {
            if(isArea) setLineSpecs(Drawable.neongreen, -0.5, 0);
            else setValueSpecs(Drawable.neongreen, -0.5);
        }
        if(value.equals("fence")) setValueSpecs(Drawable.neongreen, -0.5);
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
