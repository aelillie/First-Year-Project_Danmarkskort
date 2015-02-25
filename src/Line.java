import java.awt.*;

/**
 * Created by Anders on 21-02-2015.
 */

public class Line extends Drawable {
    int stroke_id;

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
        g.setStroke(strokes[stroke_id+1]);
        g.setColor(Color.BLACK);
        g.draw(shape);
    }
}