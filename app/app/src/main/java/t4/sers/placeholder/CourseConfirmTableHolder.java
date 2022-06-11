package t4.sers.placeholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import t4.sers.R;
import t4.sers.adapter.CourseConfirmTableAdapter;


public class CourseConfirmTableHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView confirmCourseCode;
    public final TextView confirmCourseName;
    public final TextView confirmCourseTeacher;
    public final TextView confirmCourseDate;
    public final LinearLayout confirmTableRow;

    final CourseConfirmTableAdapter mAdapter;

    public CourseConfirmTableHolder(@NonNull View itemView, CourseConfirmTableAdapter adapter, Context context) {
        super(itemView);
        confirmCourseCode = itemView.findViewById(R.id.confirm_course_code);
        confirmCourseName = itemView.findViewById(R.id.confirm_course_name);
        confirmCourseTeacher = itemView.findViewById(R.id.confirm_course_teacher);
        confirmCourseDate = itemView.findViewById(R.id.confirm_course_date);
        confirmTableRow = itemView.findViewById(R.id.confirm_course_row);
        mAdapter = adapter;
    }

    @Override
    public void onClick(View view) {

    }
}
