package View;

import Controller.SearchResultMouseHandler;
import Model.*;
import Model.MapFeatures.Highway;
import Model.MapFeatures.Route;
import Model.MapFeatures.Waterway;
import Model.Path.Edge;
import Model.QuadTree.QuadTree;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static java.lang.Math.max;

public class View extends JFrame implements Observer {
    public static final long serialVersionUID = 0;
    private Model model;
    private Canvas canvas;
    private AffineTransform transform;
    private Highway nearestNeighbor;
    private CanvasBounds bounds;
    private boolean antialias = true, showGrid = false, graph = false;
    public void toggleGraph(){
        graph = !graph;
    }
    private Point dragEndScreen, dragStartScreen;
    private int zoomLevel;
    private int zoomFactor;
    private int checkOut = 1, checkIn = 0;
    private JTextField searchArea;
    private JButton searchButton,closeDirectionListButton, zoomInButton, zoomOutButton, fullscreenButton, showRoutePanelButton, optionsButton, mapTypeButton;
    private RouteView routePanel;
    private MapTypePanel mapTypePanel = new MapTypePanel(this);
    private IconPanel iconPanel = new IconPanel();
    private OptionsPanel optionsPanel;
    private JScrollPane resultPane = new JScrollPane();
    private JScrollPane resultStartPane = new JScrollPane();
    private JScrollPane resultEndPane = new JScrollPane();
    private JScrollPane directionPane = new JScrollPane();
    private JList<Address> addressSearchResults;
    private JPanel closeDirectionList, travelTimePanel;
    private JLabel travelTimeLabel;
    private Point2D start,end;
    private Map<String,MapPointer> addressPointerMap = new HashMap<>();

    private Iterable<Edge> shortestPath;
    private Iterable<Edge> fastestPath;

    private boolean isFullscreen = false;
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();
    private String promptText = "Enter Address";
    private final JFileChooser fileChooser = new JFileChooser("data"); //sets the initial directory to data



    /**
     * Creates the window of our application.
     *
     * @param m Reference to Model class
     */
    public View(Model m) {
        super("Group G: Danmarkskort");
        model = m;
        iconPanel.addObserverToIcons(this);
        routePanel = new RouteView(this, model);
        optionsPanel = new OptionsPanel(this,model);
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
                optionsPanel.setBounds(getWidth() - 220, getHeight() - (int) (getHeight() * 0.98), 150, 80);
                optionsButton.setBounds(getWidth() - 60, getHeight() - (int) (getHeight() * 0.98), 39, 37);
                mapTypeButton.setBounds(getWidth() - 49, getHeight() - getHeight() / 3 * 2 - 45, 39, 37);
                iconPanel.setBounds(getWidth()- 214, (int)(getHeight()-getHeight()*0.98+70), 120, 180);

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

    /**
     * Sets up the AffineTransform using the bounds of the map and the screen size.
     */
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

        adjustZoomLvl(scale);
        transform.translate(-model.getBbox().getMinX(), -model.getBbox().getMaxY());

        bounds = new CanvasBounds(getBounds(), transform);
        adjustZoomFactor();
    }


    /**
     * Takes a value of amount scaled and calculates the closes zoomLevel to it and sets the affineTransform
     * to it correctly
     * @param scale - Value x and y coordinates are scaled.
     */
    public void adjustZoomLvl(double scale){
        zoomLevel = ZoomCalculator.calculateZoom(scale);
        scale = ZoomCalculator.setScale(zoomLevel);
        transform.setToScale(scale, -scale);
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
        getContentPane().setBackground(DrawAttribute.whiteblue);
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
                    resultPane.setVisible(false);
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
        layer.add(fullscreenButton, new Integer(2));
        layer.add(showRoutePanelButton, new Integer(2));
        layer.add(routePanel, new Integer(2));
        layer.add(optionsButton, new Integer(2));
        layer.add(mapTypeButton, new Integer(2));
        layer.add(mapTypePanel, new Integer(2));
        layer.add(resultPane, new Integer(3));
        layer.add(resultStartPane, new Integer(3));
        layer.add(resultEndPane, new Integer(3));
        layer.add(iconPanel, new Integer(3));
        layer.add(optionsPanel, new Integer(2));
        layer.add(directionPane, new Integer(2));
        layer.add(closeDirectionList, new Integer(3));
        layer.add(travelTimePanel, new Integer(4));
    }

