package Model.Path;

import Model.MapCalculator;
import Model.Model;
import Model.OSMNode;

import java.awt.geom.Point2D;
import java.util.Stack;
public class PathTree {

    private Graph G;
    private int s;                      // start
    private int d;                      // destination
    private double[] valueTo;           // valueTo[v] = distance (if shortest path) = time (if fastest path)s->d
    private Edge[] edgeTo;              // edgeTo[v] = last edge on shortest s->d path
    private IndexMinPQ<Double> pq;      // priority queue of vertices
    private boolean shortestPath;       //if not shortestPath, then fastest path
    private boolean walkRoute, carRoute, bikeRoute;
    private Vertices vertices = Model.getModel().getVertices();
    private Point2D end;


    /**
     * Creates a path tree
     * @param G the weighted graph
     * @param s the source vertex
     * @param d the destination vertex
     * @throws IllegalArgumentException if an edge distance is negative
     */
    public PathTree(Graph G, int s, int d) {
        for (Edge e : G.edges()) {
            if (e.distance() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative distance");
        }

        this.G = G;
        this.s = s;
        this.d = d;
        end = vertices.getVertex(d);
    }

    /**
     * Computes a paths tree from s to every other vertex in the weighted graph G
     * Depends on which type of route it is, and if it's the fastest or shortest path
     */
    public void initiate() {
        valueTo = new double[G.V()];
        edgeTo = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++)
            valueTo[v] = Double.POSITIVE_INFINITY; //infinite distance to all vertices
        valueTo[s] = 0.0; //distance 0 to self

        if (bikeRoute) relaxBikeRoute();
        else if (carRoute) relaxCarRoute();
        else if (walkRoute) relaxWalkRoute();
        else relaxCarRoute();
    }

    private void relaxBikeRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if(v == d) //found destination, stop relaxing edges
                break;
            for (Edge e : G.adj(v)) {
                if (!e.highway().isBikeAble()) continue;
                relaxDistance(e, v); //shortest path (also the fastest)
            }

        }
    }

    private void relaxCarRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {                                 /* 1 */
            int v = pq.delMin();
            if(v == d) //found destination, stop relaxing edges /* 5 */
                break;
            for (Edge e : G.adj(v)) {                           /* 2 */
                if (!e.highway().isDriveAble()) continue;       /* 3 */
                if (!shortestPath)                              /* 4 */
                    relaxTime(e, v); //fastest path
                else relaxDistance(e, v); //shortest path
            }

        }
    }


    private void relaxWalkRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if(v == d) //found destination, stop relaxing edges
                break;
            for (Edge e : G.adj(v)) {
                if (!e.highway().isWalkAble()) continue;
                relaxDistance(e, v); //shortest path (also the fastest)
            }

        }
    }

    // relax edge e and update pq if changed
    private void relaxDistance(Edge e, int v) {
        int w;
        if(v == e.either()) {
            w = e.other(v);
        }
        else w = e.either();

        if (valueTo[w] > valueTo[v] + e.distance()) {
            valueTo[w] = valueTo[v] + e.distance();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, valueTo[w]  + h(w));
            else                pq.insert(w, valueTo[w] + h(w));
        }
    }

    private double h(int i) {
        return MapCalculator.haversineDist(vertices.getVertex(i), end);
    }

    private void relaxTime(Edge e, int v) {
        int w;
        if(v == e.either()) {
            w = e.other(v);
        }
        else w = e.either();
        if (valueTo[w] > valueTo[v] + e.driveTime()) {
            valueTo[w] = valueTo[v] + e.driveTime();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, valueTo[w]);
            else                pq.insert(w, valueTo[w]);
        }
    }



    /**
     * Returns the length of a path from the source vertex s to vertex d.
     * @param v the destination vertex
     * @return the length of a path from the source vertex s to vertex d;
     *    Double.POSITIVE_INFINITY if no such path
     */
    public double distTo(int v) {
        return valueTo[v];
    }
    public double timeTo(int v) {
        return valueTo[v];
    }

    /**
     * Is there a path from the source vertex s to vertex d?
     * @param v the destination vertex
     * @return true if there is a path from the source vertex
     *    s to vertex d, and false otherwise
     */
    public boolean hasPathTo(int v) {
        return valueTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a path from the source vertex s to vertex d (in opposite direction).
     * @param v the destination vertex
     * @return a shortest path from the source vertex s to vertex d
     *    as an iterable of edges, and null if no such path
     */
    public Iterable<Edge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Edge> path = new Stack<>();
        int w = v;
        Edge e = edgeTo[v];
        while(e != null){
            path.push(e);
            if(w == e.either())
                w = e.other(e.either());
            else w = e.either();
            e = edgeTo[w];
        }
        return path;
    }

    public void useShortestPath() {
        shortestPath = true;
    }

    public void useFastestPath() {
        shortestPath = false;
    }


    public void useWalkRoute() {
        walkRoute = true;
        carRoute = false;
        bikeRoute = false;
    }

    public void useCarRoute() {
        walkRoute = false;
        carRoute = true;
        bikeRoute = false;
    }
    public void useBikeRoute() {
        walkRoute = false;
        carRoute = false;
        bikeRoute = true;
    }

    public boolean isWalkRoute() {
        return walkRoute;
    }

    public boolean isCarRoute() {
        return carRoute;
    }

    public boolean isBikeRoute() {
        return bikeRoute;
    }

    public Graph getG() {
        return G;
    }

    public int getS() {
        return s;
    }

    public int getD() {
        return d;
    }

    public double[] getValueTo() {
        return valueTo;
    }

    public Edge[] getEdgeTo() {
        return edgeTo;
    }

    public IndexMinPQ<Double> getPq() {
        return pq;
    }

    public boolean isShortestPath() {
        return shortestPath;
    }
}
