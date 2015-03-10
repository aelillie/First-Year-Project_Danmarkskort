package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anders on 10-03-2015.
 */
public abstract class MapFeature implements Colorblind, Standard {
    private Shape way;
    private int layer_value;
    private Color color;
    private double zoom_level;
    private boolean hasIcon;
    private int stroke_id;
    private boolean dashed = false;

    protected List<String> values = new ArrayList<>();

    public MapFeature(Shape way, int layer_value, Map<String, String> keyValueMap) {
        this.way = way;
        this.layer_value = layer_value;
        values = (List<String>) keyValueMap.values();
        setValueAttributes();
        setValueIcon();
    }

    public abstract void setValueAttributes();

    public abstract void setValueIcon();

    public void drawAreaBoundary(Graphics2D g) {
        g.setStroke(Drawable.strokes[1]);
        g.setColor(Color.BLACK);
        g.draw(way);
    }

    public void drawArea(Graphics2D g){
        g.setColor(color);
        g.fill(way);
    }

    public void drawLineBoundary(Graphics2D g) {
        if(!dashed) {
            g.setStroke(Drawable.strokes[stroke_id + 1]);
            g.setColor(Color.BLACK);
            g.draw(way);
        }
    }

    public void drawLine(Graphics2D g){
        g.setStroke(Drawable.strokes[stroke_id]);
        g.setColor(color);
        g.fill(way);
    }

    public void setZoom_level(double zoom_level) {
        this.zoom_level = zoom_level;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
