package com.example.nortonwei.skycar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nortonwei.skycar.Model.Contact;
import com.example.nortonwei.skycar.R;

import java.util.ArrayList;

public class FrequentPassengerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact> contactsList;

    public FrequentPassengerListAdapter(Context context, ArrayList<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Contact getItem(int position) {
        return contactsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_frequent_passenger_item, parent, false);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name_textView);
        TextView numberTextView = (TextView) convertView.findViewById(R.id.number_textView);

        nameTextView.setText(contactsList.get(position).getName());
        numberTextView.setText(contactsList.get(position).getMobile());

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("name", contactsList.get(position).getName());
            intent.putExtra("mobile", contactsList.get(position).getMobile());
            ((Activity) context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();
        });

        return convertView;
    }
}
