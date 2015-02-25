import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.geom.Point2D;
import java.util.List;

import static java.lang.Math.*;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    Model model;
    Canvas canvas;
    AffineTransform transform = new AffineTransform();
    boolean antialias = true;

    public View(Model m) {
        model = m;
        // bbox.width * xscale * .56 = 512
        // bbox.height * yscale = 512
        double xscale = 800 / .56 / model.bbox.getWidth();
        double yscale = 800 / model.bbox.getHeight();
        double scale = max(xscale, yscale);
        transform.scale(.56*scale,-scale);
        transform.translate(-model.bbox.getMinX(), -model.bbox.getMaxY());
        m.addObserver(this);
        canvas = new Canvas();
        setLayout(new BorderLayout());
        getContentPane().add(canvas, BorderLayout.CENTER);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void update(Observable obs, Object obj) {
        canvas.repaint();
    }

    public void zoom(double factor) {
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(getWidth() * (1-factor) / 2, getHeight() * (1-factor) / 2);
    }

    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx,dy));
        repaint();
    }

    public void toggleAA() {
        antialias = !antialias;
        repaint();
    }

    class Canvas extends JComponent {
        public static final long serialVersionUID = 4;
        Random rnd = new Random();
        Stroke min_value = new BasicStroke(Float.MIN_VALUE);

        @Override
        public void paint(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            g.setTransform(transform);
            if (antialias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setStroke(min_value);
            g.setColor(Color.BLACK);
            for(Shape line : model) {
                g.draw(line);
            }
            for (Drawable drawable: model.drawables) {
                drawable.drawBoundary(g);
            }
            for (Drawable drawable: model.drawables) {
                drawable.draw(g);
            }



            /*
			//Prints out the current center coordinates
			Point2D center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
			try {
				System.out.println("Center: " + transform.inverseTransform(center, null));
			} catch (NoninvertibleTransformException e) {} */
        }
    }
}
