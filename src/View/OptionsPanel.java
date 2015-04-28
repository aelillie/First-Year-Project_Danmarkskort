package View;

import Controller.OptionsPanelController;
import Model.Model;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Nicoline on 30-03-2015.
 */
public class OptionsPanel extends JPanel {

    private JButton saveButton;
    private JButton loadButton;
    private JButton toggleIconPanelButton;
    private View view;
    private Model model;

    public OptionsPanel(View view, Model model){
        this.view = view;
        setVisible(false);
        setBounds(view.getWidth() - 300, view.getHeight() - view.getHeight() / 3 - 45, 200, 200);
        setOpaque(true);
        setBackground(DrawAttribute.fadeblack);
        setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK), new EmptyBorder(0,0,0,0)));
        setLayout(new GridLayout(2,1,10,0));
        this.model = model;
        init();
    }

    private void init(){
        JPanel saveLoadPanel = new JPanel();
        saveLoadPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        saveLoadPanel.setOpaque(false);
        loadButton = new JButton("LOAD");
        loadButton.setBackground(Color.WHITE);
        loadButton.setForeground(Color.BLACK);
        loadButton.setActionCommand("load");
        saveButton = new JButton("SAVE");
        saveButton.setActionCommand("save");
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(Color.BLACK);
        saveLoadPanel.add(loadButton);
        saveLoadPanel.add(saveButton);
        saveLoadPanel.add(loadButton);
        add(saveLoadPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        bottomPanel.setOpaque(false);
        toggleIconPanelButton = new JButton("TOOGLE ICONS");
        toggleIconPanelButton.setActionCommand("toggleIconPanel");
        toggleIconPanelButton.setBackground(Color.WHITE);
        toggleIconPanelButton.setForeground(Color.BLACK);
        toggleIconPanelButton.setPreferredSize(new Dimension(135, 25));
        bottomPanel.add(toggleIconPanelButton);
        add(bottomPanel);
        new OptionsPanelController(this,view,model);
    }

    public void showOptionsPanel(){
        boolean isVisible = isVisible();
        setVisible(!isVisible);
    }

    public View getView(){ return view;}
    public JButton getSaveButton(){ return saveButton;}
    public JButton getLoadButton(){ return loadButton;}
    public JButton getToggleIconPanelButton(){ return toggleIconPanelButton;}

}
