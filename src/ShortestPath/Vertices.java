package ShortestPath;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Vertices {
    private Map<Point2D, Integer> vertex_index;
    private int counter;

    public Vertices() {
        vertex_index = new HashMap<>();
        counter = 0;
    }

    public void add(Point2D vertex) {
        if(vertex_index.containsKey(vertex)) return;
        vertex_index.put(vertex, counter);
        counter++;
        if (counter == Integer.MAX_VALUE) //will never happen
            throw new NumberFormatException("Integer limit reached");
    }

    public int V() {
        return vertex_index.size();
    }

    public int get(Point2D vertex) {
        return vertex_index.get(vertex);
    }
}
