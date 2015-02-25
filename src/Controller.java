import java.awt.event.*;

public class Controller extends MouseAdapter {
    public static final long serialVersionUID = 42;
    Model model;
    View view;
    double x;
    double y;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
        view.addMouseWheelListener(this);
        view.addKeyListener(new keyHandler());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        double dx = e.getX()-x;
        double dy = e.getY()-y;
        view.pan(dx, dy);
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e){
        view.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0){view.zoom(1.1);}
        else {view.zoom(0.9);}

    }

    private class keyHandler extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case '+':
                    view.zoom(1.1);
                    break;
                case '-':
                    view.zoom(0.9);
                    break;
                case 'a':
                    view.toggleAA();
                    break;
                case 's':
                    model.save("savegame.bin");
                    break;
                case 'l':
                    model.load("savegame.bin");
                    break;
            }
            if (e.getKeyCode() == e.VK_UP) {
                view.pan(0, 10);
            }
            if (e.getKeyCode() == e.VK_DOWN) {
                view.pan(0, -10);
            }
            if (e.getKeyCode() == e.VK_LEFT) {
                view.pan(10, 0);
            }
            if (e.getKeyCode() == e.VK_RIGHT) {
                view.pan(-10, 0);
            }

        }
    }
}

