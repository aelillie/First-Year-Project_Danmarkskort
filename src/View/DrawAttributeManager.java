package View;

import Model.ValueName;

public class DrawAttributeManager {

    private DrawAttribute[] standardView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] colorblindView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private DrawAttribute[] transportView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];


    private boolean isColorblind = false;
    private boolean isTransport = false;
    //private boolean ##another mode;
    //private boolean ##another mode;

    public DrawAttributeManager() {
        new ColorblindView(colorblindView);
        new StandardView(standardView);
        new TransportView(transportView);
    }

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
}
