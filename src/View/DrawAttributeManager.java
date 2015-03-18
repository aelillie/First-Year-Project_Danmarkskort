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
        new ColorblindView(colorblindView);
        new StandardView(standardView);
    }

    public DrawAttribute getDrawAttribute(ValueName valueName) {
        if (isColorblind) return colorblindView[valueName.ordinal()];
        else return standardView[valueName.ordinal()];
    }

    public void toggleColorblindView() {
        isColorblind = true;
    }
}
