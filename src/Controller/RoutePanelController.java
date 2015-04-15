package Controller;

import Model.Address;
import View.RouteView;
import View.View;
import Model.Model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Kevin on 16-03-2015.
 */
public class RoutePanelController implements ActionListener{
    private RouteView routeView;
    private JTextField endAddressField;
    private JTextField startAddressField;
    private JScrollPane startAddrScrollpane;
    private JScrollPane endAddrScrollpane;

    private View view;
    private int selectedNr = -1;
    private Model model;

    public RoutePanelController(RouteView routeView, Model model){
        view = routeView.getView();
        this.model = model;
        startAddressField = routeView.getStartAddressField();
        endAddressField = routeView.getEndAddressField();
        startAddrScrollpane = view.getResultStartPane();
        endAddrScrollpane = view.getResultEndPane();
        this.routeView = routeView;
        setHandlers();
    }

    private void setHandlers(){
        routeView.getFindRouteButton().addActionListener(this);

        routeView.getCarButton().addActionListener(this);
        routeView.getBicycleButton().addActionListener(this);
        routeView.getFootButton().addActionListener(this);

        startAddressField.addKeyListener(new SearchFieldKeyHandler(startAddressField,startAddrScrollpane));
        endAddressField.addKeyListener(new SearchFieldKeyHandler(endAddressField,endAddrScrollpane));
        setInputChangeHandler(startAddressField,startAddrScrollpane);
        setInputChangeHandler(endAddressField,endAddrScrollpane);
        addFocusListener("Enter start address",startAddressField);
        addFocusListener("Enter end address",endAddressField);
    }

    private void addFocusListener(final String promptText, final JTextField textField){
        textField.addFocusListener(new FocusListener() {

            @Override
            /**
             * If selected remove prompt text
             */
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(promptText)) {
                    textField.setForeground(Color.BLACK);
                    textField.setText("");
                }
            }

            @Override
            /**
             * if unselected and search field is empty sets up promptText.
             */
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(promptText);
                }
            }

        });

    }

    private void setInputChangeHandler(final JTextField textField, final JScrollPane resultPane){
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                addressSearch(1,textField,resultPane);
                selectedNr=-1;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                addressSearch(1,textField,resultPane);
                selectedNr=-1;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void derp(){
        /*Address[] results = addressSearch(2);
        if(results != null) {
            if(results.length == 1) SearchResultMouseHandler.getAddressLocation(results[0], model, view);
            view.getResultPane().setVisible(false);
            view.getCanvas().requestFocusInWindow();
        } else {
            addressSearch(1);
            view.getCanvas().requestFocusInWindow();
        }*/
    }

    private Address[] addressSearch(int type, JTextField textField, JScrollPane scrollPane){
        String input = textField.getText();
        if(input.length() < 3){
            if(input.equals("") && input != null) {
                scrollPane.setVisible(false);
                view.setCurrentAddress(null);
            }
            return null;
        } else {
            input = input.trim().toLowerCase();
            Address address = Address.parse(input);
            if(address == null) return null;
            Address[] results = model.searchForAddresses(address, type);
            if(results != null) {
                if(textField.equals(startAddressField)) view.addToResultStartPane(results);
                else if (textField.equals(endAddressField)) view.addToResultEndPane(results);
            }
            else view.getResultPane().setVisible(false);
            return results;
        }
    }


    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "findRoute");
        else if (command == "startAddressSearch") ;
        else if (command == "endAddressSearch") ;
    }

    private class SearchFieldKeyHandler extends KeyAdapter {
        private JList<Address> list = view.getAddressSearchResults();
        JTextField textField;
        JScrollPane resultPane;

        public SearchFieldKeyHandler(JTextField textField, JScrollPane resultPane){
            this.textField = textField;
            this.resultPane = resultPane;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            list = view.getAddressSearchResults();

            //Set up the keyboard handler for different keys.
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                Address selectedItem = list.getSelectedValue();
                if(!(selectedItem==null)) {
                    textField.setText(selectedItem.toString());
                    textField.setVisible(false);
                }

                Address[] results = addressSearch(2,textField,resultPane);
                if (results != null) {
                    if (results.length == 1) SearchResultMouseHandler.getAddressLocation(results[0], model, view);
                    resultPane.setVisible(false);
                    view.getCanvas().requestFocusInWindow();
                } else {
                    addressSearch(1,textField,resultPane);
                    view.getCanvas().requestFocusInWindow();
                }

            }

            if(e.getKeyCode() == KeyEvent.VK_DOWN && selectedNr < list.getModel().getSize()-1){
                list.setSelectedIndex(++selectedNr);

            }
            if(e.getKeyCode() == KeyEvent.VK_UP && selectedNr>0){
                list.setSelectedIndex(--selectedNr);
            }

        }
    }

}
