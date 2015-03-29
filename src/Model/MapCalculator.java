package Model;

import javafx.scene.transform.Affine;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class MapCalculator {
    private static final double earthRadius = 6372.8; // Radius of the earth in kilometers

    /**
     * Calculates the latitude to the y-coordinate using spherical Mercator projection
     * @param aLat The latitude of the point
     * @return the y-coordinate on a plane
     */
    public static double latToY(double aLat) {
        return Math.toDegrees(Math.log(Math.tan(Math.PI/4+Math.toRadians(aLat)/2)));
    }

    /**
     * Calculates the y-coordinate to the latitude using spherical Mercator projection
     * @param aY The y-coordinate of the point
     * @return the corresponding latitude
     */
    public static double yToLat(double aY) {
        return Math.toDegrees(2* Math.atan(Math.exp(Math.toRadians(aY))) - Math.PI/2);
    }


    //NOTE: The formula is only an approximation when applied to the Earth, as it is not a perfect sphere.
    // This means that the earthradius varies from 6356.752 km at the poles to 6378.137 km at the equator.
    public static double haversineDist(double lon1, double lat1, double lon2, double lat2) {

        lat1 = yToLat(lat1); //Transform back using the spherical Mercator projection
        lat2 = yToLat(lat2);

        double latDistance = Math.toRadians(lat2 - lat1); //Δlat = lat2 − lat1
        double lonDistance = Math.toRadians(lon2 - lon1); //Δlon = lon2 − lon1

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) //a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlon/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double distance = 2 * earthRadius * Math.asin(Math.sqrt(a)); //distance = 2 * R * arcsin(√a)
        return distance; //distance in kilometers
    }

}
