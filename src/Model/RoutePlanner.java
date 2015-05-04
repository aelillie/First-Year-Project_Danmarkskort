package Model;

import Model.Path.Edge;

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
     * Sets up fields
     * @param edges
     */
    public RoutePlanner(Iterable<Edge> edges){
        if(edges != null)
            this.edges = edges;
        else throw new IllegalArgumentException("No path was given");
    }

    /**
     * GOes through the edges creating the direction-Strings
     * @return - String[] of Directions.
     */
    public String[] getDirections(){

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


    }

    private void addToEdgeMap(String s, Edge e, Map<String,List<Edge>> edgeMap){
        List<Edge> currentStreet = edgeMap.get(s);
        if(currentStreet == null) {
            List<Edge> newStreet = new ArrayList<>();
            newStreet.add(e);
            edgeMap.put(s,newStreet);
        } else {
            currentStreet.add(e);
        }

    }

    private String[] processRouteplanStrings(ArrayList<String> streetList, HashMap<String,Double> streetLengthMap, HashMap<String,List<Edge>> streetToEdgesMap){
        String[] directions = new String[streetList.size()+1];
        int directionCount = 0;
        for (int i = streetList.size(); --i >= 0;){
            String street = streetList.get(i);

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


            double dist = streetLengthMap.get(street)*1000;
            String distString;
            if(dist < 1000) { //If the distance is less than a kilometer, display it in meters, otherwise display it in kilometers
                distString = new DecimalFormat("####").format(dist) + " m";
            } else {
                distString = new DecimalFormat("##.##").format(dist/1000) + " km";
            }
            String direction = "Follow " + street + " for " + distString;
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
    }


}
