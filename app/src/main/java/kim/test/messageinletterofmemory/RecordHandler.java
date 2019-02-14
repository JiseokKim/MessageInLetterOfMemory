package kim.test.messageinletterofmemory;

import android.os.Handler;

public class RecordHandler extends Handler {
    static final int TIMER_WORKING = 1;
    static final int TIMER_END = 2;
    static final int RECORD_STOP = 3;
    static final int RECORD_START = 4;
    static final int RECORD_END = 5;

    public RecordHandler(){

    }
}
