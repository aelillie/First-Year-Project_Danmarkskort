package Controller;

import Model.MapIcon;
import View.View;
import View.IconPanel;

import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;


public class IconController extends Observable implements ComponentListener, ItemListener {


    private URL number;
    private View v;
    //  private static boolean updateView = true;

    /**
     * Tells MapIcon which icons should be drawn.
     *
     * @param number
     */
    public IconController(URL number) {
        this.number = number;

    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    /**
     * Set the icon state to the opposite and update view
     *
     * @param e ItemEvent
     */

    public void itemStateChanged(ItemEvent e) {
        MapIcon.setIconState(this.number, !MapIcon.getIconState(this.number));
        //if(IconController.updateView)
        v.update(null, null);
    }


    public void setView(View v) {
        this.v = v;
    }


}


