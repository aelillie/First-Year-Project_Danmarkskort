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
        view.getSearchArea().setText(selectedItem.toString());
        view.getCanvas().requestFocusInWindow();
        getAddressLocation(selectedItem, model, view);
        view.getResultPane().setVisible(false);

    }

    public static void getAddressLocation(Address selectedAddr, Model m, View v){
        Map<Address, Point2D> addressMap = m.getOSMReader().getAddressMap();
        Map<Address, List<Path2D>> streetMap = m.getOSMReader().getStreetMap();
        Map<Address, Path2D> boundaryMap = m.getOSMReader().getBoundaryMap();

        Point2D addressLocation = addressMap.get(selectedAddr);
        List<Path2D> streetLocation = streetMap.get(selectedAddr);
        Path2D boundaryLocation = boundaryMap.get(selectedAddr);

        if(addressLocation == null && boundaryLocation == null) {
            v.setCurrentStreet(streetLocation);
        } else if(boundaryLocation == null && streetLocation == null){
            v.setCurrentAddress(addressLocation);
            //v.searchResultChosen(addressLocation.getX(),addressLocation.getY());
        } else if(streetLocation == null && addressLocation == null){
            v.setCurrentBoundaryLocation(boundaryLocation);
        }
    }

}