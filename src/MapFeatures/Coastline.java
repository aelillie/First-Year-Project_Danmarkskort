package MapFeatures;

import Model.OSMHandler;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class Coastline {
    private Path2D path; //The collected path of the coastline
    private Point2D start; //The startpoint of the coastline
    private Point2D end; //The endpoint of the coastline.

    public Coastline(Path2D path, Point2D start, Point2D end){
        this.path = path;
        this.start = start;
        this.end = end;
    }

    public void setEnd(Point2D newEnd){
        end = newEnd;
    }

    public Point2D getStart(){ return start; }

    public Point2D getEnd() { return end; }

    public Path2D getPath() { return path; }

    public void setPath(Path2D updatedPath) { path = updatedPath;}

    public void setStart(Point2D newStart) {start = newStart;}


    public static void processCoastlines(Path2D coastPath, Point2D startPoint, Point2D endPoint) {
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

        //Checks the list once more to make sure that every coastline supposed to be connected is connected.
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

                } else if (compareEnd.equals(start)){ //Then check if the end of the second coastline fits the start of the first coastline.
                    Path2D newPath = coastlines.get(j).getPath();
                    newPath.append(coastlines.get(i).getPath(),true);
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
