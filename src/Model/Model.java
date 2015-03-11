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
    private List<Shape> lines = new ArrayList<>(); //contains all shapes to be drawn that are not in drawables
    private List<MapIcon> mapIcons = new ArrayList<>(); //contains all the icons to be drawn

    private Map<String,List<Shape>> streetnameMap = new HashMap<>();
    private Map<Address,Point2D> addressMap = new HashMap<>(); //Contains relevant places parsed as address objects (e.g. a place Roskilde or an address Lauravej 38 2900 Hellerup etc.) linked to their coordinate.
    private ArrayList<Address> addressList = new ArrayList<>(); //Contains all addresses to be sorted according to the compareTo method.



    public List<MapIcon> getMapIcons() {
        return mapIcons;
    }

    private List<Drawable> drawables = new ArrayList<>(); //Shapes to be drawn differently

    public List<Drawable> getDrawables() {
        return drawables;
    }


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
        sortLayers();
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
    private void sortLayers() {
        Comparator<MapFeature> comparator = new Comparator<MapFeature>() {
            @Override
            /**
             * Compares two Model.Drawable objects.
             * Returns a negative integer, zero, or a positive integer as the first argument
             * is less than, equal to, or greater than the second.
             */
            public int compare(MapFeature o1, MapFeature o2) {
                if (o1.getLayerVal() < o2.getLayerVal()) return -1;
                else if (o1.getLayerVal() > o2.getLayerVal()) return 1;
                return 0;
            }
        };
        Collections.sort(mapFeatures, comparator); //iterative mergesort. ~n*lg(n) comparisons
    }

    /**
     * Recursive binary search method taking lower- and higher bounds as input. Takes O(log N) time.
     * @param list The list to be searched.
     * @param addr The address we are searching for.
     * @param low The lower bound of the part of the array we want to search.
     * @param high The higher bound of the part of the array we want to search.
     * @return The index at which we found the element.
     */
    private int binSearch(ArrayList<Address> list, Address addr, int low, int high){
        if(low > high) return -1;
        int mid = (low+high)/2;
        if (list.get(mid).compareTo(addr) < 0) return binSearch(list, addr, mid + 1, high); //if addr is larger than mid
        else if (list.get(mid).compareTo(addr) > 0) return binSearch(list, addr, low, mid - 1); //if addr is smaller than mid
        else return mid;
    }

    /**
     * Since there can be several addresses with the same name (e.g. Lærkevej in Copenhagen and Lærkevej in Roskilde),
     * this method searches the lower part and higher part of the array bounded by the first element which is found in the list to determine
     * the bounds of the similiar results in the array.
     * @param addressInput
     * @return
     */
    public int[] multipleEntriesSearch(Address addressInput){
        int index = binSearch(addressList,addressInput,0,addressList.size()-1); //Returns the index of the first found element.
        if(index < 0) return null; //Not found

        int lowerBound = index; //Search to the left of the found element
        int i = lowerBound;
        do {
            lowerBound = i;
            i = binSearch(addressList, addressInput, 0, lowerBound-1);
        } while (i != -1); //As long as we find a similiar element, keep searching to determine when we don't anymore.

        int upperBound = index; //Search to the right of the found element
        i = upperBound;
        do {
            upperBound = i;
            i = binSearch(addressList, addressInput, upperBound+1, addressList.size() - 1);
        }
        while (i != -1); //As long as we find a similiar element, keep searching to determine when we don't anymore.

        int[] range = {lowerBound, upperBound}; //The bounds of the similiar elements in the list.
        return range;
    }

    public void searchForAddresses(Address addressInput){
        int[] range = multipleEntriesSearch(addressInput); //search for one or multiple entries
        if(range == null) { //If it is not found, the return value will be negative
            System.out.println("Too bad - didn't find!");
        } else {
            System.out.println("Found something");
            int lowerBound = range[0], upperBound = range[1];
            System.out.printf("low: "+lowerBound + ", high: "+upperBound);
            for(int i = lowerBound; i <= upperBound; i++){
                Address foundAddr = addressList.get(i);
                Point2D coordinate = addressMap.get(foundAddr);
                //System.out.println("x = " + coordinate.getX() + ", y = " +coordinate.getY());
            }
        }
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


    public Map<String,List<Shape>> getStreetMap(){
        return streetnameMap;
    }

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
        /*
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
                Drawable d = (Drawable) in.readObject();
                d.getShape() = (Shape) in.readObject();
                d.getColor() = (Color) in.readObject();
                d.getDrawLevel() = in.readDouble();
                d.getLayerVal() = in.readInt();
                drawables.add(d);
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
        */
    }

    /**
     * Custom comparator that defines how to compare two addresses. Used when sorting a collection of addresses.
     */

    public class AddressComparator implements Comparator<Address> {
        @Override
        public int compare(Address addr1, Address addr2) {
            return addr1.compareTo(addr2);
        }
    }

    public Iterator<Shape> iterator() {
        return lines.iterator();
    }
}
