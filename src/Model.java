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
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Model extends Observable implements Iterable<Shape> {

    private List<Shape> lines = new ArrayList<>();
    private List<Icon> icons = new ArrayList<>();
    private List<List<Point2D>> coastlinesInCoords = new ArrayList<>();
    private Map<String,List<Shape>> streetnameMap = new HashMap<>();

    public List<Icon> getIcons() {
        return icons;
    }

    List<Drawable> drawables = new ArrayList<>();

    Rectangle2D bbox;


    public Model(String filename) {
        long time = System.nanoTime();
        if (filename.endsWith(".txt")) parseTXT(filename);
        else if (filename.endsWith(".osm")) parseOSM(filename);
        else if (filename.endsWith(".zip")) parseZIP(filename);
        else System.err.println("File not recognized");
        System.out.printf("Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }


    class OSMHandler extends DefaultHandler {
        Map<Long, Point2D> map = new HashMap<>(); //holder koordinater og et id
        Map<String, String> kv_map = new HashMap<>(); //holder keys og values i et "tag"
        Map<Long, String> role_map = new HashMap<>();
        List<Point2D> coords = new ArrayList<>(); //gemmer koordinater fra en reference
        Path2D way; //<way>
        Point2D currentCoord;
        private boolean isArea;
        private boolean isBusstop;
        private boolean isMetro;
        private boolean isSTog; //S-tog
        private boolean hasName;
        private String streetName;

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
                    if(k.equals("name")){
                        hasName = true;
                        streetName = v;
                    }
                    break;
                case "member":
                    long ref = Long.parseLong(atts.getValue("ref"));
                    String role = atts.getValue("role");
                    role_map.put(ref, role);
                    break;
            }
        }

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
                        drawables.add(new Line(way, Drawable.seawater, 4, -1.0));
                        //processCoastpoints(coords);
                    }
                    if (val.equals("wood")) drawables.add(new Area(way, Drawable.scrub, -2.0));
                    if (val.equals("scrub")) drawables.add(new Area(way, Drawable.scrub, -1.5));
                    if (val.equals("heath")) drawables.add(new Area(way, Drawable.heath, -2.0));
                    if (val.equals("grassland")) drawables.add(new Area(way, Drawable.grassland, -2.0));
                    if (val.equals("sand")) drawables.add(new Area(way, Drawable.sand, -2.0));
                    if (val.equals("scree")) drawables.add(new Area(way, Drawable.scree, -2.0));
                    if (val.equals("fell")) drawables.add(new Area(way, Drawable.fell, -2.0));
                    if (val.equals("water")) drawables.add(new Area(way, Drawable.water, -2.0));
                    if (val.equals("wetland")) drawables.add(new Area(way, Drawable.wetland, -2.0));
                }
                else if (kv_map.containsKey("waterway")) {
                    String val = kv_map.get("waterway");
                    if (val.equals("riverbank")) drawables.add(new Area(way, Drawable.seawater, -1.0));
                    if (val.equals("stream")) drawables.add(new Line(way, Drawable.seawater, 1, -1.0));
                    if (val.equals("canal")) drawables.add(new Line(way, Drawable.seawater, 2, -1.0));
                    if (val.equals("river")) drawables.add(new Line(way, Drawable.seawater, 2, -1.0));
                    if (val.equals("dam")) drawables.add(new Line(way, Drawable.seawater, 2, -1.0));

                }
                else if (kv_map.containsKey("leisure")) {
                    String val = kv_map.get("leisure");
                    if (val.equals("garden")) drawables.add(new Area(way, Drawable.garden, -1.2));
                    if (val.equals("park")) drawables.add(new Area(way, Drawable.park,-1.2));
                    if (val.equals("common")) drawables.add(new Area(way, Drawable.scrub, -1.2));
                }
                else if (kv_map.containsKey("landuse")) {
                    String val = kv_map.get("landuse");
                    if (val.equals("cemetery")) drawables.add(new Area(way, Drawable.garden, -0.4));
                    if (val.equals("construction")) drawables.add(new Area(way, Drawable.park, -0.4));
                    if (val.equals("grass")) drawables.add(new Area(way, Drawable.park, -0.4));
                    if (val.equals("greenfield")) drawables.add(new Area(way, Drawable.darkgreen, -0.4));
                    if (val.equals("industrial")) drawables.add(new Area(way, Drawable.darkgreen, -0.4));
                    if (val.equals("orchard")) drawables.add(new Area(way, Drawable.darkgreen, -0.4));

                }
                else if (kv_map.containsKey("geological")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }

                else if (kv_map.containsKey("building")) {
                    drawables.add(new Area(way, Drawable.building, -0.5));
                }
                else if (kv_map.containsKey("shop")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("tourism")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("man_made")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("military")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("historic")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("craft")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("emergency")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("aeroway")) {
                    drawables.add(new Area(way, Drawable.building, -1.0));
                }
                else if (kv_map.containsKey("amenity")) {
                    //drawables.add(new Area(way, Drawable.building));
                    String val = kv_map.get("amenity");
                    if(val.equals("parking")){
                        icons.add(new Icon(way,"data//parkingIcon.jpg"));
                        drawables.add(new Area(way,Drawable.sand, -1.0));
                    }
                }
                else if (kv_map.containsKey("barrier")) {
                    String val = kv_map.get("barrier");
                    if(val.equals("hedge")) {
                        if (isArea) drawables.add(new Area(way, Drawable.scrub, -0.3));
                        else drawables.add(new Line(way,Drawable.scrub,2, -0.3));
                    }
                }
                else if (kv_map.containsKey("boundary")) {
                    drawables.add(new Line(way, Color.WHITE, 1, -1.0));
                }
                else if (kv_map.containsKey("highway")) {
                    String val = kv_map.get("highway");
                    
                    if(hasName) addStreetName();
                    
                    if (val.equals("motorway")) drawables.add(new Line(way, Drawable.motorway, 7, -1.0));
                    else if (val.equals("motorway_link")) drawables.add(new Line(way, Drawable.motorway, 7, -1.0));
                    else if (val.equals("trunk")) drawables.add(new Line(way, Drawable.trunk, 7, -1.0));
                    else if (val.equals("trunk_link")) drawables.add(new Line(way, Drawable.trunk, 7, -1.0));
                    else if (val.equals("primary")) drawables.add(new Line(way, Drawable.primary, 6, -1.0));
                    else if (val.equals("primary_link")) drawables.add(new Line(way, Drawable.primary, 6, -1.0));
                    else if (val.equals("secondary")) drawables.add(new Line(way, Drawable.secondary, 6, -1.0));
                    else if (val.equals("secondary_link")) drawables.add(new Line(way, Drawable.secondary, 6, -0.8));
                    else if (val.equals("tertiary")) drawables.add(new Line(way, Drawable.tertiary, 5, -0.8));
                    else if (val.equals("tertiary_link")) drawables.add(new Line(way, Drawable.tertiary, 5, -0.8));
                    else if (val.equals("unclassified")) drawables.add(new Line(way, Color.WHITE, 5, -0.8));
                    else if (val.equals("residential")) drawables.add(new Line(way, Color.DARK_GRAY, 4, -1.0));
                    else if (val.equals("service")) drawables.add(new Line(way, Color.WHITE, 2, -0.8));

                    else if (val.equals("living_street")) drawables.add(new Line(way, Drawable.living_street, 2, -1.0));
                    else if (val.equals("pedestrian")) drawables.add(new Line(way, Drawable.pedestrain, 2, -0.1));
                    else if (val.equals("track")) drawables.add(new Line(way, Drawable.track, 1, 0.0));
                    else if (val.equals("bus_guideway")) drawables.add(new Line(way, Drawable.bus_guideway, 1, -0.4));
                    else if (val.equals("raceway")) drawables.add(new Line(way, Drawable.raceway, 2, -0.4));
                    else if (val.equals("road")) drawables.add(new Line(way, Drawable.road, 3, -0.4));

                    else if (val.equals("footway")) {
                        Line line = new Line(way, Drawable.footway, 12, -0.1);
                        line.setDashed();
                        drawables.add(line);
                    }
                    else if (val.equals("cycleway")) {
                        Line line = new Line(way, Drawable.cycleway, 13, -0.18);
                        line.setDashed();
                        drawables.add(line);
                    }
                    else if (val.equals("bridleway")) drawables.add(new Line(way, Drawable.bridleway, 1, -1.0));
                    else if (val.equals("steps")) drawables.add(new Line(way, Drawable.steps, 1, -1.0));
                    else if (val.equals("path")) drawables.add(new Line(way, Drawable.path, 1, -0.1));


                }
                else if (kv_map.containsKey("railway")) {
                    Line line = new Line(way, Color.DARK_GRAY, 11, -1.9);
                    line.setDashed();
                    drawables.add(line);
                }
                else if (kv_map.containsKey("bridge")) {
                    drawables.add(new Line(way, Color.GRAY, 2, -2.0));
                }
                else if (kv_map.containsKey("route")) {
                    drawables.add(new Line(way, Color.WHITE, 1, -2.0));
                }
                else {
                    lines.add(way);
                }
            } else if (qName.equals("relation")) {

            } else if (qName.equals("node")) {
                if (kv_map.containsKey("highway")) {
                    String val = kv_map.get("highway");
                    if (val.equals("bus_stop") && isBusstop) icons.add(new Icon(currentCoord, "data//busIcon.png"));
                } else if (kv_map.containsKey("railway")){
                    String val = kv_map.get("railway");
                    if(val.equals("station")) {
                        if(isMetro) icons.add(new Icon(currentCoord, "data//metroIcon.png"));
                        else if (isSTog) icons.add(new Icon(currentCoord, "data//stogIcon.png"));
                    }
                }
            }
        }

        private void addStreetName(){
            if(hasName) {
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
    }

    public Map<String,List<Shape>> getStreetMap(){
        return streetnameMap;
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

    void parseOSM(String filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(filename);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void parseTXT(String filename) {
        /* TODO: Doesn't work. NullPointerException
        try (BufferedReader input = new BufferedReader(new FileReader(filename))) {
            for (String line = input.readLine() ; line != null ; line = input.readLine()) {
                String[] words = line.split(" ");
                lines.add(new Line2D.Double(
                        Double.parseDouble(words[0]),
                        Double.parseDouble(words[1]),
                        Double.parseDouble(words[2]),
                        Double.parseDouble(words[3])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } */
    }

    public void save(String filename) {
/*		long time = System.nanoTime();
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
