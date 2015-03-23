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
        colorblindView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, 18);
        //AMENITY
        colorblindView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        colorblindView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.sand, 17);
        colorblindView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, 14);
        //BARRIER
        colorblindView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 18);
        colorblindView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, 17);
        colorblindView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        colorblindView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        //BOUNDARY
        colorblindView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 8);
        //BRIDGE
        colorblindView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, 10);
        //BUILDING
        colorblindView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightgrey, 14);
        //CRAFT
        colorblindView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //EMERGENCY
        colorblindView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //GEOLOGICAL
        colorblindView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, 18);
        //HIGHWAY
        colorblindView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.white, 15);
        colorblindView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.lightblue, 0);
        colorblindView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue, 0);
        colorblindView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, 5);
        colorblindView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, 5);
        colorblindView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, 5);
        colorblindView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, 5);
        colorblindView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, 10);
        colorblindView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, 10);
        colorblindView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, 12);
        colorblindView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, 12);
        colorblindView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, 14);
        colorblindView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, 10);
        colorblindView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, 14);
        colorblindView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, 14);
        colorblindView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightergrey, 14);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, 12);
        colorblindView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, 12);
        colorblindView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, 12);
        colorblindView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.white, 16);
        colorblindView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, 16);
        colorblindView[ValueName.CYCLEWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightblue, 15);
        colorblindView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, 17);
        colorblindView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 2, DrawAttribute.red, 16);
        colorblindView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, 16);
        //HISTORIC
        colorblindView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 15);
        colorblindView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue, 15);
        //LANDUSE
        colorblindView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        colorblindView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, 15);
        colorblindView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, 15);
        colorblindView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        colorblindView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 12);
        colorblindView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        colorblindView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        colorblindView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.darkblue, 14);
        colorblindView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 14);
        colorblindView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, 16);
        //LEISURE
        colorblindView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 12);
        colorblindView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        colorblindView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.neongreen, 14);
        colorblindView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        colorblindView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.greenblue, 13);
        colorblindView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 16);
        //MANMADE
        colorblindView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //NATURAL
        colorblindView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, 13);
        colorblindView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, 13);
        colorblindView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, 10);
        colorblindView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, 10);
        colorblindView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, 10);
        colorblindView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        colorblindView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, 9);
        colorblindView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, 10);
        colorblindView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 9);
        colorblindView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, 9);
        colorblindView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        colorblindView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, 0);
        //RAILWAY
        colorblindView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 6);
        colorblindView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 8);
        colorblindView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 8);
        colorblindView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 8);
        colorblindView[ValueName.SUBWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 10);
        //ROUTE
        colorblindView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, 12);
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
