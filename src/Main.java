import Controller.Controller;
import Model.Model;
import View.View;


public class Main {
    public static void main(String[] args) {
        Model m = Model.getModel();
        m.loadFile("savegame.bin");

       /* try {
            FileOutputStream fileOut = new FileOutputStream("");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(m);
            out.close();
            fileOut.close();
            System.out.println("Writing out succesfull!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = new FileInputStream("");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            m = (Model.Model) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Reading in succesfull!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
         */

        View v = new View(m);
        Controller c = new Controller(m,v);
        v.setVisible(true);
    }
}
