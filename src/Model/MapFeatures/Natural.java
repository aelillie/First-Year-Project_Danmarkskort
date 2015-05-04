package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Natural extends MapFeature {

    public Natural(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
        setValueIcon();

    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("water")) layer_value = 6;
    }

    @Override
    public void setValueName() {
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

        else setValueName(ValueName.NATURAL);
    }


    private void setValueIcon() {

    }


}
