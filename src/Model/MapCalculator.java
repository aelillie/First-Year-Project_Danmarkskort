package Model;

import javafx.scene.transform.Affine;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public class MapCalculator {
    private static final double earthRadius = 6372.8; // Radius of the earth in kilometers



    public static double haversineDist(Point2D point1, Point2D point2) {
        //NOTE: The formula is only an approximation when applied to the Earth, as it is not a perfect sphere.
        // This means that the earthradius varies from 6356.752 km at the poles to 6378.137 km at the equator.

        double lat1 = point1.getY();
        double lon1 = point1.getX();

        double lat2 = point2.getY();
        double lon2 = point2.getX();

        double latDistance = Math.toRadians(lat2 - lat1); //Δlat = lat2 − lat1
        double lonDistance = Math.toRadians(lon2 - lon1); //Δlon = lon2 − lon1

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) //a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlon/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

         double distance = 2 * earthRadius * Math.asin(Math.sqrt(a)); //distance = 2 * R * arcsin(√a)
        return distance; //distance in kilometers
    }

    public static double getCurrentScaleDistance(AffineTransform transform){
        /*
        Pseudo code (for use in scalebar)

        Find the width of the current zoomed bounding box:
        Find the start x of the width using translation and scale instance -  Modregn translation og scale
        getTranslateX*getScaleX


        Find endwidth of the current zoomed bounding box
        Find the end x of the width using translation and scale
        getTranslateX*getScaleX + bbox.getWidth * transform.getScale


         */

        return 0; //Do nothing for now - work in progress!
    }

}
