package Controller;

import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import Model.Model;

public class OptionsPanelController implements ActionListener {
    private View view;
    private Model model;

    public OptionsPanelController(OptionsPanel panel, View view, Model model){
        this.view = view;
        this.model = model;
        panel.getLoadButton().addActionListener(this);
        panel.getSaveButton().addActionListener(this);
        panel.getToggleIconPanelButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if(command.equals("toggleIconPanel")) view.showIconPanel();
        else if (command.equals("load")) loadSelectedFile();
        else if (command.equals("save")) model.saveBin(); view.repaint();
    }

    private void loadSelectedFile(){
        int returnValue = view.openFileChooser(); //The return value represents the action taken within the filechooser
        if(returnValue == JFileChooser.APPROVE_OPTION){ //Return value if yes/ok is chosen.

            try {
                File file = view.getFileChooser().getSelectedFile(); //fetch file
                URL fileURL = file.toURI().toURL(); //Convert to URL
                InputStream inputStream = fileURL.openStream();
                String filename = fileURL.getFile();
                model.setCurrentFilename(filename);
                model.loadFile(filename, inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.repaint();
            view.scaleAffine();
        } else { //If no file is chosen (the user pressed cancel) or if an error occured
            view.repaint();
        }
    }


}