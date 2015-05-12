package View;

/**
 * Created by Kevin on 18-03-2015.
 */
public class ZoomCalculator {

    /**
     * Given a zoom level it calculates the corresponding scale niveau for
     * the affineTransform
     * @param zoom - int
     * @return double scale for affineTransform
     */
    public static double setScale(int zoom){

        return 200 * Math.pow(1.2, zoom*2);

    }


    /**
     * given a scale from a affineTransform it finds the corresponding zoom niveau.
     * @param scale - double
     * @return - int zoom niveau
     */
    public static int calculateZoom(double scale){
        double[] scaleNiveau = makeValues(20);
        double distance =  Math.abs(scaleNiveau[0] - scale);
        if(scale == Double.NEGATIVE_INFINITY) return 0;
        int idx = 0;
        //Find the closest value
        for(int c = 1; c < scaleNiveau.length; c++){
            double cDistance = Math.abs(scaleNiveau[c] - scale);
            if(cDistance <= distance){
                idx = c+1;
                distance = cDistance;
            }

        }

        return idx;

    }

    private static double[] makeValues(int zoomLvls){

        double[] scaleNiveau = new double[zoomLvls];

        int x = 0;
        for(int i = 0; i < scaleNiveau.length; i ++){
            scaleNiveau[i] = 200 * Math.pow(1.2 , x);

            x += 2;
        }

        return scaleNiveau;

    }

}


