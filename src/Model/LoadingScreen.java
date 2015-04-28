package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

/**
 * Goal of this class is to create a visual representation for the loading of our program
 */
public class LoadingScreen extends JFrame {

    private Rectangle2D progressBar;

    //Dimension of the monitor
    private Dimension dimension;

    /**
     * Checks whether splashScreen is visible if not creates a new Frame With an unfilled rectangle
     */
    public LoadingScreen(){
        SplashScreen splash = SplashScreen.getSplashScreen();
        //Check if splash screen is visible.
        if(splash != null) { //if it's visible we'll just draw on that.

            this.dimension = SplashScreen.getSplashScreen().getSize();
            //Use the splash size to determine the size of the progressBar.
            int height = dimension.height;
            int width = dimension.width;
            progressBar = new Rectangle2D.Double(width * .1, height * .92, width * .8, 20);
            updateLoadBar(0);
        }else{ //If splashScreen isn't visible we'll make our own frame to draw on.


            setSize(400, 200);
            //use monitor to determine size
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            progressBar = new Rectangle2D.Double(getWidth() * .1, getHeight() * .4, getWidth() * .8, 80);

            //painty painty.
            setVisible(true);
            Graphics2D g = (Graphics2D) getGraphics();
            Rectangle2D rec = new Rectangle2D.Double(getWidth()*0.0001,
                    getHeight() * 0.0001,
                    getWidth() + 10,
                    getHeight() + 10);
            g.setPaint(Color.lightGray);
            g.fill(rec);
            updateLoadBar(0);
        }

    }

    /**
     * Update the loadBar filling a percentage chosen of the bar
     * @param percentage - Load Progress.
     */
    public void updateLoadBar(int percentage){
        SplashScreen splash = SplashScreen.getSplashScreen();
        //first check if splash is visible.
        if(splash != null && splash.isVisible()) {

            //Fill percentage of rectangle
            Graphics2D g = splash.createGraphics();
            g.setPaint(Color.BLACK);
            g.draw(progressBar);
            int x = (int) progressBar.getX();
            int y = (int) progressBar.getY();
            int wid = (int) progressBar.getWidth();
            int hei = (int) progressBar.getHeight();

            int doneWidth = Math.round(percentage*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            g.setPaint(Color.BLUE);
            g.fillRect(x, y+1, doneWidth, hei-1);
            splash.update();
        }else if( percentage < 100){
            //Fill percentage of rectangle.
            Graphics2D g = (Graphics2D) getGraphics();
            g.setPaint(Color.BLACK);
            g.draw(progressBar);

            //from percentage to length.
            int x = (int) progressBar.getX();
            int y = (int) progressBar.getY();
            int wid = (int) progressBar.getWidth();
            int hei = (int) progressBar.getHeight();
            int doneWidth = Math.round(percentage*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            g.setPaint(Color.BLUE);
            g.fillRect(x, y+1, doneWidth, hei-1);

        }else {
            //if loading is done we'll close the window.
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }


}
