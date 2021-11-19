package au.edu.federation.itech3106.drawboardandpiano.drawboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends View {

    private Context mContext;
    private DrawMode mDrawMode = DrawMode.PaintMode;

    //Width and height of the panel
    private int mWidth;
    private int mHeight;

    //pen
    private Paint mPaint;
    //color of pen, default is black
    private int mPaintColor = Color.BLACK;
    //Canvas color, transparent by default
    private int mCanvasColor = Color.TRANSPARENT;
    //private int mCanvasColor = Color.BLACK;
    //Brush width, default 1 pixels
    private int mPaintSize = dip2x(1);
    //橡皮擦宽度
    //private final int mEraseSize = dip2x(20);
    //Buffered bitmap
    public Bitmap mBufferBitmap;
    //Cushioned canvas
    private Canvas mBufferCanvas;
    //paths
    private Path mPath;
    //Set graphic blend mode to clear, function of eraser
    //TODO hardware acceleration needs to be turned off completely before initialization!!!
    private final PorterDuffXfermode mEraserMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    //Save path,function of undo need it
    private List<DrawPathList> savePaths;
    //Save current path,
    private List<DrawPathList> currPaths;
    //drawboard locked, when justifing the seekbar, change the size of pen, need to lock the board,
    //otherwise,some stupid errors will occurs.
    private int islock = 0;

    public DrawingBoard(Context context) {
        this(context, null);
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        //View: after api Level 11
        //shut down the hardware acceleration!!!
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    //get the pixel
    private int dip2x(float depValue) {
        final float density = getResources().getDisplayMetrics().density;
        return (int) (depValue * density + 0.5f);
    }

    private void initPath() {
        mPath = new Path();
        savePaths = new ArrayList<>();
        currPaths = new ArrayList<>();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        initCanvas();
    }

    private void initCanvas() {
        //Create a BITMAP. A BITMAP is an image drawn on the Canvas
        mBufferBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
        //The default setting
        mBufferCanvas.drawColor(mCanvasColor);
    }

    private void initPaint() {
        //Set brush anti-aliasing and shake
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //Set the brush fill method to Stroke only
        mPaint.setStyle(Paint.Style.STROKE);
        //Set the brush color
        mPaint.setColor(mPaintColor);
        //Set the brush width
        mPaint.setStrokeWidth(mPaintSize);
        //Set a round thread cap
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //Set the line segment connection to a rounded corner
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //Open the cache
        boolean isDrawingCacheEnabled = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO0 add a color background
        canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        if (currPaths == null || currPaths.isEmpty()) {
            return;
        }
        for (DrawPathList item : currPaths) {
            canvas.drawPath(item.path, item.paint);
            //Log.e("1211-1.2-1", "item: " + item);
            Log.e("1211-1.2-2", "currPaths.size(): " + currPaths.size());
        }
    }

    //Default brush mode, color
    public int getmPaintColor() {
        return mPaintColor;
    }

    public void setmPaintColor(int mPaintColor) {
        this.mPaintColor = mPaintColor;
        mPaint.setColor(mPaintColor);
    }

    public DrawMode getMode() {
        return mDrawMode;
    }

    //Set brush mode
    public void setMode(DrawMode mode) {
        if (mode != mDrawMode) {
            if (mode == DrawMode.EraserMode) {
                //Eraser mode
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setXfermode(mEraserMode);
                //mPaint.setColor(mCanvasColor);
                //The eraser color must not be transparent
                mPaint.setColor(Color.parseColor("#ffffff"));
            } else {
                //The brush model
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setXfermode(null);
                mPaint.setColor(mPaintColor);
            }
        }
        mDrawMode = mode;
    }

    //Lock the palette while adjusting the brush thickness
    public void isLock(int lock) {
        if (lock == 1) {
            islock = 1;
            Log.e("1208-1", String.valueOf(islock));
        } else {
            islock = 0;
            Log.e("1208-2", String.valueOf(islock));
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("1208-3", "Board Lock: " + String.valueOf(islock));

        if (islock == 0) {

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    //Handling click events
                    performClick();
                    //Reset all pointerids to -1
                    clearTouchRecordStatus();
                    //Add a new track
                    addNewPath(event);
                    //redraw
                    invalidate();
                    Log.e("1211-1", "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:


                    if (currPaths.size() > 0) {
                        for (int i = 0; i < event.getPointerCount(); i++) {
                            //Traverse all fingers on the current screen
                            int itemPointerId = event.getPointerId(i);//获取到这个手指的 ID
                            for (DrawPathList itemPath : currPaths) {
                                //Traverse the drawing record table and find the corresponding record by ID
                                if (itemPointerId == itemPath.pointerId) {
                                    int pointerIndex = event.findPointerIndex(itemPointerId);
                                    //All historical tracks of this sliding event are obtained through pointerIndex
                                    List<PointF> recordList = readPointList(event, pointerIndex);
                                    if (!listEquals(recordList, itemPath.record.peek())) {
                                        //Check whether the List already exists and add it to the List if it does not
                                        itemPath.record.push(recordList);
                                        addPath(recordList, itemPath.path);
                                    }
                                }
                            }
                        }
                        invalidate();
                    }

                    Log.e("1211-1", "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    //Save the path
                    //=======================
                    clearTouchRecordStatus();
                    //=======================
                    savePath();
                    mPath.reset();
                    Log.e("1211-1", "ACTION_UP");
                    break;
                //----------------------------------------------------------------------
                case MotionEvent.ACTION_POINTER_DOWN:
                    //When there is already a finger on the screen and another finger clicks
                    addNewPath(event);
                    invalidate();
                    Log.e("1211-1.2", "松前指尖数: " + String.valueOf(event.getActionIndex()));
                    Log.e("1211-1.1", "ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //There is an event on the screen when one finger is up, but the others are not
                    int pointerId = event.getPointerId(event.getActionIndex());
                    for (DrawPathList item : currPaths) {
                        if (item.pointerId == pointerId) {
                            //The finger is finished drawing, reset this PointerId to -1
                            item.pointerId = -1;
                        }
                    }
                    Log.e("1211-1.2", "松后指尖数: " + String.valueOf(event.getActionIndex()));
                    Log.e("1211-1.1", "ACTION_POINTER_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //Event cancelled
                    clearTouchRecordStatus();
                    Log.e("1211-1.1", "ACTION_CANCEL");
                    break;
            }
        }

        return true;
    }

    private void addNewPath(MotionEvent event) {

        int pointerId = event.getPointerId(event.getActionIndex());
        float x = event.getX(event.findPointerIndex(pointerId));
        float y = event.getY(event.findPointerIndex(pointerId));
        Paint paint = new Paint(mPaint);
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        DrawPathList drawPath = new DrawPathList(pointerId, paint, path);
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(x, y));
        //pointList.add(new PointF(x, y));
        Log.e("1211-1.2", "pointList: " + String.valueOf(pointList));
        drawPath.record.push(pointList);
        Log.e("1211-1.2", "drawPath: " + String.valueOf(drawPath));
        currPaths.add(drawPath);

    }

    private List<PointF> readPointList(MotionEvent event, int pointerIndex) {
        List<PointF> list = new ArrayList<>();
        for (int j = 0; j < event.getHistorySize(); j++) {
            list.add(new PointF(event.getHistoricalX(pointerIndex, j), event.getHistoricalY(pointerIndex, j)));
        }
        return list;
    }

    /**
     * Determine whether all data in the two lists are the same
     */
    private boolean listEquals(List<PointF> lis1, List<PointF> list2) {
        if (lis1.equals(list2)) {
            return true;
        }
        if (lis1.size() != list2.size()) {
            return false;
        }
        if (lis1.isEmpty()) {
            return true;
        }
        for (int i = 0; i < lis1.size(); i++) {
            PointF point1 = lis1.get(i);
            PointF point2 = list2.get(i);
            if (!point1.equals(point2)) {
                return false;
            }
        }
        return true;
    }

    private void addPath(List<PointF> list, Path path) {
        for (PointF item : list) {
            path.lineTo(item.x, item.y);
        }
    }

    private void clearTouchRecordStatus() {
        for (DrawPathList item : currPaths) {
            item.pointerId = -1;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    //todo savePath
    private void savePath() {
        savePaths.clear();
        savePaths.addAll(currPaths);
        Log.e("1206-1", "total currPaths : " + currPaths.size());
        Log.e("1206-2", "total savePath : " + savePaths.size());
    }

    //CLEAN sketchpad
    public void clean() {

        savePaths.clear();
        currPaths.clear();
        //Make the bitmap transparent
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    /**
     * undo
     */
    public void lastStep() {
        if (currPaths != savePaths) {
            if (savePaths.size() > currPaths.size()) {
                //currPaths.add(savePaths.get(savePaths.size()-1));
                currPaths.add(savePaths.get(currPaths.size()));
                Log.e("1203-1", String.valueOf(currPaths.size()));
                reDrawBitmap();
            }
        }
    }

    /**
     * ANTI undo
     */
    public void nextStep() {
        if (currPaths.size() > 0) {
            currPaths.remove(currPaths.size() - 1);
            Log.e("1203-2", "currPaths: " + String.valueOf(currPaths.size()));
            Log.e("1203-2", "savePaths: " + String.valueOf(savePaths.size()));
            reDrawBitmap();
        }
    }

    public int getmPaintSize() {
        return mPaintSize;
    }

    public void setmPaintSize(int mPaintSize) {
        this.mPaintSize = mPaintSize;
        mPaint.setStrokeWidth(mPaintSize);
    }

    //Redraw the bitmap
    private void reDrawBitmap() {
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        for (int i = 0; i < currPaths.size(); i++) {
            DrawPathList path = currPaths.get(i);
            mBufferCanvas.drawPath(path.getPath(), path.getPaint());

            //Log.e("1205-3.1","currPaths num : " + i + ", cont : " + currPaths.get(i));
        }
        invalidate();
    }

    //Reload the bitmap
    public void reLoadBitmap() {
        //mBufferBitmap.eraseColor(Color.TRANSPARENT);

        Log.e("1205-3.0000", "total currPaths : " + currPaths.size());
        for (int i = 0; i < currPaths.size(); i++) {
            DrawPathList path = currPaths.get(i);
            //
            try {
                mBufferCanvas.drawPath(path.getPath(), path.getPaint());

            } catch (Exception e) {
                Log.e("1203", String.valueOf(e));
            }
            Log.e("1205-3.2", "currPaths num : " + i + ", cont : " + currPaths.get(i));
        }
        //不加注释的话，是松开之后立即刷新视图，画迹消失
        //加注释，在画笔画之后消失。画笔画之后移动笔粗细滑条，也不会刷新出消失的笔画。
        /*不动笔触的话，点一下松开，也不会消失笔画。（会触发触碰中事件<手机因为按钮小
        所以触发触碰中事件，电脑不会>，但是画笔粗细并没有变化，怀疑和改变了画笔粗细有关系。）
         */
        /*花笔画的代码里也有这段代码invalidate()。（在松开画笔里reLoadBitmap可以解决
        但是出现图层问题）
         */
        //调节画笔粗细的时候，锁定画板，但是bug还在
        //回滚测试，改变背景按钮无关
        //测试，注释改变笔刷粗细的代码，也会掉。
        //把上边的文本更新关掉，好了...
        //把宽度设置为150dp，貌似不用删也好了。


        invalidate();
        Log.e("1205-3", "total currPaths : " + currPaths.size());
    }

    public void saveToLocal() {
        Log.e("1219","00");
        //save dates
        OutputStream outStream;
        try {
            Gson g = new Gson();
            String jsonString = g.toJson(currPaths);
            Log.e("1219",jsonString);
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/TQRecorderConfig/jsonConfig/");
            outStream = mContext.openFileOutput(dir + "data.json", Context.MODE_PRIVATE);
            JsonCode paths = new JsonCode();
            paths.save(outStream, jsonString);
            Log.e("save","saveing");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("save","error");
        }

    }
}