package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Amenity extends MapFeature {
    private boolean isBuilding = false;
    public Amenity(Path2D way, int layer_value, String value, boolean isBuilding) {
        super(way, layer_value, value);
        this.isBuilding = isBuilding;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("fountain")) layer_value = 45;
    }

    @Override
    public void setValueName() {
        if(value.equals("parking")) setValueName(ValueName.PARKING);
        else if(value.equals("university") && isBuilding ) setValueName(ValueName.UNIVERSITY);
        else if(value.equals("school") && isBuilding ) setValueName(ValueName.SCHOOL);
        else if(value.equals("university") || value.equals("school") && !isBuilding) {
            isArea = true;
            setValueName(ValueName.SCHOOL_AREA);
        }
        else if(value.equals("pub")) setValueName(ValueName.PUB);
        else if(value.equals("bar")) setValueName(ValueName.BAR);
        else if(value.equals("pharmaceutical")) setValueName(ValueName.PHARMACEUTICAL);
        else setValueName(ValueName.AMENITY);


    }

}
