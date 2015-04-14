package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Landuse extends MapFeature {
    public Landuse(Path2D way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;
    }

    @Override
    public void setPreDefValues() {
        super.setPreDefValues();
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("cemetery")) setValueName(ValueName.CEMETERY);
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
        else if(value.equals("industrial")) setValueName(ValueName.INDUSTRIAL);
        else if(value.equals("orchard")) setValueName(ValueName.ORCHARD);
        else if(value.equals("reservoir")){
            isArea = true;
            setValueName(ValueName.RESERVOIR);
        }
        else if(value.equals("basin")) {
            isArea = true;
            setValueName(ValueName.BASIN);
       } else if(value.equals("allotments")) {
            isArea = true;
            setValueName(ValueName.ALLOTMENTS);
        }
        else setValueName(ValueName.LANDUSE);
    }

}
