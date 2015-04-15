package Tests;

import Model.Model;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anders on 13-04-2015.
 */

public class ModelTest {
    Model m = Model.getModel();


    @Test
    public void setCurrentFilenameTest() {
        String input1 = "/user/directory/copenhagen.osm";
        String ExpOutput1 = "copenhagen";
        String input2 = "directory/aalborg.osm";
        String ExpOutput2 = "aalborg";
        String input3 = "denmark.zip";
        String ExpOutput3 = "denmark";
        String input4 = "/directory/fyn.zip";
        String ExpOutput4 = "fyn";
        String input5 = "/Valby.zip";
        String ExpOutput5 = "Valby";

        m.setCurrentFilename(input1);
        String actualOutput1 = m.getCurrentFilename();
        m.setCurrentFilename(input2);
        String actualOutput2 = m.getCurrentFilename();
        m.setCurrentFilename(input3);
        String actualOutput3 = m.getCurrentFilename();
        m.setCurrentFilename(input4);
        String actualOutput4 = m.getCurrentFilename();
        m.setCurrentFilename(input5);
        String actualOutput5 = m.getCurrentFilename();


        Assert.assertEquals(ExpOutput1, actualOutput1);
        Assert.assertEquals(ExpOutput2, actualOutput2);
        Assert.assertEquals(ExpOutput3, actualOutput3);
        Assert.assertEquals(ExpOutput4, actualOutput4);
        Assert.assertEquals(ExpOutput5, actualOutput5);
    }
}
