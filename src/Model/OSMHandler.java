package Model;


import MapFeatures.Barrier;
import MapFeatures.Highway;
import MapFeatures.Natural;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Contenthandler, which handles the .osm file written in XML.
 * Saving data from the file, which is being parsed, through keys and tags read.
 * Ultimately adding shapes to be drawn to lists in this class.
 */
public class OSMHandler extends DefaultHandler {
    private Map<Long, Path2D> relations = new HashMap<>(); //contains all shapes to be used as relations.
    private List<List<Point2D>> coastlinesInCoords = new ArrayList<>();
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
    protected List<String> cityNames = new ArrayList<>();
    protected List<String> postCodes = new ArrayList<>();
    protected Map<String, String> streetCityMap = new HashMap<>();

    private Rectangle2D bbox;
    private List<MapFeature> mapFeatures = new ArrayList<>();


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
                    mapFeatures.add(new Natural(way, getLayer(), kv_map.get("natural")));

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
                    mapFeatures.add(new Barrier(way, getLayer(), kv_map.get("barrier"), isArea))
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
                    mapFeatures.add(new Highway(way, getLayer(), kv_map.get("highway")))
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
