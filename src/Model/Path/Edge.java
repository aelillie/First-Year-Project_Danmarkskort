package Model.Path;

import Model.MapFeatures.Highway;
import Model.Model;
import Model.OSMNode;
import Model.ValueName;

import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * An edge connects two vertices.
 * It knows about the distance between its vertices
 * and the time it takes travelling from one end to the other
 */
public class Edge extends Line2D.Float implements Serializable {
    private static final long serialVersionUID = 128;
    private int v;
    private int w;
    private double distance; //edge's distance in km
    private boolean oneWay;
    private boolean oneWayReverse;
    private Highway highway;
    private Vertices vertices = Model.getModel().getVertices();

    /**
     * Initializes an edge from vertex v to vertex w
     * @param v the tail vertex
     * @param w the head vertex
     * @param distance the distance of the directed edge
     * @param edgePath line between this edge's end point
     * @param highway parent highway
     * @throws IndexOutOfBoundsException if either v or w is a negative integer
     */
    public Edge(int v, int w, double distance, Line2D edgePath, Highway highway) {
        //Sets the location of the end points of this Line2D to the same as those end points of the specified Line2D.
        setLine(edgePath);
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.highway = highway;
    }

    public int v(){
        return v;
    }

    public int w(){
        return w;
    }

    /**
     * Returns the distance of the edge.
     *
     * @return the distance of the edge
     */
    public double distance() {
        return distance;
    }

    /**
     * Returns the time of driving from either endpoint to the other
     * @return the driving time of the edge
     */
    public double driveTime() {
        return (distance/highway.getMaxspeed()) * 60;
    }


    /**
     * Returns the time of walking from either endpoint to the other
     * Using 5 km/h as average pace
     * @return the walking time of the edge
     */
    public double walkTime() {
        return (distance/5.0)*60;
    }

    /**
     * Returns the time of biking from either endpoint to the other
     * Using 15 km/h as average pace
     * @return the biking time of the edge
     */
    public double bikeTime() {
        return (distance/15.0)*60;
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

    public void setOneWay(boolean isOneWay) {
        oneWay = isOneWay;
    }

    public void setOneWayReverse(boolean isOneWayReverse) {
        oneWayReverse = isOneWayReverse;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public boolean isOneWayReverse() {
        return oneWayReverse;
    }

    public Highway highway() {
        return highway;
    }

    /**
     * Checks if a traffic signal is present
     * Average waiting time of 18 sec (30 % of a minute) used
     * @return 0 if no traffic light, 0.3 if traffic light present
     */
    public double trafficSignal() {
        double waitingTime = 0;
        if (vertices.getVertex(w).trafficSignal == ValueName.TRAFFICSIGNAL)
            waitingTime += 0.3;
        return  waitingTime;
    }

    /**
     * Returns a string representation of the edge.
     * @return a string representation of the edge
     */
    public String toString() {
        return String.format("%d(" + vertices.getVertex(v) + ")-%d(" + vertices.getVertex(w) +
                ") %.2f " + highway.getStreetName() + " " + highway.getMaxspeed() , v, w, distance);
    }
}
