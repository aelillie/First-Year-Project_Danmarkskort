package Model;

import MapFeatures.Coastline;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 16-03-2015.
 */
public class PathCreater {

    /**
     * Appends multiple Paths and sets the windingrule
     * @param refs  List of references to Paths in hashmap
     * @param relations Map of references to Paths
     * @return  Path2D
     */
    public static Path2D setUpMultipolygon(List<Long> refs, Map<Long, Path2D> relations) {
        Long ref = refs.get(0);
        if (relations.containsKey(ref)) {
            Path2D path = relations.get(ref);
            for (int i = 1; i < refs.size(); i++) {
                ref = refs.get(i);
                if (relations.containsKey(ref)) {
                    Path2D element = relations.get(refs.get(i));
                    path.append(element, false);
                } else
                    System.out.println(ref + " ");
            }

            path.setWindingRule(Path2D.WIND_EVEN_ODD);

            return path;

        }
        return null;
    }

    /**
     * Creates a path2D from a list containing coordinates
     * @param coords List of Point2D's
     * @return  Path  The path connected by the Points
     */
    public static Path2D setUpWay(List<Point2D> coords){
        Path2D way = new Path2D.Double();
        Point2D coord = coords.get(0);
        way.moveTo(coord.getX(), coord.getY());
        for (int i = 1; i < coords.size(); i++) {
            coord = coords.get(i);
            way.lineTo(coord.getX(), coord.getY());
        }
        return way;
    }

    public static void setUpCoastLine(Path2D coastPath, Point2D startPoint, Point2D endPoint){
        Coastline currentCoastline = new Coastline(coastPath, startPoint, endPoint);

        List<Coastline> coastlines = OSMHandler.getCoastlines();
        boolean hasBeenConnected = false;

        //Start by adding or comparing to all the other addded coastlines.
        for (int i = 0; i < coastlines.size(); i++) {
            Point2D start = coastlines.get(i).getStart();
            Point2D end = coastlines.get(i).getEnd();

            if (currentCoastline.getStart().equals(end)) {
                coastlines.get(i).getPath().append(currentCoastline.getPath(), true);
                coastlines.get(i).setEnd(currentCoastline.getEnd());
                hasBeenConnected = true;
                i = 0;
                break; //If it has been added, don't iterate anymore.
            }
        }

        if (!hasBeenConnected && !coastlines.contains(currentCoastline)) {
            coastlines.add(currentCoastline);
        }

    }

    //Checks the list once more to make sure that every coastline supposed to be connected is connected.
    public static void checkCoastlines(List<Coastline> coastlines) {

        for (int i = 0; i < coastlines.size(); i++) {
            Point2D start = coastlines.get(i).getStart();
            Point2D end = coastlines.get(i).getEnd();

            for (int j = i + 1; j < coastlines.size(); j++) {
                Point2D compareStart = coastlines.get(j).getStart();
                Point2D compareEnd = coastlines.get(j).getEnd();

                if (end.equals(compareStart)) { //First check if the end fits the start of the other coastline.
                    Path2D newPath = coastlines.get(i).getPath();
                    newPath.append(coastlines.get(j).getPath(), true);
                    coastlines.get(i).setPath(newPath);
                    coastlines.get(i).setEnd(coastlines.get(j).getEnd());
                    coastlines.remove(j);
                    i = 0;
                    break;

                } else if (compareEnd.equals(start)) { //Then check if the end of the second coastline fits the start of the first coastline.
                    Path2D newPath = coastlines.get(j).getPath();
                    newPath.append(coastlines.get(i).getPath(), true);
                    coastlines.get(i).setPath(newPath);
                    coastlines.get(i).setStart(coastlines.get(j).getStart());
                    coastlines.remove(j);
                    i = 0;
                    break;
                }
            }

        }
    }
}
