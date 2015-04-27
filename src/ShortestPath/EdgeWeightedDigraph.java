package ShortestPath;


import MapFeatures.Highway;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by woozy_000 on 13-04-2015.
 */
public class EdgeWeightedDigraph implements Serializable{
    private static final long serialVersionUID = 2;
    private int V; //Total amount of vertices
    private int E; //Total amount of edges
    private Bag<Edge>[] adj; //a bag for each vertex containing adjacent edges


    public EdgeWeightedDigraph() {
    }

    public void initialize(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public void addEdges(Collection<Highway> highways) {
        for (Highway highway : highways) {
            String value = highway.getValue();
            if(value.equals("footway") || value.equals("cycleway") || value.equals("steps") ||
                    value.equals("path") || value.equals("bridleway"))
                continue;
            addEdges(highway);
        }
    }


    private void addEdges(Highway way) {
        for (Edge edge : way.edges()) {
            if (way.isOneWay().equals("no")) { //if it's NOT a one way street
                int v = edge.from();
                int w = edge.to();
                validateVertex(v);
                validateVertex(w);
                Edge diEdge = new Edge(w,v, edge.distance());
                diEdge.setWay(edge.getWay());
                if(edge.getWay() == null)
                    System.out.print(way.getStreetName() + " ");
                adj[w].add(diEdge);
                adj[v].add(edge);
                E += 2;
            } else {
                int v = edge.from();
                int w = edge.to();
                validateVertex(v);
                validateVertex(w);
                adj[v].add(edge);
                E++;
            }
        }
    }
    


    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     * @return the outdegree of vertex <tt>v</tt>
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns all directed edges in the edge-weighted digraph.
     * To iterate over the edges in the edge-weighted graph, use foreach notation:
     * <tt>for (DirectedEdge e : G.edges())</tt>.
     * @return all edges in the edge-weighted graph as an Iterable.
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of the edge-weighted digraph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}