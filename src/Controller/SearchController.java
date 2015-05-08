package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Nicoline on 04-04-2015.
 */
public class SearchController extends MouseAdapter implements ActionListener {
    Model model;
    View view;
    private int selectedNr = -1;

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
        if (command.equals("search")) { //When the search button is clicked
            Address[] results = addressSearch(2);
            if(results != null) {
                if(results.length == 1) SearchResultMouseHandler.goToAddressLocation(results[0], model, view, "chosenAddressIcon");
                view.getResultPane().setVisible(false);
                view.getCanvas().requestFocusInWindow();
            } else {
                addressSearch(1);
                view.getCanvas().requestFocusInWindow();
            }
        }
    }


    private void setInputChangeHandler(){
        view.getSearchArea().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(!(view.getSearchArea().getText().length()<3)) {
                    addressSearch(1);
                }
                selectedNr=-1;
                //System.out.println(view.getSearchArea().getText().length());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!(view.getSearchArea().getText().length()<3)) {
                    addressSearch(1);
                }
                selectedNr=-1;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private Address[] addressSearch(int type){
        String input = view.getSearchArea().getText().trim().toLowerCase();;
        if(input.length() < 3){
            if(input.equals("") && input != null) {
                view.getResultPane().setVisible(false);
                view.removePointer("chosenAddressIcon");
            }
            view.getResultPane().setVisible(false);
            return null;
        } else {
            Address address = Address.parse(input);
            if(address == null) return null;
            Address[] results = model.searchForAddresses(address, type);
            if(results != null) view.addToResultPane(results);
            else {
                view.addNoAddressesFoundMsg(new Rectangle(26, 52, 286, 100));
                //view.getResultPane().setVisible(false);
            }
            return results;
        }
    }

    //Inner control class for keyboard Control on SearchResultPane.
    private class SearchAreaKeyHandler extends KeyAdapter {
        private JList<Address> list = view.getAddressSearchResults();


        @Override
        public void keyPressed(KeyEvent e) {
            list = view.getAddressSearchResults();

            //Set up the keyboard handler for different keys.
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPressed();
            }

            if(e.getKeyCode() == KeyEvent.VK_DOWN && selectedNr < list.getModel().getSize()-1){
                list.setSelectedIndex(++selectedNr);

            }
            if(e.getKeyCode() == KeyEvent.VK_UP && selectedNr>0){
                list.setSelectedIndex(--selectedNr);
            }

        }

        /**
         * When an address is selected with the Enter key this function tells
         * the Controller.
         */
        public void enterPressed(){
            Address selectedItem = null;
            try{
                selectedItem = list.getSelectedValue();
            } catch (NullPointerException ex){
                System.out.println("No address chosen");
            }

            if(!(selectedItem==null)) {
                view.getSearchArea().setText(selectedItem.toString());
                view.getResultPane().setVisible(false);
            }

            Address[] results = addressSearch(2);
            if (results != null) {
                if (results.length == 1) SearchResultMouseHandler.goToAddressLocation(results[0], model, view, "chosenAddressIcon");
                view.getResultPane().setVisible(false);
                view.getCanvas().requestFocusInWindow();
            } else {
                addressSearch(1);
                view.getCanvas().requestFocusInWindow();
            }

        }
    }
}

