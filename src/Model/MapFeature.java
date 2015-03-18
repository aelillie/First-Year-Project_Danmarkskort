package Model;

import java.awt.geom.Path2D;
import java.io.Serializable;

public abstract class MapFeature implements Serializable {
    protected Path2D way;
    protected int layer_value;
    protected String value;
    protected boolean isArea = false;
    protected ValueName valueName;

    public MapFeature(Path2D way, int layer_value, String value) {
        this.way = way;
        this.layer_value = layer_value;
        this.value = value;
    }

    public abstract void setValueAttributes();

    public void setValueSpecs(ValueName valueName) {
        this.valueName = valueName;
    }

    /*
    public void setLineSpecs(Color color, double zoom_level, int stroke_id) {
        setValueSpecs(color, zoom_level);
        this.stroke_id = stroke_id;
    }

    public void setValueDashedSpecs(Color color, double zoom_level, int stroke_id) {
        setLineSpecs(color, zoom_level, stroke_id);
        dashed = true;
    }
    */


 
    public ValueName getValueName() {
        return valueName;
    }

    public int getLayerVal() {
        return layer_value;
    }

    public boolean isArea(){
        return isArea;
    }

    public Path2D getShape(){
        return way;
    }

    public String getValue(){return value;}


}
