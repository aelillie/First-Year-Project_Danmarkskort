package View;

/**
 * Created by Kevin on 18-03-2015.
 */
public class ZoomCalculator {


    public static double setScale(int zoom){

        return 200 * Math.pow(1.2, zoom*2);

    }



    public static int calculateZoom(double scale){
        double[] scaleNiveau = new double[20];

        int x = 0;
        for(int i = 0; i < scaleNiveau.length; i ++){
            scaleNiveau[i] = 200 * Math.pow(1.2 , x);

            x += 2;
        }

        double distance =  Math.abs(scaleNiveau[0] - scale);

        int idx = 0;

        for(int c = 1; c < scaleNiveau.length; c++){
            double cDistance = Math.abs(scaleNiveau[c] - scale);
            if(cDistance < distance){
                idx = c;
                distance = cDistance;
            }

        }

        return idx;

    }

}


