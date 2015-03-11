package Model;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Model extends Observable implements Iterable<Shape>, Serializable {


    /**
     * Constructor, which either parses .osm or .zip files
     * @param filename either .osm or .zip (containing .osm) format
     */
    public Model(String filename) {
        long time = System.nanoTime();
        Address.addPatterns();
        if (filename.endsWith(".osm")) parseOSM(filename);
        else if (filename.endsWith(".zip")) parseZIP(filename);
        else if (filename.endsWith(".bin")) load(filename);
        else System.err.println("File not recognized");

        System.out.printf("Model.Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }


    void parseOSM(String filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(filename);

        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    void parseZIP(String filename) {
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            zip.getNextEntry();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(new OSMHandler());
            reader.parse(new InputSource(zip));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sorts the Model.Drawable elements in the drawables list from their layer value.
     * Takes use of a comparator, which compares their values.
     */



   /* public void searchForAddresses1(Model.Address addressInput){
        int index = Collections.binarySearch(addressList,addressInput,new AddressComparator());
        if(index < 0) { //If it is not found the return value will be negative
            System.out.println("Too bad - didn't find!");
        } else {
            Model.Address foundAddr = addressList.get(index);
            Point2D coordinate = addressMap.get(foundAddr);
            System.out.println("x = " + coordinate.getX() + ", y = " +coordinate.getY());
        } //if multiple results ... suggest these

    }*/




    /**
     * Writes all the objects of Model.Drawable to a binary file for faster loading. The order of the sequence is important!
     * @param filename File saved to
     */
    public void save(String filename) { /*
        long time = System.nanoTime();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                //write the boundaries and the number of shapes.
                out.writeObject(bbox.getBounds2D());
                out.writeInt(drawables.size());

                //For every object write the its
                for(Drawable d : drawables) {
                    //Write all information needed from the object order matters.
                    out.writeObject(d);
                    out.writeObject(d.getShape());
                    out.writeObject(d.getColor());
                    out.writeDouble(d.getDrawLevel());
                    out.writeInt(d.getLayerVal());
                    //TODO save all icons position and type
                    //TODO save everything!
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        System.out.printf("Model.Model save time: %d ms\n", (System.nanoTime()-time) / 1000000);
        */
    }

    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param filename file load from
     */
    public void load(String filename) {

        long time = System.nanoTime();
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            //get the bounds of the map
            Rectangle2D rec = (Rectangle2D) in.readObject();
            bbox = new Rectangle2D.Double();
            bbox.setRect(rec);

            //First int is the number of shapes
            int i = in.readInt();
            drawables.clear();
            while(i-- > 0){
                //get information needed in correct order and add to drawables-array.
                MapFeature d = (MapFeature) in.readObject();
                d.way = (Shape) in.readObject();
                d.color = (Color) in.readObject();
                d.zoom_level = in.readDouble();
                d.layer_value = in.readInt();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.printf("Model.Model load time: %d ms\n",
                (System.nanoTime() - time) / 1000000);
        sortLayers();
        setChanged();
        notifyObservers();

    }

    /**
     * Custom comparator that defines how to compare two addresses. Used when sorting a collection of addresses.
     */


    public Iterator<Shape> iterator() {
        return lines.iterator();
    }
}
