package au.edu.federation.itech3106.drawboardandpiano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import au.edu.federation.itech3106.drawboardandpiano.bitencode.PianoMusic;

public class PianoMain extends Activity {
    private Button button[];        // Button array
    private PianoMusic utils;       // Tool
    private View parent;            // Parent view
    private int buttonId[];         // button id
    private boolean havePlayed[];   // play video or not,if so ,true
    /*    private View ParentKeys;        // all views of buttons
        private View UpKeys;            // The view with the white buttons in the top row
        private View DownKeys;          // The view with the white buttons in the bottom row
        */
    private int pressedkey[];       // pressed button
    int BoardLock = 0;


    private void init() {
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("data2", Context.MODE_PRIVATE);
        String message = sharedPreferences.getString("ca","0");
        int loadboard = Integer.parseInt(message);
         */
        Intent in = getIntent();
        String message =in.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int loadboard = Integer.parseInt(message);
        Log.e("1299-1", "You Select : " + String.valueOf(loadboard));
        Log.e("1221","now message :" + message);
        // new tool class
        utils = new PianoMusic(getApplicationContext(),loadboard);

        // resource button id
        buttonId = new int[48];


/*
temp == 1||temp == 3||temp == 6||temp == 8||temp == 10||temp == 13||temp == 15||temp == 18||temp == 20||temp == 22||
temp == 25||temp == 27||temp == 30||temp == 32||temp == 34||temp == 37||temp == 39||temp == 42||temp == 44||temp == 46

pressedkey[count] == 1||pressedkey[count] == 3||pressedkey[count] == 6||pressedkey[count] == 8||pressedkey[count] == 10||pressedkey[count] == 13||pressedkey[count] == 15||pressedkey[count] == 18||pressedkey[count] == 20||pressedkey[count] == 22||
pressedkey[count] == 25||pressedkey[count] == 27||pressedkey[count] == 30||pressedkey[count] == 32||pressedkey[count] == 34||pressedkey[count] == 37||pressedkey[count] == 39||pressedkey[count] == 42||pressedkey[count] == 44||pressedkey[count] == 46

pressedkey[count] >= 28
 */


        buttonId[0] = R.id.PianoC;
        buttonId[1] = R.id.PianoCm;
        buttonId[2] = R.id.PianoD;
        buttonId[3] = R.id.PianoDm;
        buttonId[4] = R.id.PianoE;
        buttonId[5] = R.id.PianoF;
        buttonId[6] = R.id.PianoFm;
        buttonId[7] = R.id.PianoG;
        buttonId[8] = R.id.PianoGm;
        buttonId[9] = R.id.PianoA;
        buttonId[10] = R.id.PianoAm;
        buttonId[11] = R.id.PianoB;

        buttonId[12] = R.id.Pianoc;
        buttonId[13] = R.id.Pianocm;
        buttonId[14] = R.id.Pianod;
        buttonId[15] = R.id.Pianodm;
        buttonId[16] = R.id.Pianoe;
        buttonId[17] = R.id.Pianof;
        buttonId[18] = R.id.Pianofm;
        buttonId[19] = R.id.Pianog;
        buttonId[20] = R.id.Pianogm;
        buttonId[21] = R.id.Pianoa;
        buttonId[22] = R.id.Pianoam;
        buttonId[23] = R.id.Pianob;

        buttonId[24] = R.id.Pianoc1;
        buttonId[25] = R.id.Pianoc1m;
        buttonId[26] = R.id.Pianod1;
        buttonId[27] = R.id.Pianod1m;
        buttonId[28] = R.id.Pianoe1;
        buttonId[29] = R.id.Pianof1;
        buttonId[30] = R.id.Pianof1m;
        buttonId[31] = R.id.Pianog1;
        buttonId[32] = R.id.Pianog1m;
        buttonId[33] = R.id.Pianoa1;
        buttonId[34] = R.id.Pianoa1m;
        buttonId[35] = R.id.Pianob1;

        buttonId[36] = R.id.Pianoc2;
        buttonId[37] = R.id.Pianoc2m;
        buttonId[38] = R.id.Pianod2;
        buttonId[39] = R.id.Pianod2m;
        buttonId[40] = R.id.Pianoe2;
        buttonId[41] = R.id.Pianof2;
        buttonId[42] = R.id.Pianof2m;
        buttonId[43] = R.id.Pianog2;
        buttonId[44] = R.id.Pianog2m;
        buttonId[45] = R.id.Pianoa2;
        buttonId[46] = R.id.Pianoa2m;
        buttonId[47] = R.id.Pianob2;


        button = new Button[48];
        havePlayed = new boolean[48];

        // get button item
        for (int i = 0; i < button.length; i++) {
            button[i] = (Button) findViewById(buttonId[i]);
            button[i].setClickable(false);
            havePlayed[i] = false;
        }

        pressedkey = new int[5];
        for (int j = 0; j < pressedkey.length; j++) {
            pressedkey[j] = -1;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piano);
        init();


        parent = (View) findViewById(R.id.ParentKeys);
        parent.setClickable(true);
        parent.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int temp;           // Holds the clicked button object
                int tempIndex;
                int pointercount;   // pressed in the same time amount

                pointercount = event.getPointerCount();
                Log.e("1212-2", "pointercount: " + String.valueOf(pointercount));

                for (int count = 0; count < pointercount; count++) {
                    boolean moveflag = false;   // moveflag - move or not
                    temp = isInAnyScale(event.getX(count), event.getY(count), button);
                    //keyboard lock or not
                    BoardLock = utils.isBoardLock();
                    if ((temp != -1) && (BoardLock == 0)) {
                        //touch event
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:   // together with printerdown
                                Log.e("1216", String.valueOf(utils.isBoardLock()));
                                Log.e("1213-1", "ACTION_DOWN");
                            case MotionEvent.ACTION_POINTER_DOWN:
                                Log.e("1213-1", "ACTION_POINTER_DOWN");
                                Log.e("1212-3", "当前队列: " + count); // Easy debugging (output is green)
                                pressedkey[count] = temp;
                                if (!havePlayed[temp]) {// play music,press down
                                    button[temp].setBackgroundResource(R.drawable.button_pressed);
                                    // play music
                                    utils.soundPlay(temp);
                                    Log.e("1212-4", "已播放的id: " + temp);// Easy debugging (output is green)
                                    havePlayed[temp] = true;// set as playing
                                }
                                break;

                            case MotionEvent.ACTION_MOVE:
                                Log.e("1213-1", "ACTION_MOVE");
                                temp = pressedkey[count];
                                for (int i = temp + 1; i >= temp - 1; i--) {
                                    // When the buttons are on both ends, one side is out of bounds
                                    if (i < 0 || i >= button.length) {
                                        continue;
                                    }
                                    if (isInScale(event.getX(count), event.getY(count), button[i])) {// in a button
                                        moveflag = true;
                                        if (i != temp) {// nearby button
                                            boolean laststill = false;
                                            boolean nextstill = false;
                                            // Suppose the finger has been lifted from the previous position, but not really lifted, so it is not displaced
                                            pressedkey[count] = -1;
                                            for (int j = 0; j < pointercount; j++) {
                                                if (pressedkey[j] == temp) {
                                                    laststill = true;
                                                }
                                                if (pressedkey[j] == i) {
                                                    nextstill = true;
                                                }
                                            }

                                            if (!nextstill) {// The move in button is not pressed
                                                // set as recent button
                                                button[i].setBackgroundResource(R.drawable.button_pressed);
                                                // voice
                                                utils.soundPlay(i);
                                                havePlayed[i] = true;
                                            }

                                            pressedkey[count] = i;

                                            if (!laststill) {// no finger on
                                                // set up button
                                                if (temp == 1 || temp == 3 || temp == 6 || temp == 8 || temp == 10 || temp == 13 || temp == 15 || temp == 18 || temp == 20 || temp == 22 ||
                                                        temp == 25 || temp == 27 || temp == 30 || temp == 32 || temp == 34 || temp == 37 || temp == 39 || temp == 42 || temp == 44 || temp == 46)
                                                    button[temp].setBackgroundResource(R.drawable.blackbutton);
                                                else
                                                    button[temp].setBackgroundResource(R.drawable.button);
                                                havePlayed[temp] = false;
                                            }

                                            break;
                                        }
                                    }
                                }
                                break;

                            case MotionEvent.ACTION_UP:
                                Log.e("1213-1", "ACTION_UP");
                            case MotionEvent.ACTION_POINTER_UP:
                                Log.e("1213-1", "ACTION_POINTER_UP");
                                // event with point
                                tempIndex = event.getActionIndex();
                                if (tempIndex == count) {
                                    Log.e("1212-5", "多点事件: " + tempIndex);
                                    boolean still = false;
                                    //TODO  up now
                                    for (int t = count; t < 5; t++) {
                                        if (t != 4) {
                                            if (pressedkey[t + 1] >= 0) {
                                                pressedkey[t] = pressedkey[t + 1];
                                            } else {
                                                pressedkey[t] = -1;
                                            }
                                        } else {
                                            pressedkey[t] = -1;
                                        }

                                    }
                                    for (int i = 0; i < pressedkey.length; i++) {// other point or not
                                        if (pressedkey[i] == temp) {
                                            still = true;
                                            break;
                                        }
                                    }
                                    if (!still) {// no finger on the button recently
                                        if (temp == 1 || temp == 3 || temp == 6 || temp == 8 || temp == 10 || temp == 13 || temp == 15 || temp == 18 || temp == 20 || temp == 22 ||
                                                temp == 25 || temp == 27 || temp == 30 || temp == 32 || temp == 34 || temp == 37 || temp == 39 || temp == 42 || temp == 44 || temp == 46)
                                            button[temp].setBackgroundResource(R.drawable.blackbutton);
                                        else
                                            button[temp].setBackgroundResource(R.drawable.button);
                                        havePlayed[temp] = false;
                                        Log.e("1212-6", "button " + temp + " up");
                                    }
                                    break;
                                }
                        }
                    } else {

                        Log.e("1220", "BoardLock : " + BoardLock + ", temp : " + temp);
                    }
                    //
                    if (event.getActionMasked() == MotionEvent.ACTION_MOVE && !moveflag) {
                        if (pressedkey[count] != -1) {
                            if (pressedkey[count] == 1 || pressedkey[count] == 3 || pressedkey[count] == 6 || pressedkey[count] == 8 || pressedkey[count] == 10 || pressedkey[count] == 13 || pressedkey[count] == 15 || pressedkey[count] == 18 || pressedkey[count] == 20 || pressedkey[count] == 22 ||
                                    pressedkey[count] == 25 || pressedkey[count] == 27 || pressedkey[count] == 30 || pressedkey[count] == 32 || pressedkey[count] == 34 || pressedkey[count] == 37 || pressedkey[count] == 39 || pressedkey[count] == 42 || pressedkey[count] == 44 || pressedkey[count] == 46)
                                button[pressedkey[count]].setBackgroundResource(R.drawable.blackbutton);
                            else
                                button[pressedkey[count]].setBackgroundResource(R.drawable.button);
                            havePlayed[pressedkey[count]] = false;
                        }
                    }
                }
                return false;
            }
        });

