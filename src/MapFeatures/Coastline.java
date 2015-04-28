package MapFeatures;

import Model.MapFeature;
import Model.ValueName;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Coastline extends MapFeature {
    private Point2D start; //The startpoint of the coastline
    private Point2D end; //The endpoint of the coastline.

    public Coastline(Path2D path, Point2D start, Point2D end, int layer_value, String value){
        super(path,layer_value,value);
        isArea = true;
        this.start = start;
        this.end = end;
    }

    @Override
    public void setPreDefLayerValues() {
        super.setPreDefLayerValues();
    }

    public void setEnd(Point2D newEnd){
        end = newEnd;
    }

    public Point2D getStart(){ return start; }

    public Point2D getEnd() { return end; }



    public void setPath(Path2D updatedPath) { way = updatedPath;}

    public void setStart(Point2D newStart) {start = newStart;}

    @Override
    public void setValueNames(){
        setValueName(ValueName.COASTLINE);
    }

}
