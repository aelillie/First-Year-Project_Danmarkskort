package View;

import Controller.MapMenuController;
import Model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.cos;
import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    private Model model;
    private Canvas canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean antialias = true;
    private Point dragEndScreen, dragStartScreen;
    private double zoomLevel;
    private JTextField searchArea;
    private JButton searchButton, zoomInButton, zoomOutButton, loadButton, fullscreenButton, showRoutePanelButton;
    private MapMenu mapMenu;
    private RouteView routePanel = new RouteView();
    private boolean isFullscreen = false;
    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();
    private String promptText = "Enter Address";
    private final JFileChooser fc = new JFileChooser("data"); //sets the initial directory to data

    /**
     * Creates the window of our application.
     *
     * @param m Reference to Model.Model class
     */
    public View(Model m) {
        super("This is our map");
        model = m;

        /*Two helper functions to set up the AfflineTransform object and
        make the buttons and layout for the frame*/
        setScale();
        makeGUI();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //This sets up a listener for when the frame is re-sized.
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                //Re-position the buttons.
                zoomOutButton.setBounds(getWidth() - 115, getHeight() - getHeight() / 3 * 2, 39, 37);
                fullscreenButton.setBounds(getWidth() - 70, getHeight() - getHeight() / 3 * 2, 39, 37);
                zoomInButton.setBounds(getWidth() - 160, getHeight() - getHeight() / 3 * 2, 39, 37);
                mapMenu.setBounds(getWidth() - 160, getHeight() - getHeight() / 3 * 2 - 50, 130, 30);
                loadButton.setBounds(getWidth()-65, getHeight()-65,40,20);
            }
        });

        pack();
        canvas.requestFocusInWindow();
        model.addObserver(this);
        zoomLevel = model.getBbox().getWidth() * -1;
    }



    /**
     * Sets the scale for the afflineTransform object using to bounds from the osm file
     * Also sets up the frame size from screenSize
     */
    private void setScale() {
        //Get the monitors size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        //Set up the scale amount for our Afflinetransform
        double xscale = width / model.getBbox().getWidth();
        double yscale = height / model.getBbox().getHeight();
        double scale = max(xscale, yscale);
        transform.scale(scale, -scale);
        transform.translate(-model.getBbox().getMinX(), -model.getBbox().getMaxY());
        zoomLevel = model.getBbox().getWidth() * -1;
        //Set up the JFrame using the monitors resolution.
        setSize(screenSize); //screenSize
        setPreferredSize(new Dimension(800, 600)); //screenSize
        setExtendedState(Frame.NORMAL); //Frame.MAXIMIZED_BOTH
    }

    /**
     * Makes use of different layers to put JComponent on top
     * of the canvas. Creates the GUI for the
     */
    private void makeGUI() {
        //retrieve the LayeredPane stored in the frame.
        JLayeredPane layer = getLayeredPane();
        //Create the canvas and Components for GUI.
        canvas = new Canvas();

        canvas.setBounds(0, 0, getWidth(), getHeight());

        searchArea = new JTextField();
        searchArea.setText(promptText);
        //Create a FocusListener for the textField
        searchArea.addFocusListener(new FocusListener() {
            @Override
            /**
             * If selected remove prompt text
             */
            public void focusGained(FocusEvent e) {
                if (searchArea.getText().equals(promptText)) {
                    searchArea.setText("");
                }
            }

            @Override
            /**
             * if unselected and search field is empty sets up promptText.
             */
            public void focusLost(FocusEvent e) {
                if (searchArea.getText().isEmpty()) {
                    searchArea.setText(promptText);
                }
            }

        });


        //Make the components for the frame.
        makeComponents();


        layer.add(canvas, new Integer(1));
        layer.add(searchArea, new Integer(2));
        layer.add(searchButton, new Integer(2));
        layer.add(zoomInButton, new Integer(2));
        layer.add(zoomOutButton, new Integer(2));
        layer.add(loadButton, new Integer(2));
        layer.add(fullscreenButton, new Integer(2));
        layer.add(mapMenu, new Integer(2));
        layer.add(showRoutePanelButton, new Integer(2));
        layer.add(routePanel, new Integer(2));

    }

    private void makeComponents() {
        Font font = new Font("Arial", Font.PLAIN, 16);

        //Create The buttons and configure their visual design.
        searchArea.setFont(font);
        searchArea.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(4, 7, 4, 7, DrawAttribute.lightblue), BorderFactory.createRaisedBevelBorder()));
        searchArea.setBounds(20,20,300,37);

        makeSearchButton();
        makeZoomInButton();
        makeZoomOutButton();
        makeLoadButton();
        makeShowRoutePanelButton();
        makeFullscreenButton();
        makeMaptypeMenu();
    }

    private void makeMaptypeMenu() {
        mapMenu = new MapMenu();
        mapMenu.addActionListener(new MapMenuController(this));
    }

    public void changeMapType(){
        String type = mapMenu.getChosen();
        if(type.equals("Standard")) drawAttributeManager.toggleStandardView();
        else if(type.equals("Colorblind map"))drawAttributeManager.toggleColorblindView();
        else if(type.equals("Transport map")) drawAttributeManager.toggleTransportView();

        canvas.repaint();
    }

    private void makeShowRoutePanelButton() {
        showRoutePanelButton = new JButton("Route plan");
        showRoutePanelButton.setFocusable(false);
        showRoutePanelButton.setBackground(Color.WHITE);
        showRoutePanelButton.setBounds(20, 55, 100, 25);
        showRoutePanelButton.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.GRAY));
        showRoutePanelButton.setActionCommand("showRoutePanel");

    }


    private void makeFullscreenButton() {
        Dimension preferred = getPreferredSize();
        fullscreenButton = new JButton();
        fullscreenButton.setBackground(Color.WHITE);
        fullscreenButton.setIcon(new ImageIcon("data//fullscreenIcon.png"));
        fullscreenButton.setBorder(BorderFactory.createRaisedBevelBorder());
        fullscreenButton.setFocusable(false);
        fullscreenButton.setActionCommand("fullscreen");
        fullscreenButton.setBounds((int) preferred.getWidth() - 70, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37);
    }

    private void makeZoomOutButton() {
        Dimension preferred = getPreferredSize();
        zoomOutButton = new JButton();
        zoomOutButton.setBackground(Color.WHITE);
        zoomOutButton.setIcon(new ImageIcon("data//minusIcon.png"));
        zoomOutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomOutButton.setFocusable(false);
        zoomOutButton.setActionCommand("zoomOut");
        zoomOutButton.setBounds((int) preferred.getWidth() - 115, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37);
    }

    private void makeZoomInButton() {
        Dimension preferred = getPreferredSize();
        zoomInButton = new JButton();
        zoomInButton.setBackground(Color.WHITE);
        zoomInButton.setIcon(new ImageIcon("data//plusIcon.png"));
        zoomInButton.setBorder(BorderFactory.createRaisedBevelBorder()); //Temp border
        zoomInButton.setFocusable(false);
        zoomInButton.setActionCommand("zoomIn");
        zoomInButton.setBounds((int) preferred.getWidth() - 160, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37);
    }

    private void makeLoadButton(){
        Dimension preferred = getPreferredSize();
        loadButton = new JButton("LOAD");
        loadButton.setBackground(new Color(36, 45, 50));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFont(new Font("Arial", Font.BOLD, 10));
        loadButton.setBorder(BorderFactory.createRaisedBevelBorder());
        loadButton.setFocusable(false);
        loadButton.setActionCommand("load");
        loadButton.setBounds((int) preferred.getWidth()-65,(int) preferred.getHeight()-65,40,20);
    }

    private void makeSearchButton() {

        searchButton = new JButton();
        searchButton.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 4, 7, DrawAttribute.lightblue),
                BorderFactory.createRaisedBevelBorder()));
        searchButton.setBackground(new Color(36, 45, 50));
        searchButton.setIcon(new ImageIcon("data//searchIcon.png"));
        searchButton.setFocusable(false);
        searchButton.setBounds(320, 20, 43, 37);
        searchButton.setActionCommand("search");
    }

    public void showRoutePanel() {
        routePanel.showRoutePanel();
        canvas.repaint();
    }

    @Override
    public void update(Observable obs, Object obj) {
        canvas.repaint();

    }

    /**
     * The function of this method is to scale the view of the canvas by a factor given.
     * then pans the view to remove the moving towards 0,0 coord.
     *
     * @param factor Double, the factor scaling
     */
    public void zoom(double factor) {
        //Check whether we zooming in or out for adjusting the zoomLvl field
        if (factor > 1) zoomLevel += 0.0765;
        else zoomLevel -= 0.0765;
        //Scale the graphic and pan accordingly
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(getWidth() * (1 - factor) / 2, getHeight() * (1 - factor) / 2);
    }

    /**
     * Creates the inverse transformation of a point given.
     * It simply transforms a device space coordinate back
     * to user space coordinates.
     *
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
     *
     * @param e MouseWheelEvent
     */
    public void wheelZoom(MouseWheelEvent e) {
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
    public void mousePressed(MouseEvent e) {
        dragStartScreen = e.getPoint();
        dragEndScreen = null;
    }

    /**
     * Moves the screen to where the mouse was dragged, using the transforms translate method with the
     * the difference dragged by the mouse.
     *
     * @param e MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        try {
            dragEndScreen = e.getPoint();
            //Create a point2d.float with the
            Point2D.Float dragStart = transformPoint(dragStartScreen);
            Point2D.Float dragEnd = transformPoint(dragEndScreen);
            //calculate how far the screen is dragged from its start position.
            double dx = dragEnd.getX() - dragStart.getX();
            double dy = dragEnd.getY() - dragStart.getY();
            transform.translate(dx, dy);

            //remember to reposition points to avoid unstable dragging.
            dragStartScreen = dragEndScreen;
            dragEndScreen = null;
            repaint();
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Moves the canvas by a fixed amount using the Translate method.
     *
     * @param dx
     * @param dy
     */
    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        repaint();
    }

    /**
     * Making that canvas look crisp and then back to shit.
     */
    public void toggleAA() {
        antialias = !antialias;
        repaint();
    }

    /**
     * Makes the view Frame fullscreen with help of a graphic device.
     */
    public void toggleFullscreen() {
        if (!isFullscreen) {
            gd.setFullScreenWindow(this);
        } else {
            gd.setFullScreenWindow(null);
        }
        isFullscreen = !isFullscreen;
    }

    /**
     * Opens the filechooser and returns a value.
     * @return A value representing the action taken within the filechooser
     */
    public int openFileChooser(){
        int returnVal = fc.showOpenDialog(canvas); //Parent component as parameter - affects position of dialog
        return returnVal;
    }



    /**
     * The canvas object is where our map of paths and images (points) will be drawn on
     */
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


            getContentPane().setBackground(DrawAttribute.whiteblue);
            /*//Drawing everything not categorized as a area or line object.
            for (Shape line : model) {
                g.draw(line);
            }*/


            //Draw areas first
            for(MapFeature mapFeature : model.getMapFeatures()){
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                if(zoomLevel>drawAttribute.getZoomLevel()){
                    if(mapFeature.isArea()){
                        g.setColor(drawAttribute.getColor());
                        g.fill(mapFeature.getShape());
                    }
                }
            }
            //Then draw boundaries on top of areas
            for (MapFeature mapFeature : model.getMapFeatures()) {
                if (zoomLevel > -0.4) {
                    try {
                        g.setColor(Color.BLACK);
                        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                        if (drawAttribute.isDashed()) continue;
                        else if (!mapFeature.isArea())
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                        else g.setStroke(DrawAttribute.basicStrokes[0]);
                        g.draw(mapFeature.getShape());
                    }catch(NullPointerException e){
                        System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                    }
                }

            }


            //Draw the fillers on top of boundaries and areas
            for (MapFeature mapFeature : model.getMapFeatures()) {
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                if (zoomLevel > drawAttribute.getZoomLevel()) {
                    g.setColor(drawAttribute.getColor());
                  /*  if (mapFeature.isArea()) {
                        g.fill(mapFeature.getShape());
                    } else {*/
                        if (drawAttribute.isDashed())
                            g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
                        else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                        g.draw(mapFeature.getShape());
               //     }
                }
            }


            //Draws the icons.

            if (zoomLevel > 0.0) {
                for (MapIcon mapIcon : model.getMapIcons()) {
                    mapIcon.draw(g, transform);
                }
            }

            // }
/*
                //AMALIE Iterator it = model.getStreetMap().entrySet().iterator();
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
            }
*/

            /*
			//Prints out the current center coordinates
			Point2D center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
			try {
				System.out.println("Center: " + transform.inverseTransform(center, null));
			} catch (NoninvertibleTransformException e) {} */
            //}
        }
    }

    public JFileChooser getFileChooser(){ return fc;}

    public Component getCanvas() {
        return canvas;
    }

    public JTextField getSearchArea() {
        return searchArea;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getZoomInButton() {
        return zoomInButton;
    }

    public JButton getZoomOutButton() {
        return zoomOutButton;
    }

    public JButton getFullscreenButton() {
        return fullscreenButton;
    }

    public JButton getShowRoutePanelButton() {
        return showRoutePanelButton;
    }

    public JButton getLoadButton(){ return loadButton;}

}
