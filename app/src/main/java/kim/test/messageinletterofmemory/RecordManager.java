package kim.test.messageinletterofmemory;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordManager extends Thread {
    private MediaRecorder recorder;
    private RecordHandler mHandler;
    private File file;
    private String fileName;
    private TimerThread recodeTimer;
    private final int recordTimeSec = 15;
    public RecordManager(Handler mHandler){
        this.mHandler = (RecordHandler) mHandler;
    }
    public void run(){

    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    private void setRecorderInitialize(){
        try {
            //지정된 파일이름이 없으면 현재시간으로 파일이름 지정
            if (fileName == null) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");//시간표시 포멧 지정
                fileName = "record_"+dateFormat.format(date)+".3gp";
            }
            file = Environment.getExternalStorageDirectory();
            String path = file.getAbsolutePath() +"/Download/"+fileName;
            recorder = new MediaRecorder();
            //녹음할 오디오 소스는 VOIP설정을 적용한다.
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            //녹음 파일 타입을 3gp로 설정한다
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //녹음할 파일의 인코더 속성 설정 AAC or AMR
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            recorder.setOutputFile(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void recordStart(){
        try {
            setRecorderInitialize();
            recorder.prepare();//녹음 준비 완료되면
            recorder.start();//녹음 시작
            if(recodeTimer == null) {
                recodeTimer = new TimerThread(mHandler);
            }
            recodeTimer.setTimerSecond(recordTimeSec);
            recodeTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void recordStop(){
        if(recorder != null) {
            recorder.stop();//녹음 중단
            recorder.release();//녹음에 할당된 메모리 해제
            recorder = null;
            if(recodeTimer!=null){
                recodeTimer.setTimerSecond(-1);//타이머 강제종료
                recodeTimer = null;
            }
            Message message = mHandler.obtainMessage(RecordHandler.RECORD_END, fileName);
            mHandler.sendMessage(message);
        }
    }
}
