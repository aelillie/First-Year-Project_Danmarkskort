package Model;

import java.awt.*;

public abstract class Drawable{

    //Different predefined basicStrokes.
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
    static Stroke s11 = new BasicStroke(0.00011f);
    static Stroke s12 = new BasicStroke(0.00012f);
    static Stroke s13 = new BasicStroke(0.00013f);
    static Stroke s14 = new BasicStroke(0.00014f);
    static Stroke s15 = new BasicStroke(0.00015f);

    static Stroke r00 = new BasicStroke(Float.MIN_VALUE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r01 = new BasicStroke(0.00001f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r02 = new BasicStroke(0.00002f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r03 = new BasicStroke(0.00003f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r04 = new BasicStroke(0.00004f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r05 = new BasicStroke(0.00005f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r06 = new BasicStroke(0.00006f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r07 = new BasicStroke(0.00007f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r08 = new BasicStroke(0.00008f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r09 = new BasicStroke(0.00009f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r10 = new BasicStroke(0.00010f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static Stroke r11 = new BasicStroke(0.00011f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    static Stroke d1 = new BasicStroke(0.00001f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{0.0001f}, 0.00001f); //Metro
    static Stroke d2 = new BasicStroke(0.00002f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,30.0f, new float[]{0.00005f},0.00000f); //Footway
    static Stroke d3 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.0001f}, 0.000001f); //Cycleway
    static Stroke d4 = new BasicStroke(0.00002f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 20.0f, new float[]{0.00001f}, 0.000001f); //Cycleway

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


    public static Stroke[] basicStrokes = new Stroke[]{s00, s01, s02, s03, s04, s05, s06, s07, s08, s09, s10, s11,s12, s13, s14, s15};
    public static Stroke[] dashedStrokes = new Stroke[] {d1, d2, d3, d4};
    public static Stroke[] streetStrokes = new Stroke[] {r00, r01, r02, r03, r04, r05, r06, r07, r08, r09, r10, r11};

}

