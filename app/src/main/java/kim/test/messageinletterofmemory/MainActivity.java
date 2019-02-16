package kim.test.messageinletterofmemory;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    String message ="녹음시작";
    int count = 0;
    private Toast mToast;
    private Context mContext;
    private TextView recordTimerText;
    private RecordManager recordManager;
    private boolean isRecording = false;
    RecordHandler mHandler = new RecordHandler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIMER_WORKING://타이머가 시작되었을 때
                    Resources res = getResources();
                    String timerText = res.getString(R.string.timer_text,(int)msg.obj);
                    recordTimerText.setText(timerText);
                    Log.d("Handler", timerText);
                    break;
                case TIMER_END://타이머가 종료되었을 때
                case RECORD_STOP://녹음 중지 버튼을 눌렀을 때
                    recordManager.recordStop();
                    //showDialog("녹음이 종료되었습니다.");
                    break;
                case RECORD_START://녹음 시작 버튼을 눌렀을 때
                    recordManager.recordStart();
                    break;
                case RECORD_END://녹음 종료
                    showDialog(msg.obj+"이 저장되었습니다");
                    recordTimerText.setText("");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton recordButton = findViewById(R.id.record_button);
        recordTimerText = findViewById(R.id.record_timer);
        recordButton.setOnClickListener(this);
        mContext = getApplicationContext();
        recordManager = new RecordManager(mHandler);
        //하드웨어가 마이크를 지원하는지 체크
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            showDialog("녹음 기능이 지원되지 않는 기기입니다.");
        }
        //권한승인여부 확인후 메시지 띄워줌(둘 중 하나라도)
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.record_button://녹음 시작, 녹음 시작 중 누르면 녹음 중지
//                count++;
                if(!isRecording){
                    mHandler.sendEmptyMessage(RecordHandler.RECORD_START);
                    isRecording = true;
                    message = "녹음시작";
                }else{
                    mHandler.sendEmptyMessage(RecordHandler.RECORD_STOP);
                    isRecording = false;
                    message = "녹음종료";
                }
                showToast(mContext, message);
                break;
        }
    }
    public void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("메시지 확인");
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void showToast(Context context, String message){
        if(mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else {
            mToast.cancel();
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
