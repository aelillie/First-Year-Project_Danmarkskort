package QuadTree;

import Model.MapData;
import Model.MapFeature;
import Model.MapIcon;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuadTree implements Serializable{
    private static final long serialVersionUID = 8;
    private Node root;

    // helper node data type
    private class Node implements Serializable {
        private static final long serialVersionUID = 8;
        Double x, y;                                // x- and y- coordinates
        Double width, height;
        Node NW, NE, SE, SW;                   // four subtrees
        ArrayList<MapData> value;


        Node(Double x, Double y, Double width, Double height) {
            this.x = x;
            this.y = y;
            this.width = width/2;
            this.height = height/2;
            value = new ArrayList<>();


        }

        /**
         * Divide the Node into four new and spread the values.
         */
        public void subdivide(){
            Double newWidth = width/2;
            Double newHeight = height/2;
            ArrayList<MapData> tmp = value;
            value = new ArrayList<>();
            NW = new Node(x - newWidth , y -  newHeight, width, height);
            NE = new Node(x + newWidth, y - newHeight, width, height);
            SE = new Node(x + newWidth, y + newHeight, width, height);
            SW = new Node(x - newWidth ,y + newHeight, width, height);

            for(MapData data : tmp)
                insert(data);

        }

        /**
         * Add a new Value, check if node is full
         * @param values - Value to be stored in node
         */
        public void addvalue(MapData values){
            if(value.size() > 1000){
                subdivide();
                insert(values);
            }else value.add(values);
        }

    }

    /**
     * Create an instance from a bbox. Splitting the initial root using bbox values.
     * @param bbox - Bounds containing all values.
     */
    public QuadTree(Rectangle2D bbox){

        root = new Node(bbox.getCenterX(),bbox.getCenterY(), bbox.getWidth(), bbox.getHeight());
        root.subdivide();
    }


    /**
     * Insertion of values.
     *
     * @param value Value
     */
    public void insert(MapData value) {

        //First check what Type it is then use its coordinates to store it in the QuadTree
        if(value.getType() == MapIcon.class) {
            MapIcon mI = (MapIcon) value;
            insert(root, mI.getPosition().getX(), mI.getPosition().getY(), value);

        }
        else {
            MapFeature mF = (MapFeature) value;
            Rectangle2D bounds = mF.getShape().getBounds2D();

            insert(root, bounds.getCenterX(), bounds.getCenterY(), value);
        }
    }

    private void insert(Node h, Double x, Double y, MapData value) {
        //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate

        if ( less(x, h.x) &&  less(y, h.y)) {
            if(h.NW == null) h.addvalue(value);
            else insert(h.NW, x, y, value);
        }
        else if ( less(x, h.x) && !less(y, h.y)) {
            if(h.SW == null) h.addvalue(value);
            else insert(h.SW, x, y, value);
        }
        else if (!less(x, h.x) &&  less(y, h.y)) {
            if(h.NE == null) h.addvalue(value);
            else insert(h.NE, x, y, value);
        }
        else if (!less(x, h.x) && !less(y, h.y)) {
            if(h.SE == null) h.addvalue(value);
            else insert(h.SE, x, y, value);
        }
    }


    /**
     * Range search in the QuadTree
     * @param rect Range needed
     * @return List of List of MapFeatures
     */
    public ArrayList<List<MapData>> query2D(Shape rect) {

        ArrayList<List<MapData>> values = new ArrayList<>(); //List of list of All values in rect to be returned
        if(rect != null)
            query2D(root, rect, values);
        return values;
    }

    private void query2D(Node h, Shape query, ArrayList<List<MapData>> values) {
        if (h == null) return;
        Rectangle2D rect = query.getBounds2D();
        Double xmin = rect.getMinX();
        Double ymin = rect.getMinY();
        Double xmax = rect.getMaxX();
        Double ymax = rect.getMaxY();
        if (rect.intersects(h.x- h.width,h.y - h.height, h.width*2, h.height*2) || rect.contains(h.x, h.y))
            if(!h.value.isEmpty())
            values.add(h.value);

        //Recursive calls. Checking what nodes to search in.
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.NW, rect, values);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.SW, rect, values);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.NE, rect, values);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.SE, rect, values);

    }


    /*************************************************************************
     *  helper comparison functions
     *************************************************************************/

    private boolean less(Double k1, Double k2) { return k1.compareTo(k2) <  0; }
    private boolean eq  (Double k1, Double k2) { return k1.compareTo(k2) == 0; }


    /**
     * Helper function for visual presentation of the nodes.
     * @return
     */
    public ArrayList<Rectangle2D> getNodeRects(){
        ArrayList<Rectangle2D> rects = new ArrayList<>();
        if(root == null) return null;
        else {
            rects.addAll(getNodeRects(root));
        }

        return rects;
    }

    private ArrayList<Rectangle2D> getNodeRects(Node h){
        ArrayList<Rectangle2D> wut = new ArrayList<>();
        if(h == null) return null;
        else{
            wut.add(new Rectangle2D.Double(h.x - h.width, h.y - h.height, h.width * 2, h.height * 2));

            if(h.NW != null)wut.addAll(getNodeRects(h.NW));
            if(h.NE != null)wut.addAll(getNodeRects(h.NE));
            if(h.SE != null)wut.addAll(getNodeRects(h.SE));
            if(h.SW != null)wut.addAll(getNodeRects(h.SW));
        }



        return wut;
    }




}