package Model;

import ShortestPath.Edge;

import java.awt.geom.Line2D;
import java.util.ArrayDeque;
import java.util.ArrayList;


/**
 * Created by i5-4670K on 02-05-2015.
 */
public class RoutePlaner {
    private static Line2D previousWay;

    public static String[] createRoutePlan(ArrayDeque<Edge> path ) {
        ArrayList<Edge> street = new ArrayList<>();
        ArrayList<String> directions = new ArrayList<>();
        String streetName = null;
        for (int i = 0; i < path.size(); i++) {
            Edge e = path.pop();
            streetName = e.highway().getStreetName();

            if (streetName == null) {

                boolean notNewStreet = true;
                while (i < path.size() && notNewStreet) {
                    street.add(e);
                    e = path.peek();
                    if (e.highway().getStreetName() != null) {
                        notNewStreet = false;

                    } else {
                        e = path.pop();
                        i++;
                    }
                }

                directions.add(addNewDirection(street, streetName));
            } else {

                boolean notNewStreet = true;
                while (i < path.size() && notNewStreet) {
                    street.add(e);
                    e = path.peek();
                    if (!e.highway().getStreetName().equals(streetName)) {
                        notNewStreet = false;

                    } else {
                        e = path.pop();
                        i++;
                    }
                }

                directions.add(addNewDirection(street, streetName));
            }
        }




        directions.add("You've reached your Destination");
        previousWay = null;
        return toArray(directions);

    }


    private static String addNewDirection(ArrayList<Edge> ways, String streetName){
        if (streetName == null)
            streetName = "the path";
        String direction = null;
        double distance = 0.0;
        for(Edge e : ways){
            distance += e.distance();
        }


        if(previousWay == null){
            direction = String.format("Start by following %s %.2f km", streetName, distance);

            previousWay = ways.get(ways.size() - 1);

        }else{
            double angle = angleBetween2Lines(previousWay, ways.get(0));

            if(angle < 180){
                    direction = String.format("Turn left at %s And follow %.2f km", streetName, distance);
            }else if (angle > 180){
                direction = String.format("Turn right at %s And follow %.2f km", streetName, distance);
            }

            previousWay = ways.get(ways.size()-1);
        }

        ways.clear();
        return direction;

    }


    public static double angleBetween2Lines(Line2D line1, Line2D line2)
    {
        double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                line1.getX1() - line1.getX2());
        double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                line2.getX1() - line2.getX2());
        return angle1-angle2;
    }

    private static String[] toArray(ArrayList<String> list){
        String[] s = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            s[i] = list.get(i);
        }

        return s;

    }
}
