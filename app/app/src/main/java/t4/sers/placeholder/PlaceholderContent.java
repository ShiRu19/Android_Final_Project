package t4.sers.placeholder;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import t4.sers.R;
import t4.sers.activity.MainActivity;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<>();

    public static void setItems(Resources resources){
        ITEMS.clear();
        String[] itemText = resources.getStringArray(R.array.setting_text);
        TypedArray colors = resources.obtainTypedArray(R.array.setting_color);
        TypedArray icon = resources.obtainTypedArray(R.array.setting_icon);
        for(int i = 0; i < itemText.length; i++){
            Drawable drawable = icon.getDrawable(i);
            int color = colors.getColor(i, R.color.black);
            PlaceholderContent.addItem(PlaceholderContent.createPlaceholderItem(i, "test " + (i + 1), color, drawable));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceholderItem createPlaceholderItem(int position, String text, int color, Drawable drawable) {
        return new PlaceholderItem(String.valueOf(position), text, color, drawable);
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String content;
        public final int color;
        public final Drawable icon;

        public PlaceholderItem(String id, String content, int color, Drawable icon) {
            this.id = id;
            this.content = content;
            this.color = color;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}