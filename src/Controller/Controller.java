package Controller;

import Model.Model;
import View.View;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;

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
        SearchController searchController = new SearchController(model, view);
        keyHandler kH = new keyHandler();

        view.getCanvas().addKeyListener(kH);
        MouseHandler mH = new MouseHandler();
        view.addMouseListener(mH);
        view.addMouseMotionListener(mH);
        view.addMouseWheelListener(mH);

        //The controller handles what should happen if a button is pressed.

        view.getZoomInButton().addActionListener(this);
        view.getZoomOutButton().addActionListener(this);
        view.getFullscreenButton().addActionListener(this);
        view.getShowRoutePanelButton().addActionListener(this);
        view.getOptionsButton().addActionListener(this);
        view.getMapTypeButton().addActionListener(this);
        view.getCloseDirectionListButton().addActionListener(this);
    }

    @Override
    /**
     * Sets up what should happen when a button is pressed.
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("zoomIn")) view.zoom(1.2);
        else if (command.equals("zoomOut")) view.zoom(1 / 1.2);
        else if (command.equals("fullscreen")) view.toggleFullscreen();
        else if (command.equals("showRoutePanel")) view.showRoutePanel();
        else if (command.equals("findRoute"));
        else if (command.equals("showOptions")) showOptionsPanel();
        else if (command.equals("mapType")) view.showMapTypePanel();
        else if (command.equals("closeDirectionList")) view.closeDirectionList();
    }

    private void showOptionsPanel(){
        view.showOptionsPanel();
        view.repaint();
    }

    // sets up events for mouse and calls the methods in view.
    private class MouseHandler extends MouseAdapter {

        public void mouseDragged(MouseEvent e) {
            view.setAntialias(false);
            view.mouseDragged(e);
        }
        public void mouseMoved(MouseEvent e) {
            try{
                view.findNearestToMouse(e.getPoint());
            }catch(NoninvertibleTransformException x){
                System.out.print("wow something went really wrong tried to transform something that wasn't a point");
            }
        }
        public void mouseClicked(MouseEvent e) {
            try {
                if (e.getButton() == 1 && e.isAltDown())
                    view.setStartPoint(e.getPoint());
                else if (e.getButton() == 3 && e.isAltDown())
                    view.setEndPoint(e.getPoint());
                else if (e.getButton() == 1 && e.isControlDown())
                    view.setStartPoint(null);
                else if (e.getButton() == 3 && e.isControlDown())
                    view.setEndPoint(null);

            }catch(NoninvertibleTransformException ex){
                ex.printStackTrace();
            }
        }
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
                        if(e.isAltDown())
                        view.toggleAA();
                        break;
                    case 's':
                        if(e.isAltDown())
                            model.saveBin();
                        break;
                    case't':
                        if(e.isAltDown() ) {
                            view.toggleTestMode();
                            view.repaint();
                        }
                        break;
                    case 'g':
                        if(e.isAltDown() ) {
                            view.toggleGrid();
                            view.repaint();
                        }
                        break;
                    case 'p':
                        if(e.isAltDown() ) {
                            view.toggleGraph();
                            view.repaint();
                        }
                        break;

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
        }
    }
}

