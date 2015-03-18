package View;

import Model.ValueName;

public class DrawAttributeManager {
    private DrawAttribute[] standardView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] colorblindView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private boolean isColorblind = false;
    //private boolean isStandard;
    //private boolean ##another mode;
    //private boolean ##another mode;

    public DrawAttributeManager() {
        defineColorblindView();
        defineStandardView();
    }

    private void defineColorblindView() {
        //HIGHWAY Colorblind
        standardView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        standardView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        standardView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        standardView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        standardView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        standardView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        standardView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        standardView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        standardView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, -0.8);
        standardView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        standardView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        standardView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, -0.8);
        standardView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.white, -0.5);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, -0.4);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, -0.4);
        standardView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, -0.4);
        standardView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.white, -0.5);
        standardView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, -0.1);
        standardView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 2, DrawAttribute.red, -0.1);
        standardView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, -0.1);
    }

    public void defineStandardView() {
        //standardView[ValueName.VALUENAME.ordinal()] =     new DrawAttribute(isDashed, strokeId, color, zoomLevel);
        //AEROWAY
        standardView[ValueName.AEROWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1.0);
        //AMENITY
        standardView[ValueName.AMENITY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        standardView[ValueName.PARKING.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.sand, -1.0);
        standardView[ValueName.UNIVERSITY.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.lightgrey, -1.0);
        //BARRIER
        standardView[ValueName.BARRIER.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        standardView[ValueName.HENCE.ordinal()] =           new DrawAttribute(false, 1, DrawAttribute.neongreen, -0.2);
        standardView[ValueName.FENCE.ordinal()] =           new DrawAttribute(false, 1, DrawAttribute.lightblue, -1.0);
        //BOUNDARY
        standardView[ValueName.BOUNDARY.ordinal()] =        new DrawAttribute(true, 0, DrawAttribute.white, 2.0);
        //BRIDGE
        standardView[ValueName.BRIDGE.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.grey, -2.0);
        //BUILDING
        standardView[ValueName.BUILDING.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0.5);
        //CRAFT
        standardView[ValueName.CRAFT.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgrey, -0.8);
        //EMERGENCY
        standardView[ValueName.EMERGENCY.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //GEOLOGICAL
        standardView[ValueName.GEOLOGICAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //HIGHWAY
        standardView[ValueName.HIGHWAY.ordinal()] =         new DrawAttribute(false, 1, DrawAttribute.white, -1.0);
        standardView[ValueName.MOTORWAY.ordinal()] =        new DrawAttribute(false, 5, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.MOTORWAY_LINK.ordinal()] =   new DrawAttribute(false, 5, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.TRUNK.ordinal()] =           new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        standardView[ValueName.TRUNK_LINK.ordinal()] =      new DrawAttribute(false, 3, DrawAttribute.lightgreen, -1.0);
        standardView[ValueName.PRIMARY.ordinal()] =         new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        standardView[ValueName.PRIMARY_LINK.ordinal()] =    new DrawAttribute(false, 4, DrawAttribute.babyred, -1.0);
        standardView[ValueName.SECONDARY.ordinal()] =       new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        standardView[ValueName.SECONDARY_LINK.ordinal()] =  new DrawAttribute(false, 3, DrawAttribute.lightred, -1.0);
        standardView[ValueName.TERTIARY.ordinal()] =        new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        standardView[ValueName.TERTIARY_LINK.ordinal()] =   new DrawAttribute(false, 3, DrawAttribute.lightyellow, -0.8);
        standardView[ValueName.UNCLASSIFIED.ordinal()] =    new DrawAttribute(false, 1, DrawAttribute.white, -0.8);
        standardView[ValueName.RESIDENTIAL.ordinal()] =     new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        standardView[ValueName.SERVICE.ordinal()] =         new DrawAttribute(false, 2, DrawAttribute.white, -1.0);
        standardView[ValueName.LIVING_STREET.ordinal()] =   new DrawAttribute(false, 1, DrawAttribute.grey, -0.8);
        standardView[ValueName.PEDESTRIAN.ordinal()] =      new DrawAttribute(false, 1, DrawAttribute.white, -0.5);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(true, 0, DrawAttribute.brown, -0.4);
        standardView[ValueName.TRACK.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.darkblue, -0.4);
        standardView[ValueName.ROAD.ordinal()] =            new DrawAttribute(false, 1, DrawAttribute.grey, -0.4);
        standardView[ValueName.FOOTWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.white, -0.5);
        standardView[ValueName.BRIDLEWAY.ordinal()] =       new DrawAttribute(true, 0, DrawAttribute.lightgreen, -0.1);
        standardView[ValueName.STEPS.ordinal()] =           new DrawAttribute(true, 2, DrawAttribute.red, -0.1);
        standardView[ValueName.PATH.ordinal()] =            new DrawAttribute(true, 0, DrawAttribute.red, -0.1);
        //HISTORIC
        standardView[ValueName.HISTORIC.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        standardView[ValueName.ARCHAEOLOGICAL_SITE.ordinal()] = new DrawAttribute(false, 0, DrawAttribute.greenblue, -1.0);
        //LANDUSE
        standardView[ValueName.LANDUSE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        standardView[ValueName.CEMETERY.ordinal()] =        new DrawAttribute(false, 0, DrawAttribute.whitegreen, -0.8);
        standardView[ValueName.CONSTRUCTION.ordinal()] =    new DrawAttribute(false, 0, DrawAttribute.pink, -0.4);
        standardView[ValueName.GRASS.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1.0);
        standardView[ValueName.GREENFIELD.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        standardView[ValueName.INDUSTRIAL.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        standardView[ValueName.ORCHARD.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.bluegreen, -0.8);
        standardView[ValueName.RESERVOIR.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.darkblue, -0.8);
        standardView[ValueName.BASIN.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -0.8);
        //LEISURE
        standardView[ValueName.LEISURE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        standardView[ValueName.GARDEN.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1.2);
        standardView[ValueName.COMMON.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.neongreen, -1.2);
        standardView[ValueName.PARK.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.whitegreen, -1.0);
        standardView[ValueName.PITCH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.greenblue, -1.0);
        standardView[ValueName.PLAYGROUND.ordinal()] =      new DrawAttribute(false, 0, DrawAttribute.bluegreen, -1.0);
        //MANMADE
        standardView[ValueName.MANMADE.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //NATURAL
        standardView[ValueName.NATURAL.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.WOOD.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgreen, -2.0);
        standardView[ValueName.SCRUB.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightgreen, -1.5);
        standardView[ValueName.HEATH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.skincolor, -2.0);
        standardView[ValueName.GRASSLAND.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.bluegreen, -2.0);
        standardView[ValueName.SAND.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.sand, -2.0);
        standardView[ValueName.SCREE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.pink, -2.0);
        standardView[ValueName.FELL.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.orange, -2.0);
        standardView[ValueName.WATER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.whiteblue, -2.0);
        standardView[ValueName.WETLAND.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.greenblue, -2.0);
        standardView[ValueName.BEACH.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.sand, -2.0);
        standardView[ValueName.COASTLINE.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        //RAILWAY
        standardView[ValueName.RAILWAY.ordinal()] =         new DrawAttribute(true, 1, DrawAttribute.grey, -2.0);
        //ROUTE
        standardView[ValueName.ROUTE.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.white, -2.0);
        //SHOP
        standardView[ValueName.SHOP.ordinal()] =            new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //TOURISM
        standardView[ValueName.TOURISM.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightgrey, -1.0);
        //WATERWAY
        standardView[ValueName.WATERWAY.ordinal()] =         new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.RIVERBANK.ordinal()] =       new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.STREAM.ordinal()] =          new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.CANAL.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.RIVER.ordinal()] =           new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
        standardView[ValueName.DAM.ordinal()] =             new DrawAttribute(false, 0, DrawAttribute.lightblue, -1.0);
    }

    public DrawAttribute getDrawAttribute(ValueName valueName) {
        /*
        if (isColorblind) return colorblindView[valueName.ordinal()];
        else return standardView[valueName.ordinal()]; */
        return standardView[valueName.ordinal()];
    }

    public void toggleColorblindView() {
        isColorblind = true;
    }
}
