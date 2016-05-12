package it.polito.mad.insane.lab3.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import it.polito.mad.insane.lab3.Adapters.ReservationsRecyclerAdapter;
import it.polito.mad.insane.lab3.Data.Booking;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.DBHandlers.RestaurateurJsonManager;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.reservation_recycler_view);
        if(rv != null){
            RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance(this);
            List<Booking> bookingList = manager.getBookings();
            if(!bookingList.isEmpty())
            {
                TextView reservationMessage = (TextView) findViewById(R.id.no_reservation_message);
                reservationMessage.setVisibility(View.GONE);
                ReservationsRecyclerAdapter adapter = new ReservationsRecyclerAdapter(this, manager.getBookings());
                rv.setAdapter(adapter);
                LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
                mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(mLinearLayoutManagerVertical);
                rv.setItemAnimator(new DefaultItemAnimator());
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Fix Portrait Mode
        if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL ||
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                finish();  //FIXME: non basta fare così, se torno indietro riapre l'activity per prenotare con i piatti selezionati ma crasha tutto perchè in memoria non c'è nulla, credo debba essere fixato nell'onresume della RestaurantProfile
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
