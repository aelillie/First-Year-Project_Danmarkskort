package Model;


import MapFeatures.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

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
    List<Long> refs = new ArrayList<>(); //references
    List<Point2D> coords = new ArrayList<>(); //referenced coordinates
    Path2D way; //<way> tag. A way is the path from one coordinate to another
    Long id;
    Point2D currentCoord; //current coordinate read
    private boolean isArea, isBusstop, isMetro, isSTog, hasName, hasHouseNo, hasPostcode, hasCity, isStart; //controls how shapes should be added
    private String name;
    private String streetName, houseNumber,cityName, postCode;
    private List<MapIcon> mapIcons = new ArrayList<>(); //contains all the icons to be drawn

    private static List<Coastline> coastlines = new ArrayList<>();
    private Point2D startPoint;
    private Point2D endPoint;

    protected Rectangle2D bbox = new Rectangle2D.Double();

    public List<MapFeature> getMapFeatures() {
        return mapFeatures;
    }

    private List<MapFeature> mapFeatures = new ArrayList<>();

    private List<Address> addressList = new ArrayList<>();
    private Map<Address,Point2D> addressMap = new HashMap<>(); //Contains relevant places parsed as address objects (e.g. a place Roskilde or an address Lauravej 38 2900 Hellerup etc.) linked to their coordinate.

    public List<MapIcon> getMapIcons() {
        return mapIcons;
    }



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

                if(isStart){ //Saves startpoint (for use in coastlines)
                    startPoint = coord;
                    isStart = false;
                }
                 endPoint = coord;
                if (coord == null) return;
                coords.add(coord);
                break;
            }
            case "way":
                kv_map.clear();
                coords.clear();
                isArea = false;
                hasName = false;
                isStart = true;
                id = Long.parseLong(atts.getValue("id"));
                break;
            case "bounds":
                double minlat = Double.parseDouble(atts.getValue("minlat"));
                double minlon = Double.parseDouble(atts.getValue("minlon"));
                double maxlat = Double.parseDouble(atts.getValue("maxlat"));
                double maxlon = Double.parseDouble(atts.getValue("maxlon"));
                bbox.setRect( new Rectangle2D.Double(minlon, minlat, maxlon - minlon, maxlat - minlat));
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
                    if (val.equals("coastline")) Coastline.processCoastlines(way, startPoint, endPoint);

                } else if (kv_map.containsKey("waterway")) { //##New key!
                    mapFeatures.add(new Waterway(way, getLayer(), kv_map.get("waterway"), isArea));

                } else if (kv_map.containsKey("leisure")) { //##New key!
                    mapFeatures.add(new Leisure(way, getLayer(), kv_map.get("leisure")));

                } else if (kv_map.containsKey("landuse")) { //##New key!
                    mapFeatures.add(new Landuse(way, getLayer(), kv_map.get("landuse"), isArea));

                } else if (kv_map.containsKey("geological")) { //##New key!
                    mapFeatures.add(new Geological(way, getLayer(), kv_map.get("geological")));

                } else if (kv_map.containsKey("building")) { //##New key!
                    mapFeatures.add(new Building(way, getLayer(), kv_map.get("building")));

                } else if (kv_map.containsKey("shop")) { //##New key!
                    mapFeatures.add(new Shop(way, getLayer(), kv_map.get("shop")));

                } else if (kv_map.containsKey("tourism")) { //##New key!
                    mapFeatures.add(new Tourism(way, getLayer(), kv_map.get("tourism")));

                } else if (kv_map.containsKey("man_made")) { //##New key!
                    mapFeatures.add(new ManMade(way, getLayer(), kv_map.get("man_made")));
                } else if (kv_map.containsKey("military")) { //##New key!


                } else if (kv_map.containsKey("historic")) { //##New key!
                    mapFeatures.add(new Historic(way, getLayer(), kv_map.get("historic")));

                } else if (kv_map.containsKey("craft")) { //##New key!
                    mapFeatures.add(new Craft(way, getLayer(), kv_map.get("craft")));

                } else if (kv_map.containsKey("emergency")) { //##New key!
                    mapFeatures.add(new Emergency(way, getLayer(), kv_map.get("emergency")));

                } else if (kv_map.containsKey("aeroway")) { //##New key!
                    mapFeatures.add(new Aeroway(way, getLayer(), kv_map.get("aeroway")));

                } else if (kv_map.containsKey("amenity")) { //##New key!
                    mapFeatures.add(new Amenity(way, getLayer(), kv_map.get("amenity"), kv_map.containsKey("building")));
                    if (kv_map.get("amenity").equals("parking")) {
                        mapIcons.add(new MapIcon(way, "data//parkingIcon.jpg"));
                    }

                } else if (kv_map.containsKey("barrier")) { //##New key!
                    mapFeatures.add(new Barrier(way, getLayer(), kv_map.get("barrier"), isArea));

                } else if (kv_map.containsKey("boundary")) { //##New key!
                    mapFeatures.add(new Boundary(way, getLayer(), kv_map.get("boundary")));
                } else if (kv_map.containsKey("highway")) { //##New key!
                    mapFeatures.add(new Highway(way, getLayer(), kv_map.get("highway")));
                } else if (kv_map.containsKey("railway")) { //##New key!
                    mapFeatures.add(new Railway(way, getLayer(), kv_map.get("railway")));

                } else if (kv_map.containsKey("bridge")) { //##New key!
                    mapFeatures.add(new Bridge(way, getLayer(), kv_map.get("bridge")));

                } else if (kv_map.containsKey("route")) { //##New key!
                    mapFeatures.add(new Route(way, getLayer(), kv_map.get("route")));
                }

                break;

            case "relation":
                if (kv_map.containsKey("type")) {
                    String val = kv_map.get("type");
                    if (val.equals("multipolygon")) {
                        Path2D path = MultipolygonCreater.setUpMultipolygon(refs, relations);
                        if(path == null) return;
                        if (kv_map.containsKey("building"))
                            mapFeatures.add(new Multipolygon(path, getLayer(), "building"));

                        if(kv_map.containsKey("place")){
                            //TODO islets

                        }
                        /*else if (kv_map.containsKey("natural"))
                            drawables.add(new Model.Area(path, Model.Drawable.water, -1.5));*/
                        //TODO How do draw harbor.
                    }

                        //TODO look at busroute and so forth
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
                        Address addr = Address.newTown(name); //Parse places like addresses - they only have a name. Examples: Roskilde, Slotsholmskanelen, Vindinge.
                        //System.out.println(name);
                        if(place.equals("town")){
                            addressMap.put(addr,currentCoord);
                            addressList.add(addr);
                        } else if (place.equals("village")){
                            addressMap.put(addr,currentCoord);
                            addressList.add(addr);
                        }/* else if (place.equals("surburb")){
                            addressMap.put(addr,currentCoord);
                            addressList.add(addr);
                        } else if (place.equals("locality")) {
                            addressMap.put(addr,currentCoord);
                            addressList.add(addr);
                        } else if (place.equals("neighbourhood")){
                            addressMap.put(addr,currentCoord);
                            addressList.add(addr);
                        }*/
                    }

                } else if (kv_map.containsKey("addr:street")){
                    if(hasHouseNo && hasCity && hasPostcode){
                        Address addr = Address.newAddress(streetName, houseNumber, postCode, cityName);
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
                sortLayers();
                Collections.sort(addressList, new AddressComparator()); //iterative mergesort. ~n*lg(n) comparisons
                break;

        }


    }

    /**
     * Sorts the Model.Drawable elements in the drawables list from their layer value.
     * Takes use of a comparator, which compares their values.
     */

    protected void sortLayers() {
        Comparator<MapFeature> comparator = new Comparator<MapFeature>() {
            @Override
            /**
             * Compares two Model.Drawable objects.
             * Returns a negative integer, zero, or a positive integer as the first argument
             * is less than, equal to, or greater than the second.
             */
            public int compare(MapFeature o1, MapFeature o2) {
                if (o1.getLayerVal() < o2.getLayerVal()) return -1;
                else if (o1.getLayerVal() > o2.getLayerVal()) return 1;
                return 0;
            }
        };
        Collections.sort(mapFeatures, comparator); //iterative mergesort. ~n*lg(n) comparisons
        //TODO Consider quicksort (3-way). Keep in mind duplicate keys are often encountered.
    }




    private int getLayer() {
        int layer_val = 0; //default layer, if no value is defined in the OSM
        try {
            layer_val = Integer.parseInt(kv_map.get("layer")); //fetch OSM-defined layer value
        } catch (NumberFormatException e) {
            return layer_val; //return the default
        }
        return layer_val;
    }

    public void searchForAddressess(Address add){
        AddressSearcher.searchForAddresses(add, addressList, addressMap);

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



    /**
     * Recursive binary search method taking lower- and higher bounds as input. Takes O(log N) time.
     * @param list The list to be searched.
     * @param addr The address we are searching for.
     * @param low The lower bound of the part of the array we want to search.
     * @param high The higher bound of the part of the array we want to search.
     * @return The index at which we found the element.
     */



}
