package MapFeatures;

import Model.MapFeature;
import Model.Model;
import Model.ValueName;
import ShortestPath.Vertices;
import Model.MapCalculator;
import Model.PathCreater;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Highway extends MapFeature {
    private String streetName;
    private Vertices vertices =  Model.getModel().getVertices();
    private List<DiEdge> edges = new ArrayList<>();
    private List<Point2D> points = new ArrayList<>();

    public Highway(Path2D way, int layer_value, String value, boolean isArea, String streetName, List<Point2D> points) {
        super(way, layer_value, value);
        this.isArea = isArea;
        if(streetName != null)
            this.streetName = streetName.intern();
        this.points = points;
        assignEdges();
    }

    public Highway() {
    }


    /**
     * Create edges between all points in the way for the current highway
     */
    public void assignEdges() {
        for (int i = 0 ; i+1 <= points.size() ; i++) {
            Point2D v = points.get(i);
            Point2D w = points.get(i+1);
            DiEdge diEdge = new DiEdge(vertices.getIndex(v), vertices.getIndex(w), calcDist(v, w));
            edges.add(diEdge);
            diEdge.createEdge(v, w);
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

    public Iterable<DiEdge> edges() {
        return edges;
    }

    public String getStreetName(){
        return streetName;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public class DiEdge {
        private int v;
        private int w;
        private double weight;
        private Path2D edge;

        /**
         * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
         * the given <tt>weight</tt>.
         * @param v the tail vertex
         * @param w the head vertex
         * @param weight the weight of the directed edge
         * @throws java.lang.IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
         *    is a negative integer
         * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
         */
        public DiEdge(int v, int w, double weight) {
            if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
            if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
            if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        /**
         * Returns the tail vertex of the directed edge.
         * @return the tail vertex of the directed edge
         */
        public int from() {
            return v;
        }

        /**
         * Returns the head vertex of the directed edge.
         * @return the head vertex of the directed edge
         */
        public int to() {
            return w;
        }

        /**
         * Returns the weight of the directed edge.
         * @return the weight of the directed edge
         */
        public double weight() {
            return weight;
        }

        /**
         * Returns a string representation of the directed edge.
         * @return a string representation of the directed edge
         */
        public String toString() {
            return v + "->" + w + " " + String.format("%5.2f", weight);
        }

        public int getV() {
            return v;
        }

        public int getW() {
            return w;
        }

        public double getWeight() {
            return weight;
        }

        public void setV(int v) {
            this.v = v;
        }

        public void setW(int w) {
            this.w = w;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public void createEdge(Point2D point1, Point2D point2) {
            edge = PathCreater.createWay(point1, point2);
        }

        public Path2D getWay() {
            return edge;
        }
    }
}
