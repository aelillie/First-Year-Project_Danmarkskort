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
        switch (command) {
            case "standardMap":
                v.changeToStandard();
                break;
            case "colorblindMap":
                v.changeToColorblind();
                break;
            case "transportMap":
                v.changeToTransport();
                break;
        }
    }


}