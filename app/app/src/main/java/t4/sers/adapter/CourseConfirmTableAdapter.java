package t4.sers.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t4.sers.R;
import t4.sers.placeholder.CourseConfirmTableHolder;
import t4.sers.util.ConfirmCourse;

public class CourseConfirmTableAdapter extends RecyclerView.Adapter<CourseConfirmTableHolder> {

    private final LayoutInflater mInflater;
    private final List<ConfirmCourse> list;
    private final Context context;

    public CourseConfirmTableAdapter(Context context, List<ConfirmCourse> list){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CourseConfirmTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_confirm_table_holder, parent, false);
        return new CourseConfirmTableHolder(mItemView, this, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CourseConfirmTableHolder holder, int position) {
        holder.confirmCourseCode.setText(list.get(position).getCode());
        holder.confirmCourseName.setText(list.get(position).getName());
        holder.confirmCourseTeacher.setText(list.get(position).getTeacher());
        holder.confirmCourseDate.setText(list.get(position).getDurationTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
