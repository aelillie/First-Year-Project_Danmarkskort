package Model;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void loadFile(String filename, InputStream inputStream) throws IOException {
        long time = System.nanoTime();
        Address.addPatterns();
        if (filename.endsWith(".osm")) parseOSM(inputStream);
        else if (filename.endsWith(".zip")) parseZIP(inputStream);
        else if (filename.endsWith(".bin")) loadBin(inputStream);
        else System.err.println("File not recognized");
        System.out.printf("Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }

    /*
    public void loadFile(String shapeFile, String iconFile){
        long time = System.nanoTime();
        if(shapeFile.endsWith(".bin") && iconFile.endsWith(".bin"))
            loadAll(shapeFile, iconFile);
        else System.err.println("File not recognized");
        System.out.printf("Model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }*/

    private void parseOSM(InputStream inputStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(inputStream, OSMReader);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    private void parseZIP(InputStream inputStream) {
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputStream))) {
            zip.getNextEntry();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(zip), OSMReader);
            zip.close();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }



    private void loadBin(InputStream inputStream) {

        try {

            BinaryHandler.load(inputStream);

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBin(String filename) {

        try {
            BinaryHandler.save(filename);
            System.out.println("Shapes saved");
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
        return OSMReader.getBbox();
    }

    public List<MapFeature> getMapFeatures(){
        return OSMReader.getMapFeatures();
    }

    public List<MapIcon>  getMapIcons(){
        return OSMReader.getMapIcons();
    }

    public void setBBox(Rectangle2D bBox){

        OSMReader.getBbox().setRect(bBox);
    }

    public OSMHandler getOSMReader(){
        return OSMReader;
    }

}
