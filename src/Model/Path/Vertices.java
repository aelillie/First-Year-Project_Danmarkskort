package Model.Path;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertices implements Serializable {
    private static final long serialVersionUID = 16;
    private Map<Point2D, Integer> vertex_map;
    private Point2D[] vertex_index;
    private int counter;

    /**
     * Helper class to keep track of linking Node points to vertices indices.
     */
    public Vertices() {
        vertex_map = new HashMap<>();
        counter = 0;
    }

    public void add(List<Point2D> wayCoords) {
        for(Point2D vertex : wayCoords) {
            add(vertex);
        }
    }

    private void add(Point2D vertex) {
        if(vertex_map.containsKey(vertex)) return; //Vertex already added
        vertex_map.put(vertex, counter); //Put vertex in map with an index
        counter++; //next index
        if (counter == Integer.MAX_VALUE) //Won't happen in this lifetime
            throw new NumberFormatException("Integer limit reached");
    }

    /**
     * When all vertices has been added creates and array
     * to easily get point linked to vertex index.
     */
    public void createVertexIndex() {
        vertex_index = new Point2D[vertex_map.size()];
        for(Point2D point : vertex_map.keySet()){
            vertex_index[vertex_map.get(point)] = point;
        }
    }

    public int V() {
        return vertex_map.size();
    }

    public int getIndex(Point2D vertex) {
        return vertex_map.get(vertex);
    }

    public Point2D getVertex(int index) {
        return vertex_index[index];
    }

    public void clearMap(){
        vertex_map = null;
    }
}
