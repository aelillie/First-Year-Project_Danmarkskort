package Controller;

import Model.Address;
import Model.Model;
import View.MapPointer;
import View.View;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * Created by Nicoline on 04-04-2015.
 */
public class SearchResultMouseHandler extends MouseAdapter{

    private View view;
    private Model model;
    private JList<Address> searchResults;
    private JTextField textField;
    private JScrollPane scrollPane;
    private String iconType;


    public SearchResultMouseHandler(View view, Model model, JList<Address> searchResults, JTextField textField, JScrollPane scrollPane, String iconType) {
        this.view = view;
        this.model = model;
        this.searchResults = searchResults;
        this.textField = textField;
        this.scrollPane = scrollPane;
        this.iconType = iconType;
    }

    /**
     * When an address i chosen from suggestion Pane with mouse this function
     * calls the same functions as if normally chosen.
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        Address selectedItem = searchResults.getSelectedValue();
        textField.setText(selectedItem.toString());
        view.getCanvas().requestFocusInWindow();
        goToAddressLocation(selectedItem, model, view, iconType);

        scrollPane.setVisible(false);
        textField.postActionEvent();
    }

    /**
     * Moves the position of the window to the address Location
     * @param selectedAddr - Address chosen
     * @param m - Model instance
     * @param v - View Instance
     * @param iconType - End or start Position icon type
     */
    public static void goToAddressLocation(Address selectedAddr, Model m, View v, String iconType){
        Map<Address, List<Path2D>> streetMap = m.getOSMReader().getStreetMap();

        Point2D addressLocation = selectedAddr.getAddressLocation();
        List<Path2D> streetLocation = streetMap.get(selectedAddr);
        Path2D boundaryLocation = selectedAddr.getBoundaryLocation();

        if(addressLocation == null && boundaryLocation == null) {
            v.zoomOnStreet(streetLocation);
            Point2D middlePoint = getMiddlePoint(streetLocation);

            v.addPointer(new MapPointer(streetLocation,iconType));
            v.searchResultChosen(middlePoint.getX(),middlePoint.getY());

        } else if(boundaryLocation == null && streetLocation == null){
            v.zoomOnAddress();

            v.addPointer(new MapPointer(addressLocation,iconType));
            v.searchResultChosen(addressLocation.getX(), addressLocation.getY());

        } else if(streetLocation == null && addressLocation == null){
            v.addPointer(new MapPointer(boundaryLocation, iconType));
            v.searchResultChosen(boundaryLocation.getBounds().getCenterX(), boundaryLocation.getBounds().getCenterY());

        }


    }

    private static Point2D getMiddlePoint(List<Path2D> street){
        int pathCount = 0;
        double xCoordinateMean = 0;
        double yCoordinateMean = 0;
        for(Path2D path: street){
            double[] points = new double[6];
            PathIterator pathIterator = path.getPathIterator(new AffineTransform());
            while(!pathIterator.isDone()){
                pathIterator.currentSegment(points);

                xCoordinateMean += points[0];
                yCoordinateMean += points[1];

                pathCount++;

                pathIterator.next();
            }

        }

        xCoordinateMean = xCoordinateMean/pathCount;
        yCoordinateMean = yCoordinateMean/pathCount;
        return new Point2D.Double(xCoordinateMean,yCoordinateMean);
    }

    public static Point2D getPoint(Address result, Model m){
        Map<Address, List<Path2D>> streetMap = m.getOSMReader().getStreetMap();

        Point2D addressLocation = result.getAddressLocation();
        List<Path2D> streetLocation = streetMap.get(result);
        Path2D boundaryLocation = result.getBoundaryLocation();
        if(addressLocation != null)
            return addressLocation;
        else if(streetLocation != null)
            return getMiddlePoint(streetLocation);
        else if(boundaryLocation != null)
            return null;
        else return null;

    }
}