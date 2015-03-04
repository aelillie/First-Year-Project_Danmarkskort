import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Model extends Observable implements Iterable<Shape>, Serializable {

    private List<Shape> lines = new ArrayList<>(); //contains all shapes to be drawn that are not in drawables
    private List<MapIcon> mapIcons = new ArrayList<>(); //contains all the icons to be drawn
    private List<List<Point2D>> coastlinesInCoords = new ArrayList<>();
    private Map<String,List<Shape>> streetnameMap = new HashMap<>();
    protected List<String> cityNames = new ArrayList<>();
    protected List<String> postCodes = new ArrayList<>();
    protected Map<String, String> streetCityMap = new HashMap<>();

    public List<MapIcon> getMapIcons() {
        return mapIcons;
    }

    List<Drawable> drawables = new ArrayList<>(); //Shapes to be drawn differently
    List<Drawable> firstLayer = new ArrayList<>();
    List<Drawable> secondLayer = new ArrayList<>();
    List<Drawable> thirdLayer = new ArrayList<>();
    List<Drawable> lastLayer = new ArrayList<>();

    Rectangle2D bbox;


    /**
     * Constructor, which either parses .osm or .zip files
     * @param filename either .osm or .zip (containing .osm) format
     */
    public Model(String filename) {
        long time = System.nanoTime();
        if (filename.endsWith(".osm")) parseOSM(filename);
        else if (filename.endsWith(".zip")) parseZIP(filename);
        else System.err.println("File not recognized");
        sortLayers();
        System.out.printf("Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }

    /**
     * Parses .osm files and sets a content handler
     * @param filename .osm format
     */
    void parseOSM(String filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(filename);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses .zip files and sets a content handler
     * @param filename .zip format, containing .osm
     */
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
     * Contenthandler, which handles the .osm file written in XML.
     * Saving data from the file, which is being parsed, through keys and tags read.
     * Ultimately adding shapes to be drawn to lists in this class.
     */
    class OSMHandler extends DefaultHandler {
        Map<Long, Point2D> map = new HashMap<>(); //Relation between a nodes' id and coordinates
        Map<String, String> kv_map = new HashMap<>(); //relation between the keys and values in the XML file
        Map<String, String> layer_map = new HashMap<>();
        Map<Long, String> role_map = new HashMap<>(); //
        List<Point2D> coords = new ArrayList<>(); //referenced coordinates
        Path2D way; //<way> tag. A way is the path from one coordinate to another
        Point2D currentCoord; //current coordinate read
        private boolean isArea, isBusstop, isMetro, isSTog, hasName; //controls how shapes should be added
        private String streetName;
        private String cityName;
        private String postCode;


        /**
         * Reads start elements and handles what to be done from the data associated with the element.
         * @param uri The namespace URI
         * @param localName the local name (without prefix)
         * @param qName the qualified name (with prefix)
         * @param atts the attributes attached to the element
         */
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            switch (qName) { //if qName.equals(case)
                case "node": {
                    kv_map.clear();
                    isBusstop = false;
                    isMetro = false;
                    isSTog = false;


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
                    if(k.equals("addr:name")){
                        hasName = true;
                        streetName = v;
                    }
                    if(k.equals("addr:city")){
                        cityName = v;
                    }
                    if(k.equals("addr:postcode")){
                        postCode = v;
                    }
                    break;
                case "member":
                    long ref = Long.parseLong(atts.getValue("ref"));
                    String role = atts.getValue("role");
                    role_map.put(ref, role);
                    break;
            }
        }

        private int getLayer() {
            int layer;
            try {
                layer = Integer.parseInt(kv_map.get("layer"));
            } catch (NumberFormatException e) {
                layer = 0;
            }
            return layer;
        }

        /**
         * Reads end elements and handles what to be done from the data associated with the element.
         * @param uri the namespace URI
         * @param localName the local name (without prefix)
         * @param qName the qualified CML name (with prefix)
         */
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("way")) {
                way = new Path2D.Double();
                Point2D coord = coords.get(0);
                way.moveTo(coord.getX(), coord.getY());
                for (int i = 1; i < coords.size(); i++) {
                    coord = coords.get(i);
                    way.lineTo(coord.getX(), coord.getY());
                }
                if (kv_map.containsKey("natural")) {
                    String val = kv_map.get("natural");
                    if (val.equals("coastline")){
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
                }

                else if (kv_map.containsKey("waterway")) {
                    String val = kv_map.get("waterway");
                    if (val.equals("riverbank")) drawables.add(new Area(way, Drawable.lightblue, -1.0, getLayer()));
                    if (val.equals("stream")) drawables.add(new Line(way, Drawable.lightblue, 1, -1.0, getLayer()));
                    if (val.equals("canal")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));
                    if (val.equals("river")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));
                    if (val.equals("dam")) drawables.add(new Line(way, Drawable.lightblue, 2, -1.0, getLayer()));

                }
                else if (kv_map.containsKey("leisure")) {
                    String val = kv_map.get("leisure");
                    if (val.equals("garden")) drawables.add(new Area(way, Drawable.whitegreen, -1.2, getLayer()));
                    if (val.equals("park")) drawables.add(new Area(way, Drawable.whitegreen,-1.2, getLayer()));
                    if (val.equals("common")) drawables.add(new Area(way, Drawable.neongreen, -1.2, getLayer()));
                }
                else if (kv_map.containsKey("landuse")) {
                    String val = kv_map.get("landuse");
                    if (val.equals("cemetery")) drawables.add(new Area(way, Drawable.whitegreen, -0.4, getLayer()));
                    if(val.equals("construction")) {
                        if (isArea) drawables.add(new Area(way, Drawable.lightgreen, -0.3, -1));
                        else drawables.add(new Line(way,Drawable.lightgreen,2, -0.3, getLayer()));
                    }
                    if (val.equals("grass")) drawables.add(new Area(way, Drawable.whitegreen, -0.4, getLayer()));
                    if (val.equals("greenfield")) drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                    if (val.equals("industrial")) drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                    if (val.equals("orchard")) drawables.add(new Area(way, Drawable.darkgreen, -0.4, getLayer()));
                }
                else if (kv_map.containsKey("geological")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("building")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -0.5, getLayer()));
                }
                else if (kv_map.containsKey("shop")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("tourism")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("man_made")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("military")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("historic")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("craft")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("emergency")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("aeroway")) {
                    drawables.add(new Area(way, Drawable.lightgrey, -1.0, getLayer()));
                }
                else if (kv_map.containsKey("amenity")) {
                    //drawables.add(new Area(way, Drawable.lightgrey));
                    String val = kv_map.get("amenity");
                    if(val.equals("parking")){
                        mapIcons.add(new MapIcon(way,"data//parkingIcon.jpg"));
                        drawables.add(new Area(way,Drawable.sand, -1.0, getLayer()));
                    }
                }
                else if (kv_map.containsKey("barrier")) {
                    String val = kv_map.get("barrier");
                    if(val.equals("hedge")) {
                        if (isArea) drawables.add(new Area(way, Drawable.neongreen, -0.3, getLayer()));
                        else drawables.add(new Line(way,Drawable.neongreen,2, -0.3, getLayer()));
                    }
                    if(val.equals("fence")) {
                        if (isArea) drawables.add(new Area(way, Drawable.neongreen, -0.3, getLayer()));
                        else drawables.add(new Line(way,Drawable.neongreen,2, -0.3, getLayer()));
                    }
                }
                else if (kv_map.containsKey("boundary")) {
                    Line line = new Line(way, Color.WHITE, 14, 2.0, getLayer());
                    line.setDashed();
                    drawables.add(line);
                }
                else if (kv_map.containsKey("highway")) {
                    String val = kv_map.get("highway");
                    
                    if(hasName) addStreetName();
                    
                    if (val.equals("motorway")) drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
                    else if (val.equals("motorway_link")) drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
                    else if (val.equals("trunk")) drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
                    else if (val.equals("trunk_link")) drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
                    else if (val.equals("primary")) drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
                    else if (val.equals("primary_link")) drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
                    else if (val.equals("secondary")) drawables.add(new Line(way, Drawable.lightred, 6, -1.0, getLayer()));
                    else if (val.equals("secondary_link")) drawables.add(new Line(way, Drawable.lightred, 6, -0.8, getLayer()));
                    else if (val.equals("tertiary")) drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
                    else if (val.equals("tertiary_link")) drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
                    else if (val.equals("unclassified")) drawables.add(new Line(way, Color.WHITE, 5, -0.8, -1));
                    else if (val.equals("residential")) drawables.add(new Line(way, Color.DARK_GRAY, 4, -1.0, -1));
                    else if (val.equals("service")) drawables.add(new Line(way, Color.WHITE, 2, -0.8, -1));

                    else if (val.equals("living_street")) drawables.add(new Line(way, Drawable.grey, 2, -1.0, -2));
                    else if (val.equals("pedestrian")) drawables.add(new Line(way, Drawable.white, 2, -0.1, -2));
                    else if (val.equals("track")) drawables.add(new Line(way, Drawable.bloodred, 1, 0.0, getLayer()));
                    else if (val.equals("bus_guideway")) drawables.add(new Line(way, Drawable.darkblue, 1, -0.4, -2));
                    else if (val.equals("raceway")) drawables.add(new Line(way, Drawable.white, 2, -0.4, -2));
                    else if (val.equals("road")) drawables.add(new Line(way, Drawable.grey, 3, -0.4, -2));

                    else if (val.equals("footway")) {
                        Line line = new Line(way, Drawable.red, 12, -0.1, -2);
                        line.setDashed();
                        drawables.add(line);
                    }
                    else if (val.equals("cycleway")) {
                        Line line = new Line(way, Drawable.lightblue, 13, -0.18, -2);
                        line.setDashed();
                        drawables.add(line);
                    }
                    else if (val.equals("bridleway")) drawables.add(new Line(way, Drawable.lightgreen, 1, -1.0, -2));
                    else if (val.equals("steps")) {
                        Line line = new Line(way, Drawable.red, 14, -0.1, -2);
                        line.setDashed();
                        drawables.add(line);
                    }
                    else if (val.equals("path")) drawables.add(new Line(way, Drawable.red, 1, -0.1, -2));


                }
                else if (kv_map.containsKey("railway")) {
                    Line line = new Line(way, Color.DARK_GRAY, 11, -1.9, getLayer());
                    line.setDashed();
                    drawables.add(line);
                }
                else if (kv_map.containsKey("bridge")) {
                    drawables.add(new Line(way, Color.GRAY, 2, -2.0, getLayer()));
                }
                else if (kv_map.containsKey("route")) {
                    drawables.add(new Line(way, Color.WHITE, 1, -2.0, getLayer()));
                }
                else {
                    lines.add(way);
                }
            } else if (qName.equals("relation")) {

            } else if (qName.equals("node")) {
                if (kv_map.containsKey("highway")) {
                    String val = kv_map.get("highway");
                    if (val.equals("bus_stop") && isBusstop) mapIcons.add(new MapIcon(currentCoord, "data//busIcon.png"));
                } else if (kv_map.containsKey("railway")){
                    String val = kv_map.get("railway");
                    if(val.equals("station")) {
                        if(isMetro) mapIcons.add(new MapIcon(currentCoord, "data//metroIcon.png"));
                        else if (isSTog) mapIcons.add(new MapIcon(currentCoord, "data//stogIcon.png"));
                    }
                }else if(kv_map.containsKey("addr:city")) addCityName();
                else if(kv_map.containsKey("addr:postcode")) addPostcode();
            }

        }
        private void addCityName(){if(!cityNames.contains(cityName)){ cityNames.add(cityName);}}

        private void addPostcode(){if(!postCodes.contains(postCode)){postCodes.add(postCode);}}

        private void addStreetName(){
            List<Shape> value = streetnameMap.get(streetName);
            if (value == null) {
                List<Shape> list = new ArrayList<>();
                list.add(way);
                streetnameMap.put(streetName, list);
            } else {
                List<Shape> list = streetnameMap.get(streetName);
                list.add(way);
            }
        }
    }
    private void sortLayers() {
        for (Drawable drawable : drawables) {
            if (drawable.layerVal == -2) firstLayer.add(drawable);
            if (drawable.layerVal == -1) secondLayer.add(drawable);
            if (drawable.layerVal == 0) thirdLayer.add(drawable);
            if (drawable.layerVal == 1) lastLayer.add(drawable);
            //else thirdLayer.add(drawable);
        }



    }

    public Map<String,List<Shape>> getStreetMap(){
        return streetnameMap;
    }

    public void save(String filename) {
/*	long time = System.nanoTime();
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
		//try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filename))) {
			out.writeInt(lines.size());
			for (Line2D line : lines) {
				out.writeDouble(line.getX1());
				out.writeDouble(line.getY1());
				out.writeDouble(line.getX2());
				out.writeDouble(line.getY2());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.printf("Model save time: %d ms\n",
				(System.nanoTime() - time) / 1000000);*/
    }

    public void load(String filename) {
		/*
		long time = System.nanoTime();
		try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
			int n = in.readInt();
			lines.clear();
			while (n-- > 0) {
				double x1 = in.readDouble();
				double y1 = in.readDouble();
				double x2 = in.readDouble();
				double y2 = in.readDouble();
				lines.add(new Line2D.Double(x1, y1, x2, y2));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.printf("Model load time: %d ms\n",
				(System.nanoTime() - time) / 1000000);
		setChanged();
		notifyObservers();*/
    }

    public Iterator<Shape> iterator() {
        return lines.iterator();
    }
}
