package au.edu.federation.itech3106.drawboardandpiano.bitencode;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import au.edu.federation.itech3106.drawboardandpiano.R;

public class PianoMusic{

int[] MusicPiano = {
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c,
        R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c, R.raw.piano_c

};
    int[] MusicFlute = {
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c,
            R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c, R.raw.flute_c

    };
    int[] MusicXYlophone = {
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c,
            R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c, R.raw.xylophone_c

    };

     int BoardLock = 1;

    //使用soundpool播放音效
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;


    //加载ID
    public PianoMusic(final Context context, int loadboard) {
        //最多能重复按动一个键20次
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        //加载音效ID到SoundPoolMap
        Log.e("1299-3","now loadboard :" + loadboard);
        switch(loadboard){
            case 0 :
                for (int i = 0; i < MusicPiano.length; i++) {
                    soundPoolMap.put(i, soundPool.load(context, MusicPiano[i], 1));
                    Log.e("1221-1","MusicPiano " + i + " 声音加载完成");
                }
                break; //可选
            case 1 :
                for (int i = 0; i < MusicFlute.length; i++) {
                    soundPoolMap.put(i, soundPool.load(context, MusicFlute[i], 1));
                    Log.e("1221-2","MusicFlute" + i + " 声音加载完成");
                }
                break; //可选
            case 2 :
                for (int i = 0; i < MusicXYlophone.length; i++) {
                    soundPoolMap.put(i, soundPool.load(context, MusicXYlophone[i], 1));
                    Log.e("1221-3",i + "MusicXYlophone" + i +" 声音加载完成");
                }
                break; //可选
            //你可以有任意数量的case语句
            default : //可选
                Log.e("1221-4","No loadboard Pra");
        }

        //监听声音文件是否加载完毕
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.e("1215","[" + sampleId + "]LOAD COMPLETE, " + "status: " + status );

                if (sampleId == 1||sampleId == 16||sampleId == 32||sampleId  == 46) {
                    Toast toast=Toast.makeText(context, (sampleId+3) /16 + "/3 Load  Complete! ", Toast.LENGTH_LONG);
                    showMyToast(toast, 7000);
                }

                //
                if (sampleId > 47){
                    BoardLock = 0;
                    Toast toast2=Toast.makeText(context, "Load  Complete!    Enjoy!", Toast.LENGTH_LONG);
                    showMyToast(toast2, 5000);
                }
                //BoardLock = 0;
            }
        });
        //Log.e("1214-2",Music.length + " 声音加载完成");
    }

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

    //播放
    public int soundPlay(int no) {

        Log.e("1212-7",  "BoardLock: "+ BoardLock + ", KeyId: " + String.valueOf(no));
        /*reference https://blog.jiawei.xin/?p=550
        *
        *
        *
         */
        double rate = Math.pow(Math.pow(2,0.0833333333333), no);
        Log.e("1212-7.1",  "rate: " + rate);
        return soundPool.play(soundPoolMap.get(no), 100, 100, 1, 0, (float) rate);
    }

    //IsBoardLock
    public int isBoardLock() {
        return BoardLock;
    }

    //结束
    /*
    public int soundOver() {
        return soundPool.play(soundPoolMap.get(1), 100, 100, 1, 0, 1.0f);
    }
     */

    @Override
    protected void finalize() throws Throwable {
        Log.e("1212-1", "soundPool release");
        soundPool.release();
        super.finalize();
    }
}