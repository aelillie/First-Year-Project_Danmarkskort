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

        assertEquals(12, zoom);

        double scale = ZoomCalculator.setScale(zoom);

        assertEquals(15899, (int) scale);
    }

    @Test
    public void infiniteTest(){

        int zoom = ZoomCalculator.calculateZoom(Double.POSITIVE_INFINITY);

        assertEquals(20, zoom);

        zoom = ZoomCalculator.calculateZoom(Double.NEGATIVE_INFINITY);

        assertEquals(0, zoom);
    }
}
