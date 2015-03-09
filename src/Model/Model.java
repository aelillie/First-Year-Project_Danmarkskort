package Model;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Model extends Observable implements Iterable<Shape>, Serializable {
    private Map<Long, Path2D> relations = new HashMap<>(); //contains all shapes to be used as relations.
    private List<Shape> lines = new ArrayList<>(); //contains all shapes to be drawn that are not in drawables
    private List<MapIcon> mapIcons = new ArrayList<>(); //contains all the icons to be drawn
    private List<List<Point2D>> coastlinesInCoords = new ArrayList<>();

    private Map<String,List<Shape>> streetnameMap = new HashMap<>();
    private Map<Address,Point2D> addressMap = new HashMap<>(); //Contains relevant places parsed as address objects (e.g. a place Roskilde or an address Lauravej 38 2900 Hellerup etc.) linked to their coordinate.
    private ArrayList<Address> addressList = new ArrayList<>(); //Contains all addresses to be sorted according to the compareTo method.

    protected List<String> cityNames = new ArrayList<>();
    protected List<String> postCodes = new ArrayList<>();
    protected Map<String, String> streetCityMap = new HashMap<>();


    public List<MapIcon> getMapIcons() {
        return mapIcons;
    }

    private List<Drawable> drawables = new ArrayList<>(); //Shapes to be drawn differently
    private Rectangle2D bbox;

    public List<Drawable> getDrawables() {
        return drawables;
    }

    public Rectangle2D getBbox() {
        return bbox;
    }

    /**
     * Constructor, which either parses .osm or .zip files
     * @param filename either .osm or .zip (containing .osm) format
     */
    public Model(String filename) {
        long time = System.nanoTime();
        Address.addPatterns();
        if (filename.endsWith(".osm")) parseOSM(filename);
        else if (filename.endsWith(".zip")) parseZIP(filename);
        else if (filename.endsWith(".bin")) load(filename);
        else System.err.println("File not recognized");
        sortLayers();
        System.out.printf("Model.Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }


    void parseOSM(String filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(filename);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    void parseZIP(String filename) {
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            zip.getNextEntry();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(new InputSource(zip));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sorts the Model.Drawable elements in the drawables list from their layer value.
     * Takes use of a comparator, which compares their values.
     */
    private void sortLayers() {
        Comparator<Drawable> comparator = new Comparator<Drawable>() {
            @Override
            /**
             * Compares two Model.Drawable objects.
             * Returns a negative integer, zero, or a positive integer as the first argument
             * is less than, equal to, or greater than the second.
             */
            public int compare(Drawable o1, Drawable o2) {
                if (o1.getLayerVal() < o2.getLayerVal()) return -1;
                else if (o1.getLayerVal() > o2.getLayerVal()) return 1;
                return 0;
            }
        };
        Collections.sort(drawables, comparator); //iterative mergesort. ~n*lg(n) comparisons
    }

    /**
     * Recursive binary search method taking lower- and higher bounds as input. Takes O(log N) time.
     * @param list The list to be searched.
     * @param addr The address we are searching for.
     * @param low The lower bound of the part of the array we want to search.
     * @param high The higher bound of the part of the array we want to search.
     * @return The index at which we found the element.
     */
    private int binSearch(ArrayList<Address> list, Address addr, int low, int high){
        if(low > high) return -1;
        int mid = (low+high)/2;
        if (list.get(mid).compareTo(addr) < 0) return binSearch(list, addr, mid + 1, high); //if addr is larger than mid
        else if (list.get(mid).compareTo(addr) > 0) return binSearch(list, addr, low, mid - 1); //if addr is smaller than mid
        else return mid;
    }

    /**
     * Since there can be several addresses with the same name (e.g. Lærkevej in Copenhagen and Lærkevej in Roskilde),
     * this method searches the lower part and higher part of the array bounded by the first element which is found in the list to determine
     * the bounds of the similiar results in the array.
     * @param addressInput
     * @return
     */
    public int[] multipleEntriesSearch(Address addressInput){
        int index = binSearch(addressList,addressInput,0,addressList.size()-1); //Returns the index of the first found element.
        if(index < 0) return null; //Not found

        int lowerBound = index; //Search to the left of the found element
        int i = lowerBound;
        do {
            lowerBound = i;
            i = binSearch(addressList, addressInput, 0, lowerBound-1);
        } while (i != -1); //As long as we find a similiar element, keep searching to determine when we don't anymore.

        int upperBound = index; //Search to the right of the found element
        i = upperBound;
        do {
            upperBound = i;
            i = binSearch(addressList, addressInput, upperBound+1, addressList.size() - 1);
        }
        while (i != -1); //As long as we find a similiar element, keep searching to determine when we don't anymore.

        int[] range = {lowerBound, upperBound}; //The bounds of the similiar elements in the list.
        return range;
    }

    public void searchForAddresses(Address addressInput){
        int[] range = multipleEntriesSearch(addressInput); //search for one or multiple entries
        if(range == null) { //If it is not found, the return value will be negative
            System.out.println("Too bad - didn't find!");
        } else {
            System.out.println("Found something");
            int lowerBound = range[0], upperBound = range[1];
            System.out.printf("low: "+lowerBound + ", high: "+upperBound);
            for(int i = lowerBound; i <= upperBound; i++){
                Address foundAddr = addressList.get(i);
                Point2D coordinate = addressMap.get(foundAddr);
                //System.out.println("x = " + coordinate.getX() + ", y = " +coordinate.getY());
            }
        }
    }


   /* public void searchForAddresses1(Model.Address addressInput){
        int index = Collections.binarySearch(addressList,addressInput,new AddressComparator());
        if(index < 0) { //If it is not found the return value will be negative
            System.out.println("Too bad - didn't find!");
        } else {
            Model.Address foundAddr = addressList.get(index);
            Point2D coordinate = addressMap.get(foundAddr);
            System.out.println("x = " + coordinate.getX() + ", y = " +coordinate.getY());
        } //if multiple results ... suggest these

    }*/


    /**
     * Contenthandler, which handles the .osm file written in XML.
     * Saving data from the file, which is being parsed, through keys and tags read.
     * Ultimately adding shapes to be drawn to lists in this class.
     */
    class OSMHandler extends DefaultHandler {
        Map<Long, Point2D> map = new HashMap<>(); //Relation between a nodes' id and coordinates
        Map<String, String> kv_map = new HashMap<>(); //relation between the keys and values in the XML file
        Map<String, String> layer_map = new HashMap<>();
        Map<Long, String> role_map = new HashMap<>(); //
        List<Long> refs = new ArrayList<>();
        List<Point2D> coords = new ArrayList<>(); //referenced coordinates
        Path2D way; //<way> tag. A way is the path from one coordinate to another
        Long id;
        Point2D currentCoord; //current coordinate read
        private boolean isArea, isBusstop, isMetro, isSTog, hasName, hasHouseNo, hasPostcode, hasCity; //controls how shapes should be added
        private String name;
        private String streetName, houseNumber,cityName, postCode;


        /**
         * Reads start elements and handles what to be done from the data associated with the element.
         * @param uri The namespace URI
         * @param localName the local name (without prefix)
         * @param qName the qualified name (with prefix)
         * @param atts the attributes attached to the element
         */
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            switch (qName) { //if qName.equals(case)
                case "relation":{
                    kv_map.clear();
                    role_map.clear();
                    refs.clear();
                    break;
                }
                case "node": {
                    kv_map.clear();
                    isBusstop = false; isMetro = false;  isSTog = false;
                    hasName = false; hasHouseNo = false; hasPostcode = false; hasCity = false;

                    double lat = Double.parseDouble(atts.getValue("lat"));
                    double lon = Double.parseDouble(atts.getValue("lon"));
                    long id = Long.parseLong(atts.getValue("id"));
                    Point2D coord = new Point2D.Double(lon, lat);
                    currentCoord = new Point2D.Double(lon,lat);
                    map.put(id, coord);
                    break;
                }
                case "nd": {
                    long id = Long.parseLong(atts.getValue("ref"));
                    Point2D coord = map.get(id);
                    if (coord == null) return;
                    coords.add(coord);
                    break;
                }
                case "way":
                    kv_map.clear();
                    coords.clear();
                    isArea = false;
                    hasName = false;
                    id = Long.parseLong(atts.getValue("id"));
                    break;
                case "bounds":
                    double minlat = Double.parseDouble(atts.getValue("minlat"));
                    double minlon = Double.parseDouble(atts.getValue("minlon"));
                    double maxlat = Double.parseDouble(atts.getValue("maxlat"));
                    double maxlon = Double.parseDouble(atts.getValue("maxlon"));
                    bbox = new Rectangle2D.Double(minlon, minlat, maxlon - minlon, maxlat - minlat);
                    break;
                case "tag":
                    String k = atts.getValue("k");
                    String v = atts.getValue("v");
                    kv_map.put(k, v);
                    if(k.equals("area") && v.equals("yes")) isArea = true;
                    if(k.equals("highway") && v.equals("bus_stop")) isBusstop = true;
                    if(k.equals("subway")&& v.equals("yes")) isMetro = true;
                    if(k.equals("network") && v.equals("S-Tog")) isSTog = true;
                    if(k.equals("name")){
                        hasName = true;
                        name = v;
                    }
                    if(k.equals("addr:street")){
                        streetName = v;
                    }
                    if(k.equals("addr:housenumber")){
                        hasHouseNo = true;
                        houseNumber = v;
                    }
                    if(k.equals("addr:city")){
                        hasCity = true;
                        cityName = v;
                    }
                    if(k.equals("addr:postcode")){
                        hasPostcode = true;
                        postCode = v;
                    }
                    break;
                case "member":
                    long ref = Long.parseLong(atts.getValue("ref"));
                    String role = atts.getValue("role");
                    role_map.put(ref, role);
                    refs.add(Long.parseLong(atts.getValue("ref")));
                    break;
            }
        }


        private int getLayer() {
            int layer_val = 2; //default layer
            int botLayer = 1;
            int botLayer2 = 2;
            //gives space to 2 extra layers
            int topLayer = 5; //modify this to make extra layers
            try {
                int OSM_layer = Integer.parseInt(kv_map.get("layer"));
                if (OSM_layer == -2) layer_val = botLayer;
                if (OSM_layer == -1) layer_val = botLayer2;
                if (OSM_layer == 1) layer_val = topLayer;
            } catch (NumberFormatException e) {
                layer_val = 2;
            }
            return layer_val;
        }

        private int setLayer(String key) {
            /*
            if (key.equals(""))
            return 2; //default layer
            */
            return 0;
        }

        /**
         * Reads end elements and handles what to be done from the data associated with the element.
         * @param uri the namespace URI
         * @param localName the local name (without prefix)
         * @param qName the qualified CML name (with prefix)
         */
        public void endElement(String uri, String localName, String qName) {
            switch (qName) {
                case "way": //TODO: insert way names into the addresslist aswelll
                    way = new Path2D.Double();
                    Point2D coord = coords.get(0);
                    way.moveTo(coord.getX(), coord.getY());
                    for (int i = 1; i < coords.size(); i++) {
                        coord = coords.get(i);
                        way.lineTo(coord.getX(), coord.getY());
                    }
                    relations.put(id, way);

                    //start of adding shapes from keys and values
                    if (kv_map.containsKey("natural")) { //##New key!
                        String val = kv_map.get("natural");
                        if (val.equals("coastline")) {
                            drawables.add(new Line(way, Drawable.lightblue, 4, -1.0, getLayer()));
                            //processCoastpoints(coords);
                        }
                        if (val.equals("wood")) drawables.add(new Area(way, Drawable.neongreen, -2.0, getLayer()));
                        if (val.equals("scrub")) drawables.add(new Area(way, Drawable.neongreen, -1.5, getLayer()));
                        if (val.equals("heath")) drawables.add(new Area(way, Drawable.skincolor, -2.0, getLayer()));
                        if (val.equals("grassland")) drawables.add(new Area(way, Drawable.bluegreen, -2.0, getLayer()));
                        if (val.equals("sand")) drawables.add(new Area(way, Drawable.sand, -2.0, getLayer()));
                        if (val.equals("scree")) drawables.add(new Area(way, Drawable.pink, -2.0, getLayer()));
                        if (val.equals("fell")) drawables.add(new Area(way, Drawable.orange, -2.0, getLayer()));
                        if (val.equals("water")) drawables.add(new Area(way, Drawable.whiteblue, -2.0, getLayer()));
                        if (val.equals("wetland")) drawables.add(new Area(way, Drawable.greenblue, -2.0, getLayer()));
                    } else if (kv_map.containsKey("waterway")) { //##New key!
                        String val = kv_map.get("waterway");
                        if (val.equals("riverbank")) drawables.add(new Area(way, Drawable.lightblue, -1.0, getLayer()));
                        if (val.equals("stream")) drawables.add(new Line(way, Drawable.lightblue, 1, -1.0, getLayer()));
                        if (val.equals("canal")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));
                        if (val.equals("river")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));
                        if (val.equals("dam")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));

                    } else if (kv_map.containsKey("leisure")) { //##New key!
                        String val = kv_map.get("leisure");
                        if (val.equals("garden")) drawables.add(new Area(way, Drawable.whitegreen, -1.2, getLayer()));
                        if (val.equals("park")) drawables.add(new Area(way, Drawable.whitegreen, -1.2, getLayer()));
                        if (val.equals("common")) drawables.add(new Area(way, Drawable.neongreen, -1.2, getLayer()));
                    } else if (kv_map.containsKey("landuse")) { //##New key!
                        String val = kv_map.get("landuse");
                        if (val.equals("cemetery")) drawables.add(new Area(way, Drawable.whitegreen, -0.4, getLayer()));
                        if (val.equals("construction")) {
                            if (isArea) drawables.add(new Area(way, Drawable.lightgreen, -0.3, -1));
                            else drawables.add(new Line(way, Drawable.lightgreen, 2, -0.3, getLayer()));
                        }
                        if (val.equals("grass")) drawables.add(new Area(way, Drawable.whitegreen, -0.4, getLayer()));
                        if (val.equals("greenfield"))
                            drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                        if (val.equals("industrial"))
                            drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                        if (val.equals("orchard")) drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                    } else if (kv_map.containsKey("geological")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("building")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -0.5, getLayer()));
                    } else if (kv_map.containsKey("shop")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("tourism")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("man_made")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("military")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("historic")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("craft")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("emergency")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("aeroway")) { //##New key!
                        drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                    } else if (kv_map.containsKey("amenity")) { //##New key!
                        //drawables.add(new Model.Area(way, Model.Drawable.lightgrey));
                        String val = kv_map.get("amenity");
                        if (val.equals("parking")) {
                            mapIcons.add(new MapIcon(way, "data//parkingIcon.jpg"));
                            drawables.add(new Area(way, Drawable.sand, -1.0, getLayer()));
                        }
                    } else if (kv_map.containsKey("barrier")) { //##New key!
                        String val = kv_map.get("barrier");
                        if (val.equals("hedge")) {
                            if (isArea) drawables.add(new Area(way, Drawable.neongreen, -0.3, getLayer()));
                            else drawables.add(new Line(way, Drawable.neongreen, 2, -0.3, getLayer()));
                        }
                        if (val.equals("fence")) {
                            if (isArea) drawables.add(new Area(way, Drawable.neongreen, -0.3, getLayer()));
                            else drawables.add(new Line(way, Drawable.neongreen, 2, -0.3, getLayer()));
                        }
                    } else if (kv_map.containsKey("boundary")) { //##New key!
                        Line line = new Line(way, Color.WHITE, 14, 2.0, getLayer());
                        line.setDashed();
                        drawables.add(line);
                    } else if (kv_map.containsKey("highway")) { //##New key!
                        String val = kv_map.get("highway");
                        if (hasName) addStreetName();
                        if (val.equals("motorway"))
                            drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
                        else if (val.equals("motorway_link"))
                            drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
                        else if (val.equals("trunk"))
                            drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
                        else if (val.equals("trunk_link"))
                            drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
                        else if (val.equals("primary"))
                            drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
                        else if (val.equals("primary_link"))
                            drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
                        else if (val.equals("secondary"))
                            drawables.add(new Line(way, Drawable.lightred, 6, -1.0, getLayer()));
                        else if (val.equals("secondary_link"))
                            drawables.add(new Line(way, Drawable.lightred, 6, -0.8, getLayer()));
                        else if (val.equals("tertiary"))
                            drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
                        else if (val.equals("tertiary_link"))
                            drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
                        else if (val.equals("unclassified")) drawables.add(new Line(way, Color.WHITE, 5, -0.8, -1));
                        else if (val.equals("residential")) drawables.add(new Line(way, Color.DARK_GRAY, 4, -1.0, -1));
                        else if (val.equals("service")) drawables.add(new Line(way, Color.WHITE, 2, -0.8, -1));

                        else if (val.equals("living_street")) drawables.add(new Line(way, Drawable.grey, 2, -1.0, -2));
                        else if (val.equals("pedestrian")) drawables.add(new Line(way, Drawable.white, 2, -0.1, -2));
                        else if (val.equals("track"))
                            drawables.add(new Line(way, Drawable.bloodred, 1, 0.0, getLayer()));
                        else if (val.equals("bus_guideway"))
                            drawables.add(new Line(way, Drawable.darkblue, 1, -0.4, -2));
                        else if (val.equals("raceway")) drawables.add(new Line(way, Drawable.white, 2, -0.4, -2));
                        else if (val.equals("road")) drawables.add(new Line(way, Drawable.grey, 3, -0.4, -2));
                        else if (val.equals("footway")) {
                            Line line = new Line(way, Drawable.red, 12, -0.1, -2);
                            line.setDashed();
                            drawables.add(line);
                        } else if (val.equals("cycleway")) {
                            Line line = new Line(way, Drawable.lightblue, 13, -0.18, -2);
                            line.setDashed();
                            drawables.add(line);
                        } else if (val.equals("bridleway"))
                            drawables.add(new Line(way, Drawable.lightgreen, 1, -1.0, -2));
                        else if (val.equals("steps")) {
                            Line line = new Line(way, Drawable.red, 14, -0.1, -2);
                            line.setDashed();
                            drawables.add(line);
                        } else if (val.equals("path")) drawables.add(new Line(way, Drawable.red, 1, -0.1, -2));


                    } else if (kv_map.containsKey("railway")) { //##New key!
                        Line line = new Line(way, Color.DARK_GRAY, 11, -1.9, getLayer());
                        line.setDashed();
                        drawables.add(line);
                    } else if (kv_map.containsKey("bridge")) { //##New key!
                        drawables.add(new Line(way, Color.GRAY, 2, -2.0, getLayer()));
                    } else if (kv_map.containsKey("route")) { //##New key!
                        drawables.add(new Line(way, Color.WHITE, 1, -2.0, getLayer()));
                    } else {
                    }
                    break;
                case "relation":
                    if (kv_map.containsKey("type")) {
                        String val = kv_map.get("type");
                        if (val.equals("multipolygon")) {
                            Long ref = refs.get(0);
                            if (relations.containsKey(ref)) {
                                Path2D path = relations.get(ref);
                                for (int i = 1; i < refs.size(); i++) {
                                    ref = refs.get(i);
                                    if (relations.containsKey(ref)) {
                                        Path2D element = relations.get(refs.get(i));
                                        path.append(element, false);
                                    } else
                                        System.out.println(ref + " ");
                                }
                                path.setWindingRule(Path2D.WIND_EVEN_ODD);
                                if (kv_map.containsKey("building"))
                                    drawables.add(new Area(path, Drawable.lightgrey, -0.8, getLayer()));
                                if(kv_map.containsKey("place")){
                                    //TODO islets

                                }
                            /*else if (kv_map.containsKey("natural"))
                                drawables.add(new Model.Area(path, Model.Drawable.water, -1.5));*/
                                //TODO How do draw harbor.
                            }

                            //TODO look at busroute and so forth
                        }

                    }

                    break;

                case "node":
                    if (kv_map.containsKey("highway")) {
                        String val = kv_map.get("highway");
                        if (val.equals("bus_stop") && isBusstop)
                            mapIcons.add(new MapIcon(currentCoord, "data//busIcon.png"));
                    }
                    else if (kv_map.containsKey("railway")) {
                        String val = kv_map.get("railway");
                        if (val.equals("station")) {
                            if (isMetro) mapIcons.add(new MapIcon(currentCoord, "data//metroIcon.png"));
                            else if (isSTog) mapIcons.add(new MapIcon(currentCoord, "data//stogIcon.png"));
                        }
                    } else if(kv_map.containsKey("name")) {
                        String name = kv_map.get("name");
                        if(kv_map.containsKey("place")){
                            String place = kv_map.get("place");
                            name = name.toLowerCase();
                            Address addr = Address.parse(name); //Parse places like addresses - they only have a name. Examples: Roskilde, Slotsholmskanelen, Vindinge.
                            //System.out.println(name);
                            if(place.equals("town")){
                                addressMap.put(addr,currentCoord);
                                addressList.add(addr);
                            } else if (place.equals("village")){
                               addressMap.put(addr,currentCoord);
                               addressList.add(addr);
                            } else if (place.equals("surburb")){
                                addressMap.put(addr,currentCoord);
                                addressList.add(addr);
                            } else if (place.equals("locality")) {
                                addressMap.put(addr,currentCoord);
                                addressList.add(addr);
                            } else if (place.equals("neighbourhood")){
                                addressMap.put(addr,currentCoord);
                                addressList.add(addr);
                            }
                        }

                    } else if (kv_map.containsKey("addr:street")){
                        if(hasHouseNo && hasCity && hasPostcode){
                            String addressString = streetName + " " + houseNumber + " " + postCode + " " + cityName;
                            addressString = addressString.toLowerCase();
                            Address addr = Address.parse(addressString);
                            //System.out.println(addressString + ", " + currentCoord);
                            //System.out.println(addr.toString());
                            addressMap.put(addr, currentCoord);
                            addressList.add(addr);
                        }
                    }




                    //else if (kv_map.containsKey("addr:city")) addCityName();
                    //else if (kv_map.containsKey("addr:postcode")) addPostcode();


                    break;

                case "osm": //The end of the osm file
                    Collections.sort(addressList, new AddressComparator()); //iterative mergesort. ~n*lg(n) comparisons
                    break;

                }


        }



        private void addAddress(){

        }


        /**
         * Adds all unique cities parsed from the xml file to an arrayList
         */
        private void addCityName(){if(!cityNames.contains(cityName)){ cityNames.add(cityName);}}

        /**
         * Adds all unique postCodes parsed from the xml file to an arrayList
         */
        private void addPostcode(){if(!postCodes.contains(postCode)){postCodes.add(postCode);}}

        /**
         * Adds all unique addresses parsed from the xml file to an arrayList
         */
        private void addStreetName(){
            List<Shape> value = streetnameMap.get(name);
            if (value == null) {
                List<Shape> list = new ArrayList<>();
                list.add(way);
                streetnameMap.put(name, list);
            } else {
                List<Shape> list = streetnameMap.get(name);
                list.add(way);
            }
        }
    }

    public Map<String,List<Shape>> getStreetMap(){
        return streetnameMap;
    }

    /**
     * Writes all the objects of Model.Drawable to a binary file for faster loading. The order of the sequence is important!
     * @param filename File saved to
     */
    public void save(String filename) {
        long time = System.nanoTime();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                //write the boundaries and the number of shapes.
                out.writeObject(bbox.getBounds2D());
                out.writeInt(drawables.size());

                //For every object write the its
                for(Drawable d : drawables) {
                    //Write all information needed from the object order matters.
                    out.writeObject(d);
                    out.writeObject(d.getShape());
                    out.writeObject(d.getColor());
                    out.writeDouble(d.getDrawLevel());
                    out.writeInt(d.getLayerVal());
                    //TODO save all icons position and type
                    //TODO save everything!
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        System.out.printf("Model.Model save time: %d ms\n", (System.nanoTime()-time) / 1000000);

    }

    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param filename file load from
     */
    public void load(String filename) {
        /*
        long time = System.nanoTime();
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            //get the bounds of the map
            Rectangle2D rec = (Rectangle2D) in.readObject();
            bbox = new Rectangle2D.Double();
            bbox.setRect(rec);

            //First int is the number of shapes
            int i = in.readInt();
            drawables.clear();
            while(i-- > 0){
                //get information needed in correct order and add to drawables-array.
                Drawable d = (Drawable) in.readObject();
                d.getShape() = (Shape) in.readObject();
                d.getColor() = (Color) in.readObject();
                d.getDrawLevel() = in.readDouble();
                d.getLayerVal() = in.readInt();
                drawables.add(d);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.printf("Model.Model load time: %d ms\n",
                (System.nanoTime() - time) / 1000000);
        sortLayers();
        setChanged();
        notifyObservers();
        */
    }

    /**
     * Custom comparator that defines how to compare two addresses. Used when sorting a collection of addresses.
     */

    public class AddressComparator implements Comparator<Address> {
        @Override
        public int compare(Address addr1, Address addr2) {
            return addr1.compareTo(addr2);
        }
    }

    public Iterator<Shape> iterator() {
        return lines.iterator();
    }
}
