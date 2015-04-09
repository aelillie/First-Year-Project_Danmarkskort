package View;

import Model.ValueName;

/**
 * View configuration for transport
 */
public class TransportConfig {
    private DrawAttribute[] transportView;

    public TransportConfig(DrawAttribute[] transportView) {
        this.transportView = transportView;
        defineTransportView();
    }

    private void defineTransportView() {
        //transportView
        //[ValueName.VALUENAME.ordinal()] =     new DrawAttribute(isDashed, strokeId, color, zoomLevel);
        //AEROWAY
        transportView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, 18);
        //AMENITY
        transportView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        transportView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightergrey, 17);
        transportView[ValueName.PUB.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 10);
        transportView[ValueName.BAR.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 10);
        transportView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, 14);
        //BARRIER
        transportView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 18);
        transportView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, 17);
        transportView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        transportView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 17);
        //BOUNDS
        transportView[ValueName.BOUNDS.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whiteblue,-1); //should always be drawn
        //BOUNDARY
        transportView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 8);
        //BRIDGE
        transportView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, 5);
        //BUILDING
        transportView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightergrey, 15);
        //CRAFT
        transportView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //EMERGENCY
        transportView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, 17);
        //GEOLOGICAL
        transportView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, 18);
        //HIGHWAY
        transportView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_grey, 10);
        transportView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.black, 0);
        transportView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.black, 0);
        transportView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 3, DrawAttribute.black, 2);
        transportView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightergrey, 4);
        transportView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightergrey, 5);
        transportView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightergrey, 8);
        transportView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 1, DrawAttribute.lightergrey, 8);
        transportView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightergrey, 8);
        transportView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.cl_grey, 8);
        transportView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, 8);
        transportView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_grey, 8);
        transportView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.cl_grey, 8);
        transportView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightgrey, 10);
        transportView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, 16);
        transportView[ValueName.CYCLEWAY.ordinal()] =        new DrawAttribute(true, 1, DrawAttribute.cl_grey, 10);
        transportView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 1, DrawAttribute.cl_grey, 17);
        transportView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_grey, 16);
        transportView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey, 12);
        //HISTORIC
        transportView[ValueName.HISTORIC.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_grey, 15);
        transportView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.cl_grey, 15);
        //LANDUSE
        transportView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        transportView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, 15);
        transportView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, 15);
        transportView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        transportView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        transportView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        transportView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, 14);
        transportView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.whiteblue, 14);
        transportView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 14);
        transportView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, 16);
        //LEISURE
        transportView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 12);
        transportView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        transportView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, 14);
        transportView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, 12);
        transportView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, 13);
        transportView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whitegreen, 16);
        //MANMADE
        transportView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 9);
        //NATURAL
        transportView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, 13);
        transportView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, 13);
        transportView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, 10);
        transportView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, 10);
        transportView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.whitegreen, 10);
        transportView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        transportView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, 9);
        transportView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, 10);
        transportView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 9);
        transportView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.whitegreen, 9);
        transportView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, 9);
        transportView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.ground, 0);
        //RAILWAY
        transportView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 6, DrawAttribute.cl_red2, 0);
        transportView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 6, DrawAttribute.cl_red3, 0);
        transportView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 6, DrawAttribute.cl_red1, 0);
        transportView[ValueName.TRAM.ordinal()] =           new DrawAttribute(true, 6, DrawAttribute.cl_red, 0);
        transportView[ValueName.SUBWAY.ordinal()] =         new DrawAttribute(true, 6, DrawAttribute.cl_red, 0);
        //ROUTE
        transportView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.grey, 12);
        transportView[ValueName.FERRY.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 5);
        //SHOP
        transportView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        //TOURISM
        transportView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, 16);
        //WATERWAY
        transportView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        transportView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        transportView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, 10);
        transportView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, 9);
        transportView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, 9);
        transportView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, 12);
    }
}
