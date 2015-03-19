package View;

import Model.ValueName;

/**
 * Created by Anders on 18-03-2015.
 */
public class StandardView {
    private DrawAttribute[] standardView;

    public StandardView(DrawAttribute[] standardView) {
        this.standardView = standardView;
        defineStandardView();
    }

    public void defineStandardView() {
        //standardView[ValueName.VALUENAME.ordinal()] =     new DrawAttribute(isDashed, strokeId, color, zoomLevel);
        //AEROWAY
        standardView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, 8);
        //AMENITY
        standardView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        standardView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.sand, 8);
        standardView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, 8);
        //BARRIER
        standardView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        standardView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, 9);
        standardView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 9);
        standardView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 9);
        //BOUNDARY
        standardView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 8);
        //BRIDGE
        standardView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, 8);
        //BUILDING
        standardView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightgrey, 8);
        //CRAFT
        standardView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //EMERGENCY
        standardView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //GEOLOGICAL
        standardView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //HIGHWAY
        standardView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.white, 6);
        standardView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.lightblue, 0);
        standardView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue,0);
        standardView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, 4);
        standardView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, 4);
        standardView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, 0);
        standardView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, 0);
        standardView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, 1);
        standardView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, 1);
        standardView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, 2);
        standardView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, 2);
        standardView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, 5);
        standardView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, 5);
        standardView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, 7);
        standardView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, 8);
        standardView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightergrey, 8);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, 7);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, 7);
        standardView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, 7);
        standardView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.red, 8);
        standardView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, 8);
        standardView[ValueName.CYCLEWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightblue, 8);
        standardView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, 9);
        standardView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 3, DrawAttribute.red, 9);
        standardView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, 9);
        //HISTORIC
        standardView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        standardView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue,8) ;
        //LANDUSE
        standardView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        standardView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, 9);
        standardView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, 9);
        standardView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, 8);
        standardView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 9);
        standardView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 9);
        standardView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, 9);
        standardView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.darkblue, 9);
        standardView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 9);
        standardView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, 9);
        //LEISURE
        standardView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 8);
        standardView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, 8);
        standardView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.neongreen, 8);
        standardView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, 8);
        standardView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.greenblue, 8);
        standardView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 9);
        //MANMADE
        standardView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //NATURAL
        standardView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, 9);
        standardView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, 6);
        standardView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, 6);
        standardView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, 6);
        standardView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, 6);
        standardView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, 6);
        standardView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, 6);
        standardView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, 6);
        standardView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 5);
        standardView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, 5);
        standardView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, 5);
        standardView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.ground, 0);
        //RAILWAY
        standardView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 2);
        standardView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 3);
        standardView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 2);
        standardView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 3);
        standardView[ValueName.SUBWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.cl_grey1, 3);
        //ROUTE
        standardView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, 8);
        //SHOP
        standardView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, 8);
        //TOURISM
        standardView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 8);
        //WATERWAY
        standardView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, 6);
        standardView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, 6);
        standardView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, 6);
        standardView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, 5);
        standardView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, 5);
        standardView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, 5);
    }
}
