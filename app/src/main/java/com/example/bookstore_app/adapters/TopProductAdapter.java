package com.example.bookstore_app.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.example.bookstore_app.models.stats.TopProduct;

import java.util.List;

public class TopProductAdapter extends ArrayAdapter<TopProduct> {

    private Context context;
    private List<TopProduct> list;

    public TopProductAdapter(Context context, List<TopProduct> list) {
        super(context, android.R.layout.simple_list_item_2, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        TopProduct item = list.get(position);

        text1.setText(item.getBookName());
        text2.setText("Đã bán: " + item.getTotalSold());

        return convertView;
    }
}