package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Coastline extends MapFeature {
    private Path2D path; //The collected path of the coastline
    private Point2D start; //The startpoint of the coastline
    private Point2D end; //The endpoint of the coastline.

    public Coastline(Shape path, Point2D start, Point2D end, int layer_value, String value){
        super(path,layer_value,value);
        isArea = true;
        this.path = (Path2D) path;
        this.start = start;
        this.end = end;
        setValueAttributes();

    }

    public void setEnd(Point2D newEnd){
        end = newEnd;
    }

    public Point2D getStart(){ return start; }

    public Point2D getEnd() { return end; }

    public Path2D getPath() { return path; }

    public void setPath(Path2D updatedPath) { path = updatedPath;}

    public void setStart(Point2D newStart) {start = newStart;}

    public void setValueAttributes(){
        setValueSpecs(ValueName.COASTLINE);
    }

}
