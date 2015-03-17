package MapFeatures;

import View.DrawAttribute;
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
            if(isArea) setLineSpecs(DrawAttribute.neongreen, -0.5, 0);
            else setValueSpecs(DrawAttribute.neongreen, -0.5);
        }
        if(value.equals("fence")) setValueSpecs(DrawAttribute.neongreen, -0.5);
        if(value.equals("kerb")) setLineSpecs(DrawAttribute.bluegreen, -0.5, 0);

    }

}
