package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Natural extends MapFeature {

    public Natural(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        super.setPreDefValues();
        setValueAttributes();
        setValueIcon();

    }

    @Override
    public void setPreDefValues() {
        if (value.equals("water")) layer_value = 6;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("wood")) setValueName(ValueName.WOOD);
        else if (value.equals("scrub")) setValueName(ValueName.SCRUB);
        else if (value.equals("heath")) setValueName(ValueName.HEATH);
        else if (value.equals("grassland")) setValueName(ValueName.GRASSLAND);
        else if (value.equals("sand")) setValueName(ValueName.SAND);
        else if (value.equals("scree")) setValueName(ValueName.SCREE);
        else if (value.equals("fell")) setValueName(ValueName.FELL);
        else if (value.equals("water")) setValueName(ValueName.WATER);
        else if (value.equals("wetland")) setValueName(ValueName.WETLAND);
        else if (value.equals("beach")) setValueName(ValueName.BEACH);
       /* else if (value.equals("coastline")){
            isArea = false;
            setValueName(ValueName.COASTLINE);
        }*/
        else setValueName(ValueName.NATURAL);
    }


    private void setValueIcon() {

    }


}
