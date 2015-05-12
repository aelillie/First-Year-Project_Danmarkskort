package Model;

import Model.MapFeatures.Highway;
import Model.Path.Edge;
import Model.Path.PathTree;
import Model.Path.Vertices;

import java.awt.geom.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RouteFinder {
    private Model model = Model.getModel();
    private int startVertex, endVertex;
    private Iterable<Edge> shortestPath, fastestPath;
    private boolean carPressed, bikePressed, walkPressed;
    private double travelDistance, travelTime;

    /**
     * Finds the vertices closes to the points and saves them
     * @param startPoint - Point2D
     * @param endPoint - Point2D
     */
    public RouteFinder(Point2D startPoint, Point2D endPoint) {
        startVertex = findClosestVertex(startPoint);
        endVertex = findClosestVertex(endPoint);
    }

    private void setTravelInfo(PathTree p) {
        travelDistance = 0;
        travelTime = 0;
        for (Edge e : p.pathTo(endVertex)) {
            travelDistance += e.distance();
            if (p.isBikeRoute()) travelTime += e.bikeTime();
            else if (p.isWalkRoute()) travelTime += e.walkTime();
            else if (p.isCarRoute()) travelTime += e.driveTime();
        }
    }

    /**
     * Sets its field shortest path to the shortest path from startVertex to endVertex
     */
    public void setShortestRoute() {
        //Find shortest Path.
        PathTree shortestTree = new PathTree(model.getGraph(), startVertex, endVertex);
        shortestTree.useShortestPath();
        setTravelType(shortestTree);
        shortestTree.initiate();

        if(shortestTree.hasPathTo(endVertex))
            shortestPath = shortestTree.pathTo(endVertex);
        else
            throw new IllegalArgumentException("No shortest path found for the given addresses.");
        setTravelInfo(shortestTree);
    }

    /**
     * sets its field fastest path to the shortest path from startVertex to endVertex
     */
    public void setFastestRoute() {
        PathTree fastestTree = new PathTree(model.getGraph(), startVertex, endVertex);
        fastestTree.useFastestPath();

        //Sets what travelType is chosen

        setTravelType(fastestTree);
        fastestTree.initiate();
        setTravelInfo(fastestTree);
        if(fastestTree.hasPathTo(endVertex))
            fastestPath = fastestTree.pathTo(endVertex);
        else
            throw new IllegalArgumentException("No fastest path found for the given addresses.");
    }

    private void setTravelType(PathTree p){
        if(carPressed)
            p.useCarRoute();
        else if(bikePressed)
            p.useBikeRoute();
        else if(walkPressed)
            p.useWalkRoute();
        else
            p.useCarRoute();
    }


    /**
     * Finds the Nearest Highway from the MousePosition using distance from point to lineSegment
     * @param position Position of MousePointer
     */
    public static Highway findNearestHighway(Point2D position, Collection<MapData> node){

        MapFeature champion = null;
        Line2D championLine = null;

        for (MapData mp : node) {
            if (mp instanceof Highway ) {
                Highway highway = (Highway) mp;
                double[] points = new double[6];
                //Iterate through the Path2D for better precision
                PathIterator pI = highway.getWay().getPathIterator(new AffineTransform());

                pI.currentSegment(points);
                Point2D p1 = new Point2D.Double(points[0], points[1]);
                pI.next();
                if(pI.isDone()) {
                    pI.currentSegment(points);
                    Point2D p2 = new Point2D.Double(points[0], points[1]);
                    Line2D path = new Line2D.Double(p1, p2);
                    if (check(path, championLine, position)) {
                        champion = highway;
                        championLine = path;
                    }
                }
                while(!pI.isDone()) {
                    pI.currentSegment(points);
                    Point2D p2 = new Point2D.Double(points[0], points[1]);

                    Line2D path = new Line2D.Double(p1,p2);
                    p1 = p2;
                    pI.next();

                    if (check(path, championLine, position)) {
                        champion = highway;
                        championLine = path;
                    }

                }
            }
        }
        return (Highway) champion; // return closest highway.
    }

    private static boolean check(Line2D path, Line2D championLine, Point2D position){
        if(championLine == null) {
            return true;
        }
        else if(path.ptSegDist(position) < championLine.ptSegDist(position)){   //Check if closer than champion line
            return true;

        }
        return false;
    }

    private int findClosestVertex(Point2D chosenPoint){
        Rectangle2D Box = new Rectangle2D.Double(chosenPoint.getX()-0.005,
                chosenPoint.getY()-0.005, 0.01 , 0.01);


        //Extract all streets around the start address and find the closest vertex
        Collection<MapData> streets = model.getVisibleBigRoads(Box, false);
        streets.addAll(model.getVisibleStreets(Box,false));
        Highway way = null;

        //Find closest highway to point
        filterRoads(streets);

        way = findNearestHighway(chosenPoint, streets);


        //Find the closest vertex on the
        List<Edge> edges = way.getEdges();
        Edge closestEdge = null;

        for(Edge edge : edges){
            if (closestEdge == null) {
                closestEdge = edge;
            }
            else if (closestEdge.ptSegDist(chosenPoint) > edge.ptSegDist(chosenPoint)) {
                closestEdge = edge;
            }

        }
        if(closestEdge == null) return 0;
        Vertices vertices = model.getVertices();
        if(vertices.getVertex(closestEdge.either()).distance(chosenPoint) >=
                vertices.getVertex(closestEdge.other(closestEdge.either())).distance(chosenPoint))
            return closestEdge.other(closestEdge.either());

        else return closestEdge.either();
    }


    public void carPressed() {
        carPressed = true;
        bikePressed = false;
        walkPressed = false;
    }

    public void bikePressed() {
        carPressed = false;
        bikePressed = true;
        walkPressed = false;
    }

    public void walkPressed() {
        carPressed = false;
        bikePressed = false;
        walkPressed = true;
    }

    private void filterRoads(Collection<MapData> before) {

        //Goes through a collection and removes unnecessary paths
        for (Iterator<MapData> it = before.iterator(); it.hasNext(); ) {
            Highway highway = (Highway) it.next();
            if (highway.getValue().equals("footway") || highway.getValue().equals("cycleway") ||
                    highway.getValue().equals("steps") ||
                    highway.getValue().equals("path") ||
                    highway.getValue().equals("bridleway")) {
                it.remove();

            }
        }
    }

    /**
     * returns an Iterable of edges contained in the shortestPath
     * @return - Iterable<Edge>
     */
    public Iterable<Edge> getShortestPath(){
        return shortestPath;
    }

    /**
     * returns an Iterable of edges contained in the fastest Path.
     * @return - Iterable
     */
    public Iterable<Edge> getFastestPath(){
        return fastestPath;
    }

    /**
     * Returns the travel distance for the current route
     * @return travel distance
     */
    public double getTravelDistance() {
        return travelDistance;
    }

    /**
     * Returns the travel time for the current route
     * @return travel time
     */
    public double getTravelTime() {
        return travelTime;
    }
}
