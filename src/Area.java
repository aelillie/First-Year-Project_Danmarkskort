import java.awt.*;

public class Area extends Drawable {

    /**
     * Sets up the area with a color and the shape of the area.
     * @param shape Shape of area
     * @param color Color of Area
     * @param zoom  Zoom used when drawn.
     */
    public Area(Shape shape, Color color,double zoom) {
        super(shape, color, zoom);
    }

    /**
     * Fills the shape with Predefined color.
     * @param g Graphic 2D with correct transform
     */
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(shape);

    }

    /**
     * Draws the boundary of the shape with predefined stroke and black.
     * @param g Graphic 2D with correct transform
     */
    public void drawBoundary(Graphics2D g) {
        g.setStroke(strokes[1]);
        g.setColor(Color.BLACK);
        g.draw(shape);
    }
}
