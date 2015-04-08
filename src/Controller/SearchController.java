package Controller;

import Model.*;
import View.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicoline on 04-04-2015.
 */
public class SearchController extends MouseAdapter implements ActionListener {
    Model model;
    View view;

    public SearchController(Model m, View v) {
        model = m;
        view = v;
        setHandlers();
    }

    private void setHandlers(){
        view.getSearchArea().addActionListener(this);
        view.getSearchArea().addKeyListener(new SearchAreaKeyHandler());
        view.getSearchButton().addActionListener(this);
        setInputChangeHandler();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("search")) {
            Address[] results = addressSearch(2);
            if(results != null) {
                if(results.length == 1) SearchResultMouseHandler.getAddressLocation(results[0], model, view);
                view.getResultPane().setVisible(false);
                view.getCanvas().requestFocusInWindow();
            } else {
                addressSearch(1);
                view.getCanvas().requestFocusInWindow();
            }
            /*Address[] results = addressSearch(2);
            //if(results == null) addressSearch(1);
            //view.getCanvas().requestFocusInWindow();*/
        } //When the search button is clicked
    }

    private void setInputChangeHandler(){
        view.getSearchArea().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                addressSearch(1);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                addressSearch(1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private Address[] addressSearch(int type){
        String input = view.getSearchArea().getText();
        if(!input.equals("") && input != null) {
            input = input.trim().toLowerCase();
            Address address = Address.parse(input);
            if(address == null) return null;
            Address[] results = model.searchForAddresses(address, type);
            if(results != null) view.addToResultPane(results);
            else view.getResultPane().setVisible(false);
            return results;
        } else {
            view.getResultPane().setVisible(false);
            view.setCurrentAddress(null);
            return null;
        }
    }

    private class SearchAreaKeyHandler extends KeyAdapter {

        @Override
        /**
         * Listens for keyboard events
         */
        public void keyPressed(KeyEvent e) {
            //Set up the keyboard handler for different keys.
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                Address[] results = addressSearch(2);
                if(results != null) {
                    if(results.length == 1) SearchResultMouseHandler.getAddressLocation(results[0], model, view);
                    view.getResultPane().setVisible(false);
                    view.getCanvas().requestFocusInWindow();
                } else {
                    addressSearch(1);
                    view.getCanvas().requestFocusInWindow();
                }
            }
        }
    }

}

