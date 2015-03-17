package Model;

import View.DrawAttribute;

import java.awt.*;
import java.io.Serializable;

public abstract class MapFeature implements Serializable {
    protected Shape way;
    protected int layer_value;
    protected String value;
    protected double zoom_level;
    protected boolean isArea = false;
    public ValueName valueName;

    public MapFeature(Shape way, int layer_value, String value) {
        this.way = way;
        this.layer_value = layer_value;
        this.value = value;
    }

    public abstract void setValueAttributes();

    public void setValueSpecs(ValueName valueName, double zoom_level) {
        this.valueName = valueName;
        this.zoom_level = zoom_level;
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


        public void drawBoundary(Graphics2D g, DrawAttribute drawAttribute) {
            if(!isArea) {
            if(!drawAttribute.dashed) {
                g.setStroke(DrawAttribute.streetStrokes[drawAttribute.strokeId + 1]);
                g.setColor(Color.BLACK);
                g.draw(way);
            }
        } else {
            g.setStroke(DrawAttribute.basicStrokes[1]);
            g.setColor(Color.BLACK);
            g.draw(way);
        }

    }

    public void draw(Graphics2D g, DrawAttribute drawAttribute) {
        if (isArea) {
            g.setColor(drawAttribute.color);
            g.fill(way);
        } else {
            if (drawAttribute.dashed) g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.strokeId]);
            else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.strokeId]);
            g.setColor(drawAttribute.color);
            g.draw(way);
        }
    }

    public double getZoom_level(){
        return zoom_level;
    }

    public int getLayerVal() {
        return layer_value;
    }

    public void setZoom_level(double zoom_level) {
        this.zoom_level = zoom_level;
    }
}
