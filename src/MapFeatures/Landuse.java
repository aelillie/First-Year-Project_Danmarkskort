package MapFeatures;

import View.DrawAttribute;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Landuse extends MapFeature {
    public Landuse(Shape way, int layer_value, String value, boolean isArea) {
        super(way, layer_value, value);
        this.isArea = isArea;
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("cemetery")) setValueSpecs(DrawAttribute.whitegreen, -0.8);
        else if(value.equals("construction")) setValueSpecs(DrawAttribute.lightgreen, -0.4);
        else if(value.equals("grass")) {
            isArea = true;
            setValueSpecs(DrawAttribute.whitegreen, -1.0);
        }
        else if(value.equals("greenfield")) setValueSpecs(DrawAttribute.bluegreen, -.8);
        else if(value.equals("industrial")) setValueSpecs(DrawAttribute.bluegreen, -.8);
        else if(value.equals("orchard")) setValueSpecs(DrawAttribute.bluegreen, -.8);
        else if(value.equals("reservoir")) setValueSpecs(DrawAttribute.darkblue, -.8);
        else if(value.equals("allotments")) {
            isArea = true;
            setValueSpecs(DrawAttribute.brown, -.8);
        }
    }

}
