package com.example.bookstore_app.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.example.bookstore_app.models.stats.*;

import java.util.List;

public class SimpleAdapter extends BaseAdapter {

    Context context;
    List<?> list;

    public SimpleAdapter(Context context, List<?> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int i) { return list.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView t1 = view.findViewById(android.R.id.text1);
        TextView t2 = view.findViewById(android.R.id.text2);

        Object obj = list.get(i);

        if (obj instanceof UserStat) {
            UserStat u = (UserStat) obj;
            t1.setText(u.getUsername());
            t2.setText("💰 " + u.getTotalSpent());
        }

        if (obj instanceof CategoryStat) {
            CategoryStat c = (CategoryStat) obj;
            t1.setText(c.getCategoryName());
            t2.setText("📦 " + c.getTotalSold());
        }

        return view;
    }
}