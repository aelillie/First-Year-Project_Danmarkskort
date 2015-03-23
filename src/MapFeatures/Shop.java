package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;

/**
 * Created by Anders on 11-03-2015.
 */
public class Shop extends MapFeature {
    public Shop(Path2D way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
        isArea = true;
    }

    @Override
    public void setValueAttributes() {
        setValueName(ValueName.SHOP);
    }

}
