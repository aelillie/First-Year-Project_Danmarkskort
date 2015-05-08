package Model;

import Model.MapFeatures.Coastline;
import Model.QuadTree.QuadTree;
import Model.Path.Graph;
import Model.Path.Vertices;
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
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.zip.ZipInputStream;


public class Model extends Observable implements Serializable {

    private String currentFilename;
    private OSMHandler OSMReader = new OSMHandler();
    private static Model model = new Model(); //SingleTon design choice.
    private ArrayList<Address> addressList = new ArrayList<>(); //Contains all addresses to be sorted according to the compareTo method.


    private Model(){}

    public static Model getModel(){
        return model;
    }

    public void loadFile(String filename, InputStream inputStream) throws IOException {

        long time = System.nanoTime();
        if (filename.endsWith(".osm")) parseOSM(inputStream);
        else if (filename.endsWith(".zip")) parseZIP(inputStream);
        else if (filename.endsWith(".bin")) loadBin(inputStream);
        else System.err.println("File not recognized");
        inputStream.close(); //closes current input stream and releases any system resources associated with it
        System.out.printf("Complete model load time: %d ms\n", (System.nanoTime() - time) / 1000000);
    }


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


    public void saveBin() {
        saveBin(currentFilename);
    }

    private void saveBin(String filename) {
        try {
            BinaryHandler.save(filename + ".bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Address[] searchForAddresses(Address address, int type){
        return OSMReader.searchForAddressess(address, type);
    }


    public String getCurrentFilename() {
        return currentFilename;
    }

    public void setCurrentFilename(String path) {
        String filename = path;
        if (path.endsWith(".osm"))
            filename = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".osm"));
        else if (path.endsWith(".zip"))
            filename = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".zip"));
        this.currentFilename = filename;
    }

    public Rectangle2D getBbox(){
        return OSMReader.getBbox();
    }

    public List<QuadTree> getQuadTrees(){
        return OSMReader.getQuadTrees();
    }

    public void setQuadTree(List<QuadTree> qt){OSMReader.setQuadTrees(qt);}

    public void setBBox(Rectangle2D bBox){

        OSMReader.getBbox().setRect(bBox);
    }

    public List<Coastline> getCoastlines(){
        return OSMReader.getCoastlines();
    }

    public OSMHandler getOSMReader(){return OSMReader;}

    public Collection<MapData> getVisibleStreets(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getStreetTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleNatural(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getNaturalTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleBuildings(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getBuildingTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleIcons(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getIconTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleRailways(Rectangle2D visibleArea, boolean sorted) {
        return OSMReader.getRailwayTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleBigRoads(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getBigRoadTree().query2D(visibleArea, sorted);
    }

    public Collection<MapData> getVisibleCoastLines(Rectangle2D visibleArea, boolean sorted){
        return OSMReader.getCoastLineTree().query2D(visibleArea, sorted);
    }
    public Vertices getVertices() {
        return OSMReader.getVertices();
    }



    public Graph getDiGraph() {
        return OSMReader.getGraph();
    }

}

