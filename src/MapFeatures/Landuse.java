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
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("cemetery")) setValueSpecs(ValueName.CEMETERY);
        else if(value.equals("construction")) setValueSpecs(ValueName.CONSTRUCTION);
        else if(value.equals("grass")) {
            isArea = true;
            setValueSpecs(ValueName.GRASS);
        }
        else if(value.equals("greenfield")) setValueSpecs(ValueName.GREENFIELD);
        else if(value.equals("industrial")) setValueSpecs(ValueName.INDUSTRIAL);
        else if(value.equals("orchard")) setValueSpecs(ValueName.ORCHARD);
        else if(value.equals("reservoir")) setValueSpecs(ValueName.RESERVOIR);
        else if(value.equals("allotments")) {
            isArea = true;
            setValueSpecs(ValueName.ALLOTMENTS);
        }
        else setValueSpecs(ValueName.LANDUSE);
    }

}
