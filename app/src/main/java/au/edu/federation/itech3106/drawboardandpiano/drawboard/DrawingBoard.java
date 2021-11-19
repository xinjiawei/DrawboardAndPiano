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

    //当前控件的宽高
    private int mWidth;
    private int mHeight;

    //画笔
    private Paint mPaint;
    //画笔颜色，默认黑色
    private int mPaintColor = Color.BLACK;
    //画布颜色，默认透明
    private int mCanvasColor = Color.TRANSPARENT;
    //private int mCanvasColor = Color.BLACK;
    //画笔宽度,默认10个像素点
    private int mPaintSize = dip2x(1);
    //橡皮擦宽度
    //private final int mEraseSize = dip2x(20);
    //缓冲的位图
    public Bitmap mBufferBitmap;
    //缓冲的画布
    private Canvas mBufferCanvas;
    //路径
    private Path mPath;
    //设置图形混合模式为清除，橡皮擦的功能
    //需要彻底关掉硬件加速，在初始化前
    private final PorterDuffXfermode mEraserMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    //保存的路径
    private List<DrawPathList> savePaths;
    //当前的路径
    private List<DrawPathList> currPaths;
    //画板锁定状态
    private int islock = 0;

    public DrawingBoard(Context context) {
        this(context, null);
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        //View从API Level 11才加入setLayerType方法
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    //获取像素点
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
        //创建一个BITMAP，BITMAP就是Canvas绘制的图片
        mBufferBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
        //默认背景
        mBufferCanvas.drawColor(mCanvasColor);
    }

    private void initPaint() {
        //设置画笔抗锯齿和抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //设置画笔填充方式为只描边
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔颜色
        mPaint.setColor(mPaintColor);
        //设置画笔宽度
        mPaint.setStrokeWidth(mPaintSize);
        //设置圆形线帽
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置线段连接处为圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //开启缓存
        boolean isDrawingCacheEnabled = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //可以加入颜色背景
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

    //默认画笔模式,颜色
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

    //设置画笔模式
    public void setMode(DrawMode mode) {
        if (mode != mDrawMode) {
            if (mode == DrawMode.EraserMode) {
                //橡皮擦模式
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setXfermode(mEraserMode);
                //mPaint.setColor(mCanvasColor);
                //橡皮擦颜色不能是透明的
                mPaint.setColor(Color.parseColor("#ffffff"));
            } else {
                //画笔模式
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setXfermode(null);
                mPaint.setColor(mPaintColor);
            }
        }
        mDrawMode = mode;
    }

    //调节画笔粗细的时候，锁定画板
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

                    //处理点击事件
                    performClick();
                    //重置所有 PointerId 为 -1
                    clearTouchRecordStatus();
                    //新增一个轨迹
                    addNewPath(event);
                    //重绘
                    invalidate();
                    Log.e("1211-1", "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:


                    if (currPaths.size() > 0) {
                        for (int i = 0; i < event.getPointerCount(); i++) {
                            //遍历当前屏幕上所有手指
                            int itemPointerId = event.getPointerId(i);//获取到这个手指的 ID
                            for (DrawPathList itemPath : currPaths) {
                                //遍历绘制记录表，通过 ID 找到对应的记录
                                if (itemPointerId == itemPath.pointerId) {
                                    int pointerIndex = event.findPointerIndex(itemPointerId);
                                    //通过 pointerIndex 获取到此次滑动事件的所有历史轨迹
                                    List<PointF> recordList = readPointList(event, pointerIndex);
                                    if (!listEquals(recordList, itemPath.record.peek())) {
                                        //判断该 List 是否已存在，不存在则添加进去
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
                    //保存路径
                    //=======================
                    clearTouchRecordStatus();
                    //=======================
                    savePath();
                    mPath.reset();
                    Log.e("1211-1", "ACTION_UP");
                    break;
                //----------------------------------------------------------------------
                case MotionEvent.ACTION_POINTER_DOWN:
                    //屏幕上已经有了手指，此时又有别的手指点击时事件
                    addNewPath(event);
                    invalidate();
                    Log.e("1211-1.2", "松前指尖数: " + String.valueOf(event.getActionIndex()));
                    Log.e("1211-1.1", "ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //屏幕上有一根指头抬起，但有别的指头未抬起时的事件
                    int pointerId = event.getPointerId(event.getActionIndex());
                    for (DrawPathList item : currPaths) {
                        if (item.pointerId == pointerId) {
                            //该手指已绘制结束，将此 PointerId 重置为 -1
                            item.pointerId = -1;
                        }
                    }
                    Log.e("1211-1.2", "松后指尖数: " + String.valueOf(event.getActionIndex()));
                    Log.e("1211-1.1", "ACTION_POINTER_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //事件被取消
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
     * 判断两个列表中所有的数据是否相同
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

    //清除画板
    public void clean() {

        savePaths.clear();
        currPaths.clear();
        //将位图变为透明的
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    /**
     * 下一步 反撤销yi
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
     * 上一步 撤销
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

    //重绘位图
    private void reDrawBitmap() {
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        for (int i = 0; i < currPaths.size(); i++) {
            DrawPathList path = currPaths.get(i);
            mBufferCanvas.drawPath(path.getPath(), path.getPaint());

            //Log.e("1205-3.1","currPaths num : " + i + ", cont : " + currPaths.get(i));
        }
        invalidate();
    }

    //重加载位图
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
        //保存数据
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