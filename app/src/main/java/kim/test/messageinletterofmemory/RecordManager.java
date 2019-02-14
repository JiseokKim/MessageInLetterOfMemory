package kim.test.messageinletterofmemory;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.IOException;

public class RecordManager extends Thread {
    private MediaRecorder recorder;
    private RecordHandler mHandler;
    private File file;
    private String fileName;
    private TimerThread recodeTimer;
    private final int recordTimeSec = 15;
    public RecordManager(Handler mHandler){
        this.mHandler = (RecordHandler) mHandler;
        recodeTimer = new TimerThread(mHandler);
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
                fileName = "Record_Test.3gp";
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
            mHandler.sendEmptyMessage(RecordHandler.RECORD_END);
        }
    }
}
