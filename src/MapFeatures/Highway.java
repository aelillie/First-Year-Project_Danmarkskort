package MapFeatures;

import Model.Drawable;
import Model.Line;
import Model.MapFeature;

import java.awt.*;

/**
 * Created by Anders on 11-03-2015.
 */
public class Highway extends MapFeature {
    public Highway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
    }

    @Override
    public void setValueAttributes() {
        String val = kv_map.get("highway");
        if (hasName) addStreetName();
        if (val.equals("motorway"))
            drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
        else if (val.equals("motorway_link"))
            drawables.add(new Line(way, Drawable.lightblue, 7, -1.0, getLayer()));
        else if (val.equals("trunk"))
            drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
        else if (val.equals("trunk_link"))
            drawables.add(new Line(way, Drawable.neongreen, 7, -1.0, getLayer()));
        else if (val.equals("primary"))
            drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
        else if (val.equals("primary_link"))
            drawables.add(new Line(way, Drawable.babyred, 6, -1.0, getLayer()));
        else if (val.equals("secondary"))
            drawables.add(new Line(way, Drawable.lightred, 6, -1.0, getLayer()));
        else if (val.equals("secondary_link"))
            drawables.add(new Line(way, Drawable.lightred, 6, -0.8, getLayer()));
        else if (val.equals("tertiary"))
            drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
        else if (val.equals("tertiary_link"))
            drawables.add(new Line(way, Drawable.lightyellow, 5, -0.8, getLayer()));
        else if (val.equals("unclassified")) drawables.add(new Line(way, Color.WHITE, 5, -0.8, -1));
        else if (val.equals("residential")) drawables.add(new Line(way, Color.DARK_GRAY, 4, -1.0, -1));
        else if (val.equals("service")) drawables.add(new Line(way, Color.WHITE, 2, -0.8, -1));

        else if (val.equals("living_street")) drawables.add(new Line(way, Drawable.grey, 2, -1.0, -2));
        else if (val.equals("pedestrian")) drawables.add(new Line(way, Drawable.white, 2, -0.1, -2));
        else if (val.equals("track"))
            drawables.add(new Line(way, Drawable.bloodred, 1, 0.0, getLayer()));
        else if (val.equals("bus_guideway"))
            drawables.add(new Line(way, Drawable.darkblue, 1, -0.4, -2));
        else if (val.equals("raceway")) drawables.add(new Line(way, Drawable.white, 2, -0.4, -2));
        else if (val.equals("road")) drawables.add(new Line(way, Drawable.grey, 3, -0.4, -2));
        else if (val.equals("footway")) {
            Line line = new Line(way, Drawable.red, 12, -0.1, -2);
            line.setDashed();
            drawables.add(line);
        } else if (val.equals("cycleway")) {
            Line line = new Line(way, Drawable.lightblue, 13, -0.18, -2);
            line.setDashed();
            drawables.add(line);
        } else if (val.equals("bridleway"))
            drawables.add(new Line(way, Drawable.lightgreen, 1, -1.0, -2));
        else if (val.equals("steps")) {
            Line line = new Line(way, Drawable.red, 14, -0.1, -2);
            line.setDashed();
            drawables.add(line);
        } else if (val.equals("path")) drawables.add(new Line(way, Drawable.red, 1, -0.1, -2));
    }

    @Override
    public void setValueIcon() {

    }

    @Override
    public void setColorBlind() {

    }

    @Override
    public void setStandard() {

    }

}
