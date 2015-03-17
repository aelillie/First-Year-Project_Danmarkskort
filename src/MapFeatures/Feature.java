package MapFeatures;

import View.Factory;

import java.awt.*;

/**
 * Created by Anders on 17-03-2015.
 */
public class Feature {
    private Shape way;
    private int layerValue;
    private String value;

    private Color color;
    private int strokeID;
    private double zoomLevel;


    public Feature(Shape way, int layerValue, String value) {
        this.way = way;
        this.layerValue = layerValue;
        this.value = value;

        Factory.getColor(value);
        Factory.getStrokeID(value);
        setZoomLevel(value);
    }

    public void setZoomLevel(String value) {
        if(value.equals("highway"))
            zoomLevel = -2.0;
    }
}
