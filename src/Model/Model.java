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

    public void loadFile(String filename, InputSource inputSource) {
        long time = System.nanoTime();
        Address.addPatterns();
        if (filename.endsWith(".osm")) parseOSM(inputSource);
        else if (filename.endsWith(".zip")) parseZIP(inputSource);
        else if (filename.endsWith(".bin")) loadShapes(inputSource);
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

    private void parseOSM(InputSource inputSource) {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMReader);
            reader.parse(inputSource);

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    private void parseZIP(InputSource inputSource) {
        try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputSource.getByteStream()))) {
            zip.getNextEntry();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(OSMReader);
            reader.parse(new InputSource(zip));
            zip.close();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }



    private void loadShapes(InputSource inputSource) {

        try {

            BinaryHandler.loadShapes(inputSource);

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveShapes(String filename) {

        try {
            BinaryHandler.saveShapes(filename);
            System.out.println("Shapes saved");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void saveIcons(String filename){

        try{
            BinaryHandler.saveIcons(filename);
            System.out.println("Icons saved");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void saveAll(String shapeFile, String iconFile){

        try{
            BinaryHandler.saveAll(shapeFile, iconFile);
            System.out.println("All shapes and icons saved");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void loadAll(InputSource shapeSource, InputSource iconSource){
        try{
            BinaryHandler.loadAll(shapeSource, iconSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e){
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
