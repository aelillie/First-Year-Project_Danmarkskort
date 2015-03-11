package Model;

import java.awt.geom.Rectangle2D;
import java.io.*;

/**
 * Created by Kevin on 11-03-2015.
 */
public class BinaryHandler {

    /**
     * Writes all the objects of Model.Drawable to a binary file for faster loading. The order of the sequence is important!
     * @param filename File saved to
     */
    public static void save(String filename) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        //write the boundaries and the number of shapes.
        Model model = Model.getModel();
        out.writeObject(model.getBbox().getBounds2D());
        out.writeInt(model.getMapFeatures().size());

        //For every object write the its
        for(MapFeature  m: model.getMapFeatures() ) {
            //Write all information needed from the object order matters.
            out.writeObject(m);
            //TODO save all icons position and type
            //TODO save everything!
        }
    }


    /**
     * loads the shapes from a binary file. The order of the sequence is important!
     * @param filename file load from
     */
    public void load(String filename)throws IOException, ClassNotFoundException{
        Model model = Model.getModel();
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
            //get the bounds of the map
        Rectangle2D rec = (Rectangle2D) in.readObject();

        model.setBBox(rec);

        //First int is the number of shapes
        int i = in.readInt();
        model.getMapFeatures().clear();
        while(i-- > 0){
            //get information needed in correct order and add to drawables-array.
            MapFeature m = (MapFeature) in.readObject();

            model.getMapFeatures().add(m);

        }

        model.getOSMReader().sortLayers();
    }

}
