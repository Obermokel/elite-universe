package borg.ed.galaxy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import borg.ed.galaxy.data.Coord;

import java.awt.Point;

/**
 * CoordUtil
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public abstract class CoordUtil {

    static final Logger logger = LoggerFactory.getLogger(CoordUtil.class);

    public static Point coordToPointTopView(Coord coord, float xMin, float xMax, float zMin, float zMax, int imageWidth, int imageHeight) {
        float xSize = xMax - xMin;
        float zSize = zMax - zMin;
        float xPercent = (coord.getX() - xMin) / xSize;
        float zPercent = 1.0f - ((coord.getZ() - zMin) / zSize);
        return new Point(Math.round(xPercent * imageWidth), Math.round(zPercent * imageHeight));
    }

    public static Point coordToPointFrontView(Coord coord, float xMin, float xMax, float yMin, float yMax, int imageWidth, int imageHeight) {
        float xSize = xMax - xMin;
        float ySize = yMax - yMin;
        float xPercent = (coord.getX() - xMin) / xSize;
        float yPercent = 1.0f - ((coord.getY() - yMin) / ySize);
        return new Point(Math.round(xPercent * imageWidth), Math.round(yPercent * imageHeight));
    }

    public static Point coordToPointLeftView(Coord coord, float zMin, float zMax, float yMin, float yMax, int imageWidth, int imageHeight) {
        float zSize = zMax - zMin;
        float ySize = yMax - yMin;
        float zPercent = 1.0f - ((coord.getZ() - zMin) / zSize);
        float yPercent = 1.0f - ((coord.getY() - yMin) / ySize);
        return new Point(Math.round(zPercent * imageWidth), Math.round(yPercent * imageHeight));
    }

    public static int coordToAlpha(float coord, float min, float max) {
        float size = max - min;
        float center = (min + max) / 2.0f;
        float dy = Math.abs(coord - center);
        int alpha = 255 - Math.round((dy / (size / 2)) * 127);
        return Math.min(255, Math.max(0, alpha));
    }

}
