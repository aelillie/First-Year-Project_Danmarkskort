
public class Main {
    public static void main(String[] args) {
        Model m = new Model(args[0]);
        View v = new View(m);
        Controller c = new Controller(m,v);
    }
}
