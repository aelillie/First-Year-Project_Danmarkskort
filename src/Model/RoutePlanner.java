package Model;

import Model.Path.Edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 04-05-2015.
 */
public class RoutePlanner {
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


   /* public String[] getDirections(){

        HashMap<String, Double> streetLengthMap = new HashMap<>();
        HashMap<String,List<Edge>> streetEdgeMap = new HashMap<>();
        ArrayList<String> streetList = new ArrayList<>();
        for (Edge e : edges) {

            //For routeplan string processing
            String streetname = e.highway().getStreetName();
            if(streetname == null) streetname = " ";

            addToEdgeMap(streetname,e,streetEdgeMap);
            Double dist = streetLengthMap.get(streetname);

            if(dist == null){
                streetLengthMap.put(streetname, e.distance());
                streetList.add(streetname);
            } else {
                streetLengthMap.put(streetname,dist+e.distance());
            }
        }

        return processRouteplanStrings(streetList,streetLengthMap, streetEdgeMap);

    }*/

    /**
     * GOes through the edges creating the direction-Strings
     * @return - String[] of Directions.
     */
    public String[] getDirections(){
        ArrayList<String> streetNames = new ArrayList<>();
        ArrayList<List<Edge>> streetEdges = new ArrayList<>();
        String prevName = null;
        int i = -1;
        for(Edge e : edges){

            String streetName = e.highway().getStreetName();

            if(streetName == null){
                streetName = "path";
            }

            if(prevName == null || !prevName.equals(streetName)){
                streetNames.add(streetName);
                List<Edge> streets = new ArrayList<>();
                streets.add(e);
                streetEdges.add(streets);
                i++;

            }else{
                List<Edge> streets = streetEdges.get(i);
                streets.add(e);
            }

            prevName = streetName;

        }


        return processRouteplanStrings(streetNames,streetEdges);
    }

   /* private void addToEdgeMap(String s, Edge e, Map<String,List<Edge>> edgeMap){
        List<Edge> currentStreet = edgeMap.get(s);
        if(currentStreet == null) {
            List<Edge> newStreet = new ArrayList<>();
            newStreet.add(e);
            edgeMap.put(s,newStreet);
        } else {
            currentStreet.add(e);
        }

    }*/

    private String[] processRouteplanStrings(ArrayList<String> streetNames, ArrayList<List<Edge>> streetEdges){
        String[] directions = new String[streetNames.size()+1];
        int directionCount = 0;

        Edge endEdge = null;
        for (int i = streetNames.size(); --i >= 0;){
            String street = streetNames.get(i);
            List<Edge> edgesInStreets = streetEdges.get(i);
            Edge startEdge = edgesInStreets.get(edgesInStreets.size()-1);
            String turnD;
            if(endEdge == null){
                turnD = "Follow ";
            }else{
                Line2D vector = getDirectionVector(startEdge, endEdge);
                Point2D p = getOuterPoint(startEdge, endEdge);
                double x = determinatOf2Vector(vector, p);
                if(x > 0 ) {
                    //System.out.println(x + " " + street);
                    turnD = "Turn left at ";
                }else {
                    //System.out.println(x + " " + street);
                    turnD = "Turn right at ";
                }
            }

            endEdge = edgesInStreets.get(0);

            double dist = 0;
            for(Edge e : edgesInStreets){
                dist += e.distance();
            }

            dist *= 1000;
            String distString;
            if(dist < 1000) { //If the distance is less than a kilometer, display it in meters, otherwise display it in kilometers
                distString = new DecimalFormat("####").format(dist) + " m";
            } else {
                distString = new DecimalFormat("##.##").format(dist/1000) + " km";
            }
            String direction = turnD + street + " for " + distString;
            //String turnDirection =
            if(street.trim().equals("")){
                if(i != 0) direction = "Continue for " + distString + " until you reach " + streetNames.get(i-1);
                else direction = "Continue for " + distString + " until you reach your destination.";
            }
            directions[directionCount] = direction;
            directionCount++;
        }
        directions[directionCount] = "You have reached your destination.";
        return directions;
    }

