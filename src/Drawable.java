import java.awt.*;

public abstract class Drawable {
    Shape shape;
    Color color;

    static Stroke min_value = new BasicStroke(Float.MIN_VALUE);
    static Stroke s01 = new BasicStroke(0.00001f);
    static Stroke s02 = new BasicStroke(0.00002f);
    static Stroke s03 = new BasicStroke(0.00003f);
    static Stroke s04 = new BasicStroke(0.00004f);
    static Stroke s05 = new BasicStroke(0.00005f);
    static Stroke s06 = new BasicStroke(0.00006f);
    static Stroke s07 = new BasicStroke(0.00007f);
    static Stroke s08 = new BasicStroke(0.00008f);
    static Stroke s09 = new BasicStroke(0.00009f);
    static Stroke s10 = new BasicStroke(0.00010f);

    //Colors
    static Color seawater = new Color(70, 130, 180);
    static Color lightgreen = new Color(34,139,34);
    static Color darkgreen = new Color(0, 100, 0);
    static Color yellowroad = new Color(240,230,140);
    static Color coastline = new Color(0, 0, 139);
    static Color building = new Color(211,211,211);
    static Color motorway = new Color(70,130,180);
    static Color trunk = new Color(50,205,50);
    static Color primary = new Color(205,92,92);
    static Color secondary = new Color(205,92,92);
    static Color tertiary = new Color(240,230,140);
    static Color living_street = new Color(169,169,169);
    static Color pedestrain = new Color(211,211,211);
    static Color track = new Color(128,0,0);
    static Color bus_guideway = new Color(25,25,112);
    static Color raceway = new Color(255,228,225);
    static Color road = new Color(192,192,192);
    static Color footway = new Color(178,34,34);
    static Color cycleway = new Color(70,130,180);
    static Color bridleway = new Color(60,179,113);
    static Color steps = new Color(178,34,34);
    static Color path = new Color(178,34,34);
    static Color wood = new Color(34,139,34);
    static Color scrub = new Color(50,205,50);
    static Color heath = new Color(222,184,135);
    static Color grassland = new Color(60,179,113);
    static Color fell = new Color(205,133,63);
    static Color scree = new Color(188,143,143);
    static Color sand = new Color(255,228,181);
    static Color water = new Color(95,158,160);
    static Color wetland = new Color(102,205,170);
    static Color park = new Color(144,238,144);
    static Color garden = new Color(152,251,152);

    Stroke[] strokes = new Stroke[]{s01, s02, s03, s04, s05, s06, s07, s08, s09, s10};

    public Drawable(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }
    abstract void draw(Graphics2D g);

    abstract void drawBoundary(Graphics2D g);
}

