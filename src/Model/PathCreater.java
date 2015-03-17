package Model;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 16-03-2015.
 */
public class PathCreater {


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
}
