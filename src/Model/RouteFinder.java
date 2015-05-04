package Model;

import MapFeatures.Highway;
import ShortestPath.Edge;
import ShortestPath.PathTree;

import java.awt.geom.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by Anders on 04-05-2015.
 */
public class RouteFinder {
    private Model model = Model.getModel();
    private int startVertex, endVertex;
    private Iterable<Edge> shortestPath, fastestPath;
    private boolean carPressed, bikePressed, walkPressed;

    public RouteFinder(Point2D startPoint, Point2D endPoint) {
        startVertex = findClosestVertex(startPoint);
        endVertex = findClosestVertex(endPoint);
    }

    public void setShortestRoute() {
        //Find shortest Path.
        PathTree shortestTree = new PathTree(model.getDiGraph(), startVertex, endVertex);
        shortestTree.useShortestPath(true);
        setTravelType(shortestTree);
        shortestTree.initiate();
        if(shortestTree.hasPathTo(endVertex))
            shortestPath = shortestTree.pathTo(endVertex);
        else
            throw new IllegalArgumentException("No shortest path found");
    }


    public void setFastestRoute() {
        PathTree fastestTree = new PathTree(model.getDiGraph(), startVertex, endVertex);
        fastestTree.useShortestPath(false);
        setTravelType(fastestTree);
        fastestTree.initiate();
        if(fastestTree.hasPathTo(endVertex))
            fastestPath = fastestTree.pathTo(endVertex);
        else
            throw new IllegalArgumentException("No fastest path found");
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
    public static Highway findNearestHighway(Point2D position, Collection<MapData> node)throws NoninvertibleTransformException{

        MapFeature champion = null;
        Line2D championLine = null;

        for (MapData mp : node) {
            if (mp instanceof Highway ) {
                Highway highway = (Highway) mp;
                double[] points = new double[6];

                PathIterator pI = highway.getWay().getPathIterator(new AffineTransform());

                pI.currentSegment(points);
                Point2D p1 = new Point2D.Double(points[0], points[1]);
                pI.next();
                while(!pI.isDone()) {
                    pI.currentSegment(points);
                    Point2D p2 = new Point2D.Double(points[0], points[1]);

                    Line2D path = new Line2D.Double(p1,p2);
                    p1 = p2;
                    pI.next();
                    if(championLine == null) {
                        championLine = path;
                        champion = highway;
                    }
                    else if(path.ptSegDist(position) < championLine.ptSegDist(position)){
                        champion = highway;
                        championLine = path;

                    }
                }
            }
        }
        return (Highway) champion;
    }

    private int findClosestVertex(Point2D chosenPoint){
        Rectangle2D Box = new Rectangle2D.Double(chosenPoint.getX()-0.005,
                chosenPoint.getY()-0.005, 0.01 , 0.01);


        //Extract all streets around the start address and find the closest vertex
        Collection<MapData> streets = model.getVisibleBigRoads(Box, false);
        streets.addAll(model.getVisibleStreets(Box,false));
        Highway way = null;
        try {
            way = findNearestHighway(chosenPoint, streets);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
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
        return closestEdge.either();
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
}
