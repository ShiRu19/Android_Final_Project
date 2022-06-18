package t4.sers.util;

import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeRemoveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public abstract void remove(int position);
}
