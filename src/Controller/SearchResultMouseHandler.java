package Controller;

import Model.Address;
import Model.Model;
import View.MapPointer;
import View.View;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Address selectedItem = searchResults.getSelectedValue();
        textField.setText(selectedItem.toString());
        view.getCanvas().requestFocusInWindow();
        getAddressLocation(selectedItem, model, view, iconType);

        scrollPane.setVisible(false);
        textField.postActionEvent();
    }

    public static void getAddressLocation(Address selectedAddr, Model m, View v, String iconType){
        Map<Address, Point2D> addressMap = m.getOSMReader().getAddressMap();
        Map<Address, List<Path2D>> streetMap = m.getOSMReader().getStreetMap();
        Map<Address, Path2D> boundaryMap = m.getOSMReader().getBoundaryMap();

        Point2D addressLocation = addressMap.get(selectedAddr);
        List<Path2D> streetLocation = streetMap.get(selectedAddr);
        Path2D boundaryLocation = boundaryMap.get(selectedAddr);

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
        Map<Address, Point2D> addressMap = m.getOSMReader().getAddressMap();
        Map<Address, List<Path2D>> streetMap = m.getOSMReader().getStreetMap();
        Map<Address, Path2D> boundaryMap = m.getOSMReader().getBoundaryMap();

        Point2D addressLocation = addressMap.get(result);
        List<Path2D> streetLocation = streetMap.get(result);
        Path2D boundaryLocation = boundaryMap.get(result);
        if(addressLocation != null)
            return addressLocation;
        else if(streetLocation != null)
            return getMiddlePoint(streetLocation);
        else if(boundaryLocation != null)
            return null;
        else return null;

    }
}