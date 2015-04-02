package Controller;

import View.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 18-03-2015.
 */
public class MapMenuController implements ActionListener {
    private View v;
    public MapMenuController(MapTypePanel panel){
        v = panel.getView();
        panel.getStandardButton().addActionListener(this);
        panel.getColorblindButton().addActionListener(this);
        panel.getTransportButton().addActionListener(this);

    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if (command.equals("standardMap")){
            v.changeToStandard();
        } else if (command.equals("colorblindMap")){
            v.changeToColorblind();
        } else if (command.equals("transportMap")){
            v.changeToTransport();
        }
    }


}