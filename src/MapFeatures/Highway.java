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
    private Vertices vertices =  Model.getModel().getVertices();
    private List<Edge> edges = new ArrayList<>();
    private String oneWay;
    private int maxspeed;
    private double wayTravelTime;


    public Highway(Path2D way, int layer_value, String value, boolean isArea, String streetName) {
        super(way, layer_value, value);
        this.isArea = isArea;
        if(streetName != null)
            this.streetName = streetName.intern();

    }

    public Highway() {
    }


    /**
     * Create edges between all points in the way for the current highway
     */
    public void assignEdges(List<Point2D> points) {
        for (int i = 0; i + 1 < points.size(); i++) { //Edge(s) in its order of appearance in .osm
            if(oneWay.equals("yes") || oneWay.equals("no")) {
                Point2D v = points.get(i);
                Point2D w = points.get(i + 1);
                Edge edge = new Edge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
                edge.setTravelTime(maxspeed);
                edges.add(edge);
                edge.createEdge(v, w);
            }else{ //Edge(s) in its reverse order of appearance in .osm
                assert oneWay.equals("-1");
                Point2D v = points.get(i+1);
                Point2D w = points.get(i);
                Edge edge = new Edge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
                edge.setTravelTime(maxspeed);
                edges.add(edge);
                edge.createEdge(v, w);
            }
        }
    }

    private double calcDist(Point2D v, Point2D w) {
        return MapCalculator.haversineDist(v, w);

    }

    public void setWayTravelTime() {
        double traveltime = 0;
        for (Edge e : edges) {
            traveltime += e.getTravelTime();
        }
        wayTravelTime = traveltime;
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
        //else if (value.equals("bus_guideway")) setValueName(ValueName.BUS_GUIDEWAY);
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
        List<Point2D> vertices = new ArrayList<>();
        vertices.add(this.vertices.getVertex(edges.get(0).getV()));
        for (Edge e : edges) {
            vertices.add(this.vertices.getVertex(e.getW()));
        }
        return  vertices;
    }

    public String getStreetName(){
        return streetName;
    }

    public String isOneWay() {
        return oneWay;
    }
    
    public void setOneWay(String value) {
        if(value.equals("yes")) oneWay = "yes"; //one way in normal direction
        else if(value.equals("-1")) oneWay = "-1"; //one way in reverse direction
        else oneWay = "no"; //one way not present
    }

    public void setMaxSpeed(String speedLimit) {
        int speed = 0;
        try {
            speed = Integer.parseInt(speedLimit);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        maxspeed = speed;
    }
}
