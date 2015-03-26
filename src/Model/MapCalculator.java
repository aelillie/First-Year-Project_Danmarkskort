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

    /**
     * Calculates the destination point given a startpoint, a distance and a bearing.
     * @param startLon The longitude of the starting point
     * @param startLat The latitude of the starting point
     * @param distance The distance to the destination point
     * @param bearingDeg The bearing (Relative and clockwise from the north pole, N = 0 degrees) in degrees
     */
    public static Point2D calculateEndpoint(double startLon, double startLat, double distance, double bearingDeg){
        /*
        Formula:	φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
        λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
        where	φ is latitude, λ is longitude, θ is the bearing, δ is the angular distance d/R; d being the distance travelled, R the earth’s radius
         */
        double bearing = Math.toRadians(bearingDeg);
        startLon = Math.toRadians(startLat);
        startLat = Math.toRadians(startLat);


        double angularDistance = distance/earthRadius;
        double endLat = Math.asin( Math.sin(startLat)*Math.cos(angularDistance) + Math.cos(startLat)*Math.sin(angularDistance)*Math.cos(bearing) );
        double endLon = startLon + Math.atan2(Math.sin(bearing)*Math.sin(angularDistance)*Math.cos(startLat), Math.cos(angularDistance)-Math.sin(startLat)*Math.sin(endLat));

        return new Point2D.Double(endLon,endLat);
    }

}
