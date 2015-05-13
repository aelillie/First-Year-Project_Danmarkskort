package Model;

import Model.Path.Graph;
import Model.Path.Vertices;
import Model.QuadTree.QuadTree;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


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

        loadingScreen.updateLoadBar(10);

        out.writeObject(model.getOSMReader().getAddressList());
        loadingScreen.updateLoadBar(40);

        out.writeObject(model.getGraph());
        loadingScreen.updateLoadBar(60);

        out.writeObject(model.getVertices());
        loadingScreen.updateLoadBar(65);

        List<QuadTree> qT = model.getQuadTrees();

        out.writeObject(qT);

        loadingScreen.updateLoadBar(90);

        //out.writeObject(model.getCoastlines());


        loadingScreen.updateLoadBar(100);
        out.reset();
        out.close();



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

        loadingScreen.updateLoadBar(5);

        loadingScreen.updateLoadBar(20);


        model.getOSMReader().setAddressList((ArrayList<Address>) in.readObject());

        loadingScreen.updateLoadBar(40);
        model.getOSMReader().setGraph((Graph) in.readObject());
        loadingScreen.updateLoadBar(65);
        model.getOSMReader().setVertices((Vertices) in.readObject());


        model.setBBox(rec);
        List<QuadTree> qT = (List<QuadTree>) in.readObject();


        loadingScreen.updateLoadBar(80);

        model.setQuadTree(qT);


        //List<Coastline> coasts = model.getCoastlines();
        //coasts.addAll((List<Coastline>) in.readObject());
        loadingScreen.updateLoadBar(99);


        in.close();

        loadingScreen.updateLoadBar(100);
    }



}
