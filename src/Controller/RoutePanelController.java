package Controller;

import View.RouteView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kevin on 16-03-2015.
 */
public class RoutePanelController implements ActionListener{
    private RouteView route;

    public RoutePanelController(RouteView view){

        view.getFindRouteButton().addActionListener(this);

        view.getCarButton().addActionListener(this);
        view.getBicycleButton().addActionListener(this);
        view.getFootButton().addActionListener(this);

        route = view;

    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "findRoute");
    }
}
