package Controller;

import View.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 18-03-2015.
 */
public class MapMenuController implements ActionListener {
    private View v;
    private MapTypePanel mapPanel;
    public MapMenuController(MapTypePanel panel){
        v = panel.getView();
        mapPanel = panel;
        panel.getStandardButton().addActionListener(this);
        panel.getColorblindButton().addActionListener(this);
        panel.getTransportButton().addActionListener(this);

    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        switch (command) {
            case "standardMap":
                v.changeToStandard();
                mapPanel.setVisible(false);

                break;
            case "colorblindMap":
                v.changeToColorblind();
                mapPanel.setVisible(false);
                break;
            case "transportMap":
                v.changeToTransport();
                mapPanel.setVisible(false);
                break;
        }
    }


}