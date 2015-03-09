package View;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

class TextDraw {
    GeneralPath path;
    String text;
    double[] tokenWidths;
    Point2D.Double[] points;
    double offset;

    public void draw(Graphics2D g2, GeneralPath path, String text, double offset) {
        this.path = path;
        this.text = text;
        this.offset = offset;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = g2.getFont().deriveFont(0.00015f);
        g2.setFont(font);
        FontRenderContext frc = g2.getFontRenderContext();
        tokenWidths = getTokenWidths(font, frc);
        // collect path points
        collectLayoutPoints(getPathPoints());
        for(int j = 0; j < points.length-1; j++) {
           // double theta = getAngle(j);
            AffineTransform at = AffineTransform.getTranslateInstance(points[j].x, points[j].y);
            AffineTransform tmp = new AffineTransform();
            tmp.setToScale(1,-1);
            tmp.preConcatenate(at);
           // at.rotate(theta);
            g2.setFont(font.deriveFont(tmp));
            g2.drawString(text, 0, 0);
        }
    }

    /*private Point2D.Double[] stringBetween()
    {
        PathIterator pit = path.getPathIterator(null,0.01);

        while(!pit.isDone())
        {

        }
    }*/

    private double getAngle(int index) {
        double dy = points[index+1].y -points[index].y;
        double dx = points[index+1].x -points[index].x;
        return Math.atan2(dy, dx);
    }

    private Point2D.Double[] getPathPoints() {
        double flatness = 0.02;
        PathIterator pit = path.getPathIterator(null, flatness);
        int count = 0;
        while(!pit.isDone()) {
            count++;
            pit.next();
        }
        Point2D.Double[] points = new Point2D.Double[count];
        pit = path.getPathIterator(null, flatness);
        double[] coords = new double[10];
        count = 0;
        while(!pit.isDone()) {
            int type = pit.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    points[count++] = new Point2D.Double(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_CLOSE:
                    break;
                default:
                    System.out.println("unexpected type: " + type);
            }
            pit.next();
        }
        return points;
    }

    private double[] getTokenWidths(Font font, FontRenderContext frc) {
        String[] tokens = text.split("(?<=[\\w\\s])");
        double[] widths = new double[tokens.length];
        for(int j = 0; j < tokens.length; j++) {
            float width = (float)font.getStringBounds(tokens[j], frc).getWidth();
            widths[j] = width;
        }
        return widths;
    }

    private void collectLayoutPoints(Point2D.Double[] p) {
        int index = 0;
        int n = tokenWidths.length;
        double distance = offset;
        points = new Point2D.Double[n+1];
        for(int j = 0; j < tokenWidths.length; j++) {
            index = getNextPointIndex(p, index, distance);
            points[j] = p[index];
            distance = tokenWidths[j];
        }
        index = getNextPointIndex(p, index, tokenWidths[n-1]);
        points[points.length-1] = p[index];
    }

    private int getNextPointIndex(Point2D.Double[] p, int start, double targetDist) {
        for(int j = start; j < p.length; j++) {
            double distance = p[j].distance(p[start]);
            if(distance > targetDist) {
                return j;
            }
        }
        return start;
    }
}