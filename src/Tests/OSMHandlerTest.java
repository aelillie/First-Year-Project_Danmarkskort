package Tests;

import MapFeatures.*;
import Model.Model;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sun.tools.jar.Main;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * Test class for the OSMHandler class
 */
public class OSMHandlerTest {
    Model m = Model.getModel();
    InputStream inputStream;
    InputStream testStream;
    SAXParserFactory factory = SAXParserFactory.newInstance();

    /**
     * Sets up an osm test file, which enables us to cover all the functions the OSMHandler invokes
     */
    @Before
    public void loadFile() {
        try {
            inputStream = Main.class.getResourceAsStream("/data/test_map.osm");
            m.loadFile("map.osm", inputStream);

            testStream = Main.class.getResourceAsStream("/data/test_map.osm");
            SAXParser parser = factory.newSAXParser();
            parser.parse(testStream, new TestHandler());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {

    }

    class TestHandler extends DefaultHandler {

    }
}
