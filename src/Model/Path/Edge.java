package Model.Path;

import Model.MapFeatures.Highway;
import Model.Model;
import Model.OSMNode;

import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * Created by Anders on 22-04-2015.
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
     *
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
        return (distance/highway.getMaxspeed())*60 + waitingTime();
    }

    public int waitingTime() {
        OSMNode v1 = vertices.getVertex(v);
        OSMNode w1 = vertices.getVertex(w);
        int waitingTime = 0;
        if (v1.trafficSignal == 1) {
            waitingTime += 0.25; //15 sec average waiting time at a traffic signal
            v1.trafficSignal++; //makes sure another this is only done once for this node
        }
        if (w1.trafficSignal == 1) {
            waitingTime += 0.25;
            w1.trafficSignal++;
        }
        return waitingTime;
    }

    public void resetWaitingCounter() {
        OSMNode v1 = vertices.getVertex(v);
        OSMNode w1 = vertices.getVertex(w);
        if (v1.trafficSignal > 1) v1.trafficSignal = 1;
        if (w1.trafficSignal > 1) w1.trafficSignal = 1;
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

    private int hasTrafficSignals() {
        OSMNode v_node = vertices.getVertex(v);
        OSMNode w_node = vertices.getVertex(w);
        int signalWaitTime = 0;
        if (v_node.trafficSignal == 1) {
            signalWaitTime += 15;
            v_node.trafficSignal++;
        }
        if (w_node.trafficSignal == 1) {
            signalWaitTime += 15;
            w_node.trafficSignal++;
        }
        return signalWaitTime;
    }

    /**
     * Returns a string representation of the edge.
     * @return a string representation of the edge
     */
    public String toString() {
        return String.format("%d(" + vertices.getVertex(v) + ")-%d(" + vertices.getVertex(w) + ") %.2f " + highway.getStreetName(), v, w, distance);
    }
}
