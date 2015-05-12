package Model;

import Model.Path.Edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the routeplan by processing directions
 */
public class RoutePlanner {
    private static double edgeConstant = 5.0E-8; //Determined by a lot of experimental testing.
    private Iterable<Edge> edges;

    /**
     * Sets up fields, is used to generate directions for a path using a list of edges.
     * @param edges
     */
    public RoutePlanner(Iterable<Edge> edges){
        if(edges != null)
            this.edges = edges;
        else throw new IllegalArgumentException("No path was given");
    }


    /**
     * Goes through the edges creating the direction-Strings
     * @return - String[] of Directions.
     */
    public String[] getDirections(){
        ArrayList<String> streetNames = new ArrayList<>();      //List of StreetNames
        ArrayList<List<Edge>> streetEdges = new ArrayList<>();  //List of edges for each Street
        String prevName = null;
        int i = -1;
        for(Edge e : edges){

            String streetName = e.highway().getStreetName();

            if(streetName == null){
                streetName = "path";
            }

            //Check if start of new Street
            if(prevName == null || !prevName.equals(streetName)){
                //Create a list of edges for it.
                streetNames.add(streetName);
                List<Edge> streets = new ArrayList<>();
                streets.add(e);
                streetEdges.add(streets);
                i++;
            //else add the edge to the same streets list
            }else{
                List<Edge> streets = streetEdges.get(i);
                streets.add(e);
            }

            //update prevStreetName
            prevName = streetName;

        }


        return processRouteplanStrings(streetNames,streetEdges);
    }


    private String[] processRouteplanStrings(ArrayList<String> streetNames, ArrayList<List<Edge>> streetEdges){
        String[] directions = new String[streetNames.size()+1];
        int directionCount = 0;

        Edge endEdge = null;
        for (int i = streetNames.size(); --i >= 0;){    //Go through the array backwards due to stack iterator not working correctly
            String street = streetNames.get(i);
            List<Edge> edgesInStreets = streetEdges.get(i); //List of the edges for a street.
            Edge startEdge = edgesInStreets.get(edgesInStreets.size()-1);
            String turnD;   //string for turn information

            if(endEdge == null){
                turnD = "Follow ";
            }else{
                //Create a vector (line) in the correct direction
                Line2D vector = getDirectionVector(startEdge, endEdge);

                //Find the outer Point for street turning at.
                Point2D p = getOuterPoint(startEdge, endEdge);

                //Math Magic happens. values above 0 means the point is to left of the vector and the other way around
                double x = determinantOf2Vector(vector, p);
                if(x > 0 + edgeConstant ) {
                    //System.out.println(x + " " + street);
                    turnD = "Turn left at ";
                }else if(x < 0 - edgeConstant) {
                    //System.out.println(x + " " + street);
                    turnD = "Turn right at ";
                }else {
                    turnD = "Continue at ";
                }
            }

            endEdge = edgesInStreets.get(0); //Update end Edge for next street. Remembering that the array is backwards.

            //Sum the length of the street
            double dist = 0;
            for(Edge e : edgesInStreets){
                dist += e.distance();
            }
            dist *= 1000;

            //Set all the information together for the user.
            String distString;
            if(dist < 1000) { //If the distance is less than a kilometer, display it in meters, otherwise display it in kilometers
                distString = new DecimalFormat("####").format(dist) + " m";
            } else {
                distString = new DecimalFormat("##.##").format(dist/1000) + " km";
            }
            String direction = turnD + street + ", continue for " + distString;
            //String turnDirection =
            if(street.trim().equals("")){
                if(i != 0) direction = "Continue for " + distString + " until you reach " + streetNames.get(i-1);
                else direction = "Continue for " + distString + " until you reach your destination.";
            }
            directions[directionCount] = direction; //add string to array.
            directionCount++;
        }
        directions[directionCount] = "You have reached your destination.";
        return directions;
    }

    private Line2D getDirectionVector(Edge start, Edge end){
        int endW = end.w();

        int startV = start.v();
        int startW = start.w();
        //returns the line2D corresponding to a vector in the moving direction.
        //Meaning moving from P1 to P2.
        if(startV == endW || startW == endW){
            return new Line2D.Float(end.getP1(), end.getP2());
        }else{
            return new Line2D.Float(end.getP2(),end.getP1());
        }


    }

    private Point2D getOuterPoint(Edge start, Edge end){
        //finds what point of the start edge isn't on the end edge and returns it.
        int endV = end.v();
        int endW = end.w();

        int startV = start.v();

        if(startV == endW || startV == endV){
            return start.getP2();
        }else{
            return start.getP1();
        }


    }

    private static double determinantOf2Vector(Line2D vector, Point2D point){

        return  (vector.getX2()-vector.getX1())* (point.getY()-vector.getY1()) - (vector.getY2() - vector.getY1()) * (point.getX() - vector.getX1());

    }




}
