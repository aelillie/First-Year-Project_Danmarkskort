package Controller;

import Model.Address;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

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
        getAddressLocation(selectedItem);

    }

    private void getAddressLocation(Address selectedAddr){
        Map<Address, Point2D> addressMap = model.getOSMReader().getAddressMap();
        Map<Address, List<Path2D>> streetMap = model.getOSMReader().getStreetMap();

        Point2D addressLocation = addressMap.get(selectedAddr);
        if(addressLocation == null) {
            List<Path2D> streetLocation = streetMap.get(selectedAddr);
            view.setCurrentStreet(streetLocation);
        } else {
            view.setCurrentAddress(addressLocation);
        }
    }

}