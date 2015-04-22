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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.*;


public class RoutePanelController implements ActionListener{
    private RouteView routeView;
    private JTextField endAddressField;
    private JTextField startAddressField;
    private JScrollPane startAddrScrollpane;
    private JScrollPane endAddrScrollpane;
    private Map<JTextField, Rectangle> textfieldToBounds;
    private static Map<JTextField, String> textFieldToIconType;
    private View view;
    private int selectedNr = -1;
    private Model model;
    private Point2D startPoint, endPoint;

    private HashMap<JButton, Boolean> buttonDownMap;

    public RoutePanelController(RouteView routeView, Model model){
        view = routeView.getView();
        this.model = model;
        startAddressField = routeView.getStartAddressField();
        endAddressField = routeView.getEndAddressField();
        startAddrScrollpane = view.getResultStartPane();
        endAddrScrollpane = view.getResultEndPane();
        this.routeView = routeView;
        setScrollpaneBoundsAndIcon();
        setHandlers();
        setMouseDownMap();

    }


    private void setScrollpaneBoundsAndIcon(){
        textfieldToBounds = new HashMap<>();
        textfieldToBounds.put(startAddressField,new Rectangle(68,162,266,100));
        textfieldToBounds.put(endAddressField, new Rectangle(68,205,266,100));
        textFieldToIconType = new HashMap<>();
        textFieldToIconType.put(startAddressField, "startPointIcon".intern());
        textFieldToIconType.put(endAddressField, "endPointIcon".intern());
    }

    private void setHandlers(){
        routeView.getFindRouteButton().addActionListener(this);

        routeView.getCarButton().addActionListener(this);
        routeView.getBicycleButton().addActionListener(this);
        routeView.getFootButton().addActionListener(this);

        startAddressField.addKeyListener(new SearchFieldKeyHandler(startAddressField, startAddrScrollpane));
        endAddressField.addKeyListener(new SearchFieldKeyHandler(endAddressField, endAddrScrollpane));
        startAddressField.addActionListener(this);
        endAddressField.addActionListener(this);
        setInputChangeHandler(startAddressField, startAddrScrollpane);
        setInputChangeHandler(endAddressField,endAddrScrollpane);
        addFocusListener("Enter start address", startAddressField);
        addFocusListener("Enter end address", endAddressField);
    }

    private void setMouseDownMap(){
        buttonDownMap = new HashMap<>();
        buttonDownMap.put(routeView.getCarButton(),false);
        buttonDownMap.put(routeView.getBicycleButton(),false);
        buttonDownMap.put(routeView.getFootButton(),false);
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
                addressSearch(1, textField, resultPane);
                selectedNr = -1;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                addressSearch(1, textField, resultPane);
                selectedNr = -1;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }


    private Address[] addressSearch(int type, JTextField textField, JScrollPane scrollPane){
        String input = textField.getText();
        if(input.length() < 3){
            if(input.equals("") && input != null) {
                scrollPane.setVisible(false);
                view.removePointer(textFieldToIconType.get(textField));
            }
            return null;
        } else {
            input = input.trim().toLowerCase();
            Address address = Address.parse(input);
            if(address == null) return null;
            Address[] results = model.searchForAddresses(address, type);
            if(results != null) {
                view.addToResultPane(results,textField,scrollPane,textfieldToBounds.get(textField),textFieldToIconType.get(textField));
            }
            else view.getResultPane().setVisible(false);
            return results;
        }
    }


    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "findRoute") {
            if(startAddressField.getText().equals(""))
                startPoint = null;
            if(endAddressField.getText().equals(""))
                endPoint = null;

            if (startPoint != null && endPoint != null) {
                System.out.println("Working");
                try {
                    view.findRoute(startPoint, endPoint);
                }catch (NoninvertibleTransformException ex){}
            }
        }

        else if (command == "car") buttonDown(routeView.getCarButton());
        else if (command == "bicycle") buttonDown(routeView.getBicycleButton());
        else if (command == "walking") buttonDown(routeView.getFootButton());
        else if (command == "startAddressSearch"){
            Address[] results = addressSearch(2,startAddressField,startAddrScrollpane);
            if(results != null && results.length == 1) {
                startAddrScrollpane.setVisible(false);
                System.out.println("search");
                startPoint = SearchResultMouseHandler.getPoint(results[0], model);
            }else startPoint = null;
        }
        else if (command == "endAddressSearch") {
            Address[] results = addressSearch(2, endAddressField, endAddrScrollpane);
            if (results != null && results.length == 1) {
                endAddrScrollpane.setVisible(false);
                System.out.println("found!");
                endPoint = SearchResultMouseHandler.getPoint(results[0], model);
            } else endPoint = null;
        }

    }

    public void buttonDown(JButton button){
        //TODO: what to do on button down?
        boolean isButtonDown = buttonDownMap.get(button);
        routeView.changeButtonAppearence(button,isButtonDown);
        buttonDownMap.put(button,!isButtonDown);
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
                enterPressed();
            }

            if(e.getKeyCode() == KeyEvent.VK_DOWN && selectedNr < list.getModel().getSize()-1){
                list.setSelectedIndex(++selectedNr);

            }
            if(e.getKeyCode() == KeyEvent.VK_UP && selectedNr>0){
                list.setSelectedIndex(--selectedNr);
            }

        }

        public void enterPressed(){
            Address selectedItem = null;
            try{
                selectedItem = list.getSelectedValue();
            } catch (NullPointerException ex){
                System.out.println("No address chosen");
            }
            if(!(selectedItem==null)) {
                textField.setText(selectedItem.toString());
                textField.setVisible(true);
            }

            Address[] results = addressSearch(2,textField,resultPane);
            if (results != null) {
                if (results.length == 1){
                    SearchResultMouseHandler.goToAddressLocation(results[0], model, view, textFieldToIconType.get(textField));
                }
                resultPane.setVisible(false);
                view.getCanvas().requestFocusInWindow();
            } else {
                addressSearch(1,textField,resultPane);
                view.getCanvas().requestFocusInWindow();
            }
        }
    }

}
