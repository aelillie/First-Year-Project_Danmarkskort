package Controller;

import Model.Address;
import Model.Model;
import View.RouteView;
import View.View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;


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

    private HashMap<JButton, Boolean> buttonDownMap, routeTypeButtonDownMap;

    /**
     * Controller for route panel.
     * @param routeView - view Instance.
     * @param model - model Instance.
     */
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
        buttonDownMap = routeView.getButtonDownMap();
        routeTypeButtonDownMap = routeView.getRouteTypeButtonDownMap();
    }


    private void setScrollpaneBoundsAndIcon(){
        textfieldToBounds = new HashMap<>();
        textfieldToBounds.put(startAddressField, new Rectangle(63, 164, 285, 100));
        textfieldToBounds.put(endAddressField, new Rectangle(63, 206, 285, 100));
        textFieldToIconType = new HashMap<>();
        textFieldToIconType.put(startAddressField, "startPointIcon".intern());
        textFieldToIconType.put(endAddressField, "endPointIcon".intern());
    }

    private void setHandlers(){
        routeView.getFindRouteButton().addActionListener(this);

        routeView.getCarButton().addActionListener(this);
        routeView.getBicycleButton().addActionListener(this);
        routeView.getFootButton().addActionListener(this);
        routeView.getShortestPathButton().addActionListener(this);
        routeView.getFastestPathButton().addActionListener(this);

        startAddressField.addKeyListener(new SearchFieldKeyHandler(startAddressField, startAddrScrollpane));
        endAddressField.addKeyListener(new SearchFieldKeyHandler(endAddressField, endAddrScrollpane));
        startAddressField.addActionListener(this);
        endAddressField.addActionListener(this);
        routeView.getStartClearButton().addActionListener(this);
        routeView.getEndClearButton().addActionListener(this);
        setInputChangeHandler(startAddressField, startAddrScrollpane);
        setInputChangeHandler(endAddressField,endAddrScrollpane);
        addFocusListener("Enter start address", startAddressField);
        addFocusListener("Enter end address", endAddressField);
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

    private void setInputChangeHandler(final JTextField textField, final JScrollPane resultPane) {
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
            else{
                view.getResultPane().setVisible(false);
            }
            return results;
        }
    }

    /**
     * Sets up the actions to each event
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "findRoute") {
            if(startAddressField.getText().equals(""))
                startPoint = null;
            if(endAddressField.getText().equals(""))
                endPoint = null;

            if (startPoint != null && endPoint != null) {
                if(routeTypeButtonDownMap.get(routeView.getFastestPathButton())) {

                    System.out.println("Trying to find Fastest path...");
                    view.findFastestRoute(startPoint, endPoint);
                }else if(routeTypeButtonDownMap.get(routeView.getShortestPathButton())){
                    System.out.println("Tryin to find Shortest path...");
                    view.findShortestRoute(startPoint, endPoint);

                }else view.findFastestRoute(startPoint, endPoint);

            }
        }

        else if (command == "car") {
            setButtonsBoolean(routeView.getCarButton());
            //transportButtonDown(routeView.getCarButton());
        } else if (command == "bicycle"){
            setButtonsBoolean(routeView.getBicycleButton());
            //transportButtonDown(routeView.getBicycleButton());
        } else if (command == "walking"){
            setButtonsBoolean(routeView.getFootButton());
            //transportButtonDown(routeView.getFootButton());
        } else if (command == "startAddressSearch"){
            Address[] results = addressSearch(2,startAddressField,startAddrScrollpane);
            if(results != null && results.length == 1) {
                startAddrScrollpane.setVisible(false);
                System.out.println("Point A found");
                startPoint = SearchResultMouseHandler.getPoint(results[0], model);
            }else startPoint = null;
        }
        else if (command == "endAddressSearch") {
            Address[] results = addressSearch(2, endAddressField, endAddrScrollpane);
            if (results != null && results.length == 1) {
                endAddrScrollpane.setVisible(false);
                System.out.println("Point B found");
                endPoint = SearchResultMouseHandler.getPoint(results[0], model);
            } else endPoint = null;
        } else if (command == "clearStartField") {
            startAddressField.setText(null);
            view.setShortestPath(null);
            view.setFastestPath(null);
            try {
                view.setStartPoint(null);
            }catch (NoninvertibleTransformException ex){
                ex.printStackTrace();
            }
            view.clearDirectionPane();
        } else if (command == "clearEndField") {
            endAddressField.setText(null);
            view.setShortestPath(null);
            view.setFastestPath(null);
            try {
                view.setEndPoint(null);
            }catch (NoninvertibleTransformException ex){
                ex.printStackTrace();
            }
            view.clearDirectionPane();
        } else if (command == "shortestPath"){
            setRouteTypeButtonBoolean(routeView.getShortestPathButton());
        } else if (command == "fastestPath"){
            setRouteTypeButtonBoolean(routeView.getFastestPathButton());
        }
    }

    public void setRouteTypeButtonBoolean(JButton button){
        boolean isButtonDown = routeTypeButtonDownMap.get(button);

        JButton shortestPathButton = routeView.getShortestPathButton();
        JButton fastestPathButton = routeView.getFastestPathButton();
        if(!isButtonDown) {
            transportButtonDown(button, routeTypeButtonDownMap);
            if (button.equals(fastestPathButton)) {
                if (routeTypeButtonDownMap.get(shortestPathButton)) transportButtonDown(shortestPathButton, routeTypeButtonDownMap);
            } else if (button.equals(shortestPathButton)) {
                if (routeTypeButtonDownMap.get(fastestPathButton)) transportButtonDown(fastestPathButton, routeTypeButtonDownMap);
            }
        }
    }

    public void transportButtonDown(JButton button, HashMap<JButton, Boolean> map){
        boolean isButtonDown = map.get(button);
        routeView.changeButtonAppearence(button, isButtonDown);
        map.put(button, !isButtonDown);
    }

    private void setButtonsBoolean(JButton button){
        JButton bicycleButton = routeView.getBicycleButton();
        JButton footButton = routeView.getFootButton();
        JButton carButton = routeView.getCarButton();
        boolean isButtonDown = buttonDownMap.get(button);
        if(!isButtonDown) {
            routeView.changeButtonAppearence(button,isButtonDown);
            if (button.equals(bicycleButton)) {
                if (buttonDownMap.get(footButton)) transportButtonDown(footButton, buttonDownMap);
                if (buttonDownMap.get(carButton)) transportButtonDown(carButton,buttonDownMap);
            } else if (button.equals(routeView.getCarButton())) {
                if (buttonDownMap.get(bicycleButton)) transportButtonDown(bicycleButton,buttonDownMap);
                if (buttonDownMap.get(footButton)) transportButtonDown(footButton,buttonDownMap);
            } else if (button.equals(routeView.getFootButton())) {
                if (buttonDownMap.get(carButton)) transportButtonDown(carButton,buttonDownMap);
                if (buttonDownMap.get(bicycleButton)) transportButtonDown(bicycleButton,buttonDownMap);
            }
            buttonDownMap.put(button,!isButtonDown);
        }

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

    public HashMap<JButton, Boolean> getButtonDownMap(){ return buttonDownMap;}

}
