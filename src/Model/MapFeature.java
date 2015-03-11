package Model;

import java.awt.*;

public abstract class MapFeature implements Colorblind, Standard {
    protected Shape way;
    protected int layer_value;
    protected Color color;
    protected double zoom_level;
    protected boolean hasIcon;
    protected int stroke_id;
    protected boolean dashed = false;
    protected String value;

    public MapFeature(Shape way, int layer_value, String value) {
        this.way = way;
        this.layer_value = layer_value;
        this.value = value;
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
        if(!dashed) g.setStroke(Drawable.strokes[stroke_id]);
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

    public void isDashed(){
        dashed = true;
    }
}