    private void makeComponents() {
        Font font = new Font("Open Sans", Font.PLAIN, 14);

        //Create The buttons and configure their visual design.
        searchArea.setFont(font);
        searchArea.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(4, 7, 4, 7, DrawAttribute.lightblue), BorderFactory.createRaisedBevelBorder()));
        searchArea.setBounds(20, 20, 300, 37);
        searchArea.setActionCommand("searchAreaInput");

        makeFrontGUIButtons();
    }

    /**
     * Creates the buttons to always be displayed at the front of the GUI.
     */
    private void makeFrontGUIButtons(){
        Dimension preferred = getPreferredSize();

        optionsButton = new JButton();
        makeFrontButton(optionsButton, "optionsIcon", "showOptions", new Rectangle((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight() * 0.98), 39, 37));
        makeSearchButton();

        zoomInButton = new JButton();
        makeFrontButton(zoomInButton, "plusIcon", "zoomIn", new Rectangle((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37));

        zoomOutButton = new JButton();
        makeFrontButton(zoomOutButton, "minusIcon", "zoomOut", new Rectangle((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) (preferred.getHeight() / 3 * 2 + 45), 39, 37));

        makeShowRoutePanelButton();

        fullscreenButton = new JButton();
        makeFrontButton(fullscreenButton,"fullscreenIcon","fullscreen",new Rectangle((int) preferred.getWidth() - 60, (int) (preferred.getHeight() - preferred.getHeight() / 3 * 2 + 100), 39, 37));

        mapTypeButton = new JButton();
        makeFrontButton(mapTypeButton, "layerIcon", "mapType", new Rectangle((int) preferred.getWidth() - 49, (int) (preferred.getHeight() - preferred.getHeight() / 3 * 2 - 45), 39, 37));
        makeCloseDirectionListPanel();
    }

    /**
     * The panel above the routedirection scrollpane displaying the time and the distance.
     */
    private void makeCloseDirectionListPanel(){
        closeDirectionList = new JPanel();
        closeDirectionList.setVisible(false);
        closeDirectionList.setBounds(26, 280, 400, 20);
        closeDirectionList.setBackground(DrawAttribute.fadeblack);
        closeDirectionList.setLayout(new BoxLayout(closeDirectionList, BoxLayout.LINE_AXIS));
        closeDirectionList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        closeDirectionListButton = new JButton(new ImageIcon(this.getClass().getResource("/data/resetButtonWhiteIcon.png")));
        closeDirectionListButton.setOpaque(false);
        closeDirectionListButton.setBackground(DrawAttribute.fadeblack);
        closeDirectionListButton.setActionCommand("closeDirectionList");
        closeDirectionList.add(closeDirectionListButton);

        travelTimePanel = new JPanel();
        travelTimePanel.setOpaque(false);
        travelTimePanel.setBounds(26,280,275,20);
        travelTimePanel.setVisible(false);
        travelTimeLabel = new JLabel();
        travelTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        travelTimeLabel.setForeground(Color.WHITE);
        travelTimePanel.add(travelTimeLabel);
    }

    /**
     * Changes the configuration of how the view should draw objects.
     */
    public void changeToStandard(){
        drawAttributeManager.toggleStandardView();
        getContentPane().setBackground(DrawAttribute.whiteblue);
        canvas.repaint();
    }

    /**
     * Changes the configuration of how the view should draw objects.
     */
    public void changeToColorblind(){
        drawAttributeManager.toggleColorblindView();
        getContentPane().setBackground(DrawAttribute.cl_whiteblue);
        canvas.repaint();
    }

    /**
     * Changes the configuration of how the view should draw objects.
     */
    public void changeToTransport(){
        drawAttributeManager.toggleTransportView();
        getContentPane().setBackground(DrawAttribute.whiteblue);
        canvas.repaint();
    }

    /**
     * The method called when directions needs to be added to the scrollpanel
     * @param directionArray
     */
    private void addToDirectionPane(String[] directionArray){
        JList<String> directionStringList = new JList<>(directionArray); //Everything to be added to the scrollpane is added as a JList
        directionPane.setVisible(true);
        directionPane.setViewportView(directionStringList); //Set the display of the scrollpane to the current JList
        directionPane.setBounds(26, 300, 400, 200);
        directionPane.setBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        directionPane.getViewport().setBackground(Color.WHITE); //The viewport is where the scrollpane elements are display
        directionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        closeDirectionList.setVisible(true);
    }

    /**
     * Displays a message for the user when no address was found
     * @param bounds - bounds for the result pane
     */
    public void addNoAddressesFoundMsg(Rectangle bounds){
        String[] msgArray = new String[1];
        msgArray[0] = "No addresses found";
        resultPane.setVisible(true);
        resultPane.setBounds(bounds);
        resultPane.setViewportView(new JList<>(msgArray));
        resultPane.getViewport().setBackground(Color.WHITE);
        resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Adds the array of addresses to the scroll pane.
     * @param resultArray
     */
    public void addToResultPane(Address[] resultArray){
        addressSearchResults = new JList<>(resultArray);
        resultPane.setVisible(true);
        resultPane.setViewportView(addressSearchResults);
        resultPane.setBounds(26, 52, 286, 100);
        resultPane.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        resultPane.getViewport().setBackground(Color.WHITE);
        resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        resultPane.getViewport().getView().addMouseListener(new SearchResultMouseHandler(this, model, addressSearchResults, searchArea, resultPane, "chosenAddressIcon"));
    }

    /**
     * Adds the array of addresses to a specific scroll pane for text field.
     * @param resultsArray
     * @param textfield -
     * @param scrollPane
     * @param bounds
     * @param iconType
     */
    public void addToResultPane(Address[] resultsArray, JTextField textfield, JScrollPane scrollPane, Rectangle bounds, String iconType){
        addressSearchResults = new JList<>(resultsArray);
        scrollPane.setVisible(true);
        scrollPane.setViewportView(addressSearchResults);
        scrollPane.setBounds(bounds);
        scrollPane.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().getView().addMouseListener(new SearchResultMouseHandler(this, model, addressSearchResults, textfield, scrollPane, iconType));
    }

    /**
     * Default method for creating buttons for the front GUI.
     * @param button the button to be created
     * @param icon the string specifying the icon name
     * @param actionCommand The actioncommand to be used in the controllers.
     * @param bounds Where the button should be situated on the GUI
     */
    private void makeFrontButton(JButton button, String icon, String actionCommand, Rectangle bounds){
        button.setIcon(new ImageIcon(MapIcon.iconURLs.get(icon)));
        button.setFocusable(false);
        button.setOpaque(false);
        button.setBackground(DrawAttribute.fadeblack);
        button.setBorderPainted(false);
        button.setRolloverEnabled(false);
        button.setActionCommand(actionCommand);
        button.setBounds(bounds);
    }


    private void makeShowRoutePanelButton() {
        showRoutePanelButton = new JButton("Route plan");
        showRoutePanelButton.setFocusable(false);
        showRoutePanelButton.setBackground(Color.WHITE);
        showRoutePanelButton.setBounds(20, 55, 100, 25);
        showRoutePanelButton.setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.GRAY));
        showRoutePanelButton.setActionCommand("showRoutePanel");
    }

    /**
     * Used to add a start or end pointer to the map
     * @param mapPointer MapPointer
     */
    public void addPointer(MapPointer mapPointer){
        addressPointerMap.put(mapPointer.getType(), mapPointer);
        canvas.repaint();
    }

    /**
     * Used to remove a specific Pointer from the map.
     * @param - String what type of pointer.
     */
    public void removePointer(String iconType){
        addressPointerMap.remove(iconType);
        canvas.repaint();
    }

    /**
     * stores the position given and if both start and end is given calls for fastest route
     * between these.
     * @param p - Point
     * @throws NoninvertibleTransformException
     */
    public void setStartPoint(Point2D p)throws NoninvertibleTransformException{
        if(p == null){
            start = null;
            fastestPath = null;
            removePointer("startPointIcon");
            closeDirectionList();
            routePanel.getStartAddressField().setForeground(Color.gray);
            routePanel.getStartAddressField().setText("Enter start address");
            return;
        }
        //Recalibrate position for precision
        Insets x = getInsets();
        p.setLocation(p.getX(), p.getY() - x.top + x.bottom);

        //Transform from screen coordinates to map Values.
        start = transformPoint(p);
        MapPointer startPoint = new MapPointer(start, "startPointIcon".intern());
        addPointer(startPoint);
        if(end != null && start != null)    //check if a route should be found.
            findFastestRoute(start, end);
        repaint();
    }

    /**
     * stores the position given and if both start and end is given calls for fastest route
     * between these.
     * @param p - Point
     * @throws NoninvertibleTransformException
     */
    public void setEndPoint(Point2D p)throws NoninvertibleTransformException{
        if(p == null){      //if given null it removes the point and path.
            end = null;
            fastestPath = null;
            removePointer("endPointIcon");
            closeDirectionList();
            routePanel.getEndAddressField().setForeground(Color.gray);
            routePanel.getEndAddressField().setText("Enter end address");
            return;
        }
        Insets x = getInsets(); //Recalibrate position for precision
        p.setLocation(p.getX(), p.getY() - x.top + x.bottom);
        end = transformPoint(p);

        addPointer(new MapPointer(end, "endPointIcon".intern()));
        if(end != null && start != null)
            findFastestRoute(start, end);
        repaint();
    }


    private void makeSearchButton() {
        searchButton = new JButton();
        searchButton.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 4, 7, DrawAttribute.lightblue),
                BorderFactory.createRaisedBevelBorder()));
        searchButton.setBackground(new Color(36, 45, 50));
        searchButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("searchIcon")));
        searchButton.setFocusable(false);
        searchButton.setBounds(320, 20, 43, 37);
        searchButton.setActionCommand("search");
    }


    public void showRoutePanel() {
        routePanel.showRoutePanel();
        if(!routePanel.isVisible()) closeDirectionList();
        if(mapTypePanel.isVisible()) mapTypePanel.setVisible(false);
        if(optionsPanel.isVisible()) {
            optionsPanel.setVisible(false);
            if(iconPanel.isVisible()) iconPanel.setVisible(false);
        }
        canvas.repaint();
    }

    public void showMapTypePanel(){
        mapTypePanel.showMapTypePanel();
        if(mapTypePanel.isVisible() && optionsPanel.isVisible()){
            optionsPanel.setVisible(false);
            if(iconPanel.isVisible()) iconPanel.setVisible(false);
        }
        if(routePanel.isVisible()) routePanel.setVisible(false); closeDirectionList();
        canvas.repaint();
    }

    public void closeDirectionList(){
        closeDirectionList.setVisible(false);
        directionPane.setVisible(false);
        travelTimeLabel.setVisible(false);
    }

    public void showIconPanel(){
        iconPanel.showIconPanel();
        canvas.repaint();
    }

    public void showOptionsPanel(){
        optionsPanel.showOptionsPanel();
        if(!optionsPanel.isVisible() && iconPanel.isVisible()) iconPanel.setVisible(false);
        if(optionsPanel.isVisible()&& mapTypePanel.isVisible()) mapTypePanel.setVisible(false);
        if(routePanel.isVisible()) routePanel.setVisible(false); closeDirectionList();
        canvas.repaint();
    }

    public void searchResultChosen(double lon, double lat){
        centerOnLatLon(new Point2D.Double(lon, lat));
    }

    public void zoomOnAddress(){
        adjustZoomLvl(16000);
        adjustZoomFactor();
    }

    public void zoomOnStreet(List<Path2D> streetSeqments){
        Path2D pathConnected = new Path2D.Double();
        for(Path2D waySeg: streetSeqments){
            pathConnected.append(waySeg,false);
        }
        Rectangle2D pathRec = pathConnected.getBounds2D();

        double scaleX = getWidth()/ pathRec.getWidth();
        double scaleY = getHeight()/ pathRec.getHeight();

        adjustZoomLvl(Math.max(scaleX, scaleY) * 0.5);
        adjustZoomFactor();
    }

    //Get the center of the current size of the contentpane in lat and longtitude points
    private Point2D getCenterLatLon(){
        Point2D.Double result = new Point2D.Double();
        Point2D.Double screenCenter = new Point2D.Double();
        screenCenter.x = getWidth()/2; //contentpane width/height
        screenCenter.y = getHeight()/2;
        try{
            transform.inverseTransform(screenCenter,result); //transform to lat/lon using the current transform
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //Center map on the following map coordinates
    private void centerOnLatLon(Point2D newCenter){
        Point2D currentCenter = getCenterLatLon();
        double dx = currentCenter.getX() - newCenter.getX();
        double dy = currentCenter.getY() - newCenter.getY();
        panMapCoords(dx, dy);
    }
    //Pan map with lat/lon, translate rather than preconcatenate
    public void panMapCoords(double dx, double dy){
        transform.translate(dx, dy);
        repaint();
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
        }System.out.println("level " + zoomLevel);
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
    private Point2D.Float transformPoint(Point2D p1) throws NoninvertibleTransformException {
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
            Insets x = getInsets();
            Point p = e.getPoint();
            p.setLocation(p.getX() , p.getY()-x.top + x.bottom);
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

    /*
    Checks if the user zooms in
     */
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

    /*
    checks if the user zooms out
     */
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

    public void toggleTestMode(){
        bounds.toggleTestMode();
    }

    public void toggleGrid(){
        showGrid = !showGrid;
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

    /**
     * Finds fastest route between to points and stores it for drawing.
     * @param start - Point2D start point
     * @param end - Point2D end point
     */
    public void findFastestRoute(Point2D start, Point2D end){
        shortestPath = null;
        try {
            RouteFinder routeFinder = new RouteFinder(start, end); //Create a RouteFinder and let it handle it.
            travelMethod(routeFinder);
            routeFinder.setFastestRoute();
            fastestPath = routeFinder.getFastestPath(); //retrieve the fastest route from it
            setTravelInfo(routeFinder);
            RoutePlanner routePlanner = new RoutePlanner(fastestPath); //create a routePlanner to get direction for path
            addToDirectionPane(routePlanner.getDirections());
        }catch(IllegalArgumentException | NullPointerException e){
            addToDirectionPane(new String[]{"No fastest path between the two locations was Found"});
            setTravelInfo(null);
            fastestPath = null;

        }

        repaint();

    }

    /*
    finds shortest route between two points and stores it for drawing
     */
    public void findShortestRoute(Point2D start, Point2D end){
        fastestPath = null;
        try {
            RouteFinder routeFinder = new RouteFinder(start, end);
            travelMethod(routeFinder);
            routeFinder.setShortestRoute();
            shortestPath = routeFinder.getShortestPath();
            setTravelInfo(routeFinder);
            RoutePlanner routePlanner = new RoutePlanner(shortestPath);
            addToDirectionPane(routePlanner.getDirections());
        }catch(IllegalArgumentException  | NullPointerException e){
            addToDirectionPane(new String[]{"No Shortest Path between the two locations was Found"});
            setTravelInfo(null);
            shortestPath = null;
        }

        repaint();

    }

    private void setTravelInfo(RouteFinder routeFinder) {
        if(routeFinder == null){
            travelTimeLabel.setText("");
            return;
        }
        //calculate total distance and travel Time and add it to a label.
        double travelTime = routeFinder.getTravelTime();
        double seconds = (travelTime%1);
        double minutes = travelTime - seconds;
        String timeString;
        if(travelTime/60 >= 1){
            double hours_dec = minutes/60;
            double minutes_dec = hours_dec%1;
            double hours = hours_dec-minutes_dec;
            minutes = minutes_dec*60;
            timeString = new DecimalFormat("##").format(hours) + " hr(s) " + new DecimalFormat("##").format(minutes) + " min";

        } else {
            if (seconds <= 0.25)
                seconds = 0;
            else if (seconds >= 0.75) { //DecimalFormat will take care of this
                seconds = 0;
                minutes += 1;
            } else seconds = 0.5;

            timeString = minutes + seconds + " min";
        }
        double travelDistance = routeFinder.getTravelDistance();
        if (travelDistance < 1)  {
            travelDistance *= 1000;
            travelTimeLabel.setText(String.format("Travel time: " + timeString + "   Distance: %.0f m", travelDistance  ));
        } else
        travelTimeLabel.setText(String.format("Travel time: "  + timeString + "   Distance: %.2f km", travelDistance  ));
        travelTimePanel.setVisible(true);
        travelTimeLabel.setVisible(true);
    }

    /**
     * Finds the closest visible highway to the mouse position given
     * @param position - Position of mouse.
     * @throws NoninvertibleTransformException
     */
    public void findNearestToMouse(Point2D position) throws NoninvertibleTransformException{
        //Take insets into account when using mouseCoordinates.
        Insets x = getInsets();
        position.setLocation(position.getX(), position.getY()-x.top + x.bottom);
        Point2D coordinates = transformPoint(position);
        Rectangle2D mouseBox = new Rectangle2D.Double(coordinates.getX()-0.005,
                coordinates.getY() -0.005,
                0.01 , 0.01);
        Collection<MapData> streets = model.getVisibleStreets(mouseBox, false);
        streets.addAll(model.getVisibleBigRoads(mouseBox, false));
        filterRoads(streets);  //remove all highways without names.


        nearestNeighbor = RouteFinder.findNearestHighway(coordinates, streets);
        repaint();
    }

    private void filterRoads(Collection<MapData> before){

        for(Iterator<MapData> it = before.iterator(); it.hasNext();){
            Highway highway = (Highway) it.next();
            if(highway.getValue().equals("footway") || highway.getValue().equals("cycleway") ||
                    highway.getValue().equals("steps") ||
                    highway.getValue().equals("path") ||
                    highway.getValue().equals("bridleway")) {
                it.remove();
                continue;
            }
            DrawAttribute draw = drawAttributeManager.getDrawAttribute(highway.getValueName());
            if(draw.getZoomLevel() > zoomLevel || highway.getStreetName() == null) {
                it.remove();
            }
        }
    }

    public void clearDirectionPane(){
        directionPane.setVisible(false);
        closeDirectionList.setVisible(false);
        travelTimePanel.setVisible(false);
        travelTimeLabel.setVisible(false);

    }


    private void travelMethod(RouteFinder routeFinder){
        routeFinder.carPressed(); //by default
        HashMap<JButton, Boolean> buttonMap = routePanel.getButtonDownMap();
        for (JButton button : buttonMap.keySet()) {  //Check what travel type is chosen a set it for the route finder.
            boolean isPressed = buttonMap.get(button);
            if (button.equals(routePanel.getBicycleButton()) && isPressed) routeFinder.bikePressed();
            else if (button.equals(routePanel.getFootButton()) && isPressed) routeFinder.walkPressed();
            else if (button.equals(routePanel.getCarButton()) && isPressed) routeFinder.carPressed();
        }
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
    }

    /**
     * Assigns a zoomfactor to each zoomlevel
     * The zoomfactor controls the width of the highway strokes
     */
    private void adjustZoomFactor(){
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
        private Collection<MapFeature> mapFStreets;
        private Collection<MapFeature> mapFAreas;
        private Collection<MapIcon> mapIcons;
        private Collection<MapFeature> coastLines;
        private DrawAttribute drawAttribute;
        private Graphics2D g;
        private boolean sorted;

        @Override
        public void paint(Graphics _g) {
            g = (Graphics2D) _g;
            getData();

            //Set the Transform for Graphic2D element before drawing.
            g.setTransform(transform);
            if (antialias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setStroke(min_value); //Just for good measure.


            for (MapFeature coastLine : coastLines) {
                setDrawAttribute(coastLine.getValueName());
                g.setColor(drawAttribute.getColor());
                g.fill(coastLine.getWay());
            }

            g.setColor(Color.BLACK);


            //Draw areas first
            for (MapFeature mapFeature : mapFAreas) {
                try {
                    setDrawAttribute(mapFeature.getValueName());
                    if (zoomLevel >= drawAttribute.getZoomLevel()) {
                        if (mapFeature.isArea()) {
                            g.setColor(drawAttribute.getColor());
                            g.fill(mapFeature.getWay());
                        } else {
                            g.setColor(drawAttribute.getColor());
                            g.setStroke(DrawAttribute.basicStrokes[drawAttribute.getStrokeId()]);
                            g.draw(mapFeature.getWay());
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                }
            }

            //Then draw boundaries on top of areas
            for (MapFeature area : mapFAreas) {
                if (zoomLevel > 13) {
                    try {
                        g.setColor(Color.BLACK);
                        setDrawAttribute(area.getValueName());
                        if (drawAttribute.isDashed()) continue;
                        else if (!area.isArea()) {
                            g.setColor(drawAttribute.getColor());
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                        }
                        else g.setStroke(DrawAttribute.basicStrokes[0]);
                        g.draw(area.getWay());
                    } catch (NullPointerException e) {
                        System.out.println(area.getValueName() + " " + area.getValue());
                    }
                }
            }

            //Then draw Boundaries for Streets
            for(MapFeature street : mapFStreets){
                if (zoomLevel > 13) {
                    g.setColor(Color.BLACK);
                    setDrawAttribute(street.getValueName());
                    if (drawAttribute.isDashed()) continue;
                    if (street instanceof Route) continue;
                    else if (!street.isArea()) {
                        g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor + 1]);
                    } else g.setStroke(DrawAttribute.basicStrokes[0]);
                    g.draw(street.getWay());
                }
            }


            //Draw the fillers on top of boundaries and areas
               for (MapFeature mapFeature : mapFStreets) {
                setDrawAttribute(mapFeature.getValueName());
                if (zoomLevel >= drawAttribute.getZoomLevel()) {
                    g.setColor(drawAttribute.getColor());
                    if (drawAttribute.isDashed()) {
                        if(zoomLevel > 13 && !(mapFeature instanceof  Route))
                            g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
                        else  g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                    }
                    else {
                        if (mapFeature instanceof Highway) {
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor]);
                        } else {
                            g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                        }
                    }
                    g.draw(mapFeature.getWay());
                }
            }


            //drawing the graph of vertices and edges if enabled
            if(graph) {
                for (MapFeature street : mapFStreets) {
                    if (!(street instanceof Highway)) continue;
                    Highway streetedge = (Highway) street;
                    Iterable<Edge> edges = streetedge.edges();
                    if (edges == null) continue;
                    Iterator<Edge> it = edges.iterator();
                    g.setStroke(new BasicStroke(0.0001f));
                    g.setPaint(Color.CYAN);
                    while (it.hasNext()) {
                        Edge e = it.next();
                        if (e.isOneWay())
                            g.setPaint(Color.orange);
                        else if (e.isOneWayReverse())
                            g.setPaint(Color.PINK);
                        g.draw(e);
                    }


                }
                for (MapFeature street : mapFStreets) {
                    if (!(street instanceof Highway)) continue;
                    Highway streetedge = (Highway) street;
                    List<Point2D> points = streetedge.getPoints();
                    for(Point2D p : points){
                        g.setPaint(Color.yellow);
                        g.draw(new Rectangle2D.Double(p.getX(), p.getY(), 0.000005, 0.000005));
                    }

                }
            }

            //Draw the shortestPath if not null
            if (shortestPath != null) {
                g.setColor(DrawAttribute.cl_pink);
                g.setStroke(DrawAttribute.streetStrokes[5 + zoomFactor]);
                for (Edge e : shortestPath) {
                    g.draw(e);
                }
            }
            //Draw the fastest path if not null
            if (fastestPath != null) {
                g.setColor(DrawAttribute.cl_blue4);
                g.setStroke(DrawAttribute.streetStrokes[5 + zoomFactor]);
                for (Edge e : fastestPath) {
                    g.draw(e);
                }
            }

            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(0.0005f));


            if(showGrid) {
                List<QuadTree> trees = model.getQuadTrees();
                g.setColor(Color.green);
                for (Rectangle2D rec : trees.get(6).getNodeRects())
                    g.draw(rec);
            }


            //Draw the icons
            if (zoomLevel > 13) {
                for (MapIcon mapIcon : mapIcons) {
                    if(mapIcon.isVisible()) {
                        mapIcon.draw(g, transform);
                    }
                }
            }
            g.setColor(Color.BLACK);
            if(bounds.testmode()) {
                Rectangle2D windowBounds = bounds.getBounds();
                g.draw(windowBounds);
            }

            Scalebar scalebar = new Scalebar(g, zoomLevel, View.this, transform);


            //Draws chosen searchResult (either street or address) as well as start or endpoint address
            for(Map.Entry<String, MapPointer> entry : addressPointerMap.entrySet()) {
                MapPointer mp = entry.getValue();
                mp.draw(g,transform);
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
            updateStreetName();
        }

        private void getData(){
            mapFStreets = new ArrayList<>();
            mapFAreas = new ArrayList<>();
            mapIcons = new ArrayList<>();
            coastLines = new ArrayList<>();
            //Get a rectangle of the part of the map shown on screen
            bounds.updateBounds(getVisibleRect());
            Rectangle2D windowBounds = bounds.getBounds();
            sorted = zoomLevel > 11;

            coastLines = (Collection<MapFeature>) (Collection<?>) model.getVisibleCoastLines(windowBounds);

            Collection < MapData > bigRoads = model.getVisibleBigRoads(windowBounds, sorted);
            mapFStreets = (Collection<MapFeature>)(Collection<?>) bigRoads;

            if (zoomLevel > 4)
                mapFAreas.addAll((Collection<MapFeature>)(Collection<?>) model.getVisibleLanduse(windowBounds, sorted));

            if (zoomLevel > 7)
                mapFAreas.addAll((Collection<MapFeature>)(Collection<?>)model.getVisibleNatural(windowBounds, sorted));

            if(zoomLevel > 7) {
                mapFStreets.addAll((Collection<MapFeature>) (Collection<?>) model.getVisibleStreets(windowBounds, sorted));
            }

            if(zoomLevel > 9) {
                mapFStreets.addAll((Collection<MapFeature>) (Collection<?>) model.getVisibleRailways(windowBounds, sorted));
            }



            if(zoomLevel > 10) {
                mapFAreas.addAll((Collection<MapFeature>)(Collection<?>) model.getVisibleBuildings(windowBounds, sorted));
            }


            if(zoomLevel > 13) {
                mapIcons = (Collection<MapIcon>) (Collection<?>) model.getVisibleIcons(windowBounds);
            }


            mapFAreas.addAll((Collection<MapFeature>)(Collection<?>) model.getVisibleBigForests(windowBounds, sorted));
            mapFAreas.addAll((Collection<MapFeature>) (Collection<?>) model.getVisibleBikLakes(windowBounds, sorted));

        }

        private void setDrawAttribute(ValueName valueName) {
            drawAttribute = drawAttributeManager.getDrawAttribute(valueName);
        }

        private void updateStreetName(){
            g.setColor(DrawAttribute.fadewhite);
            Rectangle2D streetArea = new Rectangle2D.Double(getRootPane().getContentPane().getWidth() * 0.01,
                    getRootPane().getContentPane().getHeight() - 26,
                    150,
                    20);
            g.fill(streetArea);
            if(nearestNeighbor != null && nearestNeighbor.getStreetName() != null) {

                g.setColor(Color.black);
                g.drawString(nearestNeighbor.getStreetName(), (int) (getRootPane().getContentPane().getWidth() * 0.01 + 1),
                        getRootPane().getContentPane().getHeight() - 10);
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

    public JButton getOptionsButton(){ return optionsButton;}

    public JButton getMapTypeButton() { return mapTypeButton;}

    public JScrollPane getResultPane() { return resultPane; }

    public Model getModel(){return model;}

    public JList<Address> getAddressSearchResults() { return addressSearchResults; }

    public JScrollPane getResultEndPane() {return resultEndPane;}

    public JScrollPane getResultStartPane() {return resultStartPane;}

    public JButton getCloseDirectionListButton(){ return closeDirectionListButton;}

    public void setShortestPath(Iterable<Edge> it ) { shortestPath = it;}
    public void setFastestPath(Iterable<Edge> it ) { fastestPath = it; }

}
