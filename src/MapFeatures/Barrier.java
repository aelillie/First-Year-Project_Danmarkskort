package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Barrier extends MapFeature{


    public Barrier(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("hence"))setValueSpecs(ValueName.HENCE);
        if(value.equals("fence")) setValueSpecs(ValueName.FENCE);
        if(value.equals("kerb")) setValueSpecs(ValueName.KERB);
        else setValueSpecs(ValueName.BARRIER);
    }

}
