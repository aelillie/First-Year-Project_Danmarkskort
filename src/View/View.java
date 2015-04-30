package View;

import Controller.SearchResultMouseHandler;
import MapFeatures.Bounds;
import MapFeatures.Highway;
import MapFeatures.Route;
import Model.*;
import QuadTree.QuadTree;
import ShortestPath.Edge;
import ShortestPath.PathTree;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
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
    private JPanel closeDirectionList;

    private int destination;

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
        super("This is our map");
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
    }

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
        makeShowRoutePanelButton();
        makeFullscreenButton();
        makeMapTypeButton();
        makeCloseDirectionListPanel();
        //makeResultPane();
    }

    public void makeCloseDirectionListPanel(){
        closeDirectionList = new JPanel();
        closeDirectionList.setVisible(false);
        closeDirectionList.setBounds(26, 280, 400, 20);
        closeDirectionList.setOpaque(true);
        closeDirectionList.setBackground(DrawAttribute.fadeblack);
        closeDirectionList.setLayout(new BoxLayout(closeDirectionList, BoxLayout.LINE_AXIS));
        closeDirectionList.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        closeDirectionListButton = new JButton(new ImageIcon(this.getClass().getResource("/data/resetButtonWhiteIcon.png")));
        closeDirectionListButton.setOpaque(false);
        closeDirectionListButton.setBackground(DrawAttribute.fadeblack);
        closeDirectionListButton.setActionCommand("closeDirectionList");
        closeDirectionList.add(closeDirectionListButton);

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

    public void processRouteplanStrings(ArrayList<String> streetList, HashMap<String,Double> streetLengthMap){
        String[] directions = new String[streetList.size()+1];
        int directionCount = 0;
        for (int i = streetList.size(); --i >= 0;){
            String street = streetList.get(i);
            double dist = streetLengthMap.get(street)*1000;
            String distString;
            if(dist < 1000) { //If the distance is less than a kilometer, display it in meters, otherwise display it in kilometers
                distString = new DecimalFormat("####").format(dist) + " m";
            } else {
                distString = new DecimalFormat("##.##").format(dist/1000) + " km";
            }
            String direction = "Follow " + street + " for " + distString;
            if(street.trim().equals("")){
                if(i != 0) direction = "Continue for " + distString + " until you reach " + streetList.get(i-1);
                else direction = "Continue for " + distString + " until you reach your destination.";
            }
            directions[directionCount] = direction;
            directionCount++;
        }
        directions[directionCount] = "You have reached your destination.";
        addToDirectionPane(directions);
    }


    public void addToDirectionPane(String[] directionArray){
        JList<String> directionStringList = new JList<>(directionArray);
        directionPane.setVisible(true);
        directionPane.setViewportView(directionStringList);
        directionPane.setBounds(26, 300, 400, 200);
        directionPane.setBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
        directionPane.getViewport().setBackground(Color.WHITE);
        directionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        closeDirectionList.setVisible(true);

    }

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

    private void makeMapTypeButton(){
        Dimension preferred = getPreferredSize();
        mapTypeButton = new JButton();
        mapTypeButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("layerIcon")));
        mapTypeButton.setFocusable(false);
        mapTypeButton.setOpaque(false);
        mapTypeButton.setBackground(DrawAttribute.fadeblack);
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
        optionsButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("optionsIcon")));
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

    public void addPointer(MapPointer mapPointer){
        addressPointerMap.put(mapPointer.getType(), mapPointer);
        canvas.repaint();
    }

    public void removePointer(String iconType){
        addressPointerMap.remove(iconType);
        canvas.repaint();
    }



    private void makeFullscreenButton() {
        Dimension preferred = getPreferredSize();
        fullscreenButton = new JButton();
        fullscreenButton.setBackground(Color.BLACK);
        fullscreenButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("fullscreenIcon")));
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
        zoomOutButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("minusIcon")));
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
        zoomInButton.setIcon(new ImageIcon(MapIcon.iconURLs.get("plusIcon")));
        zoomInButton.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomInButton.setFocusable(false);
        zoomInButton.setOpaque(false);
        zoomInButton.setBackground(DrawAttribute.fadeblack);
        zoomInButton.setBorderPainted(false);
        zoomInButton.setRolloverEnabled(false);
        zoomInButton.setActionCommand("zoomIn");
        zoomInButton.setBounds((int) preferred.getWidth() - 60, (int) preferred.getHeight() - (int) preferred.getHeight() / 3 * 2, 39, 37);
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
        //scalesomething();
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
    public Point2D getCenterLatLon(){
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
    public void centerOnLatLon(Point2D newCenter){
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
     * Finds the Nearest Highway from the MousePosition using distance from point to lineSegment
     * @param position Position of MousePointer
     */
    public Highway findNearestHighway(Point2D position, Collection<MapData> node)throws NoninvertibleTransformException{

        MapFeature champion = null;
        Line2D championLine = null;

        for (MapData mp : node) {
            if (mp instanceof Highway ) {
                Highway highway = (Highway) mp;
                double[] points = new double[6];

                PathIterator pI = highway.getWay().getPathIterator(new AffineTransform());

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
        nearestNeighbor = (Highway) champion;
        //System.out.println("Street: " + nearestNeighbor.getStreetName() + " v: " + nearestNeighbor.getV() + " w: " + nearestNeighbor.getW() + " distance: " + nearestNeighbor.getDistance());
        if (nearestNeighbor != null) {
            destination = nearestNeighbor.getVertex(0);
        }
        repaint();
        return (Highway) champion;
    }

    public void findNearestToMouse(Point2D position) throws NoninvertibleTransformException{
        //Take insets into account when using mouseCoordinates.
        Insets x = getInsets();
        position.setLocation(position.getX(), position.getY()-x.top + x.bottom);
        Point2D coordinates = transformPoint(position);
        Rectangle2D mouseBox = new Rectangle2D.Double(coordinates.getX()-0.05,
                coordinates.getY() -0.05,
                0.1 , 0.1);
        Collection<MapData> streets = model.getVisibleStreets(mouseBox, false);
        filterRoads(streets);  //remove all highways without names.


        nearestNeighbor = findNearestHighway(coordinates, streets);
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

    /**
     * Finds shortest path between 2 points.
     * @param startPoint - Coordinates of start Address
     * @param endPoint - Coordinates of end address
     * @throws NoninvertibleTransformException
     */
    public void findRoute(Point2D startPoint, Point2D endPoint)throws NoninvertibleTransformException{

        Rectangle2D startBox = new Rectangle2D.Double(startPoint.getX()-0.045,
                startPoint.getY()-0.045, 0.09 , 0.09);

        Rectangle2D endBox = new Rectangle2D.Double(endPoint.getX()-0.045,
                endPoint.getY()-0.045, 0.09 , 0.09);

        Highway startWay = findNearestHighway(startPoint, model.getVisibleStreets(startBox, false));

        int startPointIndex = findClosestVertex(startPoint, startWay);
        Highway endWay = findNearestHighway(endPoint, model.getVisibleStreets(endBox, false));
        int endPointIndex = findClosestVertex(endPoint, endWay);

        //Find shortest Path.
        PathTree shortestTree = new PathTree(model.getDiGraph(), startPointIndex, endPointIndex);
        shortestTree.useShortestPath(true);
        shortestTree.useCarRoute();
        HashMap<JButton, Boolean> buttonMap = routePanel.getButtonDownMap();
        for (JButton button : buttonMap.keySet()) {
            boolean isPressed = buttonMap.get(button);
            if (button.equals(routePanel.getBicycleButton()) && isPressed) shortestTree.useBikeRoute();
            else if (button.equals(routePanel.getFootButton()) && isPressed) shortestTree.useWalkRoute();
            else if (button.equals(routePanel.getCarButton()) && isPressed) shortestTree.useCarRoute();
        }
        shortestTree.initiate();

        PathTree fastestTree = new PathTree(model.getDiGraph(), startPointIndex, endPointIndex);
        fastestTree.useShortestPath(false);
        fastestTree.useCarRoute();
        for (JButton button : buttonMap.keySet()) {
            boolean isPressed = buttonMap.get(button);
            if (button.equals(routePanel.getBicycleButton()) && isPressed) fastestTree.useBikeRoute();
            else if (button.equals(routePanel.getFootButton()) && isPressed) fastestTree.useWalkRoute();
            else if (button.equals(routePanel.getCarButton()) && isPressed) fastestTree.useCarRoute();
        }
        fastestTree.initiate();
        if(shortestTree.hasPathTo(endPointIndex) && fastestTree.hasPathTo(endPointIndex)){
            shortestPath = shortestTree.pathTo(endPointIndex);
            fastestPath = fastestTree.pathTo(endPointIndex);

        }


        //Shortest path
        System.out.println("");
        System.out.println("Shortest path from A to B");
        double distance = shortestTree.distTo(endPointIndex);
        if (distance < 1) {
            distance *= 1000;
            System.out.println("Distance: " + String.format("%.0f", distance) + " m");
        } else System.out.println("Distance: " + String.format("%.2f", distance) + " km");
        double travelTime = 0;
        if(shortestPath == null) return;
        HashMap<String, Double> streetLengthMap = new HashMap<>();
        ArrayList<String> streetList = new ArrayList<>();
        for (Edge e : shortestPath) {
            if (shortestTree.isWalkRoute()) travelTime += e.walkTime();
            else if (shortestTree.isBikeRoute()) travelTime += e.bikeTime();
            else travelTime += e.driveTime();

            String streetname = e.highway().getStreetName();
            if(streetname == null) streetname = " ";
            Double dist = streetLengthMap.get(streetname);

            if(dist == null){
                streetLengthMap.put(streetname, e.distance());
                streetList.add(streetname);
            } else {
                streetLengthMap.put(streetname,dist+e.distance());
            }
        }

        processRouteplanStrings(streetList,streetLengthMap);

        System.out.println("Time: " + String.format("%.2f", travelTime) + " minutes\n");

        //Fastest path
        System.out.println("Fastest Path from A to B");
        double fastDist = fastestTree.distTo(endPointIndex);
        if (distance < 1) {
            fastDist *= 1000;
            System.out.println("Distance: " + String.format("%.0f", fastDist) + " m");
        } else System.out.println("Distance: " + String.format("%.2f", fastDist) + " km");
        double fastTime = 0;
        if(fastestPath == null) return;
        for (Edge e : fastestPath) {
            fastTime += e.driveTime();
        }
        System.out.println("Time: " + String.format("%.2f", fastTime) + " minutes");

        repaint();
    }

    public void findShortestPath() {
        //Functions as a test when pressed "l"
        System.out.println("");
        System.out.println("Shortest path:");
        int source = 0;
        PathTree SPpathTree = new PathTree(model.getDiGraph(), source, destination);
        SPpathTree.useShortestPath(true);
        HashMap<JButton, Boolean> buttonMap = routePanel.getButtonDownMap();

        for (JButton button : buttonMap.keySet()) {
            boolean isPressed = buttonMap.get(button);
            if (button.equals(routePanel.getBicycleButton()) && isPressed) SPpathTree.useBikeRoute();
            else if (button.equals(routePanel.getFootButton()) && isPressed) SPpathTree.useWalkRoute();
            else if (button.equals(routePanel.getCarButton()) && isPressed) SPpathTree.useCarRoute();
        }
        SPpathTree.initiate();
        shortestPath = SPpathTree.pathTo(destination);

        double distance = SPpathTree.distTo(destination);
        if (distance < 1) {
            distance *= 1000;
            System.out.println("Distance: " + String.format("%.0f", distance) + " m");
        } else System.out.println("Distance: " + String.format("%.2f", distance) + " km");
        double travelTime = 0;
        if(!SPpathTree.hasPathTo(destination)) {
            System.out.println("NO PATH WAS FOUND");
            return;
        }
        for (Edge e : shortestPath) {
            if (SPpathTree.isWalkRoute())
                travelTime += e.walkTime();
            else if (SPpathTree.isBikeRoute())
                travelTime += e.bikeTime();
            else
                travelTime += e.driveTime();
        }
        System.out.println("Time: " + String.format("%5.2f", travelTime) + " minutes");
        System.out.println("");
        repaint();
    }

    public void findFastestPath() {
        //Functions as a test when pressed "f"
        int source = 0;
        PathTree FPpathTree = new PathTree(model.getDiGraph(), source, destination);
        FPpathTree.useShortestPath(false);
        HashMap<JButton, Boolean> buttonMap = routePanel.getButtonDownMap();
        for (JButton button : buttonMap.keySet()) {
            boolean isPressed = buttonMap.get(button);
            if (button.equals(routePanel.getBicycleButton()) && isPressed) FPpathTree.useBikeRoute();
            else if (button.equals(routePanel.getFootButton()) && isPressed) FPpathTree.useWalkRoute();
            else if (button.equals(routePanel.getCarButton()) && isPressed) FPpathTree.useCarRoute();
        }
        FPpathTree.initiate();
        fastestPath = FPpathTree.pathTo(destination);

        double time = FPpathTree.timeTo(destination);
        System.out.println("");
        System.out.println("Fastest path:");
        double distance = 0;
        if(!FPpathTree.hasPathTo(destination)) {
            System.out.println("NO PATH FOUND");
            return;
        }
        for (Edge e : fastestPath) {
            distance += e.distance();
        }
        if (distance < 1) {
            distance *= 1000;
            System.out.println("Distance: " + String.format("%.0f", distance) + " m");
        } else System.out.println("Distance: " + String.format("%.2f", distance) + " km");
        System.out.println("Time: " + String.format("%5.2f", time) + " minutes");
        System.out.println("");
        repaint();
    }


    private int findClosestVertex(Point2D chosenPoint, Highway way){
        List<Edge> edges = way.getEdges();
        Edge closestEdge = null;

        for(Edge edge : edges){
            if (closestEdge == null) {
                closestEdge = edge;
            }
            else if (closestEdge.ptSegDist(chosenPoint) > edge.ptSegDist(chosenPoint)) {
                closestEdge = edge;
            }

        }
        if(closestEdge == null) return 0;
        return closestEdge.either();
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
        private Collection<MapFeature> mapFStreets;
        private Collection<MapFeature> mapFAreas;
        private Collection<MapIcon> mapIcons;
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

            Bounds box = PathCreater.createBounds(model.getBbox());
            setDrawAttribute(box.getValueName());
            g.setColor(drawAttribute.getColor());
            g.fill(box.getWay());

            for (MapFeature coastLine : OSMHandler.getCoastlines()) {
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
                    }
                }
                } catch (NullPointerException e) {
                        System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                }

            }

            //Then draw boundaries on top of areas
            for (MapFeature Area : mapFAreas) {
                if (zoomLevel > 14) {
                    try {
                        g.setColor(Color.BLACK);
                        setDrawAttribute(Area.getValueName());
                        if (drawAttribute.isDashed()) continue;
                        else if (!Area.isArea())
                             g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                        else g.setStroke(DrawAttribute.basicStrokes[0]);
                        g.draw(Area.getWay());
                    } catch (NullPointerException e) {
                        System.out.println(Area.getValueName() + " " + Area.getValue());
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


            //Justing drawing the of vertices and edges
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

            if (shortestPath != null) {
                g.setColor(DrawAttribute.cl_darkorange);
                g.setStroke(DrawAttribute.streetStrokes[4 + zoomFactor]);
                for (Edge e : shortestPath) {
                    g.draw(e);
                }
            }
            if (fastestPath != null) {
                g.setColor(DrawAttribute.lightgreen);
                g.setStroke(DrawAttribute.streetStrokes[4 + zoomFactor]);
                for (Edge e : fastestPath) {
                    g.draw(e);
                }
            }

            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(0.0005f));


            if(showGrid) {
                List<QuadTree> trees = model.getQuadTrees();
                g.setColor(Color.green);
                for (Rectangle2D rec : trees.get(0).getNodeRects())
                    g.draw(rec);
            }


            //Draw the icons
            if (zoomLevel >= 15) {
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

            paintNeighbor(g);

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

            bounds.updateBounds(getVisibleRect());
            Rectangle2D windowBounds = bounds.getBounds();
            if(zoomLevel > 11)
                sorted = true;
            else sorted = false;

            Collection < MapData > streets = model.getVisibleStreets(windowBounds, sorted);
            mapFStreets = (Collection<MapFeature>)(Collection<?>) streets;

            if(zoomLevel > 9){
                mapFStreets.addAll((Collection<MapFeature>) (Collection<?>) model.getVisibleRailways(windowBounds, sorted));

            }

            if(zoomLevel > 8){
                mapFAreas = (Collection<MapFeature>)(Collection<?>)model.getVisibleNatural(windowBounds,sorted);

            }

            if(zoomLevel >= 13){
                mapFAreas.addAll((Collection<MapFeature>)(Collection<?>) model.getVisibleBuildings(windowBounds, sorted));
            }

            if(zoomLevel >= 15){
                mapIcons = (Collection<MapIcon>) (Collection<?>) model.getVisibleIcons(windowBounds, sorted);
            }

        }

        private void paintNeighbor(Graphics2D g) {
            if (nearestNeighbor != null) {
                DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(nearestNeighbor.getValueName());
                g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + zoomFactor]);
                g.setColor(Color.CYAN);
                g.draw(nearestNeighbor.getWay());
            }
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

    public void setShortestPath(Iterable<Edge> it ) { shortestPath = it; }
    public void setFastestPath(Iterable<Edge> it ) { fastestPath = it; }

}
