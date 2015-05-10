package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Landuse extends MapFeature {
    private int length;

    public Landuse(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        if (!this.isArea) //if area isn't declared true in setValueAttr
            this.isArea = isArea; //set area to the the value specified in the parameter
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
        if (value.equals("basin")) layer_value = 42;
        else if (value.equals("forest")) layer_value = 41;
        else if (value.equals("industrial") || value.equals("commercial"))
            layer_value = -5;
        else if (value.equals("farmland") || value.equals("farmyard"))
            layer_value = -5;
        else if (value.equals("pitch"))
            layer_value = -4;
        else if (value.equals("grass"))
            layer_value = -4;
        else if (value.equals("meadow"))
            layer_value = -5;
        else if (value.equals("residential"))
            layer_value = -19;
        else if (value.equals("military"))
            layer_value = -5;

    }

    @Override
    public void setValueName() {
        if(value.equals("cemetery")) {
            isArea = true;
            setValueName(ValueName.CEMETERY);
        }
        else if(value.equals("meadow") || value.equals("recreation_ground") || value.equals("village_green")) {
            isArea = true;
            setValueName(ValueName.MEADOW);
        }
        else if(value.equals("farmland") || value.equals("farmyard") || value.equals("farm")){
            isArea = true;
            setValueName(ValueName.FARMLAND);
        }
        else if(value.equals("residential")) {
            isArea = true;
            setValueName(ValueName.RESIDENTIAL_AREA);
        }
        else if(value.equals("forest")) {
            isArea = true;
            setValueName(ValueName.FOREST);
        }
        else if(value.equals("construction")) setValueName(ValueName.CONSTRUCTION);
        else if(value.equals("grass")) {
            isArea = true;
            setValueName(ValueName.GRASS);
        }
        else if(value.equals("greenfield")) setValueName(ValueName.GREENFIELD);
        else if(value.equals("brownfield")) {
                isArea = true;
                setValueName(ValueName.BROWNFIELD);
        }
        else if(value.equals("industrial") || value.equals("commercial")) {
            isArea = true;
            setValueName(ValueName.INDUSTRIAL);
        }
        else if(value.equals("orchard")) setValueName(ValueName.ORCHARD);
        else if(value.equals("reservoir")){
            isArea = true;
            setValueName(ValueName.RESERVOIR);
        }
        else if(value.equals("basin")) {
            isArea = true;
            setValueName(ValueName.BASIN);
        }
        else if(value.equals("military")) {
            isArea = true;
            setValueName(ValueName.MILITARY);

        }
        else if(value.equals("allotments")) {
            isArea = true;
            setValueName(ValueName.ALLOTMENTS);
        }
        else setValueName(ValueName.LANDUSE);
    }

    public void setLength(int length) { this.length = length; }


}
