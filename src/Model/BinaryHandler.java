package Model;

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
     * @param filename file load from
     */
    public static void loadShapes(String filename)throws IOException, ClassNotFoundException {
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
        //get the bounds of the map
        Rectangle2D rec = (Rectangle2D) in.readObject();

        model.setBBox(rec);
        List<MapFeature> mapF = model.getMapFeatures();

        model.getMapFeatures().clear();

        mapF.addAll((List<MapFeature>) in.readObject());

    }

    public static void loadIcons(String filename)throws IOException, ClassNotFoundException{
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));

        List<MapIcon> icons = model.getMapIcons();
        icons.addAll((List<MapIcon>)in.readObject());

    }

    public static void saveIcons(String filename)throws IOException{

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();

        out.writeObject(model.getMapIcons());
    }


    public static void loadAll(String shapeFile, String iconFile)throws IOException, ClassNotFoundException{
        loadShapes(shapeFile);
        loadIcons(iconFile);
    }

    public static void saveAll(String shapeFile, String iconFile) throws  IOException{
        saveShapes(shapeFile);
        saveIcons(iconFile);
    }

}
