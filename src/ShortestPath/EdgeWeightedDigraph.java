package ShortestPath;


import MapFeatures.Highway;
import java.util.List;

/**
 * Created by woozy_000 on 13-04-2015.
 */
public class EdgeWeightedDigraph {
    private int V; //Total amount of vertices
    private int E; //Total amount of edges
    private Bag<DiEdge>[] adj; //a bag for each vertex containing adjacent edges


    public EdgeWeightedDigraph() {
    }

    public void initialize(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<DiEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<DiEdge>();
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

    public void addEdges(List<Highway> highways) {
        for (Highway e : highways) {
            switch (e.getValue()) {
                case "motorway":
                    addEdges(e);
                    break;
                case "motorway_link":
                    addEdges(e);
                    break;
                case "trunk_link":
                    addEdges(e);
                    break;
                case "primary_link":
                    addEdges(e);
                    break;
                case "secondary_link":
                    addEdges(e);
                    break;
                case "tertiary_link":
                    addEdges(e);
                    break;
                case "trunk":
                    addEdges(e);
                    break;
                case "primary":
                    addEdges(e);
                    break;
                case "secondary":
                    addEdges(e);
                    break;
                case "tertiary":
                    addEdges(e);
                    break;
                case "unclassified":
                    addEdges(e);
                    break;
                case "residential":
                    addEdges(e);
                    break;
                case "service":
                    addEdges(e);
                    break;
                case "living_street":
                    addEdges(e);
                    break;
                case "road":
                    addEdges(e);
                    break;
            }
        }
    }


    private void addEdges(Highway e) {
        for (DiEdge diEdge : e.edges()) {
            int v = diEdge.from();
            int w = diEdge.to();
            validateVertex(v);
            validateVertex(w);
            adj[v].add(diEdge);
            E++;
        }
        addOtherEdge(e);
    }

    private void addOtherEdge(Highway e) {
        Highway u = new Highway(e.getWay(), e.getLayerVal(), e.getValue(), e.isArea(), e.getStreetName());
        for (DiEdge diEdge : u.edges()) {
            int W = diEdge.getW();
            int V = diEdge.getV();
            double weight = diEdge.getWeight();
            DiEdge di = new DiEdge(W, V, weight);
            int v = di.from();
            adj[v].add(di);
            E++;
        }
    }

    /**
     * Returns the directed edges incident from vertex <tt>v</tt>.
     * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<DiEdge> adj(int v) {
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
    public Iterable<DiEdge> edges() {
        Bag<DiEdge> list = new Bag<DiEdge>();
        for (int v = 0; v < V; v++) {
            for (DiEdge e : adj(v)) {
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
            for (DiEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}