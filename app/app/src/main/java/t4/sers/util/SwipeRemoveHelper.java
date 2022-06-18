package t4.sers.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import t4.sers.R;

public class SwipeRemoveHelper extends ItemTouchHelper.SimpleCallback {

    Drawable background;
    Drawable xMark;
    int xMarkMargin;
    boolean initiated;

    public SwipeRemoveHelper() {
        super(0, ItemTouchHelper.RIGHT);
    }

    private void init(Context context) {
        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_circle_24);
        if(xMark != null) {
            xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        xMarkMargin = (int) context.getResources().getDimension(R.dimen.swipe_remove_icon_margin);
        initiated = true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        SwipeRemoveAdapter adapter = (SwipeRemoveAdapter) viewHolder.getBindingAdapter();
        if(adapter != null) {
            adapter.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        if (viewHolder.getBindingAdapterPosition() == -1) {
            return;
        }

        if (!initiated) {
            init(recyclerView.getContext());
        }

        // draw red background
        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getLeft() + xMarkMargin;
        int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
