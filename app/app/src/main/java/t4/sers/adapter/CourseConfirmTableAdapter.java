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

public class CourseConfirmTableAdapter extends RecyclerView.Adapter<CourseConfirmTableHolder> {

    private final LayoutInflater mInflater;
    private final List<String> code;
    private final List<String> name;
    private final List<String> teacher;
    private final List<String> date;
    private final Context context;

    public CourseConfirmTableAdapter(Context context, List<String> code, List<String> name, List<String> teacher, List<String> date){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.code = code;
        this.name = name;
        this.date = date;
        this.teacher = teacher;
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
        holder.confirmCourseCode.setText(code.get(position));
        holder.confirmCourseName.setText(name.get(position));
        holder.confirmCourseTeacher.setText(teacher.get(position));
        holder.confirmCourseDate.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return code.size();
    }
}
