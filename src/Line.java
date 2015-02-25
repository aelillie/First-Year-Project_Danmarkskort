import java.awt.*;

public class Line extends Drawable {
    int stroke_id;
    private boolean dashed = false;

    public Line(Shape shape, Color color, int stroke_id) {
        super(shape, color);
        this.stroke_id = stroke_id;
    }

    public void draw(Graphics2D g) {
        g.setStroke(strokes[stroke_id]);
        g.setColor(color);
        g.draw(shape);
    }

    public void drawBoundary(Graphics2D g) {
        if(!dashed) {
            g.setStroke(strokes[stroke_id + 1]);
            g.setColor(Color.BLACK);
            g.draw(shape);
        }
    }

    public void setDashed(){
        dashed = true;
    }
}