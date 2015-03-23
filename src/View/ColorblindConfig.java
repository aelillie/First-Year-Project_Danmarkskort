package View;

import Model.ValueName;

/**
 * View configuration for color blinds
 */
public class ColorblindConfig {

    private DrawAttribute[] colorblindView;

    public ColorblindConfig(DrawAttribute[] colorblindView) {
        this.colorblindView = colorblindView;
        defineColorblindView();
    }

    private void defineColorblindView() {
        //[ValueName.VALUENAME.ordinal()] =     new DrawAttribute(isDashed, strokeId, color, zoomLevel);
        //AEROWAY
        colorblindView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1.0);
        //AMENITY
        colorblindView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        colorblindView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.sand, -1.0);
        colorblindView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1.0);
        //BARRIER
        colorblindView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        colorblindView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, -0.2);
        colorblindView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0.5);
        colorblindView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0.5);
        //BOUNDARY
        colorblindView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 2.0);
        //BRIDGE
        colorblindView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, -2.0);
        //BUILDING
        colorblindView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_pink, -0.5);
        //CRAFT
        colorblindView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0.8);
        //EMERGENCY
        colorblindView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //GEOLOGICAL
        colorblindView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //HIGHWAY
        colorblindView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 5, DrawAttribute.cl_yellow, -1.0);
        colorblindView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.cl_yellow, -1.0);
        colorblindView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        colorblindView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        colorblindView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        colorblindView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        colorblindView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        colorblindView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        colorblindView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        colorblindView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        colorblindView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, -0.8);
        colorblindView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        colorblindView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        colorblindView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, -0.8);
        colorblindView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightergrey, -0.5);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, -0.4);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, -0.4);
        colorblindView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, -0.4);
        colorblindView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.white, -0.5);
        colorblindView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, -0.5);
        colorblindView[ValueName.CYCLEWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightblue, -0.1);
        colorblindView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, -0.1);
        colorblindView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 2, DrawAttribute.red, -0.1);
        colorblindView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, -0.1);
        //HISTORIC
        colorblindView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        colorblindView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue, -1.0);
        //LANDUSE
        colorblindView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        colorblindView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, -0.8);
        colorblindView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, -0.4);
        colorblindView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1.0);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        colorblindView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        colorblindView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        colorblindView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.darkblue, -0.8);
        colorblindView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0.8);
        colorblindView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0.8);
        //LEISURE
        colorblindView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.0);
        colorblindView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.2);
        colorblindView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.2);
        colorblindView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.0);
        colorblindView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.0);
        colorblindView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_green, -1.0);
        //MANMADE
        colorblindView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //NATURAL
        colorblindView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, -2.0);
        colorblindView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, -1.5);
        colorblindView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, -2.0);
        colorblindView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, -2.0);
        colorblindView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, -2.0);
        colorblindView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, -2.0);
        colorblindView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, -2.0);
        colorblindView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -2.0);
        colorblindView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, -2.0);
        colorblindView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, -2.0);
        colorblindView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.ground, -1.0);
        //RAILWAY
        colorblindView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2.0);
        colorblindView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2.0);
        colorblindView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2.0);
        colorblindView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2.0);
        colorblindView[ValueName.SUBWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, -2.0);
        //ROUTE
        colorblindView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, -2.0);
        //SHOP
        colorblindView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //TOURISM
        colorblindView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //WATERWAY
        colorblindView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        colorblindView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
    }
}
