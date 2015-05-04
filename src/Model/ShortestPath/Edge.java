package Model.ShortestPath;

import Model.MapFeatures.Highway;

import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * Created by Anders on 22-04-2015.
 */
public class Edge extends Line2D.Float implements Serializable {
    private static final long serialVersionUID = 128;
    private int v;
    private int w;
    private double distance; //edge's distance
    //private Line2D edgePath;
    private boolean oneWay;
    private boolean oneWayReverse;
    private Highway highway;

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
    public Edge(int v, int w, double distance, Line2D edgePath, Highway highway) {
        setLine(edgePath);
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        //if (Double.isNaN(distance)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.distance = distance;
        //this.edgePath = edgePath;
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
        return (distance/highway.getMaxspeed())*60;
    }

    /**
     * Returns the time of walking from either endpoint to the other
     * @return the walking time of the edge
     */
    public double walkTime() {
        return (distance/5.0)*60;
    }

    /**
     * Returns the time of biking from either endpoint to the other
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

   /* public Line2D getEdgePath() {
        return edgePath;
    }*/

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
}
