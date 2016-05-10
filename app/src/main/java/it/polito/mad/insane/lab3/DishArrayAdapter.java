package it.polito.mad.insane.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renato on 10/05/2016.
 */
public class DishArrayAdapter extends ArrayAdapter<Dish> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Integer> quantities;
    private List<Dish> data = null;

    public DishArrayAdapter(Context context, int layoutResourceId, List<Dish> data, ArrayList<Integer> quantities) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.quantities = quantities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DishHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((MakeReservation) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DishHolder();
            holder.name = (TextView) row.findViewById(R.id.summary_dish_name);
            holder.quantity = (TextView) row.findViewById(R.id.summary_dish_quantity);
            holder.totalPrice = (TextView) row.findViewById(R.id.summary_dish_total_price);

            row.setTag(holder);
        } else {
            holder = (DishHolder) row.getTag();
        }

        Dish dish = data.get(position);
        holder.name.setText(dish.getName());
        holder.quantity.setText(MessageFormat.format("{0}x", String.valueOf(quantities.get(position))));
        DecimalFormat df = new DecimalFormat("0.00");
        holder.totalPrice.setText(MessageFormat.format("{0}€", String.valueOf(df.format(dish.getPrice() * quantities.get(position)))));

        return row;
    }

    static class DishHolder
    {
        private TextView quantity;
        private TextView name;
        private TextView totalPrice;
    }
}
