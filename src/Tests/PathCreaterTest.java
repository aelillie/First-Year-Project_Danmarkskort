package Tests;


import Model.MapFeatures.Coastline;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

public class PathCreaterTest {

    private static final double DELTA = 1e-15;
    private static List<Coastline> testList;

   @Test
    public void testProcessCoastline(){
        testList = new ArrayList<>();
        Point2D start = new Point2D.Float(1,1);
        Point2D middle = new Point2D.Float(2,2);
        Point2D end = new Point2D.Float(3,3);

        Path2D one = new Path2D.Double();
        one.moveTo(start.getX(),start.getY());
        one.lineTo(middle.getX(), middle.getY());
        one.lineTo(end.getX(),end.getY());

        processCoastlines(one,start,end,testList);

        start = new Point2D.Float(3,3);
        middle = new Point2D.Float(4,4);
        end = new Point2D.Float(5,5);

        Path2D two = new Path2D.Double();
        two.moveTo(start.getX(),start.getY());
        two.lineTo(middle.getX(),middle.getY());
        two.lineTo(end.getX(),end.getY());

        processCoastlines(two,start,end,testList);

        start = new Point2D.Float(5,5);
        middle = new Point2D.Float(6,6);
        end = new Point2D.Float(7,7);

        Path2D three = new Path2D.Double();
        three.moveTo(start.getX(),start.getY());
        three.lineTo(middle.getX(),middle.getY());
        three.lineTo(end.getX(),end.getY());

        processCoastlines(three,start,end,testList);

       //Check whether they get connected to one coastline
       assertEquals(1,testList.size());

       Coastline coastline = testList.get(0);

       //Check whether the coastlines end and start point are correct (that they get correctly connected)
       assertEquals(end.getX(),coastline.getEnd().getX(),DELTA);
       assertEquals(end.getY(),coastline.getEnd().getY(),DELTA);
       assertEquals(1,coastline.getStart().getX(),DELTA);
       assertEquals(1,coastline.getStart().getY(),DELTA);
    }

    public static void processCoastlines(Path2D coastPath, Point2D startPoint, Point2D endPoint, List<Coastline> coastlines) {
        Coastline currentCoastline = new Coastline(coastPath, startPoint, endPoint, -2, "coastline");
        boolean hasBeenConnected = false;

        //Start by adding or comparing to all the other addded coastlines.
        for (int i = 0; i < coastlines.size(); i++) {
            Point2D start = coastlines.get(i).getStart();
            Point2D end = coastlines.get(i).getEnd();

            if (currentCoastline.getStart().equals(end)) {
                coastlines.get(i).getWay().append(currentCoastline.getWay(), true);
                coastlines.get(i).setEnd(currentCoastline.getEnd());
                hasBeenConnected = true;
                i = 0;
                break; //If it has been added, don't iterate anymore.
            }
        }

        if (!hasBeenConnected && !coastlines.contains(currentCoastline)) {
            coastlines.add(currentCoastline);
        }
        try {
            connectCoastlines(coastlines);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void connectCoastlines(List<Coastline> coastlines) {

        for (int i = 0; i < coastlines.size(); i++) {
            Point2D start = coastlines.get(i).getStart();
            Point2D end = coastlines.get(i).getEnd();

            for (int j = i + 1; j < coastlines.size(); j++) {
                Point2D compareStart = coastlines.get(j).getStart();
                Point2D compareEnd = coastlines.get(j).getEnd();

                if (end.equals(compareStart)) { //First check if the end fits the start of the other coastline.
                    Path2D newPath = coastlines.get(i).getWay();
                    newPath.append(coastlines.get(j).getWay(), true);
                    coastlines.get(i).setPath(newPath);
                    coastlines.get(i).setEnd(coastlines.get(j).getEnd());
                    coastlines.remove(j);
                    i = 0;
                    break;

                } else if (compareEnd.equals(start)) { //Then check if the end of the second coastline fits the start of the first coastline.
                    Path2D newPath = coastlines.get(j).getWay();
                    newPath.append(coastlines.get(i).getWay(), true);
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
