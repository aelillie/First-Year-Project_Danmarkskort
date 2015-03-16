package MapFeatures;

import Model.DrawAttributes;
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
            if(isArea) setLineSpecs(DrawAttributes.neongreen, -0.5, 0);
            else setValueSpecs(DrawAttributes.neongreen, -0.5);
        }
        if(value.equals("fence")) setValueSpecs(DrawAttributes.neongreen, -0.5);
        if(value.equals("kerb")) setLineSpecs(DrawAttributes.bluegreen, -0.5, 0);

    }

}
