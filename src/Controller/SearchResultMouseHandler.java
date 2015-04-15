package Controller;

import Model.Address;
import Model.Model;
import View.View;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

import View.*;
import Model.*;

import javax.swing.*;

/**
 * Created by Nicoline on 04-04-2015.
 */
public class SearchResultMouseHandler extends MouseAdapter{

    private View view;
    private Model model;
    private JList<Address> searchResults;

    public SearchResultMouseHandler(View view, Model model, JList<Address> searchResults) {
        this.view = view;
        this.model = model;
        this.searchResults = searchResults;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Address selectedItem = searchResults.getSelectedValue();
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
            v.zoomOnStreet(streetLocation);
            v.setCurrentStreet(streetLocation);
            Point2D middlePoint = getMiddlePoint(streetLocation);
            v.searchResultChosen(middlePoint.getX(),middlePoint.getY());

        } else if(boundaryLocation == null && streetLocation == null){
            v.zoomOnAddress();
            v.setCurrentAddress(addressLocation);
            v.searchResultChosen(addressLocation.getX(), addressLocation.getY());

        } else if(streetLocation == null && addressLocation == null){
            v.setCurrentBoundaryLocation(boundaryLocation);
            v.searchResultChosen(boundaryLocation.getBounds().getCenterX(),boundaryLocation.getBounds().getCenterY());

        }
    }

    private static Point2D getMiddlePoint(List<Path2D> street){
        int pathCount = 0;
        double xCoordinateMean = 0;
        double yCoordinateMean = 0;
        for(Path2D path: street){
            Rectangle2D rect = path.getBounds2D();
            xCoordinateMean += rect.getX();
            yCoordinateMean += rect.getY();
            pathCount++;
        }

        xCoordinateMean = xCoordinateMean/pathCount;
        yCoordinateMean = yCoordinateMean/pathCount;
        return new Point2D.Double(xCoordinateMean,yCoordinateMean);
    }




}