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

        out.writeObject(model.getMapIcons());

        out.writeObject(model.getCoastlines());
        //TODO total redo

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

        model.setBBox(rec);
        QuadTree qT = (QuadTree) in.readObject();

        model.setQuadTree(qT);

        List<MapIcon> icons = model.getMapIcons();
        icons = (List<MapIcon>)in.readObject();

        List<Coastline> coasts = model.getCoastlines();
        coasts.addAll((List<Coastline>)in.readObject());

        //TODO total redo with quadTree

    }



}
