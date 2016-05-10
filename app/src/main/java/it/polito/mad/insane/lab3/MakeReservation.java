package it.polito.mad.insane.lab3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSException;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MakeReservation extends AppCompatActivity {

    private static Calendar reservationDate = null;
    private static RestaurateurJsonManager manager = null;
    private static String restaurantId;
    private static String additionalNotes = "";
    private static double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        int[] quantities = bundle.getIntArray("selectedQuantities");
        restaurantId = bundle.getString("ID");

        manager = RestaurateurJsonManager.getInstance(this);
        List<Dish> dishes = manager.getRestaurant(restaurantId).getDishes();

        final List<Dish> dishesToDisplay = new ArrayList<>();
        ArrayList<Integer> quantitiesToDisplay = new ArrayList<>();
        totalPrice = 0;
        for(int i=0; i < dishes.size(); i++){
            if(quantities[i] != 0){
                dishesToDisplay.add(dishes.get(i));
                quantitiesToDisplay.add(quantities[i]);
                totalPrice += dishes.get(i).getPrice() * quantities[i];
            }
        }
        TextView tv = (TextView) findViewById(R.id.reservation_total_price);
        DecimalFormat df = new DecimalFormat("0.00");
        if (tv != null) {
            tv.setText(MessageFormat.format("{0}€", String.valueOf(df.format(totalPrice))));
        }

        tv = (TextView) findViewById(R.id.reservation_restaurant_name);
        if (tv != null) {
            tv.setText(manager.getRestaurant(restaurantId).getProfile().getRestaurantName());
        }

        DishArrayAdapter adapter = new DishArrayAdapter(this, R.layout.dish_listview_item, dishesToDisplay, quantitiesToDisplay);

        ListView mylist = (ListView) findViewById(R.id.reservation_dish_list);
        if (mylist != null) {
            mylist.setAdapter(adapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakeReservation.this);
                    builder.setTitle(MakeReservation.this.getResources().getString(R.string.alert_title))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    saveReservation(dishesToDisplay);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveReservation(List<Dish> dishesToDisplay) {
        Booking b = new Booking();
        b.setDate_time(reservationDate);
        b.setDishes(dishesToDisplay);
        b.setID("001"); //FIXME bisogna creare un campo ID nel db da incrementare ogni volta?
        b.setRestaurantID(restaurantId);
        b.setTotalPrice(totalPrice);
        EditText et = (EditText) findViewById(R.id.reservation_additional_notes);
        if(et != null){
            additionalNotes = et.getText().toString();
            b.setNote(additionalNotes);
        }
        //TODO decrementare le available quantity dei piatti

        manager.getBookings().add(b);
        manager.saveDbApp(); //TODO salvare la prenotazione nel DB
        reservationDate = null;
        additionalNotes = "";
        totalPrice = 0;
        finish();
        Intent intent = new Intent(MakeReservation.this, MyReservations.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(reservationDate != null) {
            Button button = (Button) findViewById(R.id.reservation_hour);
            if (button != null) {
                button.setText(new StringBuilder().append(pad(reservationDate.get(Calendar.HOUR_OF_DAY)))
                        .append(":").append(pad(reservationDate.get(Calendar.MINUTE))));
            }

            button = (Button) findViewById(R.id.reservation_date);
            if (button != null) {
                button.setText(new StringBuilder().append(pad(reservationDate.get(Calendar.DAY_OF_MONTH))).
                        append("/").append(pad(reservationDate.get(Calendar.MONTH) + 1))
                        .append("/").append(reservationDate.get(Calendar.YEAR)));
            }
        }
        if(!additionalNotes.equals("")) {
            EditText et = (EditText) findViewById(R.id.reservation_additional_notes);
            if (et != null) {
                et.setText(additionalNotes);
            }
        }

    }

    public void showTimePickerDialog(View view) {

        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "reservationTimePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "reservationDatePicker");
    }

    public void setHour(int hourOfDay, int minute){

        reservationDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reservationDate.set(Calendar.MINUTE, minute);
        Button button = (Button) findViewById(R.id.reservation_hour);
        if (button != null) {
            button.setText(new StringBuilder().append(pad(hourOfDay))
                    .append(":").append(pad(minute)));
        }

    }

    private void setDate(int year, int month, int day) {
        reservationDate = Calendar.getInstance();
        reservationDate.set(Calendar.YEAR, year);
        reservationDate.set(Calendar.MONTH, month);
        reservationDate.set(Calendar.DAY_OF_MONTH, day);
        Button button = (Button) findViewById(R.id.reservation_date);
        if (button != null) {
            button.setText(new StringBuilder().append(pad(day)).append("/").append(pad(month + 1)).append("/").append(year));
        }
    }

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }




    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity())){
            };
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((MakeReservation) getActivity()).setHour(hourOfDay, minute);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((MakeReservation)getActivity()).setDate(year,month,day);
        }
    }
}
