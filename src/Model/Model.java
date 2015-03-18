package Model;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.zip.ZipInputStream;


public class Model extends Observable implements Serializable {

    private OSMHandler OSMReader = new OSMHandler();
    private static Model model = new Model();
    private ArrayList<Address> addressList = new ArrayList<>(); //Contains all addresses to be sorted according to the compareTo method.





    private Model(){}

    public static Model getModel(){
        return model;
    }
    public void loadFile(String filename) {
        long time = System.nanoTime();
        Address.addPatterns();
        if (filename.endsWith(".osm")) parseOSM(filename);
        else if (filename.endsWith(".zip")) parseZIP(filename);
        else if (filename.endsWith(".bin")) loadBinary(filename);
        else System.err.println("File not recognized");

        System.out.printf("Model.Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }

    private void parseOSM(String filename) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMReader);
            reader.parse(filename);

        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void parseZIP(String filename) {
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            zip.getNextEntry();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMReader);
            reader.parse(new InputSource(zip));
            zip.close();
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void loadBinary(String filename) {

        try {

            BinaryHandler.load(filename);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void saveBin(String filename) {

        try {
            BinaryHandler.save(filename);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void searchForAddresses(Address address){
        OSMReader.searchForAddressess(address);

    }


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





    public Rectangle2D getBbox(){
        return OSMReader.bbox;
    }

    public List<MapFeature> getMapFeatures(){
        return OSMReader.getMapFeatures();
    }

    public List<MapIcon>  getMapIcons(){
        return OSMReader.getMapIcons();
    }

    public void setBBox(Rectangle2D bBox){
        OSMReader.bbox.setRect(bBox);
    }

    public OSMHandler getOSMReader(){
        return OSMReader;
    }

}
