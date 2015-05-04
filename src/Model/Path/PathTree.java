package Model.Path;

import java.util.Stack;

public class PathTree {

    private Graph G;
    private int s;
    private int d;
    private double[] valueTo;          // valueTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private boolean shortestPath;
    private boolean walkRoute, carRoute, bikeRoute;


    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
     * the edge-weighted digraph <tt>G</tt>.
     * @param G the edge-weighted digraph
     * @param s the source vertex
     * @param d the destination vertex
     * @throws IllegalArgumentException if an edge distance is negative
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public PathTree(Graph G, int s, int d) {
        for (Edge e : G.edges()) {
            if (e.distance() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative distance");
        }

        this.G = G;
        this.s = s;
        this.d = d;
    }

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

        // check optimality conditions
        // assert check(G, s);
    }

    private void relaxBikeRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v)) {
                if (!e.highway().isBikeAble()) continue;
                relaxDistance(e, v); //shortest path (also the fastest)
            }
            if(v == d) //found destination, stop relaxing edges
                break;
        }
    }

    private void relaxCarRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v)) {
                if (!e.highway().isDriveAble()) continue;
                if (!shortestPath) relaxTime(e, v); //fastest path
                else relaxDistance(e, v); //shortest path
            }
            if(v == d) //found destination, stop relaxing edges
                break;
        }
    }

        // check optimality conditions

        //assert(check(G, s));
    private void relaxWalkRoute() {
        // relax vertices in order of distance/travel time from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, valueTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v)) {
                if (!e.highway().isWalkAble()) continue;
                relaxDistance(e, v); //shortest path (also the fastest)
            }
            if(v == d) //found destination, stop relaxing edges
                break;
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
            if (pq.contains(w)) pq.decreaseKey(w, valueTo[w] + h(w));
            else                pq.insert(w, valueTo[w] + h(w));
        }
    }

    private double h(int i) {
        //TODO: A*
        return 0.0;
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
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     *    <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distTo(int v) {
        return valueTo[v];
    }
    public double timeTo(int v) {
        return valueTo[v];
    }

    /**
     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     *    <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return valueTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
     *    as an iterable of edges, and <tt>null</tt> if no such path
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


    // check optimality conditions:
    // (i) for all edges e:            valueTo[e.to()] <= valueTo[e.from()] + e.distance()
    // (ii) for all edge e on the SPT: valueTo[e.to()] == valueTo[e.from()] + e.distance()
    private boolean check(Graph G, int s) {

        // check that edge weights are nonnegative
        for (Edge e : G.edges()) {
            if (e.distance() < 0) {
                System.err.println("negative edge distance detected");
                return false;
            }
        }

        // check that valueTo[v] and edgeTo[v] are consistent
        if (valueTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("valueTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && valueTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("valueTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy valueTo[w] <= valueTo[v] + e.distance()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (valueTo[v] + e.distance() < valueTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy valueTo[w] == valueTo[v] + e.distance()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            int v = e.either();
            if (w != e.other(v)) return false;
            if (valueTo[v] + e.distance() != valueTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    public void useShortestPath(boolean use) {
        shortestPath = use;
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
}
