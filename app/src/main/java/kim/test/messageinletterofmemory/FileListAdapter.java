package kim.test.messageinletterofmemory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ContentViewHolder> {
    private static final String TAG = "FileListAdapter";
    private ArrayList<String> files;
    public FileListAdapter(ArrayList files) {
        this.files = new ArrayList();
        if(files == null){
            //this.files.add(0,"현재 파일이 없습니다");
        }else {
            this.files = files;
        }
        Log.d(TAG, "list size: "+ files.size());
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_list_view, viewGroup, false);
        Log.d(TAG, "view create: "+ i);
        return new ContentViewHolder(view);
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder contentViewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        contentViewHolder.fileNameTextView.setText(files.get(i));
        contentViewHolder.recipientTextView.setText(""+i);
        Log.d(TAG, "view itmes: "+ i);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "called getItemCount");
        return files.size();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ContentViewHolder extends RecyclerView.ViewHolder{
        private TextView fileNameTextView;
        private TextView reservationDateTextView;
        private TextView recipientTextView;
        private ImageButton editButton;
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.file_name);
            reservationDateTextView = itemView.findViewById(R.id.reservation_date);
            recipientTextView = itemView.findViewById(R.id.recipient);
            editButton = itemView.findViewById(R.id.file_edit_btn);
        }
    }
}
