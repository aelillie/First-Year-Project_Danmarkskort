package Controller;

import View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 18-03-2015.
 */
public class MapMenuController implements ActionListener {
    private View v;
    public MapMenuController(View v){
        this.v = v;


    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if (command.equals("mapTypeChange"))
            v.changeMapType();

    }


}
