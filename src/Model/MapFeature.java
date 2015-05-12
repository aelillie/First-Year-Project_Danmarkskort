package Model;

import java.awt.geom.Path2D;
import java.io.Serializable;

/**
 * ##Top class in the hierarchy of map features##
 * Creates objects of map features with a shape, layer to be drawn on and the exact value for a given key passed from
 * the OSMHandler. When an a map feature is created, it is given an ENUM value name, and in some cases,
 * whether the map feature is an area or not.
 */
public abstract class MapFeature implements Serializable, MapData {
    public static final long serialVersionUID = 16;
    protected Path2D way;
    protected int layer_value;
    protected String value;
    protected Boolean isArea = false;
    protected ValueName valueName;

    public MapFeature(Path2D way, int layer_value, String value) {
        this.way = way;
        this.layer_value = layer_value;
        this.value = value.intern();
        setPreDefLayerValues();
        setValueName();
    }


    /**
     * Defines a larger spectrum of layer values
     * Predefined layer values getIndex their values multiplied
     * by a factor 10
     */
    public void setPreDefLayerValues() {
        if (layer_value == -5) layer_value = -50;
        else if (layer_value == -4) layer_value = -40;
        else if (layer_value == -3) layer_value = -30;
        else if (layer_value == -2) layer_value = -20;
        else if (layer_value == -1) layer_value = -10;
        else if (layer_value == 0) layer_value = 0;
        else if (layer_value == 1) layer_value = 10;
        else if (layer_value == 2) layer_value = 20;
        else if (layer_value == 3) layer_value = 30;
        else if (layer_value == 4) layer_value = 40;
        else if (layer_value == 5) layer_value = 50;
    }

    /**
     * When instances are created this method is
     * called to set the ValueName of the object
     */
    public abstract void setValueName(); //Every new map feature calls this method to set specific attributes

    @Override
    public String toString() {
        return "MapFeature{" +
                ", layer_value=" + layer_value +
                ", value='" + value + '\'' +
                ", isArea=" + isArea +
                ", valueName=" + valueName +
                '}';
    }

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
    public Path2D getWay(){
        return way;
    }
    public String getValue(){return value;}


    public Class getClassType(){
        return this.getClass().getSuperclass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapFeature)) return false;

        MapFeature that = (MapFeature) o;

        if (layer_value != that.layer_value) return false;
        if (way != null ? !way.equals(that.way) : that.way != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (isArea != null ? !isArea.equals(that.isArea) : that.isArea != null) return false;
        return valueName == that.valueName;

    }

    @Override
    public int hashCode() {
        int result = way != null ? way.hashCode() : 0;
        result = 31 * result + layer_value;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (isArea != null ? isArea.hashCode() : 0);
        result = 31 * result + (valueName != null ? valueName.hashCode() : 0);
        return result;
    }
}
