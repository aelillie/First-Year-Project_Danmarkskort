package Model;

import java.awt.geom.Path2D;
import java.io.Serializable;

/**
 * ##Top class in the hierarchy of map features##
 * Creates objects of map features with a shape, layer to be drawn on and the exact value for a given key passed from
 * the OSMHandler. When an a map feature is created, it is given an ENUM value name, and in some cases,
 * whether the map feature is an area or not.
 */
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


    public abstract void setValueAttributes(); //Every new map feature calls this method to set specific attributes

    /**
     * Assigns a value name from the ENUM class ValueName to map feature created
     * @param valueName ENUM value name
     */

    //GETTERS AND SETTERS
    public void setValueName(ValueName valueName) {
        this.valueName = valueName;
    }

 
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
