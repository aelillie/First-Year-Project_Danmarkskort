package Model;

import Model.MapFeatures.Coastline;
import Model.QuadTree.QuadTree;
import Model.Path.Graph;
import Model.Path.Vertices;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Helper class to save and load data in binary format
 */
public class BinaryHandler{

    /**
     * Writes all the objects of Model.Drawable to a binary file for faster loading. The order of the sequence is important!
     * @param filename File saved to
     */
    protected static void save(String filename) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();
        LoadingScreen loadingScreen = new LoadingScreen();
        out.writeObject(model.getBbox().getBounds2D());
        loadingScreen.updateLoadBar(5);

        loadingScreen.updateLoadBar(20);

        out.writeObject(model.getOSMReader().getStreetMap());
        loadingScreen.updateLoadBar(35);


        out.writeObject(model.getOSMReader().getAddressList());
        loadingScreen.updateLoadBar(50);

        out.writeObject(model.getDiGraph());
        loadingScreen.updateLoadBar(60);

        out.writeObject(model.getVertices());
        loadingScreen.updateLoadBar(65);

        List<QuadTree> qT = model.getQuadTrees();

        out.writeObject(qT);

        loadingScreen.updateLoadBar(90);

        out.writeObject(model.getCoastlines());


        loadingScreen.updateLoadBar(100);
        out.reset();
        out.close();

        System.out.print(filename + " saved");

    }


    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param inputStream file load from
     */
    protected static void load(InputStream inputStream)throws IOException, ClassNotFoundException {
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(inputStream));
        model.getOSMReader().initializeCollections();
        //get the bounds of the map
        Rectangle2D rec = (Rectangle2D) in.readObject();

        LoadingScreen loadingScreen = new LoadingScreen();

        loadingScreen.updateLoadBar(15);

        model.getOSMReader().setStreetMap((Map<Address, List<Path2D>>) in.readObject());

        loadingScreen.updateLoadBar(25);


        model.getOSMReader().setAddressList((ArrayList<Address>) in.readObject());

        loadingScreen.updateLoadBar(55);
        model.getOSMReader().setGraph((Graph) in.readObject());
        loadingScreen.updateLoadBar(65);
        model.getOSMReader().setVertices((Vertices) in.readObject());

        long time = System.nanoTime();
        model.setBBox(rec);
        List<QuadTree> qT = (List<QuadTree>) in.readObject();


        loadingScreen.updateLoadBar(80);

        model.setQuadTree(qT);
        System.out.println("done in " + (System.nanoTime() - time) / 1000000);

        List<Coastline> coasts = model.getCoastlines();
        coasts.addAll((List<Coastline>) in.readObject());
        loadingScreen.updateLoadBar(99);


        in.close();

        loadingScreen.updateLoadBar(100);
    }



}
