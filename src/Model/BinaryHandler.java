package Model;

import MapFeatures.Coastline;
import QuadTree.QuadTree;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;
import java.util.Map;

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

        out.writeObject(model.getOSMReader().getAddressMap());
        out.writeObject(model.getOSMReader().getStreetMap());
        out.writeObject(model.getOSMReader().getBoundaryMap());
        out.writeObject(model.getOSMReader().getAddressList());

        List<QuadTree> qT = model.getQuadTrees();
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


        model.getOSMReader().setAddressMap((Map<Address,Point2D>) in.readObject());

        model.getOSMReader().setStreetMap((Map<Address, List<Path2D>>) in.readObject());

        model.getOSMReader().setBoundaryMap((Map<Address, Path2D>) in.readObject());

        model.getOSMReader().setAddressList((List<Address>) in.readObject());


        long time = System.nanoTime();
        model.setBBox(rec);
        List<QuadTree> qT = (List<QuadTree>) in.readObject();

        model.setQuadTree(qT);
        System.out.println("done in " + (System.nanoTime() - time) / 1000000);


        List<Coastline> coasts = model.getCoastlines();
        coasts.addAll((List<Coastline>)in.readObject());
        in.close();

    }



}
