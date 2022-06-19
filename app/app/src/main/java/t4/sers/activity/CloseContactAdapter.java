package t4.sers.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t4.sers.R;

public class CloseContactAdapter extends RecyclerView.Adapter<CloseContactAdapter.ViewHolder> {

    private List<String> mData;

    CloseContactAdapter(List<String> data) {
        mData = data;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtItem1;
        private Button btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            txtItem1 = (TextView) itemView.findViewById(R.id.txtItem1);
            btnRemove = (Button) itemView.findViewById(R.id.btnRemove);
            // 點擊項目時
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "click " +getAbsoluteAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
            // 點擊項目中的Button時
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //刪除功能
                    removeItem(getAbsoluteAdapterPosition());
                }

            });
        }
    }
    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_close_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 設置txtItem1要顯示的內容
        holder.txtItem1.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}