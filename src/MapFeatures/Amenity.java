package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Amenity extends MapFeature {
    private boolean isBuilding = false;
    public Amenity(Shape way, int layer_value, String value, boolean isBuilding) {
        super(way, layer_value, value);
        isArea = true;
        setValueAttributes();
        this.isBuilding = isBuilding;
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("parking")) setValueSpecs(ValueName.PARKING);
        else if(value.equals("university") && isBuilding )setValueSpecs(ValueName.UNIVERSITY);
        else {
            isArea = false;
            setValueSpecs(ValueName.AMENITY);
        }
        //TODO: Does not work. in newSmall the university is not drawn because it is not said to be building
    }

}
