package Controller;

import Model.Address;
import Model.Model;
import View.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Controller extends MouseAdapter implements ActionListener {
    Model model;
    View view;


    public Controller(Model m, View v) {
        model = m;
        view = v;

        setHandlers();
    }

    private void setHandlers(){
        //Set up Handlers for mouse and keyboard and let controller set these for view.
        keyHandler kH = new keyHandler();
        view.getCanvas().addKeyListener(kH);
        view.getSearchArea().addKeyListener(kH);
        MouseHandler mH = new MouseHandler();
        view.addMouseListener(mH);
        view.addMouseMotionListener(mH);
        view.addMouseWheelListener(mH);

        // The controller handles what should happen if a button is pressed.
        view.getSearchArea().addActionListener(this);
        view.getSearchButton().addActionListener(this);
        view.getZoomInButton().addActionListener(this);
        view.getZoomOutButton().addActionListener(this);
        view.getLoadButton().addActionListener(this);
        view.getFullscreenButton().addActionListener(this);
        view.getShowRoutePanelButton().addActionListener(this);
        view.getOptionsButton().addActionListener(this);
    }

    @Override
    /**
     * Sets up what should happen when a button is pressed.
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("zoomIn")) view.zoom(1.2);
        else if (command.equals("zoomOut")) view.zoom(1 / 1.2);
        else if (command.equals("load")) loadSelectedFile();
        else if (command.equals("fullscreen")) view.toggleFullscreen();
        else if (command.equals("search")) addressSearch();
        else if (command.equals("showRoutePanel")) view.showRoutePanel();
        else if (command.equals("findRoute"));
        else if (command.equals("showOptions"))view.repaint();
    }

    private void addressSearch(){
        String input = view.getSearchArea().getText().trim().toLowerCase();
        Address address = Address.parse(input);
        //System.out.println(address.street()+" " + address.house()+" "+address.side()+ " "+address.city()+" "+address.postcode());
        view.getCanvas().requestFocusInWindow();
        model.searchForAddresses(address);
    }

    private void loadSelectedFile(){
        int returnValue = view.openFileChooser(); //The returnvalue represents the action taken within the filechooser
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


    // sets up events for mouse and calls the methods in view.
    private class MouseHandler extends MouseAdapter {

        public void mouseDragged(MouseEvent e) {
            view.setAntialias(false);
            view.mouseDragged(e);
        }
        public void mouseMoved(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {

            view.mousePressed(e);
        }
        public void mouseWheelMoved(MouseWheelEvent e) {
            view.wheelZoom(e);
        }
        public void mouseReleased(MouseEvent e) {
            view.setAntialias(true);
            view.repaint();
        }
    }
    private class keyHandler extends KeyAdapter{

        @Override
        /**
         * Listens for keyboard events
         */
        public void keyPressed(KeyEvent e) {
            //InputStream inputStream = Controller.class.getResourceAsStream("/binaryModel.bin");
            //Set up the keyboard handler for different keys.
            if(!view.getSearchArea().hasFocus()) {
                switch (e.getKeyChar()) {
                    case '+':
                        view.zoom(1.2);
                        break;
                    case '-':
                        view.zoom(1 / 1.2);
                        break;
                    case 'a':
                        view.toggleAA();
                        break;
                    case 's':
                        model.saveBin(model.getCurrentFilename() + ".bin");
                        break;
                    /*case 'l': use loadSelectedFile in runtime instead
                        try {
                            model.loadFile("binaryModel.bin", inputStream);
                        } catch (NullPointerException | IOException n) {
                            System.out.println("There is no 'binaryModel.bin' to load.");
                            n.printStackTrace();
                        }
                        break;*/

                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    view.pan(0, 10);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    view.pan(0, -10);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    view.pan(10, 0);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    view.pan(-10, 0);
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                addressSearch();
            }
        }
    }
}

