package kim.test.messageinletterofmemory;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TimerThread extends Thread {
    int sec;
    private RecordHandler mHandler;
    public TimerThread(Handler mHandler){
        this.mHandler = (RecordHandler) mHandler;
    }
    public void setTimerSecond(int sec){
        this.sec = sec;
    }

    @Override
    public void run() {
        while(sec>0){
            try {
                Log.d("Timer",sec+"");
                Message message = mHandler.obtainMessage(RecordHandler.TIMER_WORKING,sec);
                mHandler.sendMessage(message);
                Thread.sleep(1000);
                sec--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessage(RecordHandler.TIMER_END);
    }
}
