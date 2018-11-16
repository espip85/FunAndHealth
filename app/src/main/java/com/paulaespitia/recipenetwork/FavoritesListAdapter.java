package com.paulaespitia.recipenetwork;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.halfbit.pinnedsection.PinnedSectionListView;

public class FavoritesListAdapter extends ArrayAdapter<FavoritesListAdapter.ListItem> implements PinnedSectionListView.PinnedSectionListAdapter, Filterable {

    private List<ListItem> originalData = null;
    private List<ListItem> filteredData = null;

    public FavoritesListAdapter(@NonNull Context context, int resource, @NonNull List<String> users, @NonNull List<String> recipes) {
        super(context, resource);
        add(new ListItem(ListItem.SECTION_HEADER, "Users"));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextColor(Color.DKGRAY);
        view.setTag("" + position);
        ListItem item = getItem(position);
        if (item.type == ListItem.SECTION_HEADER) {
            //view.setOnClickListener(PinnedSectionListActivity.this);
            view.setBackgroundColor(Color.GREEN);
        }
        return view;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == ListItem.SECTION_HEADER;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }

    public static class ListItem {
        public static final int SECTION_HEADER = 0;
        public static final int USER = 1;
        public static final int RECIPE = 2;

        public final int type;
        public final String text;

        public ListItem(int type, String text) {
            this.type = type;
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
