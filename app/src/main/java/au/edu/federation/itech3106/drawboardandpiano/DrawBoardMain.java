package au.edu.federation.itech3106.drawboardandpiano;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3106.drawboardandpiano.drawboard.DrawMode;
import au.edu.federation.itech3106.drawboardandpiano.drawboard.DrawingBoard;

public class DrawBoardMain extends AppCompatActivity implements View.OnClickListener{

    private DrawingBoard mDrawingBoard;//画板
    //代表颜色选项
    private ImageView Black;
    private ImageView Accent;
    private ImageView Primary;
    private ImageView Red;
    private ImageView color5;
    private ImageView color6;
    private ImageView color7;
    private ImageView color8;
    private ImageView color9;
    private ImageView color10;
    private ImageView color11;
    private ImageView color12;
    private ImageView color13;
    private ImageView color14;
    private ImageView color15;
    private ImageView color16;
    //对画板的操作
    private ImageView mPaint;
    private ImageView mEraser;
    private ImageView mClean;
    private ImageView mLast;
    private ImageView mAntiLast;

    private ImageView boardBackground;
    private TextView show;
    //记录画笔大小
    private float size;
    int lock = 0;
    int tmp = 0;
    int tmp1 = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawboard);

        show = findViewById(R.id.textView);
        initView();//绑定
        initEvent();//匹配监听事件
        Intent i = getIntent();
        String message =i.getStringExtra(MainActivity.EXTRA_MESSAGE);
        show.setText(message);

        //sreenbar.bringToFront();

        Context mContext = DrawBoardMain.this;
        bindViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //默认画笔大小
       size  = dip2x(1);
    }

    private void initView(){//对象 找控件
        mDrawingBoard = findViewById(R.id.draw_board);
        //mSlider = findViewById(R.id.slider);
        Black = findViewById(R.id.iv_one);
        Accent = findViewById(R.id.iv_two);
        Primary = findViewById(R.id.iv_three);
        Red = findViewById(R.id.iv_four);
        color5 =findViewById(R.id.iv_five);
        color6 =findViewById(R.id.iv_six);
        color7 =findViewById(R.id.iv_seven);
        color8 =findViewById(R.id.iv_eight);
        color9 =findViewById(R.id.iv_one1);
        color10 =findViewById(R.id.iv_two2);
        color11 =findViewById(R.id.iv_three3);
        color12 =findViewById(R.id.iv_four4);
        color13 =findViewById(R.id.iv_five5);
        color14 =findViewById(R.id.iv_six6);
        color15 =findViewById(R.id.iv_seven7);
        color16 =findViewById(R.id.iv_eight8);


        mPaint = findViewById(R.id.iv_paint);
        mEraser = findViewById(R.id.iv_eraser);
        mClean = findViewById(R.id.iv_clean);
        mLast = findViewById(R.id.iv_last);
        mAntiLast = findViewById(R.id.iv_antilast);
        boardBackground = findViewById(R.id.boardbackground);
    }

    //获取像素点
    private int dip2x(float depValue){
        final float density = getResources().getDisplayMetrics().density;
        return (int)(depValue*density+0.5f);
    }
    //
    public void onClickBlackbac (View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(tmp1 == 0){
            tmp1 =1;
            CharSequence text = "Change Background to black！";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else {
            tmp1 = 0;
            CharSequence text = "Change Background to white！";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if(tmp == 0) {
            tmp = 1;
            //mDrawingBoard.setBackgroundColor(Color.BLACK);
            boardBackground.setBackgroundColor(Color.BLACK);
            //mDrawingBoard.setBackgroundResource(R.drawable.eraser);
        }else if (tmp == 1){
            tmp = 0;
            //mDrawingBoard.setBackgroundColor(Color.TRANSPARENT);
            boardBackground.setBackgroundColor(Color.TRANSPARENT);
            //mDrawingBoard.setBackgroundResource(R.drawable.paint);
        }else {
            Log.e("1204","Change error");
        }
        //Log.e("1204-1", String.valueOf(mDrawingBoard.getmPaintColor()));
        mDrawingBoard.reLoadBitmap();
        //view.invalidate();
        //mBufferCanvas.drawColor(mCanvasColor);
        Log.e("1210-1","Change background");
    }

    //实现滑动SeekBar改变画笔线条粗细大小
    private void bindViews() {
        SeekBar sb_normal = (SeekBar) findViewById(R.id.sb_normal);

        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.e("1204-2", "触碰SeekBar中");
                show.setText("当前笔刷:" + (((progress+1)/4)+1) + "  / 26px ");

                if (size > 0) {
                    mDrawingBoard.setmPaintSize((int) ((progress+1) /4));
                    //mDrawingBoard.setmPaintSize((int) ((3+1) /4));
                    //
                    int nowsize = mDrawingBoard.getmPaintSize();
                    Log.e("1200", String.valueOf(nowsize));
                }
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lock = 1;
                mDrawingBoard.isLock(lock);
                Log.e("1204-1", "触碰SeekBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lock = 0;
                mDrawingBoard.isLock(lock);
                Log.e("1204-3", "放开SeekBar");
                //TODO what the fuck
                mDrawingBoard.reLoadBitmap();
                //Log.e("1205", "");

            }
        });
    }
    private void initEvent(){

        Black.setOnClickListener(this);
        Accent.setOnClickListener(this);
        Primary.setOnClickListener(this);
        Red.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
        color7.setOnClickListener(this);
        color8.setOnClickListener(this);
        color9.setOnClickListener(this);
        color10.setOnClickListener(this);
        color11.setOnClickListener(this);
        color12.setOnClickListener(this);
        color13.setOnClickListener(this);
        color14.setOnClickListener(this);
        color15.setOnClickListener(this);
        color16.setOnClickListener(this);
        //设置默认画笔背景为蓝色
        mPaint.getBackground().setLevel(1);
        mPaint.getDrawable().setLevel(1);
        mPaint.setOnClickListener(this);
        mEraser.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mLast.setOnClickListener(this);
        mAntiLast.setOnClickListener(this);

    }

    //设置画板清空对话框
    private void alertDialogClean(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要请空画板吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDrawingBoard.clean();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final  AlertDialog dialog = builder.show();
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_one:
                mDrawingBoard.setmPaintColor(Color.BLACK);
                break;
            case R.id.iv_two:
                mDrawingBoard.setmPaintColor(Color.parseColor("#D81B60"));
                break;
            case R.id.iv_three:
                mDrawingBoard.setmPaintColor(Color.parseColor("#008577"));
                break;
            case R.id.iv_four:
                Log.e("1201","BUTTON");
                mDrawingBoard.setmPaintColor(Color.RED);
                break;
            case R.id.iv_five:
                Log.e("1201","BUTTON");
                mDrawingBoard.setmPaintColor(Color.parseColor("#1DE9B6"));
                break;
            case R.id.iv_six:
                Log.e("1201","BUTTON");
                mDrawingBoard.setmPaintColor(Color.parseColor("#D500F9"));
                break;
            case R.id.iv_seven:
                mDrawingBoard.setmPaintColor(Color.parseColor("#C51162"));
                break;
            case R.id.iv_eight:
                mDrawingBoard.setmPaintColor(Color.parseColor("#F1F8E9"));
                break;
            case R.id.iv_one1:
                mDrawingBoard.setmPaintColor(Color.parseColor("#00ff00"));
                break;
            case R.id.iv_two2:
                mDrawingBoard.setmPaintColor(Color.parseColor("#0000ff"));
                break;
            case R.id.iv_three3:
                mDrawingBoard.setmPaintColor(Color.parseColor("#ffffff"));
                break;
            case R.id.iv_four4:
                mDrawingBoard.setmPaintColor(Color.parseColor("#2e97ff"));
                break;
            case R.id.iv_five5:
                mDrawingBoard.setmPaintColor(Color.parseColor("#b9bdc5"));
                break;
            case R.id.iv_six6:
                mDrawingBoard.setmPaintColor(Color.parseColor("#ff9800"));
                break;
            case R.id.iv_seven7:
                mDrawingBoard.setmPaintColor(Color.parseColor("#ffeb3b"));
                break;
            case R.id.iv_eight8:
                mDrawingBoard.setmPaintColor(Color.parseColor("#8BC34A"));
                break;
            case R.id.iv_paint:
                if (mDrawingBoard.getMode() != DrawMode.PaintMode) {
                    mDrawingBoard.setMode(DrawMode.PaintMode);
                }
                mPaint.getDrawable().setLevel(1);
                mPaint.getBackground().setLevel(1);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);

                break;
            case R.id.iv_eraser:
                if (mDrawingBoard.getMode() != DrawMode.EraserMode) {
                    mDrawingBoard.setMode(DrawMode.EraserMode);
                }
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(1);
                mEraser.getBackground().setLevel(1);
                break;
            case R.id.iv_clean:
                alertDialogClean();
                break;
                //反撤销
            case R.id.iv_antilast:
                mDrawingBoard.lastStep();
                break;
            case R.id.iv_last://撤销
                mDrawingBoard.nextStep();
                break;
            case R.id.iv_save://撤销
                mDrawingBoard.saveToLocal();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}
