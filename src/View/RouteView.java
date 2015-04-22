package View;

import Controller.RoutePanelController;
import Model.Model;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 16-03-2015.
 */
public class RouteView extends JPanel{

    private JTextField startAddressField, endAddressField;
    private JPanel startEndAddressPanel;
    private JButton findRouteButton;
    private JButton carButton, bicycleButton, footButton;
    private View view;
    private Map<JButton,ImageIcon> iconWhiteEquivalenceMap;
    private Map<JButton, ImageIcon> iconBlackEquivalenceMap;
    private ImageIcon carOptionIcon, walkingOptionIcon,bicycleOptionIcon;

    /**
     * Creates a panel used for getting a path from A to B in the program
     */
    public RouteView(View view, Model model){

        setVisible(false);
        setBounds(20, 79, 342, 180);
        setOpaque(true);
        setBackground(Color.WHITE);
        this.view = view;
        setBorder(new MatteBorder(1, 1, 1, 1, new Color(161, 161, 161)));
        setLayout(new BorderLayout());
        makeFindRoutePanel();
        add(startEndAddressPanel, BorderLayout.CENTER);
        RoutePanelController rp = new RoutePanelController(this,model);

    }

    private void makeFindRoutePanel(){
        JPanel transportTypePanel = new JPanel();
        transportTypePanel.setBackground(Color.WHITE);
        transportTypePanel.setBounds(20, 200, 342, 50);
        transportTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        transportTypePanel.setBorder(new MatteBorder(0,0,1,0, new Color(161, 161, 161)));

        carOptionIcon = new ImageIcon(this.getClass().getResource("/data/carOptionIcon.png"));
        carButton = new JButton("Car",carOptionIcon);
        carButton.setFocusable(false);
        carButton.setForeground(new Color(114, 114, 114));
        carButton.setBackground(Color.WHITE);
        carButton.setActionCommand("car");

        bicycleOptionIcon = new ImageIcon(this.getClass().getResource("/data/bicycleOptionIcon.png"));
        bicycleButton = new JButton("Bicycle",bicycleOptionIcon);
        bicycleButton.setFocusable(false);
        bicycleButton.setForeground(new Color(114, 114, 114));
        bicycleButton.setBackground(Color.WHITE);
        bicycleButton.setActionCommand("bicycle");

        walkingOptionIcon = new ImageIcon(this.getClass().getResource("/data/walkingOptionIcon.png"));
        footButton = new JButton("By foot",walkingOptionIcon);
        footButton.setFocusable(false);
        footButton.setForeground(new Color(114, 114, 114));
        footButton.setBackground(Color.WHITE);
        footButton.setActionCommand("walking");

        transportTypePanel.add(carButton);
        transportTypePanel.add(bicycleButton);
        transportTypePanel.add(footButton);



        makeStartEndAddressPanel();
        createIconEquivalenceMap();

        add(transportTypePanel, BorderLayout.NORTH);

    }

    private void makeStartEndAddressPanel(){
        startEndAddressPanel = new JPanel();
        startEndAddressPanel.setLayout(new GridBagLayout()); //A layout allowing you to customize grids moreso than a standard GridLayout.

        GridBagConstraints c;
        JLabel startIconLabel = new JLabel(new ImageIcon(this.getClass().getResource("/data/startPointIcon.png")));
        c = new GridBagConstraints();
        c.fill= GridBagConstraints.NONE;
        c.gridx = 0; //Which column the component should be in.
        c.gridy = 0; //Which row the component should be in.
        c.weightx = 0.15; //The weight amongst the elements in the row
        c.weighty = 0.5; //The weight amongst the elements in the column.
        startEndAddressPanel.add(startIconLabel,c);


        startAddressField = new JTextField();
        startAddressField.setActionCommand("startAddressSearch");
        startAddressField.setText("Enter start address");
        startAddressField.setForeground(Color.GRAY);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 15;
        c.weightx = 0.85;
        c.weighty = 0.5;
        c.insets = new Insets(5,0,0,25); //Inset/distance from the right.
        startEndAddressPanel.add(startAddressField,c);
        JLabel endIconLabel = new JLabel(new ImageIcon(this.getClass().getResource("/data/endPointIcon.png")));
        c = new GridBagConstraints();
        c.fill= GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.15;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        startEndAddressPanel.add(endIconLabel,c);

        endAddressField = new JTextField();
        endAddressField.setActionCommand("endAddressSearch");
        endAddressField.setForeground(Color.GRAY);
        endAddressField.setText("Enter end address");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 15;
        c.weightx = 0.85;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0,0,0,25); //Inset/distance from the right
        startEndAddressPanel.add(endAddressField,c);

        findRouteButton = new JButton("Find route");
        findRouteButton.setBackground(Color.WHITE);
        findRouteButton.setFocusable(false);
        findRouteButton.setActionCommand("findRoute");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LAST_LINE_END; //Where the component is situated - in this case to the lower right corner.
        //c.weighty = 0.1;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0,0,10,10);
        startEndAddressPanel.add(findRouteButton,c);
    }

    public void createIconEquivalenceMap(){
        iconWhiteEquivalenceMap = new HashMap<>();
        iconWhiteEquivalenceMap.put(carButton,new ImageIcon(this.getClass().getResource("/data/carOptionIconWhite.png")));
        iconWhiteEquivalenceMap.put(bicycleButton, new ImageIcon(this.getClass().getResource("/data/bicycleOptionIconWhite.png")));
        iconWhiteEquivalenceMap.put(footButton,new ImageIcon(this.getClass().getResource("/data/walkingOptionIconWhite.png")));

        iconBlackEquivalenceMap = new HashMap<>();
        iconBlackEquivalenceMap.put(carButton,carOptionIcon);
        iconBlackEquivalenceMap.put(bicycleButton,bicycleOptionIcon);
        iconBlackEquivalenceMap.put(footButton,walkingOptionIcon);
    }



    public void changeButtonAppearence(JButton button, boolean buttonDown){
        ImageIcon newIcon;
        if(buttonDown){
            newIcon = iconBlackEquivalenceMap.get(button);
            button.setBackground(Color.WHITE);
            button.setForeground(Color.GRAY);
            button.setIcon(newIcon);
        } else {
            newIcon = iconWhiteEquivalenceMap.get(button);
            button.setBackground(Color.GRAY);
            button.setForeground(Color.WHITE);
            button.setIcon(newIcon);
        }
    }

    /**
     * Toggle the panel on and off
     */
    public void showRoutePanel(){
        boolean isVisible = isVisible();
        setVisible(!isVisible);
    }

    public JTextField getEndAddressField() {return endAddressField;}

    public JTextField getStartAddressField() {return startAddressField;}

    public JButton getFindRouteButton() { return findRouteButton; }

    public JButton getCarButton() { return carButton; }

    public JButton getBicycleButton() { return bicycleButton; }

    public JButton getFootButton() { return footButton; }

    public View getView() { return view;}
}
