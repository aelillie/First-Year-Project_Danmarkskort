package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Multipolygon extends MapFeature {

    public Multipolygon(Path2D way, int layer_value, String value) {

        //TODO this is still not done at all!
        super(way, layer_value, value);
        setValueAttributes();
        isArea = true;
    }

    @Override
    public void setValueAttributes() {
        if(value.equals("building")) setValueName(ValueName.BUILDING);
    }

}
