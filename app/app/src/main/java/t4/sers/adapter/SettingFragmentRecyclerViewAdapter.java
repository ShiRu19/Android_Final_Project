package t4.sers.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import t4.sers.databinding.FragmentSettingBinding;
import t4.sers.placeholder.PlaceholderContent.PlaceholderItem;
import t4.sers.util.SwipeRemoveAdapter;


public class SettingFragmentRecyclerViewAdapter extends SwipeRemoveAdapter {

    @Override
    public void remove(int position) {
        PlaceholderItem placeholderItem = mValues.get(position);
        mValues.remove(placeholderItem);
    }

    public interface OnItemClickHandler {
        void onItemClick(String text);
    }

    private final List<PlaceholderItem> mValues;
    private final Context context;
    private final OnItemClickHandler clickHandler;

    public SettingFragmentRecyclerViewAdapter(Context context, List<PlaceholderItem> items, OnItemClickHandler onItemClickHandler) {
        this.context = context;
        mValues = items;
        clickHandler = onItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentSettingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingFragmentRecyclerViewAdapter.ViewHolder viewHolder = (SettingFragmentRecyclerViewAdapter.ViewHolder) holder;
        viewHolder.mItem = mValues.get(position);
        viewHolder.mImageView.setImageDrawable(mValues.get(position).icon);
        viewHolder.mImageView.setColorFilter(mValues.get(position).color);
        viewHolder.mContentView.setText(mValues.get(position).content);
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

            binding.getRoot().setOnClickListener(view -> {
                clickHandler.onItemClick(mContentView.getText().toString());
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}