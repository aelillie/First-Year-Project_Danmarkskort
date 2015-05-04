package Model;

/**
 * A simple interface used for abstraction, every class implementing this interface can be sorted
 * by our QuadTree and stored in our QuadTree.
 */
public interface MapData {

    Class getClassType();

    int getLayerVal();

    boolean equals(Object o);

}
