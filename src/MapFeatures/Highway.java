package MapFeatures;

import Model.MapFeature;
import Model.Model;
import Model.ValueName;
import ShortestPath.Edge;
import ShortestPath.Vertices;
import Model.MapCalculator;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Highway extends MapFeature {
    private String streetName;
    private List<Edge> edges = new ArrayList<>();
    private String oneWay;
    private int maxspeed;

    public Highway() {}

    public Highway(Path2D way, int layer_value, String value, boolean isArea, String streetName, String maxspeed) {
        super(way, layer_value, value);
        this.isArea = isArea;
        if(streetName != null)
            this.streetName = streetName.intern();
        if (maxspeed == null) //no max speed defined in OSM
            setPreDefMaxSpeed(); //default values set by OSM standards
        else
            setMaxSpeed(maxspeed);
    }

    private void setMaxSpeed(String maxspeed) {
        try {
            this.maxspeed = Integer.parseInt(maxspeed);
        } catch (NumberFormatException e) {
            setPreDefMaxSpeed();
        }
    }

    private void setPreDefMaxSpeed() {
        switch (value) {
            case "motorway":
                maxspeed = 130;
                break;
            case "primary":
                maxspeed = 80;
                break;
            case "secondary":
                maxspeed = 80;
                break;
            case "tertiary":
                maxspeed = 80;
                break;
            case "unclassified":
                maxspeed = 80;
                break;
            default:
                maxspeed = 50;
                break;
        }
    }


    /**
     * Create edges between all points in the way for the current highway
     */
    public void assignEdges(List<Point2D> points) {
        Vertices vertices = Model.getModel().getVertices();
        for (int i = 0; i + 1 < points.size(); i++) {
            switch (oneWay) {
                case "no": { //Edge(s) in its order of appearance in .osm
                    Point2D v = points.get(i);
                    Point2D w = points.get(i + 1);
                    Edge edge = new Edge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
                    edge.setTravelTime(maxspeed);
                    edges.add(edge);
                    edge.createEdge(v, w);
                    break;
                }
                case "yes": { //Edge(s) in its order of appearance in .osm (one way)
                    Point2D v = points.get(i);
                    Point2D w = points.get(i + 1);
                    Edge edge = new Edge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
                    edge.setTravelTime(maxspeed);
                    edge.setOneWay(true);
                    edges.add(edge);
                    edge.createEdge(v, w);
                    break;
                }
                default: { //Edge(s) in its reverse order of appearance in .osm
                    assert oneWay.equals("-1");
                    Point2D v = points.get(i + 1);
                    Point2D w = points.get(i);
                    Edge edge = new Edge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
                    edge.setTravelTime(maxspeed);
                    edge.setOneWayReverse(true);
                    edges.add(edge);
                    edge.createEdge(v, w);
                    break;
                }
            }
        }
    }

    private double calcDist(Point2D v, Point2D w) {
        return MapCalculator.haversineDist(v, w);

    }

    @Override
    public void setPreDefValues() {
        super.setPreDefValues();
        if (value.equals("motorway") || value.equals("motorway_link")) layer_value = 17;
        else if (value.equals("trunk") || value.equals("trunk_link")) layer_value = 16;
        else if (value.equals("primary") || value.equals("primay_link")) layer_value = 15;
        else if (value.equals("secondary") || value.equals("secondary_link")) layer_value = 14;
        else if (value.equals("tertiary") || value.equals("tertiary_link")) layer_value = 13;
        else if (value.equals("residential")) layer_value = 12;
        else if (value.equals("footway") && isArea) layer_value = 11;
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("motorway") || value.equals("motorway_link")) setValueName(ValueName.MOTORWAY);
        else if (value.equals("trunk") || value.equals("trunk_link")) setValueName(ValueName.TRUNK);
        else if (value.equals("primary") || value.equals("primay_link")) setValueName(ValueName.PRIMARY);
        else if (value.equals("secondary") || value.equals("secondary_link")) setValueName(ValueName.SECONDARY);
        else if (value.equals("tertiary") || value.equals("tertiary_link")) setValueName(ValueName.TERTIARY);
        else if (value.equals("unclassified")) setValueName(ValueName.UNCLASSIFIED);
        else if (value.equals("residential")) setValueName(ValueName.RESIDENTIAL);
        else if (value.equals("service")) setValueName(ValueName.SERVICE);
        else if (value.equals("living_street")) setValueName(ValueName.LIVING_STREET);
        else if (value.equals("pedestrian")) setValueName(ValueName.PEDESTRIAN);
        else if (value.equals("track")) setValueName(ValueName.TRACK);
        else if (value.equals("road")) setValueName(ValueName.ROAD);
        else if (value.equals("footway") && isArea) setValueName(ValueName.FOOTWAY_AREA);
        else if (value.equals("footway")) setValueName(ValueName.FOOTWAY);
        else if (value.equals("cycleway")) setValueName(ValueName.CYCLEWAY);
        else if (value.equals("bridleway")) setValueName(ValueName.BRIDLEWAY);
        else if (value.equals("steps")) setValueName(ValueName.STEPS);
        else if (value.equals("path")) setValueName(ValueName.PATH);
        else setValueName(ValueName.HIGHWAY);
    }

    public Iterable<Edge> edges() {
        return edges;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertex(int i) {
        return edges.get(i).getV();
    }

    public List<Point2D> getPoints() {
        Vertices vertices = Model.getModel().getVertices();
        List<Point2D> localVertices = new ArrayList<>();
        localVertices.add(vertices.getVertex(edges.get(0).getV()));
        for (Edge e : edges) {
            localVertices.add(vertices.getVertex(e.getW()));
        }
        return  localVertices;
    }

    public String getStreetName(){
        return streetName;
    }

    public String isOneWay() {
        return oneWay;
    }
    
    public void setOneWay(String value) {
        switch (value) {
            case "yes":
                oneWay = "yes"; //one way in normal direction
                break;
            case "-1":
                oneWay = "-1"; //one way in reverse direction
                break;
            default:
                oneWay = "no"; //one way not present
                break;
        }
    }

}
