package MapFeatures;

import Model.Drawable;
import Model.MapFeature;

import java.awt.*;
import java.util.Map;

public class Natural extends MapFeature {

    public Natural(Shape way, int layer_value, String value) {
        super(way, layer_value, value);
        setValueAttributes();
        setValueIcon();
    }

    @Override
    public void setValueAttributes() {
        if (value.equals("wood")) setUpWood();
        if (value.equals("scrub")) setUpScrub();
        if (value.equals("heath")) setUpHeath();
        if (value.equals("grassland")) setUpGrassLand();
        if (value.equals("sand")) setUpSand();
        if (value.equals("scree")) setUpScree();
        if (value.equals("fell")) setUpFell();
        if (value.equals("water")) setUpWater();
        if (value.equals("wetland")) setUpWetland();
        if (value.equals("beach")) setUpBeach();
        if (value.equals("coastline")) setUpCoastline();
    }

    private void setUpWood() {
        setColor(Drawable.darkgreen);
        setZoom_level(2.0);
    }

    private void setUpCoastline() {
        setColor(Drawable.darkblue);
        setZoom_level(2.0);
    }

    private void setUpWetland() {

    }

    private void setUpWater() {

    }

    private void setUpScree() {
    }

    private void setUpFell() {

    }

    private void setUpSand() {

    }

    private void setUpGrassLand() {

    }

    private void setUpBeach() {

    }

    private void setUpHeath() {

    }

    private void setUpScrub() {

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
