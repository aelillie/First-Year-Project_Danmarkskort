package View;

import Controller.MapMenuController;
import Model.MapFeature;
import Model.MapIcon;
import Model.Model;
import Model.*;
import javafx.scene.transform.NonInvertibleTransformException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    private Model model;
    private Canvas canvas;
    private AffineTransform transform;

    public boolean isAntialias() {
        return antialias;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    private boolean antialias = true;
    private Point dragEndScreen, dragStartScreen;
    private int zoomLevel;
    private Scalebar scalebar;
    private int checkOut = 1, checkIn = 0;
    private JTextField searchArea;
    private JButton searchButton, zoomInButton, zoomOutButton, loadButton, fullscreenButton, showRoutePanelButton, optionsButton;
    private MapMenu mapMenu;
    private RouteView routePanel = new RouteView();
    private IconPanel iconPanel = new IconPanel();
    private boolean isFullscreen = false;
    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();
    private String promptText = "Enter Address";
    private final JFileChooser fileChooser = new JFileChooser("data"); //sets the initial directory to data



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
                zoomOutButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2+39, 30, 35);
                fullscreenButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2+100, 30, 35);
                zoomInButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2, 30, 35);
                mapMenu.setBounds(getWidth() - 150, getHeight() - getHeight() / 3 * 2 - 50, 130, 30);
                loadButton.setBounds(getWidth()-65, getHeight()-65,40,20);
                optionsButton.setBounds((int) getWidth() - 60, (int) getHeight() - (int) (getHeight()*0.98), 39, 37);
                repaint();
            }
        });

        pack();
        canvas.requestFocusInWindow();
        model.addObserver(this);
       
    }



    /**
     * Sets the scale for the afflineTransform object using to bounds from the osm file
     * Also sets up the frame size from screenSize
     */
    public void setScale() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scaleAffine();
        Scalebar.putZoomLevelDistances();

        //Set up the JFrame using the monitors resolution.
        setSize(screenSize); //screenSize
        setPreferredSize(new Dimension(800, 600)); //screenSize
        setExtendedState(Frame.NORMAL); //Frame.MAXIMIZED_BOTH
    }

    public void scaleAffine(){
        //Get the monitors size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        transform = new AffineTransform();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        //Set up the scale amount for our Afflinetransform
        double xscale = width / model.getBbox().getWidth();
        double yscale = height / model.getBbox().getHeight();
        double scale = max(xscale, yscale);
        zoomLevel = Scaler.calculateZoom(scale);
        scale = Scaler.setScale(zoomLevel);
        transform.scale(scale, -scale);
        transform.translate(-model.getBbox().getMinX(), -model.getBbox().getMaxY());
        
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
        layer.add(optionsButton, new Integer(2));
      //  layer.add(iconPanel, new Integer(2));
        layer.add(iconPanel, new Integer(2));

    }

    private void makeComponents() {
        Font font = new Font("Arial", Font.PLAIN, 16);

        //Create The buttons and configure their visual design.
        searchArea.setFont(font);
        searchArea.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(4, 7, 4, 7, DrawAttribute.lightblue), BorderFactory.createRaisedBevelBorder()));
        searchArea.setBounds(20,20,300,37);

        makeOptionsButton();

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

    private void makeOptionsButton(){
        Dimension preferred = getPreferredSize();
        optionsButton = new JButton();
        optionsButton.setFocusable(false);
        optionsButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight()*0.98), 39, 37);
        optionsButton.setIcon(new ImageIcon(MapIcon.optionsIcon));
        optionsButton.setOpaque(false);
        optionsButton.setBackground(new Color(0,0,0,180));
        optionsButton.setBorderPainted(false);
        optionsButton.setRolloverEnabled(false);
        optionsButton.setActionCommand("showOptions");
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
        fullscreenButton.setBackground(Color.BLACK);
        fullscreenButton.setIcon(new ImageIcon(MapIcon.fullscreenIcon));
        fullscreenButton.setBorder(BorderFactory.createRaisedBevelBorder());
        fullscreenButton.setFocusable(false);
        fullscreenButton.setOpaque(false);
        fullscreenButton.setActionCommand("fullscreen");
        fullscreenButton.setBackground(new Color(0,0,0,180));
        fullscreenButton.setBorderPainted(false);
        fullscreenButton.setRolloverEnabled(false);
        fullscreenButton.setBounds((int)preferred.getWidth() - 60,(int) (preferred.getHeight() - preferred.getHeight() / 3 * 2+100), 39, 37);
    }

    private void makeZoomOutButton() {
        Dimension preferred = getPreferredSize();
        zoomOutButton = new JButton();
        zoomOutButton.setBackground(Color.BLACK);
        zoomOutButton.setIcon(new ImageIcon(MapIcon.minusIcon));
        zoomOutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomOutButton.setFocusable(false);
        zoomOutButton.setOpaque(false);
        zoomOutButton.setBackground(new Color(0,0,0,180));
        zoomOutButton.setBorderPainted(false);
        zoomOutButton.setRolloverEnabled(false);
        zoomOutButton.setActionCommand("zoomOut");
        zoomOutButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight() / 3 * 2+45), 39, 37);
    }

    private void makeZoomInButton() {
        Dimension preferred = getPreferredSize();
        zoomInButton = new JButton();
        zoomInButton.setBackground(Color.BLACK);
        zoomInButton.setIcon(new ImageIcon(MapIcon.plusIcon));
        zoomInButton.setBorder(BorderFactory.createRaisedBevelBorder()); //Temp border
        zoomInButton.setFocusable(false);
        zoomInButton.setOpaque(false);
        zoomInButton.setBackground(new Color(0,0,0,180));
        zoomInButton.setBorderPainted(false);
        zoomInButton.setRolloverEnabled(false);
        zoomInButton.setActionCommand("zoomIn");
        zoomInButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37);
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
        loadButton.setBounds((int) preferred.getWidth() - 65, (int) preferred.getHeight() - 65, 40, 20);
    }

    private void makeSearchButton() {

        searchButton = new JButton();
        searchButton.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 4, 7, DrawAttribute.lightblue),
                BorderFactory.createRaisedBevelBorder()));
        searchButton.setBackground(new Color(36, 45, 50));
        searchButton.setIcon(new ImageIcon(MapIcon.searchIcon));
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
        //Scale the graphic and pan accordingly
        if(factor>1 && zoomLevel!=20) {
            transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
            pan(getWidth() * (1 - factor) / 2, getHeight() * (1 - factor) / 2);
            checkForZoomIn();
        }else if(zoomLevel!=0 && factor<1){
            transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
            pan(getWidth() * (1 - factor) / 2, getHeight() * (1 - factor) / 2);
            checkForZoomOut();
        }System.out.println(zoomLevel);
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
            if (wheelRotation > 0 && zoomLevel != 0) {
                //point2d before zoom
                Point2D p1 = transformPoint(p);
                transform.scale(1 / 1.2, 1 / 1.2);
                //point after zoom
                Point2D p2 = transformPoint(p);
                transform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY()); //Pan towards mouse
                checkForZoomOut();
                repaint();

            } else if (wheelRotation < 0 && zoomLevel != 20) {
                Point2D p1 = transformPoint(p);
                transform.scale(1.2, 1.2);
                Point2D p2 = transformPoint(p);
                transform.translate(p2.getX() - p1.getX(), p2.getY() - p1.getY()); //Pan towards mouse
                checkForZoomIn();
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

    private void checkForZoomIn(){
        if(checkIn == 1){
            zoomLevel++;
            checkOut = 1;
            checkIn = 0;
        } else{
            checkOut = 0;
            checkIn = 1;
        }

    }

    private void checkForZoomOut(){
        if(checkOut == 1){
            zoomLevel--;
            checkOut = 0;
            checkIn = 1;
        } else{
            checkOut = 1;
            checkIn = 0;
        }
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

    public void setAntialias(boolean antialias){
        this.antialias = antialias;
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
        //scaleAffine();
    }

    /**
     * Opens the filechooser and returns a value.
     * @return A value representing the action taken within the filechooser
     */
    public int openFileChooser(){
        int returnVal = fileChooser.showOpenDialog(getCanvas()); //Parent component as parameter - affects position of dialog
        FileNameExtensionFilter filter =  new FileNameExtensionFilter("ZIP & OSM & BIN", "osm", "zip", "bin","OSM","ZIP","BIN"); //The allowed files in the filechooser
        fileChooser.setFileFilter(filter); //sets the above filter
        return returnVal;
        //TODO: When in fullscreen and opening the dialog, it closes the window?!?
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


            g.setColor(DrawAttribute.whiteblue);
            g.fill(model.getBbox());
            //getContentPane().setBackground(DrawAttribute.whiteblue);
            /*//Drawing everything not categorized as a area or line object.
            for (Shape line : model) {
                g.draw(line);
            }*/

            g.setColor(Color.BLACK);


            //Draw areas first
            for(MapFeature mapFeature : model.getMapFeatures()){
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                if(zoomLevel >= drawAttribute.getZoomLevel()){ //TODO: NullerPointerException when loading "København" and changing to transport map
                    if(mapFeature.isArea()){
                        g.setColor(drawAttribute.getColor());
                        g.fill(mapFeature.getShape());
                    }
                }
            }
            //Then draw boundaries on top of areas
            for (MapFeature mapFeature : model.getMapFeatures()) {
                if (zoomLevel > 14) {
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
                if (zoomLevel >= drawAttribute.getZoomLevel()) {
                    g.setColor(drawAttribute.getColor());
                  /*  if (mapFeature.isArea()) {
                        g.fill(mapFeature.getShape());
                    } else {*/

                        if (drawAttribute.isDashed()) g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
                        else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                        g.draw(mapFeature.getShape());
               //     }
                }
            }
            //Draws the icons.

            if (zoomLevel >= 16) {
                for (MapIcon mapIcon : model.getMapIcons()) {
                    mapIcon.draw(g, transform);
                }
            }

           scalebar = new Scalebar(g,zoomLevel,View.this,transform);


            g.setTransform(new AffineTransform());
            g.setColor(new Color(0,0,0,180));
            RoundRectangle2D optionsButtonArea = new RoundRectangle2D.Double(getContentPane().getWidth()-50,(int)(getContentPane().getHeight()-getContentPane().getHeight()*0.98),60,40,15,15);
            RoundRectangle2D zoomInOutArea = new RoundRectangle2D.Double(getContentPane().getWidth() - 30, getContentPane().getHeight() - getContentPane().getHeight() / 3 * 2+10, 60, 80, 15, 15);
            RoundRectangle2D fullscreenArea = new RoundRectangle2D.Double(getContentPane().getWidth()-30, getContentPane().getHeight() - getContentPane().getHeight() / 3 * 2+110,60,38,15,15);
            g.fill(optionsButtonArea);
            g.fill(zoomInOutArea);
            g.fill(fullscreenArea);

            g.setColor(Color.BLACK);


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



    public JFileChooser getFileChooser(){ return fileChooser;}

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

    public JButton getOptionsButton(){ return optionsButton;}

}
