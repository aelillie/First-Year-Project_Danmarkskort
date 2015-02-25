import java.awt.event.*;

public class Controller extends MouseAdapter {
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

    public void mouseDragged(MouseEvent e) {
        view.mouseDragged(e);
    }
    public void mouseMoved(MouseEvent e) { ;; }
    public void mouseClicked(MouseEvent e) { ;; }
    public void mouseEntered(MouseEvent e) { ;; }
    public void mouseExited(MouseEvent e) { ;; }

    public void mousePressed(MouseEvent e) {
        view.mousePressed(e);
    }
    public void mouseWheelMoved(MouseWheelEvent e){
        view.wheelZoom(e);
    }
    public void mouseReleased(MouseEvent e) { ;;  }

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

