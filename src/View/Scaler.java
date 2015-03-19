package View;

/**
 * Created by Kevin on 18-03-2015.
 */
public class Scaler {


    public static double setScale(double scale){
        double[] scaleNiveau = new double[10];

        int x = 0;
        for(int i = 0; i < scaleNiveau.length; i ++){
            scaleNiveau[i] = 200 * Math.pow(1.2 , x);

            x += 4;
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

        return scaleNiveau[idx];

    }



    public static int calculateZoom(double scale){
        double value = 200;
        int x = 0;
        while(value+1 <= scale){

            value = 200 * Math.pow(1.2, ++x * 4);


        }
        return x;

    }

}


