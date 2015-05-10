package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

public class Natural extends MapFeature {

    public Natural(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("water")) layer_value = 6;
        else if (value.equals("grassland")) layer_value = 8;
        else if (value.equals("wood")) layer_value = 9;

    }

    @Override
    public void setValueName() {
        switch (value) {
            case "wood":
                setValueName(ValueName.WOOD);
                break;
            case "scrub":
                setValueName(ValueName.SCRUB);
                break;
            case "forest":
                setValueName(ValueName.FOREST);
                break;
            case "heath":
                setValueName(ValueName.HEATH);
                break;
            case "grassland":
                setValueName(ValueName.GRASSLAND);
                break;
            case "sand":
                setValueName(ValueName.SAND);
                break;
            case "scree":
                setValueName(ValueName.SCREE);
                break;
            case "fell":
                setValueName(ValueName.FELL);
                break;
            case "water":
                setValueName(ValueName.WATER);
                break;
            case "wetland":
                setValueName(ValueName.WETLAND);
                break;
            case "mud":
                setValueName(ValueName.MUD);
                break;
            case "beach":
                setValueName(ValueName.BEACH);
                break;
            default:
                setValueName(ValueName.NATURAL);
                break;
        }
    }

}
