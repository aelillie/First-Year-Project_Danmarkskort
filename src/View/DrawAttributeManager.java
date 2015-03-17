package View;

import Model.ValueName;

public class DrawAttributeManager {
    private DrawAttribute[] standardView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] colorblindView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private boolean isColorblind;
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
        //HIGHWAY
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

    public DrawAttribute getDrawAttribute(ValueName valueName) {
        if (isColorblind) return colorblindView[valueName.ordinal()];
        else return standardView[valueName.ordinal()];
    }

    public void toggleColorblindView() {
        isColorblind = true;
    }
}
