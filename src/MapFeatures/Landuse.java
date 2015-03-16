package MapFeatures;

import Model.DrawAttributes;
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
        if(value.equals("cemetery")) setValueSpecs(DrawAttributes.whitegreen, -0.8);
        else if(value.equals("construction")) setValueSpecs(DrawAttributes.lightgreen, -0.4);
        else if(value.equals("grass")) {
            isArea = true;
            setValueSpecs(DrawAttributes.whitegreen, -1.0);
        }
        else if(value.equals("greenfield")) setValueSpecs(DrawAttributes.bluegreen, -.8);
        else if(value.equals("industrial")) setValueSpecs(DrawAttributes.bluegreen, -.8);
        else if(value.equals("orchard")) setValueSpecs(DrawAttributes.bluegreen, -.8);
        else if(value.equals("reservoir")) setValueSpecs(DrawAttributes.darkblue, -.8);
        else if(value.equals("allotments")) {
            isArea = true;
            setValueSpecs(DrawAttributes.brown, -.8);
        }
    }

    @Override
    public void setValueIcon() {

    }

    @Override
    public void setColorBlind() {

    }

    @Override
    public void setStandard() {

    }
}
