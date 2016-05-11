package it.polito.mad.insane.lab3.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import it.polito.mad.insane.lab3.Activities.MakeReservationActivity;
import it.polito.mad.insane.lab3.Activities.MyReservationsActivity;
import it.polito.mad.insane.lab3.Data.Booking;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.DBHandlers.RestaurateurJsonManager;

/**
 * Created by Renato on 10/05/2016.
 */
public class BookingsRecyclerAdapter extends RecyclerView.Adapter<BookingsRecyclerAdapter.BookingsViewHolder>{

    private final Context context;
    private List<Booking> mData; //actual data to be displayed
    private LayoutInflater mInflater;

    public BookingsRecyclerAdapter(Context context, List<Booking> data){
        this.context = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.mInflater.inflate(R.layout.reservation_list_item, parent, false);

        BookingsViewHolder holder = new BookingsViewHolder(view); // create the holder
        return holder;
    }

    @Override
    public void onBindViewHolder(BookingsViewHolder holder, int position) {
        Booking currentObj = this.mData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class BookingsViewHolder extends RecyclerView.ViewHolder {

//        private ImageView restaurantPhoto; //TODO implementare selezione immagini
        private TextView restaurantName;
        private TextView ID;
        private TextView date;
        private TextView hour;
        private TextView nItems;
        private TextView totalPrice;
        private View view;
        private ImageView trash;
        private RestaurateurJsonManager manager;

        public BookingsViewHolder(View itemView) {
            super(itemView);
            this.manager=RestaurateurJsonManager.getInstance(null);
            this.restaurantName = (TextView) itemView.findViewById(R.id.reservation_cardview_restaurant_name);
            this.ID = (TextView) itemView.findViewById(R.id.reservation_cardview_ID);
            this.date = (TextView) itemView.findViewById(R.id.reservation_cardview_date);
            this.hour = (TextView) itemView.findViewById(R.id.reservation_cardview_hour);
            this.nItems = (TextView) itemView.findViewById(R.id.reservation_cardview_nItems);
            this.totalPrice = (TextView) itemView.findViewById(R.id.reservation_cardview_price);
            this.trash = (ImageView) itemView.findViewById(R.id.delete_reservation);
            this.view=itemView;


            this.trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: va gestita l'eliminazione e relativo invalidate della listview (va copiata la cancellazione dell'item dall'adapter dal lab2)
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.delete_reservation_alert_title))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    manager.deleteReservation(ID.getText().toString());
                                    Intent i = new Intent(context,MyReservationsActivity.class);
                                    AppCompatActivity act=(AppCompatActivity)context;
                                    act.startActivity(i);
                                    act.finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    Dialog dialog = builder.create();
                    dialog.show();
                }
            });

        }

        public void setData(Booking current, int position) {
            RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance(context);
            restaurantName.setText(manager.getRestaurant(current.getRestaurantID()).getProfile().getRestaurantName());
            ID.setText(current.getID());
            nItems.setText(MessageFormat.format("{0} dishes", current.getDishes().size()));
            DecimalFormat df = new DecimalFormat("0.00");
            totalPrice.setText(MessageFormat.format("{0}€", String.valueOf(df.format(current.getTotalPrice()))));
            Calendar calendar = current.getDate_time();
            hour.setText(MessageFormat.format("{0}:{1}", calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)));
            date.setText(MessageFormat.format("{0}/{1}/{2}", pad(calendar.get(Calendar.DAY_OF_MONTH)),
                    pad(calendar.get(Calendar.MONTH) + 1), pad(calendar.get(Calendar.YEAR))));
        }

        private String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }
    }
}
