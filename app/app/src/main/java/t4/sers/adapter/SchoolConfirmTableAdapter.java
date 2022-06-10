package t4.sers.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import t4.sers.R;
import t4.sers.placeholder.SchoolConfirmTableHolder;

public class SchoolConfirmTableAdapter extends RecyclerView.Adapter<SchoolConfirmTableHolder> {

    private final LayoutInflater mInflater;
    private final List<String> index;
    private final List<String> uuid;
    private final List<String> studentClass;
    private final List<String> date;
    private final Context context;

    public SchoolConfirmTableAdapter(Context context, List<String> index, List<String> uuid, List<String> studentClass, List<String> date){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.index = index;
        this.uuid = uuid;
        this.studentClass = studentClass;
        this.date = date;
    }

    @NonNull
    @Override
    public SchoolConfirmTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.school_confirm_table_holder, parent, false);
        return new SchoolConfirmTableHolder(mItemView, this, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull SchoolConfirmTableHolder holder, int position) {
        holder.confirmCaseID.setText(index.get(position));
        holder.confirmCaseUUID.setText(uuid.get(position));
        holder.confirmCaseStudentClass.setText(studentClass.get(position));
        Date current = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(sdf.format(current).equals(date.get(position))) {
            holder.confirmTableRow.setBackgroundColor(context.getColor(R.color.yellow_200));
        }
        holder.confirmCaseDate.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return index.size();
    }
}
