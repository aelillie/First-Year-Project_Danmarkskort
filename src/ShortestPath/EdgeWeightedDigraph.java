package ShortestPath;


import MapFeatures.Highway;
import Model.MapFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woozy_000 on 13-04-2015.
 */
public class EdgeWeightedDigraph {
    private int V; //Total amount of vertices
    private int E; //Total amount of edges
    private Bag<Highway>[] adj; //a bag for each vertex containing adjacent edges


    public EdgeWeightedDigraph() {
    }

    public void initialize(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Highway>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Highway>();
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

    public void addEdges(List<Highway> edges) {
        for (Highway e : edges) {
            switch (e.getValue()) {
                case "motorway":
                    addEdge(e);
                    break;
                case "motorway_link":
                    addEdge(e);
                    break;
                case "trunk_link":
                    addEdge(e);
                    break;
                case "primary_link":
                    addEdge(e);
                    break;
                case "secondary_link":
                    addEdge(e);
                    break;
                case "tertiary_link":
                    addEdge(e);
                    break;
                case "trunk":
                    addEdge(e);
                    break;
                case "primary":
                    addEdge(e);
                    break;
                case "secondary":
                    addEdge(e);
                    break;
                case "tertiary":
                    addEdge(e);
                    break;
                case "unclassified":
                    addEdge(e);
                    break;
                case "residential":
                    addEdge(e);
                    break;
                case "service":
                    addEdge(e);
                    break;
                case "living_street":
                    addEdge(e);
                    break;
                case "road":
                    addEdge(e);
                    break;
            }
        }
    }


    private void addEdge(Highway e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        E++;
        addOtherEdge(e);
    }

    private void addOtherEdge(Highway e) {
        Highway u = new Highway(e.getWay(), e.getLayerVal(), e.getValue(), e.isArea(), e.getStreetName());
        u.setV(e.getW());
        u.setW(e.getV());
        u.setWeight(e.getWeight());
        int v = u.from();
        int w = u.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(u);
        E++;
    }

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Highway> adj(int v) {
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
    public Iterable<Highway> edges() {
        Bag<Highway> list = new Bag<Highway>();
        for (int v = 0; v < V; v++) {
            for (Highway e : adj(v)) {
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
            for (Highway e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}