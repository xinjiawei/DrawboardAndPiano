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
                //Intent是一种运行时绑定（run-time binding）机制，它能在程序运行过程中连接两个不同的组件。
                //page1为先前已添加的类，并已在AndroidManifest.xml内添加活动事件(<activity android:name="page1"></activity>),
                // 在存放资源代码的文件夹下下，

                Intent i = new Intent(MainActivity.this , DrawBoardMain.class);
                ////启动
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
        Log.e("cwj", "内置SD卡路径 = " + Environment.getExternalStorageDirectory());
        Log.e("cwj", "内置路径 = " + getFilesDir());
        //Log.e("cwj", "内置路径 = " + Context.getExternalFilesDir(dir).getAbsolutePath());
        File directory_pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        Log.e("0000", "directory_pictures="+directory_pictures);
    }

    //点击按钮弹出一个单选对话框
    public void click2(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select want you want!");
        final String items[] = {"Piano", "Flute", "Xylophone"};
        //-1代表没有条目被选中
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1.把选中的条目取出来
                String item = items[which];
                /*
                SharedPreferences sharedPreferences= getSharedPreferences("data2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                 */
                //Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
                //2.然后把对话框关闭
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
                ////启动
                Log.e("1299-3",message);
                i.putExtra(EXTRA_MESSAGE, message);
                startActivity(i);
            }
        });
        builder.show();
    }
}