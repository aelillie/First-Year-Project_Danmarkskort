package Model.MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Historic extends MapFeature {
    public Historic(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        isArea = true;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
    }

    @Override
    public void setValueName() {
        if(value.equals("archaeological_site")) setValueName(ValueName.ARCHAEOLOGICAL_SITE);
        else setValueName(ValueName.HISTORIC);
    }

}
