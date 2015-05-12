package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

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
        else if (value.equals("hospital") || value.equals("pharmaceutical")) layer_value = -5;

    }

    @Override
    public void setValueName() {
        if(value.equals("parking")) {
            isArea = true;
            setValueName(ValueName.PARKING);
        }
        else if(value.equals("university") && isBuilding ) setValueName(ValueName.UNIVERSITY);
        else if(value.equals("school") && isBuilding ) setValueName(ValueName.SCHOOL);
        else if(value.equals("university") || value.equals("school") && !isBuilding) {
            isArea = true;
            setValueName(ValueName.SCHOOL_AREA);
        }
        else if(value.equals("pub")) setValueName(ValueName.PUB);
        else if(value.equals("bar")) setValueName(ValueName.BAR);
        else if(value.equals("pharmaceutical")) {
            isArea = true;
            setValueName(ValueName.PHARMACEUTICAL);
        }
        else if(value.equals("hospital")) {
            isArea = true;
            setValueName(ValueName.HOSPITAL);
        }
        else setValueName(ValueName.AMENITY);


    }

}
