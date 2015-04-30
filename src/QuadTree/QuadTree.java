package QuadTree;

import Model.MapData;
import Model.MapFeature;
import Model.MapIcon;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.*;

public class QuadTree implements Serializable{
    private static final long serialVersionUID = 8;
    private int cap = 0;
    private Node root;

    // helper node data type
    private class Node implements Serializable {
        private static final long serialVersionUID = 8;
        Double x, y;                                // x- and y- coordinates
        Double width, height;
        Node NW, NE, SE, SW;                   // four subtrees
        HashSet<MapData> value;

        Node(Double x, Double y, Double width, Double height) {
            this.x = x;
            this.y = y;
            this.width = width/2;
            this.height = height/2;
            value = new HashSet<>();


        }

        /**
         * Divide the Node into four new and spread the values.
         */
        public void subDivide(){
            Double newWidth = width/2;
            Double newHeight = height/2;


            NW = new Node(x - newWidth , y -  newHeight, width, height);
            NE = new Node(x + newWidth, y - newHeight, width, height);
            SE = new Node(x + newWidth, y + newHeight, width, height);
            SW = new Node(x - newWidth ,y + newHeight, width, height);

            for(MapData mp : value)
                insertAgain(mp, this);
            value = null;
        }

        /**
         * Add a new Value, check if node is full
         * @param values - Value to be stored in node
         */
        public void addvalue(MapData values){
            if(value.size() == cap){
                subDivide();
                insert(values);
            }else
                value.add(values);

        }

        public Rectangle2D getRect(){
            return new Rectangle2D.Double(x- width,y - height, width*2, height*2);
        }

    }

    /**
     * Create an instance from a bbox. Splitting the initial root using bbox values.
     * @param bbox - Bounds containing all values.
     */
    public QuadTree(Rectangle2D bbox, int capacity){
        cap = capacity;
        root = new Node(bbox.getCenterX(),bbox.getCenterY(), bbox.getWidth(), bbox.getHeight());
        root.subDivide();
    }


    /**
     * Insertion of values.
     *
     * @param value Value
     */
    public void insert(MapData value) {

        //First check what Type it is then use its coordinates to store it in the QuadTree
        if(value.getClassType() == MapIcon.class) {
            MapIcon mI = (MapIcon) value;
            insert(root, mI.getPosition().getX(), mI.getPosition().getY(), value);

        }
        else {
            MapFeature mF = (MapFeature) value;
            Rectangle2D rec = mF.getWay().getBounds2D();
            insertPath(root, rec.getX(), rec.getX() + rec.getWidth(), rec.getY(), rec.getY() + rec.getHeight(), mF);

        }
    }

    public void insertAgain(MapData value, Node h) {

        //First check what Type it is then use its coordinates to store it in the QuadTree
        if(value.getClassType() == MapIcon.class) {
            MapIcon mI = (MapIcon) value;
            insert(h, mI.getPosition().getX(), mI.getPosition().getY(), value);

        }
        else {
            MapFeature mF = (MapFeature) value;
            Rectangle2D rec = mF.getWay().getBounds2D();
            insertPath(h, rec.getX(), rec.getX() + rec.getWidth(), rec.getY(), rec.getY() + rec.getHeight(), mF);

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

    //TODO fix this.
    private void insertPath(Node h, Double minX, Double maxX, Double minY, Double maxY , MapData value){

        if (less(minX, h.x) &&  less(minY, h.y)) {
            if (h.NW == null){
                h.addvalue(value);
                return;
            }
            else insertPath(h.NW, minX, maxX, minY, maxY, value);
        }

        if (less(minX, h.x) && !less(maxY, h.y)){
            if(h.SW == null){
                h.addvalue(value);
                return;
            }
            else insertPath(h.SW, minX, maxX, minY, maxY, value);
        }


        if (!less(maxX, h.x) &&  less(minY, h.y)){
            if(h.NE == null){
                h.addvalue(value);
                return;
            }
            else insertPath(h.NE, minX, maxX, minY, maxY, value);
        }


        if (!less(maxX, h.x) && !less(maxY, h.y)){
            if(h.SE == null){
                h.addvalue(value);
                return;
            }
            else insertPath(h.SE, minX, maxX, minY, maxY, value);
        }

    }


    /**
     * Range search in the QuadTree
     * @param rect Range needed
     * @return List of List of MapFeatures
     */
    public Collection<MapData> query2D(Shape rect, boolean sorted) {
        Collection<MapData> values;
        if(sorted){
            values = new TreeSet<>(new Comparator<MapData>() {
                @Override
                /**
                 * Compares two MapFeature objects.
                 * Returns a negative integer, zero, or a positive integer as the first argument
                 * is less than, equal to, or greater than the second.
                 */
                public int compare(MapData o1, MapData o2) {
                    if(o1.equals(o2)) return 0;
                    else if (o1.getLayerVal() < o2.getLayerVal()) return -1;
                    else if (o1.getLayerVal() > o2.getLayerVal()) return 1;
                    return -1;
                }
            });
        }else {
            values = new ArrayList<>();
        }

        if(rect != null)
            query2D(root, rect, values);
        else{
            System.out.println("View was null");
        }

        return values;
    }

    private void query2D(Node h, Shape query, Collection<MapData> values) {
        if (h == null) return;
        Rectangle2D rect = query.getBounds2D();
        Double xmin = rect.getMinX();
        Double ymin = rect.getMinY();
        Double xmax = rect.getMaxX();
        Double ymax = rect.getMaxY();
        if (rect.intersects(h.getRect()) || rect.contains(h.x, h.y))
            if(h.value != null) {
                values.addAll(h.value);
            }
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
            wut.add(h.getRect());

            if(h.NW != null)wut.addAll(getNodeRects(h.NW));
            if(h.NE != null)wut.addAll(getNodeRects(h.NE));
            if(h.SE != null)wut.addAll(getNodeRects(h.SE));
            if(h.SW != null)wut.addAll(getNodeRects(h.SW));
        }
        return wut;
    }




}