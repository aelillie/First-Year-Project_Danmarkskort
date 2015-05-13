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
    private RouteView routeView; //the routepanel
    private JTextField endAddressField; //field for end adress
    private JTextField startAddressField; //field for start address
    private JScrollPane startAddrScrollpane; //scrollPane for the start addresses
    private JScrollPane endAddrScrollpane; //scrollPane for the end addresses
    private Map<JTextField, Rectangle> textfieldToBounds; //The bounds specifying the position of the textfields.
    private static Map<JTextField, String> textFieldToIconType;
    private View view;
    private int selectedNr = -1; //used for default selection of addresses in the resulting scrollpane
    private Model model;
    private Point2D startPoint, endPoint;
    private HashMap<JButton, Boolean> buttonDownMap, routeTypeButtonDownMap; //The maps of booleans specifying whether buttons are pressed down or not (false = not pressed, true = pressed)

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

    /**
     * Sets the bound- and icon defaults for the textfields.
     */
    private void setScrollpaneBoundsAndIcon(){
        textfieldToBounds = new HashMap<>();
        textfieldToBounds.put(startAddressField, new Rectangle(63, 164, 285, 100));
        textfieldToBounds.put(endAddressField, new Rectangle(63, 206, 285, 100));
        textFieldToIconType = new HashMap<>();
        textFieldToIconType.put(startAddressField, "startPointIcon".intern());
        textFieldToIconType.put(endAddressField, "endPointIcon".intern());
    }

    /**
     * Sets the action handlers for textfields and buttons concerning the routepanel.
     */
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

    /**
     * This method creates a focuslistener for each textfield (its usages can be find above).
     * @param promptText The default text to be displayed in the textfield when nothing is input.
     * @param textField The textfield given the particular prompt text.
     */
    private void addFocusListener(final String promptText, final JTextField textField){
        textField.addFocusListener(new FocusListener() {

            @Override
            /**
             * If selected, remove prompt text
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

    /**
     *  This method creates an InputChangeHandler for the specified textfield. The changehandler handles events regarding input into the textfields.
     * @param textField The textfield to be given a particular inputchangehandler.
     * @param resultPane The scrollpane that the search results will be displayed in.
     */
    private void setInputChangeHandler(final JTextField textField, final JScrollPane resultPane) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            /*
            If something is input into the textfield, this is called.
             */
            @Override
            public void insertUpdate(DocumentEvent e) {
                addressSearch(1, textField, resultPane);
                selectedNr = -1;
            }

            /*
            If something is removed within the textfield, this is called.
             */
            @Override
            public void removeUpdate(DocumentEvent e) {
                addressSearch(1, textField, resultPane);
                selectedNr = -1;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }


    /**
     * This address calls to the model to search for the addresses given an input in a given textfield and chooses appropiately what to do depending on the search results.
     * @param type Type of compare used for the search: either 1 = startsWith compare otherwise equality compare
     * @return The search results (Address objects)
     */
    private Address[] addressSearch(int type, JTextField textField, JScrollPane scrollPane){
        String input = textField.getText();
        if(input.length() < 3){ //If the length of the search input is less than 3, don't search, return null.
            if(input.equals("")) {
                scrollPane.setVisible(false); //If the search input is empty, the scrollpane in which the results are displayed need not be visible.
                view.removePointer(textFieldToIconType.get(textField)); //Remove the pointer in case a given address has already been chosen and a pointer on the map is displayed.
            }
            view.getResultPane().setVisible(false);
            return null;
        } else {
            input = input.trim().toLowerCase();
            Address address = Address.parse(input); //Parse to address object
            if(address == null) return null;
            Address[] results = model.searchForAddresses(address, type);
            if(results != null) {
                view.addToResultPane(results,textField,scrollPane,textfieldToBounds.get(textField),textFieldToIconType.get(textField)); //Add the results to the scrollpane to display them.
            }
            else{
                view.getResultPane().setVisible(false); //If no results, set the scrollpane invisible.
            }
            return results; //Return the results - in case they are null, the client code can decide what to do with this.
        }
    }

    /**
     * Sets up the actions to each event
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "findRoute") { //The "Find route" button
            if(startAddressField.getText().equals(""))
                startPoint = null;
            if(endAddressField.getText().equals(""))
                endPoint = null;
            if (startPoint != null && endPoint != null) {
                if(routeTypeButtonDownMap.get(routeView.getFastestPathButton())) {
                    view.findFastestRoute(startPoint, endPoint);
                } else if(routeTypeButtonDownMap.get(routeView.getShortestPathButton())){
                    view.findShortestRoute(startPoint, endPoint);
                } else view.findFastestRoute(startPoint, endPoint);
            }
        }
        else if (command == "car") { //The car button
            setButtonsBoolean(routeView.getCarButton());
        } else if (command == "bicycle"){ //The bicycle button
            setButtonsBoolean(routeView.getBicycleButton());
        } else if (command == "walking"){ //The walking/by foot button
            setButtonsBoolean(routeView.getFootButton());
        } else if (command == "startAddressSearch"){ //The start adress field
            Address[] results = addressSearch(2,startAddressField,startAddrScrollpane);
            if(results != null && results.length == 1) {
                startAddrScrollpane.setVisible(false);
                startPoint = SearchResultMouseHandler.getPoint(results[0], model);
            }else startPoint = null;
        }
        else if (command == "endAddressSearch") { //End address field
            Address[] results = addressSearch(2, endAddressField, endAddrScrollpane);
            if (results != null && results.length == 1) {
                endAddrScrollpane.setVisible(false);
                endPoint = SearchResultMouseHandler.getPoint(results[0], model);
            } else endPoint = null;
        } else if (command == "clearStartField") { //The "X" button near the startfield
            startAddressField.setForeground(Color.GRAY);
            startAddressField.setText("Enter start address");
            view.requestFocusInWindow();
            view.setShortestPath(null);
            view.setFastestPath(null);
            try {
                view.setStartPoint(null);
            }catch (NoninvertibleTransformException ex){
                ex.printStackTrace();
            }
            view.clearDirectionPane();
        } else if (command == "clearEndField") { //The "X" button near the endfield
            startAddressField.setForeground(Color.GRAY);
            endAddressField.setText("Enter end address");
            view.requestFocusInWindow();
            view.setShortestPath(null);
            view.setFastestPath(null);
            try {
                view.setEndPoint(null);
            }catch (NoninvertibleTransformException ex){
                ex.printStackTrace();
            }
            view.clearDirectionPane();
        } else if (command == "shortestPath"){ //"Shortest" button
            setRouteTypeButtonBoolean(routeView.getShortestPathButton());
        } else if (command == "fastestPath"){ //"Fastest" button
            setRouteTypeButtonBoolean(routeView.getFastestPathButton());
        }
    }

    /**
     * Sets the boolean specifying whether the route type buttons are down or not when one button is pressed.
     * @param button The button which is pressed.
     */
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

    /**
     * Changes the appearance and boolean specifying whether the button is up or down.
     * @param button The button to be changed.
     * @param map The map specifying the boolean values
     */
    public void transportButtonDown(JButton button, HashMap<JButton, Boolean> map){
        boolean isButtonDown = map.get(button);
        routeView.changeButtonAppearence(button, isButtonDown);
        map.put(button, !isButtonDown);
    }

    /**
     * Makes sure to set the other booleans specifying whether the button is pressed or not of the other transport buttons once one is clicked.
     * @param button The button which has been pressed.
     */
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

    /**
     * The Keyhandler which enables the use of keybindings - "ENTER" for search and the arrows up and down for selecting an address.
     */
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
            //7+ 23 + 5 + 1 + 16 + 12 + 17 + 1 = 82 classes in our program.
            //Set up the keyboard handler for different keys.
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPressed();
            }

            //Sets the selected item according to the direction of the arrow keys that have been pressed.
            if(e.getKeyCode() == KeyEvent.VK_DOWN && selectedNr < list.getModel().getSize()-1){
                list.setSelectedIndex(++selectedNr);

            }
            if(e.getKeyCode() == KeyEvent.VK_UP && selectedNr>0){
                list.setSelectedIndex(--selectedNr);
            }

        }

        /**
         * The method reacting to enter pressed.
         */
        public void enterPressed(){
            Address selectedItem = null;
            try{
                selectedItem = list.getSelectedValue(); //Obtains the selected value - catches the exception if none is chosen.
            } catch (NullPointerException ex){
                System.out.println("No address chosen");
            }
            if(!(selectedItem==null)) {
                textField.setText(selectedItem.toString()); //Sets the textfields text to the selected address' name.
                textField.setVisible(true);
                SearchResultMouseHandler.goToAddressLocation(selectedItem,model,view,textFieldToIconType.get(textField));
            } else { //If none is selected, search for the current input in the textfield.
                Address[] results = addressSearch(2, textField, resultPane); //Search for the address corresponding exactly to what is typed in.
                if (results != null) {
                    if (results.length == 1) {
                        SearchResultMouseHandler.goToAddressLocation(results[0], model, view, textFieldToIconType.get(textField));
                    }
                    resultPane.setVisible(false);
                    view.getCanvas().requestFocusInWindow();
                }
            }
        }
    }

    public HashMap<JButton, Boolean> getButtonDownMap(){ return buttonDownMap;}

    public JScrollPane getEndAddrScrollpane() { return endAddrScrollpane;}

    public JScrollPane getStartAddrScrollpane() { return startAddrScrollpane;}
}
