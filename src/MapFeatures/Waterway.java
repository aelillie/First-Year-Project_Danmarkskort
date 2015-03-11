package MapFeatures;

import Model.Drawable;
import Model.MapFeature;

import java.awt.*;

public class Waterway extends MapFeature {

    public Waterway(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("riverbank")) setUpbank();
        if (value.equals("stream")) setUpStream();
        if (value.equals("canal")) setUpCanal();
        if (value.equals("river")) setUpRiver();
        if (value.equals("dam")) setUpDam();
    }

    private void setUpbank(){
        setColor(Drawable.lightblue);
        setZoom_level(-1.0);
    }

    private void setUpStream(){
        setColor(Drawable.greenblue);
        setZoom_level(-1.0);
    }

    private void setUpCanal(){
        setColor(Color.BLUE);
        setZoom_level(-1.0);
    }

    private void setUpRiver(){
        setColor(Color.blue);
        setZoom_level(-1.0);
    }

    private void setUpDam(){
        setColor(Color.blue);
        setZoom_level(-1.0);
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