    private Line2D getDirectionVector(Edge start, Edge end){
        int endW = end.w();

        int startV = start.v();
        int startW = start.w();

        if(startV == endW || startW == endW){

            return new Line2D.Float(end.getP1(), end.getP2());
        }else{
            return new Line2D.Float(end.getP2(),end.getP1());
        }


    }

    private Point2D getOuterPoint(Edge start, Edge end){
        int endV = end.v();
        int endW = end.w();

        int startV = start.v();

        if(startV == endW || startV == endV){
            return start.getP2();
        }else{
            return start.getP1();
        }


    }

    /*private String[] processRouteplanStrings(ArrayList<String> streetList, HashMap<String,Double> streetLengthMap, HashMap<String,List<Edge>> streetToEdgesMap){
        String[] directions = new String[streetList.size()+1];
        int directionCount = 0;

        Edge endEdge = null;
        for (int i = streetList.size(); --i >= 0;){
            String street = streetList.get(i);
            List<Edge> streetInEdges = streetToEdgesMap.get(street);
            Edge startEdge = streetInEdges.get(0);
            String turnD;
            if(endEdge == null){
                turnD = "Follow ";
            }else{
                if(angleBetween2Lines(startEdge,endEdge) > -1) {
                    //System.out.println(angleBetween2Lines(startEdge, endEdge) + " " + street);
                    turnD = "Turn right ";
                }else {
                    //System.out.println(angleBetween2Lines(startEdge, endEdge) + " " + street);
                    turnD = "Turn left ";
                }
            }

            endEdge = streetInEdges.get(streetInEdges.size()-1);

            double dist = streetLengthMap.get(street)*1000;
            String distString;
            if(dist < 1000) { //If the distance is less than a kilometer, display it in meters, otherwise display it in kilometers
                distString = new DecimalFormat("####").format(dist) + " m";
            } else {
                distString = new DecimalFormat("##.##").format(dist/1000) + " km";
            }
            String direction = turnD + street + " for " + distString;
            //String turnDirection =
            if(street.trim().equals("")){
                if(i != 0) direction = "Continue for " + distString + " until you reach " + streetList.get(i-1);
                else direction = "Continue for " + distString + " until you reach your destination.";
            }
            directions[directionCount] = direction;
            directionCount++;
        }
        directions[directionCount] = "You have reached your destination.";
        return directions;
    }*/


    /*List<Edge> streetInEdges = streetToEdgesMap.get(street);
            Edge endEdge = streetInEdges.get(streetInEdges.size()-1);
            Point2D startPoint = new Point2D.Double(endEdge.getX1(),endEdge.getY1());
            Point2D endPoint = new Point2D.Double(endEdge.getX2(),endEdge.getY2());
            Point2D vector = new Point2D.Double(endPoint.getX()-startPoint.getX(),endPoint.getY()-startPoint.getY());

            double determinant = 0;
            double dotProduct = 0;
            if(i != 0){
                System.out.println("\n"+street);
                List<Edge> nextStreetInEdges = streetToEdgesMap.get(streetList.get(i-1));
                Point2D destination = new Point2D.Double(nextStreetInEdges.get(nextStreetInEdges.size()-1).getX2(),nextStreetInEdges.get(nextStreetInEdges.size()-1).getY2());
                System.out.println("Destination: " + streetList.get(i-1));

                determinant = vector.getX()*destination.getY()-vector.getY()*destination.getX();
                dotProduct = vector.getX()*destination.getX()+vector.getY()*destination.getY();
                System.out.println("Determinant: " + determinant);
                System.out.println("Dotproduct: " + dotProduct);
                if(determinant < 0) System.out.println("It is to the left");
                else if (determinant > 0) System.out.println("It is to the right");
            }*/



    public static double determinatOf2Vector(Line2D vector, Point2D point){

        return  Math.signum((vector.getX2()-vector.getX1())* (point.getY()-vector.getY1()) - (vector.getY2() - vector.getY1()) * (point.getX() - vector.getX1()));

    }


}
