package it.polito.mad.insane.lab3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MyReservations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.reservation_recycler_view);
        //FIXME ci sono mille prenotazioni, c'e' qualcosa che non va
        if(rv != null){
            RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance(this);
            BookingsRecyclerAdapter adapter = new BookingsRecyclerAdapter(this, manager.getBookings());
            rv.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(mLinearLayoutManagerVertical);
            rv.setItemAnimator(new DefaultItemAnimator());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
