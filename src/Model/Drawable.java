package Model;

import java.awt.*;

public abstract class Drawable{
    protected Shape shape;
    protected Color color;
    protected Double drawLevel;
    protected int layerVal;

    //Different predefined strokes.
    static Stroke s00 = new BasicStroke(Float.MIN_VALUE);
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
    static Stroke s11 = new BasicStroke(0.00001f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{0.0001f}, 0.00001f); //Metro
    static Stroke s12 = new BasicStroke(0.00002f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,30.0f, new float[]{0.00005f},0.00000f); //Footway
    static Stroke s13 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.0001f}, 0.000001f); //Cycleway
    static Stroke s14 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.00001f}, 0.000001f); //Cycleway

    //Predefined colors to use.
    public static Color lightblue = new Color(70, 130, 180);
    public static Color lightgreen = new Color(34,139,34);
    public static Color darkgreen = new Color(0, 100, 0);
    public static Color lightyellow = new Color(240,230,140);
    public static Color darkblue = new Color(0, 0, 139);
    public static Color lightgrey = new Color(211,211,211);
    public static Color neongreen = new Color(50,205,50);
    public static Color babyred = new Color(205,92,92);
    public static Color lightred = new Color(205, 54, 60);
    public static Color grey = new Color(169,169,169);
    public static Color white = new Color(211,211,211);
    public static Color bloodred = new Color(128,0,0);
    public static Color red = new Color(178,34,34);
    public static Color skincolor = new Color(222,184,135);
    public static Color bluegreen = new Color(60,179,113);
    public static Color orange = new Color(205,133,63);
    public static Color pink = new Color(188,143,143);
    public static Color sand = new Color(255,228,181);
    public static Color whiteblue = new Color(95,158,160);
    public static Color greenblue = new Color(102,205,170);
    public static Color whitegreen = new Color(144,238,144);


    public static Stroke[] strokes = new Stroke[]{s00, s01, s02, s03, s04, s05, s06, s07, s08, s09, s10, s11,s12, s13, s14};

    /**
     * Sets up everything needed.
     * @param shape Shape to be drawn
     * @param color Color of shape
     * @param drawLevel When to draw
     * @param layerVal Layer it should be drawn in
     */
    public Drawable(Shape shape, Color color, double drawLevel, int layerVal) {
        this.shape = shape;
        this.color = color;
        this.drawLevel = drawLevel;
        this.layerVal = layerVal;
    }
    public Drawable(){}

    //Abstract methods.
    public abstract void draw(Graphics2D g);

    public abstract void drawBoundary(Graphics2D g);

    public int getLayerVal() {
        return layerVal;
    }

    public Double getDrawLevel() {
        return drawLevel;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}

