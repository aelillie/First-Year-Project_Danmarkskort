package ShortestPath;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Vertices {
    private Map<Point2D, Integer> vertex_map;
    private Point2D[] vertex_index;
    private int counter;

    public Vertices() {
        vertex_map = new HashMap<>();
        counter = 0;
    }

    public void add(Point2D vertex) {
        if(vertex_map.containsKey(vertex)) return; //Vertex already added
        vertex_map.put(vertex, counter); //Put vertex in map with an index
        counter++; //next index
        if (counter == Integer.MAX_VALUE) //will never happen
            throw new NumberFormatException("Integer limit reached");
    }

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
}
