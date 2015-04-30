package Model;

/**
 * A simple interface used for abstraction, every class implementing this interface can be sorted
 * by our QuadTree and stored in our QuadTree.
 */
public interface MapData {

    public Class getClassType();

    public int getLayerVal();

    public boolean equals(Object o);

}
