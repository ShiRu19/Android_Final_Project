package t4.sers.placeholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import t4.sers.R;
import t4.sers.adapter.CourseTableAdapter;
import t4.sers.adapter.SchoolConfirmTableAdapter;


public class SchoolConfirmTableHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView confirmCaseID;
    public final TextView confirmCaseUUID;
    public final TextView confirmCaseStudentClass;
    public final TextView confirmCaseDate;
    public final LinearLayout confirmTableRow;

    final SchoolConfirmTableAdapter mAdapter;

    public SchoolConfirmTableHolder(@NonNull View itemView, SchoolConfirmTableAdapter adapter, Context context) {
        super(itemView);
        confirmCaseID = itemView.findViewById(R.id.student_confirm_index);
        confirmCaseUUID = itemView.findViewById(R.id.student_confirm_uuid);
        confirmCaseStudentClass = itemView.findViewById(R.id.student_confirm_school_class);
        confirmCaseDate = itemView.findViewById(R.id.student_confirm_date);
        confirmTableRow = itemView.findViewById(R.id.school_confirm_table_row);
        mAdapter = adapter;
    }

    @Override
    public void onClick(View view) {

    }
}
