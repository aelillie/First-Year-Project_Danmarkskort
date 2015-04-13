package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by i5-4670K on 11-04-2015.
 */
public class LoadingScreen extends JFrame {

    private Rectangle2D progressBar;

    private Dimension dimension;


    public LoadingScreen(){
        SplashScreen splash = SplashScreen.getSplashScreen();
        if(splash != null) {
            this.dimension = SplashScreen.getSplashScreen().getSize();

            int height = dimension.height;
            int width = dimension.width;
            progressBar = new Rectangle2D.Double(width * .1, height * .92, width * .8, 20);
            updateLoadBar(0);
        }else{
            setSize(400, 200);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            progressBar = new Rectangle2D.Double(getWidth() * .1, getHeight() * .4, getWidth() * .8, 80);

            setAlwaysOnTop(true);
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

    public void updateLoadBar(int percentage){
        SplashScreen splash = SplashScreen.getSplashScreen();

        if(splash != null && splash.isVisible()) {
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
            Graphics2D g = (Graphics2D) getGraphics();
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

        }else {

            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }


}
