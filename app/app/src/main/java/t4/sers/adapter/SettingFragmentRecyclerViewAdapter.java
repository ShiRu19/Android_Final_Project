package t4.sers.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t4.sers.R;
import t4.sers.databinding.FragmentSettingBinding;
import t4.sers.placeholder.PlaceholderContent.PlaceholderItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SettingFragmentRecyclerViewAdapter extends RecyclerView.Adapter<SettingFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;
    private final Context context;

    public SettingFragmentRecyclerViewAdapter(Context context, List<PlaceholderItem> items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentSettingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImageView.setImageDrawable(mValues.get(position).icon);
        holder.mImageView.setColorFilter(mValues.get(position).color);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentSettingBinding binding) {
            super(binding.getRoot());
            mImageView = binding.imageView3;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}