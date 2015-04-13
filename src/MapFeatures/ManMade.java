package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Kevin on 11-03-2015.
 */
public class ManMade extends MapFeature{

    public ManMade(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setPreDefValues() {
        super.setPreDefValues();
    }

    @Override
    public void setValueAttributes() {
        setValueName(ValueName.MANMADE);
    }

}
