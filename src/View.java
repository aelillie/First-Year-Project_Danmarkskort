import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    Model model;
    Canvas canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean antialias = true;
    private Point dragEndScreen, dragStartScreen;
    private double zoomLevel;


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
        zoomLevel = 1 / (model.bbox.getWidth() * 10); //TODO more precise way to do this??
    }

    @Override
    public void update(Observable obs, Object obj) {
        canvas.repaint();
    }

    public void zoom(double factor) {
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(getWidth() * (1-factor) / 2, getHeight() * (1-factor) / 2);
    }
    private Point2D.Float transformPoint(Point p1) throws NoninvertibleTransformException {
        AffineTransform inverse = transform.createInverse();
        Point2D.Float p2 = new Point2D.Float();
        inverse.transform(p1, p2); // create a destination p2 from the Point p1
        return p2;
    }

    /**
     * Zooms in on the canvas with the mouseWheel. Also translates (pans) towards
     * mouse point when zooming by using its Point2d.
     * @param e MouseWheelEvent
     */
    public void wheelZoom(MouseWheelEvent e){
        try {
            int wheelRotation = e.getWheelRotation();
            Point p = e.getPoint();
            if (wheelRotation > 0) {
                //point2d before zoom
                Point2D p1 = transformPoint(p);
                transform.scale(1 / 1.2, 1 / 1.2);
                //point after zoom
                Point2D p2 = transformPoint(p);
                transform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY()); //Pan towards mouse
                zoomLevel -= 0.2; //Decrease zoomLevel
                repaint();

            } else {
                Point2D p1 = transformPoint(p);
                transform.scale(1.2, 1.2);
                Point2D p2 = transformPoint(p);
                transform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY()); //Pan towards mouse
                zoomLevel += 0.2; //increase zoomLevel
                repaint();

            }
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Sets the point of where the mouse was dragged from
     *
     * @param e MouseEvent
     */
    public void mousePressed(MouseEvent e){
        dragStartScreen = e.getPoint();
        dragEndScreen = null;
    }

    /**
     * Moves the screen to where the mouse was dragged, using the transforms translate method with the
     * the difference dragged by the mouse.
     * @param e MouseEvent
     */
    public void mouseDragged(MouseEvent e){
        try {
            dragEndScreen = e.getPoint();
            //Create a point2d.float with the
            Point2D.Float dragStart = transformPoint(dragStartScreen);
            Point2D.Float dragEnd = transformPoint(dragEndScreen);
            //calculate how far the screen is dragged from its start position.
            double dx = dragEnd.getX() - dragStart.getX();
            double dy = dragEnd.getY() - dragStart.getY();
            transform.translate(dx, dy);
            dragStartScreen = dragEndScreen;
            dragEndScreen = null;
            repaint();
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

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
            if(zoomLevel > 3.6) {
                try {

                    BufferedImage img = ImageIO.read(new File("parkingIcon.jpg"));
                    for (Shape p : model.getIcons()) {
                        double centerX = p.getBounds2D().getCenterX();
                        double centerY = p.getBounds2D().getCenterY();

                        AffineTransform it = AffineTransform.getTranslateInstance(centerX, centerY);
                        it.scale((1 / transform.getScaleX()), (1 / transform.getScaleY()));
                        g.drawImage(img, it, null);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
