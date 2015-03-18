package MapFeatures;

import Model.ValueName;
import View.DrawAttribute;
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
        if (value.equals("wood")) setValueSpecs(ValueName.WOOD);
        else if (value.equals("scrub")) setValueSpecs(ValueName.SCRUB);
        else if (value.equals("heath")) setValueSpecs(ValueName.HEATH);
        else if (value.equals("grassland")) setValueSpecs(ValueName.GRASSLAND);
        else if (value.equals("sand")) setValueSpecs(ValueName.SAND);
        else if (value.equals("scree")) setValueSpecs(ValueName.SCREE);
        else if (value.equals("fell")) setValueSpecs(ValueName.FELL);
        else if (value.equals("water")) setValueSpecs(ValueName.WATER);
        else if (value.equals("wetland")) setValueSpecs(ValueName.WETLAND);
        else if (value.equals("beach")) setValueSpecs(ValueName.BEACH);
       /* else if (value.equals("coastline")){
            isArea = false;
            setValueSpecs(ValueName.COASTLINE);
        }*/
        else setValueSpecs(ValueName.NATURAL);
    }


    private void setValueIcon() {

    }


}
