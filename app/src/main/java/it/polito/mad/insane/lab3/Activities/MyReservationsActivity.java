package it.polito.mad.insane.lab3.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import it.polito.mad.insane.lab3.adapters.ReservationsRecyclerAdapter;
import it.polito.mad.insane.lab3.data.Booking;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.dBHandlers.RestaurateurJsonManager;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // finish the RestaurantProfile activity if is not finished
        if(RestaurantProfile.RestaurantProfileActivity != null)
            RestaurantProfile.RestaurantProfileActivity.finish();

        setContentView(R.layout.activity_my_reservations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.reservation_recycler_view);
        if(rv != null)
        {
            RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance(this);
            List<Booking> bookingList = manager.getBookings();
            if(!bookingList.isEmpty())
            {
                TextView reservationMessage = (TextView) findViewById(R.id.no_reservation_message);
                reservationMessage.setVisibility(View.GONE);
                ReservationsRecyclerAdapter adapter = new ReservationsRecyclerAdapter(this, manager.getBookings());
                rv.setAdapter(adapter);

                if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                {
                    // 10 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        // 2 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,2);
                        rv.setLayoutManager(mGridLayoutManager);
                    }else
                    {
                        // 3 column
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
                        rv.setLayoutManager(mGridLayoutManager);
                    }

                } else if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                {
                    // 7 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        // 2 column
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,2);
                        rv.setLayoutManager(mGridLayoutManager);

                    }else
                    {
                        // 1 column
                        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
                        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                        rv.setLayoutManager(mLinearLayoutManagerVertical);

                    }
                }else {
                    //small and normal
                    // 1 column
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    rv.setLayoutManager(mLinearLayoutManagerVertical);
                }

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
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
