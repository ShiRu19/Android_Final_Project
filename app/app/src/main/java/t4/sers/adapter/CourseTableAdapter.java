package t4.sers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t4.sers.R;
import t4.sers.placeholder.CourseTableViewHolder;

public class CourseTableAdapter extends RecyclerView.Adapter<CourseTableViewHolder> {

    private final LayoutInflater mInflater;
    private final List<String> courseCode;
    private final List<String> courseName;
    private final List<String> courseRemote;
    private final Context context;

    public CourseTableAdapter(Context context, List<String> courseCode, List<String> courseName, List<String> courseRemote){
        mInflater = LayoutInflater.from(context);
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseRemote = courseRemote;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_table_holder, parent, false);
        return new CourseTableViewHolder(mItemView, this, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseTableViewHolder holder, int position) {
        String code = courseCode.get(position);
        String name = courseName.get(position);
        String remote = courseRemote.get(position);
        holder.courseCode.setText(code);
        holder.courseName.setText(name);
        holder.courseRemote.setText(remote);
        holder.index = position;
    }

    @Override
    public int getItemCount() {
        return courseCode.size();
    }
}
