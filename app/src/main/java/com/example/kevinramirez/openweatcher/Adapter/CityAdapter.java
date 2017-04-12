package com.example.kevinramirez.openweatcher.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.kevinramirez.openweatcher.Model.City;

import java.util.ArrayList;

/**
 * Created by william.montiel on 4/12/2017.
 */

public class CityAdapter extends ArrayAdapter<City> {

    private ArrayList<City> values;
    private ArrayList<City> itemsAll;
    private ArrayList<City> suggestions;

    public CityAdapter(Context context, int viewResourceId, ArrayList<City> values) {
        super(context, viewResourceId, values);
        this.values = values;
        this.itemsAll = (ArrayList<City>) values.clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public City getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getNameCity());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getNameCity());

        return label;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((City)(resultValue)).getNameCity();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (City client : itemsAll) {
                    if(client.getNameCity().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(client);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<City> filteredList = (ArrayList<City>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (City c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
