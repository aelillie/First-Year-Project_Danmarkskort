public class Main {
    public static void main(String[] args) {
        Model m = new Model("small.osm");
        View v = new View(m);
        Controller c = new Controller(m,v);
    }
}
