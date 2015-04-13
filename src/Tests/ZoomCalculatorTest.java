package Tests;

import View.ZoomCalculator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kevin on 09-04-2015.
 */
public class ZoomCalculatorTest {

    @Test
    public void zoomCalculation(){

        int zoom = ZoomCalculator.calculateZoom(10000);

        assertEquals(11, zoom);

        double scale = ZoomCalculator.setScale(zoom);

        assertEquals(11041, (int) scale);
    }
}
