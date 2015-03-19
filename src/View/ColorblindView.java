package View;

import Model.ValueName;

/**
 * Created by Anders on 18-03-2015.
 */
public class ColorblindView {

    private DrawAttribute[] colorblindView;

    public ColorblindView(DrawAttribute[] colorblindView) {
        this.colorblindView = colorblindView;
        defineColorblindView();
    }

    private void defineColorblindView() {
        //colorblindView
        //[ValueName.VALUENAME.ordinal()] =     new DrawAttribute(isDashed, strokeId, color, zoomLevel);
        //AEROWAY
        colorblindView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1);
        //AMENITY
        colorblindView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        colorblindView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.sand, -1);
        colorblindView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1);
        //BARRIER
        colorblindView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        colorblindView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, -0);
        colorblindView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        colorblindView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        //BOUNDARY
        colorblindView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 2);
        //BRIDGE
        colorblindView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, -2);
        //BUILDING
        colorblindView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0);
        //CRAFT
        colorblindView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0);
        //EMERGENCY
        colorblindView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //GEOLOGICAL
        colorblindView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //HIGHWAY
        colorblindView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.white, -1);
        colorblindView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1);
        colorblindView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1);
        colorblindView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, -1);
        colorblindView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, -1);
        colorblindView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, -1);
        colorblindView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, -1);
        colorblindView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0);
        colorblindView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0);
        colorblindView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, -0);
        colorblindView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, -1);
        colorblindView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, -1);
        colorblindView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, -0);
        colorblindView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightergrey, -0);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, -0);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, -0);
        colorblindView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, -0);
        colorblindView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.white, -0);
        colorblindView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, -0);
        colorblindView[ValueName.CYCLEWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        colorblindView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, -0);
        colorblindView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 2, DrawAttribute.red, -0);
        colorblindView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, -0);
        //HISTORIC
        colorblindView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        colorblindView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue, -1);
        //LANDUSE
        colorblindView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        colorblindView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, -0);
        colorblindView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, -0);
        colorblindView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0);
        colorblindView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0);
        colorblindView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0);
        colorblindView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.darkblue, -0);
        colorblindView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0);
        colorblindView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0);
        //LEISURE
        colorblindView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        colorblindView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        colorblindView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.neongreen, -1);
        colorblindView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        colorblindView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.greenblue, -1);
        colorblindView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -1);
        //MANMADE
        colorblindView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //NATURAL
        colorblindView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, -2);
        colorblindView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, -1);
        colorblindView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, -2);
        colorblindView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, -2);
        colorblindView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, -2);
        colorblindView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, -2);
        colorblindView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, -2);
        colorblindView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -2);
        colorblindView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, -2);
        colorblindView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, -2);
        colorblindView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        //RAILWAY
        colorblindView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2);
        colorblindView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2);
        colorblindView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2);
        colorblindView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2);
        colorblindView[ValueName.SUBWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2);
        //ROUTE
        colorblindView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, -2);
        //SHOP
        colorblindView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //TOURISM
        colorblindView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //WATERWAY
        colorblindView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        colorblindView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
    }
}
