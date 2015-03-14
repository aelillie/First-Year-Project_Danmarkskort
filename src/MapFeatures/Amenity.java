package MapFeatures;

import Model.DrawAttributes;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Amenity extends MapFeature {
    private Boolean isBuilding = false;
    public Amenity(Shape way, int layer_value, String value, Boolean isBuilding) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
        this.isBuilding = isBuilding;
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("parking")) setValueSpecs(DrawAttributes.sand, -1.0);
        else if(value.equals("university") && isBuilding )setValueSpecs(DrawAttributes.lightgrey, -1.0);

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
