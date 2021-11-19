package au.edu.federation.itech3106.drawboardandpiano;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE ="" ;
    private Button mBtnlogin1;
    private Button mBtnlogin2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnlogin1=findViewById(R.id.drawboard);
        mBtnlogin2=findViewById(R.id.piano);
        mBtnlogin1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent(MainActivity.this , DrawBoardMain.class);
                //Start
                String message = "30393065 Jiawei Xin";
                i.putExtra(EXTRA_MESSAGE, message);
                startActivity(i);
            }
        });
        mBtnlogin2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                click2(v);

            }
        });
        Log.e("cwj", " intel SD card path = " + Environment.getExternalStorageDirectory());
        Log.e("cwj", "intel storge = " + getFilesDir());
        File directory_pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        Log.e("0000", "directory_pictures="+directory_pictures);
    }

    //AlertDialog
    public void click2(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select want you want!");
        final String items[] = {"Piano", "Flute", "Xylophone"};
        //-1 : means not select
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1.selectd it
                String item = items[which];
                //2.close the dialog
                dialog.dismiss();
                String message = "null";
                Intent i = new Intent(MainActivity.this , PianoMain.class);
                switch (item) {
                    case "Piano":
                        message = "0";
                        break;
                    case "Flute":
                        message = "1";
                        break;
                    case "Xylophone":
                        message = "2";
                        break;
                    default:
                        Log.e("1221", "error");
                }
                //start
                Log.e("1299-3",message);
                i.putExtra(EXTRA_MESSAGE, message);
                startActivity(i);
            }
        });
        builder.show();
    }
}