/*        ParentKeys = (View) findViewById(R.id.ParentKeys);
        UpKeys = (View) findViewById(R.id.UpKeys);
        DownKeys = (View) findViewById(R.id.DownKeys);*/
    }

    /*
    public int getStr2() {
        return loadboard;
        //return 1;
    }
     */


    //Determine whether a point is within the range of a button
    private boolean isInScale(float x, float y, Button button) {
        // tempParent.getTop()Gets the upper-right ordinate of the parent view of the button relative to its parent view
        View tempParent;
        tempParent = (View) button.getParent();
        if (x > button.getLeft()
                && x < button.getRight()
                && y > button.getTop() + tempParent.getTop()
                && y < button.getBottom() + tempParent.getTop()
        ) {
            return true;
        } else {
            return false;
        }
    }

    //
    private int isInAnyScale(float x, float y, Button[] button) {
        // tempParent.getTop()Determines whether a point is within a button in a button set
        View tempParent;
        for (int i = button.length - 1; i >= 0; i--) {
            tempParent = (View) button[i].getParent();
            if (x > button[i].getLeft()
                    && x < button[i].getRight()
                    && y > button[i].getTop() + tempParent.getTop()
                    && y < button[i].getBottom() + tempParent.getTop()
            ) {
                return i;
            }
        }
        return -1;
    }
}
