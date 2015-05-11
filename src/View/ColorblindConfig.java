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
        colorblindView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, 9);
        colorblindView[ValueName.TERMINAL.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.purple, 9);
        colorblindView[ValueName.RUNWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.teal, 9);
        colorblindView[ValueName.TAXIWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.teal, 9);
        //AMENITY
        colorblindView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_pink, 14);
        colorblindView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 14);
        colorblindView[ValueName.PUB.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 13);
        colorblindView[ValueName.BAR.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 13);
        colorblindView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_darkorange, 12);
        colorblindView[ValueName.SCHOOL_AREA.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_purple, 12);
        colorblindView[ValueName.PHARMACEUTICAL.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_purple, 12);
        colorblindView[ValueName.HOSPITAL.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_purple, 12);
        //BARRIER
        colorblindView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 15);
        colorblindView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_green, 15);
        colorblindView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 15);
        colorblindView[ValueName.KERB.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.lightblue, 15);
        //BOUNDARY
        colorblindView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.cl_darkorange, 8);
        //BRIDGE
        colorblindView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 10);
        //BOUNDS
        colorblindView[ValueName.BOUNDS.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue,-5);
        //BUILDING
        colorblindView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 11);
        //CRAFT
        colorblindView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_orange, 15);
        //EMERGENCY
        colorblindView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_orange, 15);
        //GEOLOGICAL
        colorblindView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_orange, 15);
        //HIGHWAY
        colorblindView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_white, 12);
        colorblindView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.cl_white, 0);
        colorblindView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.cl_green, 0);
        colorblindView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.cl_pink, 0);
        colorblindView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.cl_green, 4);
        colorblindView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.cl_white, 6);
        colorblindView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.cl_purple, 11);
        colorblindView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.cl_purple, 11);
        colorblindView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.cl_purple, 11);
        colorblindView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, 12);
        colorblindView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.cl_pink, 12);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.cl_pink, 12);
        colorblindView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.cl_purple, 12);
        colorblindView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_pink, 12);
        colorblindView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.cl_pink, 12);
        colorblindView[ValueName.CYCLEWAY.ordinal()] =        new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 12);
        colorblindView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.cl_lightblue, 12);
        colorblindView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 3, DrawAttribute.cl_lightblue, 14);
        colorblindView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.cl_lightblue, 12);
        //HISTORIC
        colorblindView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_purple, 15);
        colorblindView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.cl_purple, 15);
        //LANDUSE
        colorblindView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_purple, 6);
        colorblindView[ValueName.FOREST.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.darkgreen, -5);
        colorblindView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, 10);
        colorblindView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.cl_pink, 15);
        colorblindView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 6);
        colorblindView[ValueName.RESIDENTIAL_AREA.ordinal()] = new DrawAttribute(false, 1, DrawAttribute.cl_pink, 6);
        colorblindView[ValueName.MEADOW.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_green, 6);
        colorblindView[ValueName.FARMLAND.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.teal, 6);
        colorblindView[ValueName.MILITARY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.teal, 10);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_purple, 6);
        colorblindView[ValueName.BROWNFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 12);
        colorblindView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_pink, 6);
        colorblindView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 10);
        colorblindView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 10);
        colorblindView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, 10);
        colorblindView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_purple, 6);
        //LEISURE
        colorblindView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_green, 10);
        colorblindView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen,  10);
        colorblindView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_green, 10);
        colorblindView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_green,  9);
        colorblindView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 11);
        colorblindView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 10);
        //MANMADE
        colorblindView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        //MULTIPOLYGON
        colorblindView[ValueName.MULTIPOLYGON.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.cl_darkorange, 15);
        //NATURAL
        colorblindView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.MUD.ordinal()] =             new DrawAttribute(false, 1, DrawAttribute.cl_darkorange, 9);
        colorblindView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_green, 9);
        colorblindView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_green, 9);
        colorblindView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        colorblindView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 9);
        colorblindView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        colorblindView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        colorblindView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_darkorange, 10);
        colorblindView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_whiteblue, -5);
        colorblindView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_darkgreen, 9);
        colorblindView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_pink, 9);
        colorblindView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_orange, 0);
        //PLACE
        colorblindView[ValueName.PLACE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_orange, 13);
        colorblindView[ValueName.ISLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_orange, 13);
        colorblindView[ValueName.ISLET.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_orange, 13);
        //RAILWAY
        colorblindView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        colorblindView[ValueName.SUBWAY.ordinal()] =          new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 8);
        //ROUTE
        colorblindView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_pink, 12);
        colorblindView[ValueName.FERRY.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_lightblue, 12);

        //POWER
        colorblindView[ValueName.POWER.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_pink, 14);
        colorblindView[ValueName.POWER_AREA.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_pink, 14);
        //SHOP
        colorblindView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.cl_orange, 16);
        //TOURISM
        colorblindView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.cl_orange, 16);
        //WATERWAY
        colorblindView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
        colorblindView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.cl_lightblue, 9);
    }
}
