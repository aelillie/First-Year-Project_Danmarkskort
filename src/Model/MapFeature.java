package Model;

import View.DrawAttribute;

import java.awt.*;
import java.io.Serializable;

public abstract class MapFeature implements Serializable {
    protected Shape way;
    protected int layer_value;
    protected String value;
    protected boolean isArea = false;
    public ValueName valueName;

    public MapFeature(Shape way, int layer_value, String value) {
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


    public void drawBoundary(Graphics2D g, DrawAttribute drawAttribute) {
        g.setColor(Color.BLACK);
        if(!isArea && !drawAttribute.isDashed()) g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
        else g.setStroke(DrawAttribute.basicStrokes[1]);
        g.draw(way);

    }

    public void draw(Graphics2D g, DrawAttribute drawAttribute) {
        g.setColor(drawAttribute.getColor());
        if (isArea) {
            g.fill(way);
        } else {
            if (drawAttribute.isDashed()) g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
            else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
            g.draw(way);
        }
    }

    public int getLayerVal() {
        return layer_value;
    }


}
