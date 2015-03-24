package Model;

import org.xml.sax.InputSource;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;

/**
 * Helper class to save and load data in binary format
 */
public final class BinaryHandler {

    /**
     * Writes all the objects of Model.Drawable to a binary file for faster loading. The order of the sequence is important!
     * @param filename File saved to
     */
    public static void saveShapes(String filename) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();
        out.writeObject(model.getBbox().getBounds2D());

        List<MapFeature> mapF = model.getMapFeatures();
        out.writeObject(mapF);

    }


    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param inputSource file load from
     */
    public static void loadShapes(InputSource inputSource)throws IOException, ClassNotFoundException {
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(inputSource.getByteStream()));
        //get the bounds of the map
        Rectangle2D rec = (Rectangle2D) in.readObject();

        model.setBBox(rec);
        List<MapFeature> mapF = model.getMapFeatures();

        model.getMapFeatures().clear();

        mapF.addAll((List<MapFeature>) in.readObject());

    }

    public static void loadIcons(InputSource iconSource)throws IOException, ClassNotFoundException{
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(iconSource.getByteStream()));

        List<MapIcon> icons = model.getMapIcons();
        icons.addAll((List<MapIcon>)in.readObject());

    }

    public static void saveIcons(String filename)throws IOException{

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();

        out.writeObject(model.getMapIcons());
    }


    public static void loadAll(InputSource shapeSource, InputSource iconSource)throws IOException, ClassNotFoundException{
        loadShapes(shapeSource);
        loadIcons(iconSource);
    }

    public static void saveAll(String shapeFile, String iconFile) throws  IOException{
        saveShapes(shapeFile);
        saveIcons(iconFile);
    }

}
