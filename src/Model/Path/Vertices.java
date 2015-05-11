package Model.Path;

import Model.OSMNode;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertices implements Serializable {
    private static final long serialVersionUID = 16;
    private Map<OSMNode, Integer> vertex_map;
    private OSMNode[] vertex_index;
    private int counter;

    /**
     * Helper class to keep track of linking Node points to vertices indices.
     */
    public Vertices() {
        vertex_map = new HashMap<>();
        counter = 0;
    }

    /**
     * Add a list of vertices to the vertex_map
     * @param wayCoords the coordinates making up a single way
     */
    public void add(List<OSMNode> wayCoords) {
        for(OSMNode vertex : wayCoords) {
            add(vertex);
        }
    }

    /**
     * Add a new vertex to the vertex_map with the current index number,
     * and increment the counter, for the next vertex to be added
     * @param vertex A Point2D coordinate set
     */
    private void add(OSMNode vertex) {
        if(vertex_map.containsKey(vertex)) return; //Vertex already added
        vertex_map.put(vertex, counter); //Put vertex in map with an index
        counter++; //next index
        if (counter == Integer.MAX_VALUE) //Won't happen in this lifetime
            throw new NumberFormatException("Integer limit reached");
    }

    /**
     * When all vertices has been added creates an array
     * to easily get point linked to vertex index.
     */
    public void createVertexIndex() {
        vertex_index = new OSMNode[vertex_map.size()];
        for(OSMNode point : vertex_map.keySet()){
            vertex_index[vertex_map.get(point)] = point;
        }
    }

    public int V() {
        return vertex_map.size();
    }

    public int getIndex(Point2D vertex) {
        return vertex_map.get(vertex);
    }

    public OSMNode getVertex(int index) {
        return vertex_index[index];
    }

    public void clearMap(){
        vertex_map = null;
    }
}
