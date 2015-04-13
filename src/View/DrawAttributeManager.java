package View;

import Model.ValueName;

public class DrawAttributeManager {

    //Different view configurations
    private DrawAttribute[] standardView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] colorblindView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] transportView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];


    private boolean isColorblind = false;
    private boolean isTransport = false;
    //private boolean ##another mode;
    //private boolean ##another mode;

    /**
     * Creates different view types
     */
    public DrawAttributeManager() {
        new ColorblindConfig(colorblindView);
        new StandardConfig(standardView);
        new TransportConfig(transportView);
    }

    /**
     * Depending on which view type that is enabled, returns a DrawAttribute from a view configuration
     * @param valueName ENUM value name
     * @return DrawAttribute with stroke, color and zoom level
     */
    public DrawAttribute getDrawAttribute(ValueName valueName) {
        if (isColorblind) return colorblindView[valueName.ordinal()];
        else if (isTransport) return transportView[valueName.ordinal()];
        else return standardView[valueName.ordinal()];
    }

    public void toggleStandardView() {
        isColorblind = false;
        isTransport = false;
    }

    public void toggleColorblindView() {
        isColorblind = true;
        isTransport = false;
    }

    public void toggleTransportView() {
        isColorblind = false;
        isTransport = true;
    }

    public boolean isColorblind() {
        return isColorblind;
    }

    public boolean isTransport() {
        return isTransport;
    }
}
