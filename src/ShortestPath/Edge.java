package ShortestPath;

import Model.PathCreater;
import Model.Model;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Created by Anders on 22-04-2015.
 */
public class Edge implements Serializable {
    private static final long serialVersionUID = 128;
    private Vertices vertices = Model.getModel().getVertices();
    private int v;
    private int w;
    private double distance; //edge's weight
    private Path2D edge;
    private double travelTime; //min pr. distance

    /**
     * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt> with
     * the given <tt>distance</tt>.
     *
     * @param v      the tail vertex
     * @param w      the head vertex
     * @param distance the distance of the directed edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *                                             is a negative integer
     * @throws IllegalArgumentException            if <tt>distance</tt> is <tt>NaN</tt>
     */
    public Edge(int v, int w, double distance) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(distance)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.distance = distance;
    }

    /**
     * Returns the tail vertex of the directed edge.
     *
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     *
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    /**
     * Returns the distance of the directed edge.
     *
     * @return the distance of the directed edge
     */
    public double weight() {
        return distance;
    }

    /**
     * Returns either endpoint of the edge.
     * @return either endpoint of the edge
     */
    public int either() {
        return v;
    }

    /**
     * Returns the endpoint of the edge that is different from the given vertex
     * (unless the edge represents a self-loop in which case it returns the same vertex).
     * @param vertex one endpoint of the edge
     * @return the endpoint of the edge that is different from the given vertex
     *   (unless the edge represents a self-loop in which case it returns the same vertex)
     * @throws java.lang.IllegalArgumentException if the vertex is not one of the endpoints
     *   of the edge
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Returns a string representation of the directed edge.
     *
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", distance);
    }

    public int getV() {
        return v;
    }

    public int getW() {
        return w;
    }

    public double getDistance() {
        return distance;
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void createEdge(Point2D point1, Point2D point2) {
        edge = PathCreater.createWay(point1, point2);
    }

    public Path2D getWay() {
        return edge;
    }

    public void setWay(Path2D edge){this.edge = edge ;}

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int maxspeed) {
        double minPrKm = 60/maxspeed;
        travelTime = minPrKm*distance;
    }
}