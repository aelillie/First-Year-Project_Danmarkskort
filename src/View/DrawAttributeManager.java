package View;

import Model.ValueName;


/**
 * Created by Anders on 17-03-2015.
 */
public class DrawAttributeManager {
    static DrawAttribute[] standardView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    static DrawAttribute[] colorblindView = new DrawAttribute[ValueName.LAST_VALUE_NAME.ordinal()];
    private boolean isColorblind;

    public DrawAttributeManager() {
        constructColorblindView();
        constructStandardView();
    }

    private void constructColorblindView() {

    }

    public void constructStandardView() {
        standardView[ValueName.PRIMARY.ordinal()] = new DrawAttribute(false, 4, DrawAttribute.babyred);

    }

    public DrawAttribute getDrawAttribute(ValueName valueName) {
        if (isColorblind) return colorblindView[valueName.ordinal()];
        else return standardView[valueName.ordinal()];
    }

    public boolean setColorblindView() {
        isColorblind = true;
    }
}
