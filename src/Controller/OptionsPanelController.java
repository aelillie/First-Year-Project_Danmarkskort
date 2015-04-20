package Controller;

import View.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 18-03-2015.
 */
public class OptionsPanelController implements ActionListener {
    private View v;

    public OptionsPanelController(OptionsPanel panel){
        v = panel.getView();
        panel.getLoadButton().addActionListener(this);
        panel.getSaveButton().addActionListener(this);
        panel.getToggleIconPanelButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if(command.equals("toggleIconPanel")) v.showIconPanel();
    }


}