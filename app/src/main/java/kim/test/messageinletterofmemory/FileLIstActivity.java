package kim.test.messageinletterofmemory;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileLIstActivity extends AppCompatActivity {
    static final String TAG ="FileListActivity";
    private RecyclerView fileListRecyclerView;
    private RecyclerView.Adapter fileListViewAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        fileListRecyclerView = findViewById(R.id.file_list_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        fileListRecyclerView.setLayoutManager(layoutManager);

        fileListViewAdapter = new FileListAdapter(getFileNameList(File.separator+"VoiceMemory"+File.separator, "^([\\S]+(\\.3gp)$)"));//정규표현식
        fileListRecyclerView.setAdapter(fileListViewAdapter);
        Log.d(TAG, "onCreate");
    }

    private ArrayList getFileNameList(String path, String fileNameFilterPattern){
        ArrayList<String> files = new ArrayList();
        File fileDir = new File(Environment.getExternalStorageDirectory(),path);
        if(!fileDir.exists()||!fileDir.isDirectory()){
            fileDir.mkdir();
            Log.d(TAG, "directory create:"+fileDir.getAbsolutePath());
        }
        String[] list = fileDir.list();
        if(list.length>0) {//list not null
            for (int i = 0; i < list.length; i++) {
                if (list[i].matches(fileNameFilterPattern)) {
                    files.add(list[i]);
                    Log.d(TAG, files.get(i).toString());
                }
            }
        }
        if(files.size() == 0){
            return null;
        }
        return files;
    }
}
