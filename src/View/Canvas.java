package View;

import Model.MapFeature;
import Model.MapIcon;
import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * The canvas object is where our map of paths and images (points) will be drawn on
 */
public class Canvas extends JComponent {

    public static final long serialVersionUID = 4;
    private Stroke min_value = new BasicStroke(Float.MIN_VALUE);
    private AffineTransform transform;
    private View view;
    private Model model = Model.getModel();
    private DrawAttributeManager drawAttributeManager = new DrawAttributeManager();

    public Canvas(View view, AffineTransform transform) {
        this.transform = transform;
        this.view = view;
    }

    @Override
    public void paint(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;

        //Set the Transform for Graphic2D element before drawing.
        g.setTransform(transform);
        if (view.isAntialias()) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g.setStroke(min_value); //Just for good measure.


        g.setColor(DrawAttribute.whiteblue);
        g.fill(model.getBbox());
        //getContentPane().setBackground(DrawAttribute.whiteblue);
            /*//Drawing everything not categorized as a area or line object.
            for (Shape line : model) {
                g.draw(line);
            }*/

        g.setColor(Color.BLACK);

        //Draw areas first
        for(MapFeature mapFeature : model.getMapFeatures()){
            DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
            if(view.getZoomLevel() >= drawAttribute.getZoomLevel()){ //TODO: NullerPointerException when loading "København" and changing to transport map
                if(mapFeature.isArea()){
                    g.setColor(drawAttribute.getColor());
                    g.fill(mapFeature.getShape());
                }
            }
        }
        //Then draw boundaries on top of areas
        for (MapFeature mapFeature : model.getMapFeatures()) {
            if (view.getZoomLevel() > 14) {
                try {
                    g.setColor(Color.BLACK);
                    DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
                    if (drawAttribute.isDashed()) continue;
                    else if (!mapFeature.isArea())
                        g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId() + 1]);
                    else g.setStroke(DrawAttribute.basicStrokes[0]);
                    g.draw(mapFeature.getShape());
                }catch(NullPointerException e){
                    System.out.println(mapFeature.getValueName() + " " + mapFeature.getValue());
                }
            }

        }


        //Draw the fillers on top of boundaries and areas
        for (MapFeature mapFeature : model.getMapFeatures()) {
            DrawAttribute drawAttribute = drawAttributeManager.getDrawAttribute(mapFeature.getValueName());
            if (view.getZoomLevel() >= drawAttribute.getZoomLevel()) {
                g.setColor(drawAttribute.getColor());
                  /*  if (mapFeature.isArea()) {
                        g.fill(mapFeature.getShape());
                    } else {*/

                if (drawAttribute.isDashed()) g.setStroke(DrawAttribute.dashedStrokes[drawAttribute.getStrokeId()]);
                else g.setStroke(DrawAttribute.streetStrokes[drawAttribute.getStrokeId()]);
                g.draw(mapFeature.getShape());
                //     }
            }
        }
        //Draws the icons.

        if (view.getZoomLevel() >= 17) {
            for (MapIcon mapIcon : model.getMapIcons()) {
                mapIcon.draw(g, transform);
            }
        }

        // }
/*
                //AMALIE Iterator it = model.getStreetMap().entrySet().iterator();
            while (it.hasNext()) {
                int count1 = 0;
                Map.Entry pair = (Map.Entry) it.next();
                java.util.List<Shape> list = (java.util.List<Shape>) pair.getValue();
                String streetName = (String) pair.getKey();
                //g.setStroke(txSt);
                TextDraw txtDr = new TextDraw();
                System.out.println(streetName);
                Path2D.Double street1 = new Path2D.Double();
                g.setColor(Color.BLACK);
                for (Shape street : list) {
                    //if(count == 0){
                    //	street1 =  (Path2D.Double) street;
                    //	count++;
                    //} else {
                    //	street1.append(street,true);
                    //}
                    txtDr.draw(g,new GeneralPath(street),streetName,70.);
                }
            }
*/

            /*
			//Prints out the current center coordinates
			Point2D center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
			try {
				System.out.println("Center: " + transform.inverseTransform(center, null));
			} catch (NoninvertibleTransformException e) {} */
        //}
    }

    public void changeMapType(){
        String type = view.getMapMenu().getChosen();
        switch (type) {
            case "Standard":
                drawAttributeManager.toggleStandardView();
                break;
            case "Colorblind map":
                drawAttributeManager.toggleColorblindView();
                break;
            case "Transport map":
                drawAttributeManager.toggleTransportView();
                break;
        }
        repaint();
    }


}
