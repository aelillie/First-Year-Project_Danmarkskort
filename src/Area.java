import java.awt.*;

public class Area extends Drawable {

    public Area(Shape shape, Color color,double zoom) {
        super(shape, color, zoom);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(shape);

    }

    public void drawBoundary(Graphics2D g) {
        g.setStroke(strokes[1]);
        g.setColor(Color.BLACK);
        g.draw(shape);
    }
}
