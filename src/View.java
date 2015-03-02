import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    Model model;
    Canvas canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean antialias = true;
    private Point dragEndScreen, dragStartScreen;
    protected double zoomLevel;
    private JButton button;

    /**
     * Creates the window of our application.
     *
     * @param m Reference to Model class
     */
    public View(Model m) {
        super("This is our map");
        model = m;

        setScale();
        makeGUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();


    }

    /**
     * Sets the scale for the afflineTransform object using to bounds from the osm file
     * Also sets up the frame size from screenSize
     */
    private void setScale(){
        // bbox.width * xscale * .56 = 512
        // bbox.height * yscale = 512
        double xscale = 800 / .56 / model.bbox.getWidth();
        double yscale = 800 / model.bbox.getHeight();
        double scale = max(xscale, yscale);
        transform.scale(.56*scale,-scale);
        transform.translate(-model.bbox.getMinX(), -model.bbox.getMaxY());
        model.addObserver(this);

        //Set up the JFrame using the monitors resolution.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setSize((int) width, (int) height);
        setPreferredSize(new Dimension(800, 800));
    }

    /**
     * Makes use of different layers to put JComponent on top
     * of the canvas.
     */
    private void makeGUI(){
        //retrieve the LayeredPane stored in the frame.
        JLayeredPane layer = getLayeredPane();
        //Create the canvas and Components for GUI.
        canvas = new Canvas();
        canvas.setBounds(0,0,getWidth(),getHeight());
        button = new JButton("hey");
        button.setFocusable(false);
        button.setBounds(20,20,200,50);

        //Add them to their layers.
        layer.add(canvas, new Integer(1));
        layer.add(button, new Integer(2));




    }
    @Override
    public void update(Observable obs, Object obj) {
        canvas.repaint();
    }

    /**
     * The function of this method is to scale the view of the canvas by a factor given.
     * then pans the view to remove the moving towards 0,0 coord.
     * @param factor double the factor zooming
     */
    public void zoom(double factor) {
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(getWidth() * (1-factor) / 2, getHeight() * (1-factor) / 2);
    }

    /**
     * Creates the inverse transformation of a point given.
     * It simply transforms a device space coordinate back
     * to user space coordinates.
     * @param p1 Point2D mouse position
     * @return Point2D The point after inverse of the scale.
     * @throws NoninvertibleTransformException
     */
    private Point2D.Float transformPoint(Point p1) throws NoninvertibleTransformException {
        Point2D.Float p2 = new Point2D.Float();
        transform.inverseTransform(p1, p2); // create a destination p2 from the Point p1
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
                zoomLevel -= 0.0765; //Decrease zoomLevel
                repaint();

            } else {
                Point2D p1 = transformPoint(p);
                transform.scale(1.2, 1.2);
                Point2D p2 = transformPoint(p);
                transform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY()); //Pan towards mouse
                zoomLevel += 0.0765; //increase zoomLevel
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

    /**
     * Making that canvas look crisp and then back to shit.
     */
    public void toggleAA() {
        antialias = !antialias;
        repaint();
    }

    class Canvas extends JComponent {
        public static final long serialVersionUID = 4;
        Stroke min_value = new BasicStroke(Float.MIN_VALUE);

        @Override
        public void paint(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            //Set the Transform for Graphic2D element before drawing.
            g.setTransform(transform);
            if (antialias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setStroke(min_value); //Just for good measure.
            g.setColor(Color.BLACK);
            //Drawing everything not categorized as a area or line object.
            for (Shape line : model) {
                g.draw(line);
            }
            //Draw EVERYTHING
            for (Drawable drawable : model.drawables) {
                if (zoomLevel > -0.4)
                    drawable.drawBoundary(g);           //TODO Does this even have to be called from here?
            }
            for (Drawable drawable : model.drawables) {
                if (drawable.drawLevel < zoomLevel)
                    drawable.draw(g);
            }
            if (zoomLevel > 0.0) {
                for (Icon icon : model.getIcons()) {
                    icon.draw(g, transform);
                }


                // }

                //AMALIE
            /*Iterator it = model.getStreetMap().entrySet().iterator();
            while (it.hasNext()) {
                int count1 = 0;
                Map.Entry pair = (Map.Entry) it.next();
                java.util.List<Shape> list = (java.util.List<Shape>) pair.getValue();
                String streetName = (String) pair.getKey();
                //g.setStroke(txSt);
                TextDraw txtDr = new TextDraw();
                System.out.println(streetName);
                Path2D.Double street1 = new Path2D.Double();
                g.setColor(Color.BLACK);
                for (Shape street : list) {
                    //if(count == 0){
                    //	street1 =  (Path2D.Double) street;
                    //	count++;
                    //} else {
                    //	street1.append(street,true);
                    //}
                    txtDr.draw(g,new GeneralPath(street),streetName,70.);
                }
            }*/


            /*
			//Prints out the current center coordinates
			Point2D center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
			try {
				System.out.println("Center: " + transform.inverseTransform(center, null));
			} catch (NoninvertibleTransformException e) {} */
            }
        }
    }

}
