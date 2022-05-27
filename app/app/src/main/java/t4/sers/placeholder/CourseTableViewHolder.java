package t4.sers.placeholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import t4.sers.R;
import t4.sers.adapter.CourseTableAdapter;

public class CourseTableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView courseCode;
    public final TextView courseName;
    public final TextView courseRemote;

    public int index;

    final CourseTableAdapter mAdapter;


    public CourseTableViewHolder(@NonNull View itemView, CourseTableAdapter adapter, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);
        courseCode = itemView.findViewById(R.id.course_table_code);
        courseName = itemView.findViewById(R.id.course_table_name);
        courseRemote = itemView.findViewById(R.id.course_table_remote);
        mAdapter = adapter;
    }

    @Override
    public void onClick(View view) {

    }
}
