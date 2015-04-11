package View;

import Controller.SearchResultMouseHandler;
import MapFeatures.Bounds;
import MapFeatures.Coastline;
import MapFeatures.Highway;
import Model.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    private Model model;
    private Canvas canvas;
    private AffineTransform transform;
    private MapFeature nearestNeighbor;
    private CanvasBounds bounds;
    private boolean antialias = true;
    private Point dragEndScreen, dragStartScreen;
    private int zoomLevel;
    private int zoomFactor;
    private Scalebar scalebar;
    private int checkOut = 1, checkIn = 0;
    private JTextField searchArea;
    private JButton searchButton, zoomInButton, zoomOutButton, loadButton, fullscreenButton, showRoutePanelButton, optionsButton, mapTypeButton;
    private MapMenu mapMenu;
    private RouteView routePanel = new RouteView();
    private MapTypePanel mapTypePanel = new MapTypePanel(this);
   // private IconPanel iconPanel = new IconPanel();
    private SearchResultMouseHandler searchResultMH;
    private JScrollPane resultPane = new JScrollPane();
    private JList<Address> addressSearchResults;

    private List<Path2D> currentStreetLocations;
    private Point2D currentAddressLocation;
    private Path2D currentBoundaryLocation;

    private boolean isFullscreen = false;
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
        searchResultMH = new SearchResultMouseHandler(this, model);

        /*Two helper functions to set up the AfflineTransform object and
        make the buttons and layout for the frame*/
        setScale();
        makeGUI();
        adjustZoomFactor();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //This sets up a listener for when the frame is re-sized.
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                //Re-position the buttons.
                zoomOutButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2 + 39, 30, 35);
                fullscreenButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2 + 100, 30, 35);
                zoomInButton.setBounds(getWidth() - 45, getHeight() - getHeight() / 3 * 2, 30, 35);
                //mapMenu.setBounds(getWidth() - 300, getHeight() - getHeight() / 3 * 2 - 50, 130, 30);
                mapTypePanel.setBounds(getWidth()-330, getHeight() - getHeight() / 3 * 2 - 45, 280, 200);
                loadButton.setBounds(getWidth() - 65, getHeight() - 65, 40, 20);
                optionsButton.setBounds(getWidth() - 60, getHeight() - (int) (getHeight() * 0.98), 39, 37);
                mapTypeButton.setBounds(getWidth() - 49, getHeight() - getHeight() / 3 * 2 - 45, 39, 37);

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
        zoomLevel = ZoomCalculator.calculateZoom(scale);
        scale = ZoomCalculator.setScale(zoomLevel);
        transform.scale(scale, -scale);
        transform.translate(-model.getBbox().getMinX(), -model.getBbox().getMaxY());

        bounds = new CanvasBounds(getBounds(), transform);
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
        searchArea.setForeground(Color.GRAY);
        searchArea.setText(promptText);
        //Create a FocusListener for the textField
        searchArea.addFocusListener(new FocusListener() {
            @Override
            /**
             * If selected remove prompt text
             */
            public void focusGained(FocusEvent e) {
                if (searchArea.getText().equals(promptText)) {
                    searchArea.setForeground(Color.BLACK);
                    searchArea.setText("");
                }
            }

            @Override
            /**
             * if unselected and search field is empty sets up promptText.
             */
            public void focusLost(FocusEvent e) {
                if (searchArea.getText().isEmpty()) {
                    searchArea.setForeground(Color.GRAY);
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
        layer.add(showRoutePanelButton, new Integer(2));
        layer.add(routePanel, new Integer(2));
        layer.add(optionsButton, new Integer(2));
        layer.add(mapTypeButton, new Integer(2));
        layer.add(mapTypePanel, new Integer(2));
        layer.add(resultPane, new Integer(3));
      //  layer.add(iconPanel, new Integer(2));

    }

    private void makeComponents() {
        Font font = new Font("Open Sans", Font.PLAIN, 14);

        //Create The buttons and configure their visual design.
        searchArea.setFont(font);
        searchArea.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(4, 7, 4, 7, DrawAttribute.lightblue), BorderFactory.createRaisedBevelBorder()));
        searchArea.setBounds(20, 20, 300, 37);
        searchArea.setActionCommand("searchAreaInput");

        makeOptionsButton();

        makeSearchButton();
        makeZoomInButton();
        makeZoomOutButton();
        makeLoadButton();
        makeShowRoutePanelButton();
        makeFullscreenButton();
        makeMapTypeButton();
        //makeResultPane();
    }

    public void changeToStandard(){
       drawAttributeManager.toggleStandardView();
        canvas.repaint();
    }

    public void changeToColorblind(){
        drawAttributeManager.toggleColorblindView();
        canvas.repaint();
    }

    public void changeToTransport(){
        drawAttributeManager.toggleTransportView();
        canvas.repaint();
    }

    private void makeResultPane(){
        resultPane = new JScrollPane();

       //resultPane.setBounds(26,52,286,200);

    }

    public void addToResultPane(Address[] resultArray){
        addressSearchResults = new JList<>(resultArray);
        resultPane.setVisible(true);
        resultPane.setViewportView(addressSearchResults);
        resultPane.setBounds(26, 52, 286, 100);
        resultPane.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        resultPane.getViewport().setBackground(Color.WHITE);
        resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        resultPane.getViewport().getView().addMouseListener(searchResultMH);
    }

    private void makeMapTypeButton(){
        Dimension preferred = getPreferredSize();
        mapTypeButton = new JButton();
        mapTypeButton.setIcon(new ImageIcon(MapIcon.layerIcon));
        mapTypeButton.setFocusable(false);
        mapTypeButton.setOpaque(false);
        mapTypeButton.setBackground(new Color(0, 0, 0, 180));
        mapTypeButton.setBorderPainted(false);
        mapTypeButton.setRolloverEnabled(false);
        mapTypeButton.setActionCommand("mapType");
        mapTypeButton.setBounds((int) preferred.getWidth() - 49, (int) (preferred.getHeight() - preferred.getHeight() / 3 * 2 - 45), 39, 37);
    }

    private void makeOptionsButton(){
        Dimension preferred = getPreferredSize();
        optionsButton = new JButton();
        optionsButton.setFocusable(false);
        optionsButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight() * 0.98), 39, 37);
        optionsButton.setIcon(new ImageIcon(MapIcon.optionsIcon));
        optionsButton.setOpaque(false);
        optionsButton.setBackground(DrawAttribute.fadeblack);
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
        fullscreenButton.setBackground(DrawAttribute.fadeblack);
        fullscreenButton.setBorderPainted(false);
        fullscreenButton.setRolloverEnabled(false);
        fullscreenButton.setBounds((int) preferred.getWidth() - 60, (int) (preferred.getHeight() - preferred.getHeight() / 3 * 2 + 100), 39, 37);
    }

    private void makeZoomOutButton() {
        Dimension preferred = getPreferredSize();
        zoomOutButton = new JButton();
        zoomOutButton.setBackground(Color.BLACK);
        zoomOutButton.setIcon(new ImageIcon(MapIcon.minusIcon));
        zoomOutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomOutButton.setFocusable(false);
        zoomOutButton.setOpaque(false);
        zoomOutButton.setBackground(DrawAttribute.fadeblack);
        zoomOutButton.setBorderPainted(false);
        zoomOutButton.setRolloverEnabled(false);
        zoomOutButton.setActionCommand("zoomOut");
        zoomOutButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight() / 3 * 2 + 45), 39, 37);
    }

    private void makeZoomInButton() {
        Dimension preferred = getPreferredSize();
        zoomInButton = new JButton();
        zoomInButton.setBackground(Color.BLACK);
        zoomInButton.setIcon(new ImageIcon(MapIcon.plusIcon));
        zoomInButton.setBorder(BorderFactory.createRaisedBevelBorder()); //Temp border
        zoomInButton.setFocusable(false);
        zoomInButton.setOpaque(false);
        zoomInButton.setBackground(DrawAttribute.fadeblack);
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

    public void showMapTypePanel(){
        mapTypePanel.showMapTypePanel();
        canvas.repaint();
    }

    public void searchResultChosen(double lon, double lat){

        Point2D sourcePoint = new Point2D.Double(lon,lat);
        Point2D destinationPoint = new Point2D.Double();
        transform.transform(sourcePoint,destinationPoint);
        Point2D northWestSource = new Point2D.Double(destinationPoint.getX()-300, destinationPoint.getY()-300);
        Point2D southEastSource = new Point2D.Double(destinationPoint.getX()+300, destinationPoint.getY()+300);
        Point2D northWest = new Point2D.Double();
        Point2D southEast = new Point2D.Double();

        try {
            transform.inverseTransform(northWestSource, northWest);
            transform.inverseTransform(southEastSource, southEast);
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(northWest);
        System.out.println(southEast);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleWidth = southEast.getX()-northWest.getX();
        double scaleHeight = southEast.getY()- northWest.getY();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        double xscale = width / scaleWidth;
        double yscale = height / scaleHeight;
        double scale = max(xscale, yscale);
        transform.setToScale(scale, scale);
        transform.setToTranslation(-northWest.getX(), -southEast.getY());
    }

    public void setCurrentStreet(List<Path2D> streetLocation){
        currentAddressLocation = null;
        currentBoundaryLocation = null;
        currentStreetLocations = streetLocation;
        canvas.repaint();
    }

    public void setCurrentAddress(Point2D addrLocation){
        currentStreetLocations = null;
        currentBoundaryLocation = null;
        currentAddressLocation = addrLocation;
        canvas.repaint();
    }

    public void setCurrentBoundaryLocation(Path2D boundaryLocation){
        currentAddressLocation = null;
        currentStreetLocations = null;
        currentBoundaryLocation = boundaryLocation;
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
        }System.out.println("level " +zoomLevel);
        System.out.println("factor " + zoomFactor);
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
        resultPane.setVisible(false);
        canvas.requestFocusInWindow();
    }

    private void checkForZoomIn(){
        if(checkIn == 1){
            zoomLevel++;
            adjustZoomFactor();
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
            adjustZoomFactor();
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
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(JFrame.NORMAL);
        }
        isFullscreen = !isFullscreen;
    }

    public void findNearest(Point position){
        if(zoomLevel < 11) return;
        //Rectangle2D rec = new Rectangle2D.Double(position.getX(), position.getY(),0,0);
        ArrayList<MapData> node = model.getVisibleStreets(bounds.getBounds());

        MapFeature champion = null;
        Line2D championLine = null;

        for (MapData mp : node) {
            if (mp instanceof Highway) {
                MapFeature highway = (MapFeature) mp;
                double[] points = new double[6];
                PathIterator pI = highway.getShape().getPathIterator(transform);
                pI.currentSegment(points);
                Point2D p1 = new Point2D.Double(points[0], points[1]);
                pI.next();
                while(!pI.isDone()) {
                    pI.currentSegment(points);
                    Point2D p2 = new Point2D.Double(points[0], points[1]);

                    Line2D path = new Line2D.Double(p1,p2);
                    p1 = p2;
                    pI.next();
                    if(championLine == null) {
                        championLine = path;
                        champion = highway;
                    }
                    else if(path.ptSegDist(position) < championLine.ptSegDist(position)){
                        champion = highway;
                        championLine = path;

                    }
                }
            }
        }
        nearestNeighbor = champion;

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

    public void adjustZoomFactor(){
        switch(zoomLevel){
            case 0:zoomFactor = 39; break;
            case 1:zoomFactor = 37; break;
            case 2:zoomFactor = 35; break;
            case 3:zoomFactor = 33; break;
            case 4:zoomFactor = 31; break;
            case 5:zoomFactor = 29; break;
            case 6:zoomFactor = 27; break;
            case 7:zoomFactor = 25; break;
            case 8:zoomFactor = 23; break;
            case 9:zoomFactor = 21; break;
            case 10:zoomFactor = 20; break;
            case 11:zoomFactor = 18; break;
            case 12:zoomFactor = 16; break;
            case 13:zoomFactor = 14; break;
            case 14:zoomFactor = 12; break;
            case 15:zoomFactor = 10; break;
            case 16:zoomFactor = 8; break;
            case 17:zoomFactor = 6; break;
            case 18:zoomFactor = 4; break;
            case 19:zoomFactor = 2; break;
            case 20:zoomFactor = 0; break;
        }
    }

    /**
     * The canvas object is where our map of paths and images (points) will be drawn on
     */
    class Canvas extends JComponent {
        public static final long serialVersionUID = 4;
        Stroke min_value = new BasicStroke(Float.MIN_VALUE);
        private ArrayList<MapFeature> mapFeatures = new ArrayList<>();
        private ArrayList<MapIcon> mapIcons = new ArrayList<>();

        @Override
        public void paint(Graphics _g) {
            Graphics2D g = (Graphics2D) _g;
            getData();

            //Set the Transform for Graphic2D element before drawing.
            g.setTransform(transform);
            if (antialias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



            g.setStroke(min_value); //Just for good measure.

            Bounds box = PathCreater.createBounds(model.getBbox());
            DrawAttribute drawBox = drawAttributeManager.getDrawAttribute(box.getValueName());
            g.setColor(drawBox.getColor());
            g.fill(box.getShape());

            for (MapFeature coastLine : OSMHandler.getCoastlines()) {
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(coastLine.getValueName());
                g.setColor(drawAttribute.getColor());
                g.fill(coastLine.getShape());
            }


            if(zoomLevel > 12)
                model.sortLayers(mapFeatures);

            g.setColor(Color.BLACK);


            //Draw areas first


            for (MapFeature mapFeature : mapFeatures) {
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                if (zoomLevel >= drawAttribute.getZoomLevel()) {
                    if (mapFeature.isArea()) {
                        g.setColor(drawAttribute.getColor());
                        g.fill(mapFeature.getShape());
                    }
                }
            }
            //Then draw boundaries on top of areas
           /* for (MapFeature mapFeature : mapFeatures) {
                if (zoomLevel > 14) {
                    try {
                        g.setColor(Color.BLACK);
                        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                        if (drawAttribute.isDashed()) continue;
                        else if (!mapFeature.isArea())
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                        else g.setStroke(DrawAttribute.basicStrokes[0]);
                        g.draw(mapFeature.getShape());
                    } catch (NullPointerException e) {
                        System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                    }
                }
            }*/

            //Then draw boundaries on top of areas
            for (MapFeature mapFeature : mapFeatures) {
                if (zoomLevel > 14) {
                    try {
                        g.setColor(Color.BLACK);
                        DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                        if (drawAttribute.isDashed()) continue;
                        else if (!mapFeature.isArea())
                            if (mapFeature instanceof Highway) {
                                g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor + 1]);
                            } else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                        else g.setStroke(DrawAttribute.basicStrokes[0]);
                        g.draw(mapFeature.getShape());
                    } catch (NullPointerException e) {
                        System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                    }
                }
            }


            //Draw the fillers on top of boundaries and areas
            for (MapFeature mapFeature : mapFeatures) {
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                if (zoomLevel >= drawAttribute.getZoomLevel()) {
                    g.setColor(drawAttribute.getColor());
                    if (drawAttribute.isDashed())
                        g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
                    else {
                        if (mapFeature instanceof Highway) {
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor]);
                        } else {
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                        }
                    }
                    g.draw(mapFeature.getShape());
                }
            }


            //Draws the icons.

            if (zoomLevel >= 15) {
                for (MapIcon mapIcon : mapIcons) {
                    mapIcon.draw(g, transform);
                }
            }
            g.draw(bounds.getBounds());

            scalebar = new Scalebar(g, zoomLevel, View.this, transform);

            paintNeighbor(g);

            //Draws chosen searchResult (either street or address)
            //Current address:
            if(currentAddressLocation != null){
                MapIcon currentAddrTag = new MapIcon(currentAddressLocation,MapIcon.chosenAddressIcon);
                currentAddrTag.draw(g,transform);
            }
            //Current street:
            if(currentStreetLocations != null){
                for(Path2D streetLocation : currentStreetLocations) {
                    g.setStroke(new BasicStroke(0.000035f)); //TODO: Varying stroke and color according to drawattribute
                    g.setColor(DrawAttribute.cl_red);
                    g.draw(streetLocation);
                }
            }

            //Transparent GUI elements
            g.setTransform(new AffineTransform());
            g.setColor(DrawAttribute.fadeblack);
            RoundRectangle2D optionsButtonArea = new RoundRectangle2D.Double(getContentPane().getWidth()-50,(int)(getContentPane().getHeight()-getContentPane().getHeight()*0.98),60,40,15,15);
            RoundRectangle2D zoomInOutArea = new RoundRectangle2D.Double(getContentPane().getWidth() - 30, getContentPane().getHeight() - getContentPane().getHeight() / 3 * 2+10, 60, 80, 15, 15);
            RoundRectangle2D fullscreenArea = new RoundRectangle2D.Double(getContentPane().getWidth()-30, getContentPane().getHeight() - getContentPane().getHeight() / 3 * 2+110,60,38,15,15);
            RoundRectangle2D mapTypeButtonArea = new RoundRectangle2D.Double(getContentPane().getWidth() - 30, getContentPane().getHeight() - getContentPane().getHeight() / 3 * 2 - 33, 39, 37,15,15);
            g.fill(optionsButtonArea);
            g.fill(zoomInOutArea);
            g.fill(fullscreenArea);
            g.fill(mapTypeButtonArea);

        }

        private void getData(){
            mapFeatures = new ArrayList<>();
            mapIcons = new ArrayList<>();

            bounds.updateBounds(getVisibleRect());
            Rectangle2D windowBounds = bounds.getBounds();


            ArrayList<MapData> streets = model.getVisibleStreets(windowBounds);
            mapFeatures = (ArrayList<MapFeature>)(List<?>) streets;

            if(zoomLevel > 8){
                mapFeatures.addAll((ArrayList<MapFeature>)(List<?>)model.getVisibleNatural(windowBounds));

            }

            if(zoomLevel >= 13){
                mapFeatures.addAll((ArrayList<MapFeature>)(List<?>) model.getVisibleBuildings(windowBounds));
            }

            if(zoomLevel >= 15){
                mapIcons = (ArrayList<MapIcon>) (List<?>) model.getVisibleIcons(windowBounds);
            }

        }

        private void paintNeighbor(Graphics2D g){
            if(nearestNeighbor != null) {

                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(nearestNeighbor.getValueName());
                g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor]);
                g.setColor(Color.CYAN);
                g.draw(nearestNeighbor.getShape());
            }
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

    public JButton getShowRoutePanelButton() { return showRoutePanelButton;}

    public JButton getLoadButton(){ return loadButton;}

    public JButton getOptionsButton(){ return optionsButton;}

    public JButton getMapTypeButton() { return mapTypeButton;}

    public JScrollPane getResultPane() { return resultPane; }

    public JList<Address> getAddressSearchResults() { return addressSearchResults; }
}
