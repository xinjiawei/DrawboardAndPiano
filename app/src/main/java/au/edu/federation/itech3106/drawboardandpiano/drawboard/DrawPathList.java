
package au.edu.federation.itech3106.drawboardandpiano.drawboard;


import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.List;
import java.util.Stack;

public class DrawPathList {

    public int pointerId = -1;

    public Paint paint;

    public Path path;

    public Stack<List<PointF>> record;

    DrawPathList(int pointerId,Paint paint,Path path)
    {
        this.pointerId = pointerId;
        this.path = path;
        this.paint = paint;
        record = new Stack<>();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}