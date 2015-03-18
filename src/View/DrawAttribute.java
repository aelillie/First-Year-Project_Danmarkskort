package View;

import java.awt.*;

/**
 * Helper class, which only contains predefined strokes and colors.
 */
public class DrawAttribute {

    private boolean dashed;
    private int strokeId;
    private Color color;
    private double zoomLevel;

    public DrawAttribute(boolean dashed, int strokeId, Color color, double zoomLevel) {
        this.dashed = dashed;
        this.strokeId = strokeId;
        this.color = color;
        this.zoomLevel = zoomLevel;
    }

    //Different predefined basicStrokes.
    public static Stroke s00 = new BasicStroke(Float.MIN_VALUE);
    public static Stroke s01 = new BasicStroke(0.00001f);
    public static Stroke s02 = new BasicStroke(0.00002f);
    public static Stroke s03 = new BasicStroke(0.00003f);
    public static Stroke s04 = new BasicStroke(0.00004f);
    public static Stroke s05 = new BasicStroke(0.00005f);
    public static Stroke s06 = new BasicStroke(0.00006f);
    public static Stroke s07 = new BasicStroke(0.00007f);
    public static Stroke s08 = new BasicStroke(0.00008f);
    public static Stroke s09 = new BasicStroke(0.00009f);
    public static Stroke s10 = new BasicStroke(0.00010f);
    public static Stroke s11 = new BasicStroke(0.00011f);
    public static Stroke s12 = new BasicStroke(0.00012f);
    public static Stroke s13 = new BasicStroke(0.00013f);
    public static Stroke s14 = new BasicStroke(0.00014f);
    public static Stroke s15 = new BasicStroke(0.00015f);

    public static Stroke r00 = new BasicStroke(Float.MIN_VALUE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r01 = new BasicStroke(0.00001f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r02 = new BasicStroke(0.00002f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r03 = new BasicStroke(0.00003f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r04 = new BasicStroke(0.00004f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r05 = new BasicStroke(0.00005f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r06 = new BasicStroke(0.00006f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r07 = new BasicStroke(0.00007f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r08 = new BasicStroke(0.00008f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r09 = new BasicStroke(0.00009f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r10 = new BasicStroke(0.00010f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static Stroke r11 = new BasicStroke(0.00011f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static Stroke d1 = new BasicStroke(0.00001f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{0.0001f}, 0.00001f); //Metro
    public static Stroke d2 = new BasicStroke(0.00002f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,30.0f, new float[]{0.00005f},0.00000f); //Footway
    public static Stroke d3 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.0001f}, 0.000001f); //Cycleway
    public static Stroke d4 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.00001f}, 0.000001f); //
    public static Stroke d5 = new BasicStroke(0.00003f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[]{0.00005f}, 0.00003f); //Railways transportview


    //Predefined colors to use.
    public static Color black = new Color(0, 0, 0);
    public static Color lightblue = new Color(70, 130, 180);
    public static Color lightgreen = new Color(34,139,34);
    public static Color darkgreen = new Color(0, 100, 0);
    public static Color lightyellow = new Color(240,230,140);
    public static Color darkblue = new Color(0, 0, 139);
    public static Color lightgrey = new Color(211,211,211);
    public static Color lightergrey = new Color(226, 226, 226);
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
    public static Color brown = new Color(139,69,19);
    public static Color ground = new Color(238,238,238);

    //Predefined colors for colorblind view
    public static Color cl_red = new Color(255, 0, 0);
    public static Color cl_red1 = new Color(213, 0, 0);
    public static Color cl_red2 = new Color(151, 0, 0);
    public static Color cl_red3 = new Color(105, 0, 0);
    public static Color cl_red4 = new Color(75, 0, 0);
    public static Color cl_blue = new Color(0, 15, 255);
    public static Color cl_blue1 = new Color(0, 15, 195);
    public static Color cl_blue2 = new Color(0, 15, 138);
    public static Color cl_blue3 = new Color(0, 14, 103);
    public static Color cl_blue4 = new Color(0, 11, 58);
    public static Color cl_grey = new Color(173, 171, 175);
    public static Color cl_grey1 = new Color(129, 127, 131);
    public static Color cl_grey2 = new Color(87, 85, 88);



    public static Color[] colors = new Color[] {lightblue, lightgreen, darkgreen, lightyellow, darkblue, lightgrey, neongreen, babyred, lightred,
                                                grey, white, bloodred, red, skincolor, bluegreen, orange, pink, sand, whiteblue, greenblue, whitegreen, brown};
    public static Stroke[] basicStrokes = new Stroke[]{s00, s01, s02, s03, s04, s05, s06, s07, s08, s09, s10, s11,s12, s13, s14, s15};
    public static Stroke[] dashedStrokes = new Stroke[] {d1, d2, d3, d4, d5};
    public static Stroke[] streetStrokes = new Stroke[] {r00, r01, r02, r03, r04, r05, r06, r07, r08, r09, r10, r11};

    public boolean isDashed() {
        return dashed;
    }

    public int getStrokeId() {
        return strokeId;
    }

    public Color getColor() {
        return color;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }
}

