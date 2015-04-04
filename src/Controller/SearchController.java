package Controller;

import Model.*;
import View.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

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
        //view.getResultPane().getViewport().getView().addMouseListener(new ResultPaneMouseHandler());
        setInputChangeHandler();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("searchAreaInput")){ addressSearch(); view.getCanvas().requestFocusInWindow(); } //might want to do something else here
        else if (command.equals("search")) { addressSearch(); view.getCanvas().requestFocusInWindow(); }//Might want to do something else here
    }

    private void setInputChangeHandler(){
        view.getSearchArea().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                addressSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                addressSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void addressSearch(){
        String input = view.getSearchArea().getText();
        if(!input.equals("") && input != null) {
            input.trim().toLowerCase();
            Address address = Address.parse(input);
            Address[] results = model.searchForAddresses(address);
            if(results != null) view.addToResultPane(results);
            else view.getResultPane().setVisible(false);
        } else {
            view.getResultPane().setVisible(false);
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
                addressSearch();
            }
        }
    }

    private class ResultPaneMouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Address selectedItem = view.getAddressSearchResults().getSelectedValue();
            System.out.println("DV");
        }
    }

    //TODO: Get position when clicking a element from the scrollpane - scrollpane listener also
}

