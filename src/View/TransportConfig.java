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
        transportView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        transportView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightergrey, -1);
        transportView[ValueName.PUB.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 10);
        transportView[ValueName.BAR.ordinal()]     =         new DrawAttribute(false, 1, DrawAttribute.cl_pink, 10);
        transportView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1);
        //BARRIER
        transportView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        transportView[ValueName.HENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.neongreen, -0);
        transportView[ValueName.FENCE.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        transportView[ValueName.KERB.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        transportView[ValueName.KERB.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, -0);
        //BOUNDS
        transportView[ValueName.BOUNDS.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whiteblue,-5);
        //BOUNDARY
        transportView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 2);
        //BRIDGE
        transportView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, -2);
        //BUILDING
        transportView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightergrey, -0);
        //CRAFT
        transportView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0);
        //EMERGENCY
        transportView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //GEOLOGICAL
        transportView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //HIGHWAY
        transportView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.cl_grey, -1);
        transportView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.black, -1);
        transportView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.black, -1);
        transportView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 3, DrawAttribute.black, -1);
        transportView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightergrey, -1);
        transportView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightergrey, -0);
        transportView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightergrey, -0);
        transportView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 1, DrawAttribute.lightergrey, -1);
        transportView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightergrey, -1);
        transportView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, -0);
        transportView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.lightgrey, -0);
        transportView[ValueName.FOOTWAY_AREA.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.lightgrey, -0);
        transportView[ValueName.CYCLEWAY.ordinal()] =        new DrawAttribute(true, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.cl_grey, -0);
        transportView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 1, DrawAttribute.cl_grey, -0);
        //HISTORIC
        transportView[ValueName.HISTORIC.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.cl_grey, -1);
        transportView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.cl_grey, -1);
        //LANDUSE
        transportView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        transportView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, -0);
        transportView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, -0);
        transportView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whitegreen, -0);
        transportView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0);
        transportView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0);
        transportView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0);
        transportView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0);
        transportView[ValueName.ALLOTMENTS.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0);
        //LEISURE
        transportView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        transportView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        //MANMADE
        transportView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //NATURAL
        transportView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        transportView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, -2);
        transportView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1);
        transportView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, -2);
        transportView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.whitegreen, -2);
        transportView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, -2);
        transportView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, -2);
        transportView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, -2);
        transportView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -2);
        transportView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.whitegreen, -2);
        transportView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, -2);
        transportView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.ground, -1);
        //RAILWAY
        transportView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 4, DrawAttribute.cl_red2, -3);
        transportView[ValueName.LIGHT_RAIL.ordinal()] =      new DrawAttribute(true, 4, DrawAttribute.cl_red3, -3);
        transportView[ValueName.RAIL.ordinal()] =            new DrawAttribute(true, 4, DrawAttribute.cl_red1, -3);
        transportView[ValueName.TRAM.ordinal()] =            new DrawAttribute(true, 4, DrawAttribute.cl_red, -3);
        transportView[ValueName.SUBWAY.ordinal()] =          new DrawAttribute(true, 4, DrawAttribute.cl_red, -3);
        //ROUTE
        transportView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.grey, 12);
        transportView[ValueName.FERRY.ordinal()] =           new DrawAttribute(true, 1, DrawAttribute.lightblue, 12);
        //SHOP
        transportView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //TOURISM
        transportView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1);
        //WATERWAY
        transportView[ValueName.WATERWAY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        transportView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        transportView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        transportView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -1);
        transportView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
        transportView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, -1);
    }
}
