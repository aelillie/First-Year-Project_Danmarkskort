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
        setPreDefValues();
    }

    protected void setPreDefValues() {
        if (layer_value == 0) setPreDefValues();
        if (value.equals("motorway") || value.equals("motorway_link")) layer_value = 7; //TODO: What if there's a bridge? (Value max 5)
        else if (value.equals("trunk") || value.equals("trunk_link")) layer_value = 6;
        else if (value.equals("primary") || value.equals("primay_link")) layer_value = 5;
        else if (value.equals("secondary") || value.equals("secondary_link")) layer_value = 4;
        else if (value.equals("tertiary") || value.equals("tertiary_link")) layer_value = 3;
        else if (value.equals("residential")) layer_value = 2;
        else layer_value = 1;
    }

    /**
     * When instances are created this method is
     * called to set the ValueName of the object
     */
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

    public Path2D getWay() {
        return way;
    }

    public int getLayer_value() {
        return layer_value;
    }
}
