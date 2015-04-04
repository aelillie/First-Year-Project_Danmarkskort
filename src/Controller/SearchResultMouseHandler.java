package Controller;

import Model.Address;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import View.*;
import Model.*;

/**
 * Created by Nicoline on 04-04-2015.
 */
public class SearchResultMouseHandler extends MouseAdapter {

    View view;
    Model model;

    public SearchResultMouseHandler(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Address selectedItem = view.getAddressSearchResults().getSelectedValue();
        System.out.println("WOW something happened! (NOT)");
        System.out.println(selectedItem);

    }
}