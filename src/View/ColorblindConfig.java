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
        colorblindView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, 18);
        //AMENITY
        colorblindView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        colorblindView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 17);
        colorblindView[ValueName.PUB.ordinal()]     =             new DrawAttribute(false, 1, DrawAttribute.cl_pink, 17);
        colorblindView[ValueName.BAR.ordinal()]     =             new DrawAttribute(false, 1, DrawAttribute.cl_pink, 17);
        colorblindView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_darkorange, 14);
        colorblindView[ValueName.SCHOOL_AREA.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_green, 14);
        colorblindView[ValueName.PHARMACEUTICAL.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_green, 14);
        //BARRIER
        colorblindView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 18);
        colorblindView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_green, 17);
        colorblindView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        colorblindView[ValueName.KERB.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        //BOUNDARY
        colorblindView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 8);
        //BRIDGE
        colorblindView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, 10);
        //BOUNDS
        colorblindView[ValueName.BOUNDS.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue,0);
        //BUILDING
        colorblindView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 14);
        //CRAFT
        colorblindView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //EMERGENCY
        colorblindView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //GEOLOGICAL
        colorblindView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, 18);
        //HIGHWAY
        colorblindView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_white, 15);
        colorblindView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.cl_white, 0);
        colorblindView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.cl_green, 5);
        colorblindView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.cl_whiteblue, 5);
        colorblindView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.cl_whiteblue, 10);
        colorblindView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.cl_white, 12);
        colorblindView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.cl_purple, 14);
        colorblindView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.cl_purple, 10);
        colorblindView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.cl_purple, 14);
        colorblindView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, 14);
        colorblindView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_pink, 14);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.cl_pink, 14);
        colorblindView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.cl_purple, 12);
        colorblindView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_pink, 16);
        colorblindView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.cl_pink, 16);
        colorblindView[ValueName.CYCLEWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightblue, 15);
        colorblindView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.cl_pink, 17);
        colorblindView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 3, DrawAttribute.cl_pink, 16);
        colorblindView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.cl_pink, 16);
        //HISTORIC
        colorblindView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 15);
        colorblindView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue, 15);
        //LANDUSE
        colorblindView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        colorblindView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, 15);
        colorblindView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.cl_pink, 15);
        colorblindView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_green, 12);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 12);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.brown, 12);
        colorblindView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        colorblindView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        colorblindView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 14);
        colorblindView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 14);
        colorblindView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 16);
        //LEISURE
        colorblindView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_green, 11);
        colorblindView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_green,  11);
        colorblindView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_green, 14);
        colorblindView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_green,  12);
        colorblindView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_green, 13);
        colorblindView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_green, 16);
        //MANMADE
        colorblindView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //NATURAL
        colorblindView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, 13);
        colorblindView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, 13);
        colorblindView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, 10);
        colorblindView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, 10);
        colorblindView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, 10);
        colorblindView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        colorblindView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        colorblindView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, 10);
        colorblindView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 9);
        colorblindView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, 9);
        colorblindView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        colorblindView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_orange, 0);
        //RAILWAY
        colorblindView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 6);
        colorblindView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.SUBWAY.ordinal()] =          new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 10);
        //ROUTE
        colorblindView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, 12);
        colorblindView[ValueName.FERRY.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 12);
        //SHOP
        colorblindView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        //TOURISM
        colorblindView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        //WATERWAY
        colorblindView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        colorblindView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        colorblindView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        colorblindView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, 9);
        colorblindView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, 9);
        colorblindView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, 12);
    }
}
