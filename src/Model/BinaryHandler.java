package Model;

import MapFeatures.Coastline;
import QuadTree.QuadTree;

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
    public static void save(String filename) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();
        out.writeObject(model.getBbox().getBounds2D());


        QuadTree qT = model.getQuadTree();
        out.writeObject(qT);


        out.writeObject(model.getCoastlines());


        out.close();
        System.out.print(filename + " saved");

    }


    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param inputStream file load from
     */
    public static void load(InputStream inputStream)throws IOException, ClassNotFoundException {
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(inputStream));
        model.getOSMReader().initializeCollections();
        //get the bounds of the map
        Rectangle2D rec = (Rectangle2D) in.readObject();

        long time = System.nanoTime();
        model.setBBox(rec);
        QuadTree qT = (QuadTree) in.readObject();

        model.setQuadTree(qT);
        System.out.println("done in " + (System.nanoTime() - time) / 1000000);


        List<Coastline> coasts = model.getCoastlines();
        coasts.addAll((List<Coastline>)in.readObject());
        in.close();

    }



}
