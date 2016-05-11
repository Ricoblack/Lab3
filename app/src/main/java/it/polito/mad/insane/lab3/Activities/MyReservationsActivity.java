package it.polito.mad.insane.lab3.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import it.polito.mad.insane.lab3.Adapters.ReservationsRecyclerAdapter;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.DBHandlers.RestaurateurJsonManager;

public class MyReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: gestire il pulsante indietro e la freccia sulla toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView) findViewById(R.id.reservation_recycler_view);

        if(rv != null){
            RestaurateurJsonManager manager = RestaurateurJsonManager.getInstance(this);
            ReservationsRecyclerAdapter adapter = new ReservationsRecyclerAdapter(this, manager.getBookings());
            rv.setAdapter(adapter);
            LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
            mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(mLinearLayoutManagerVertical);
            rv.setItemAnimator(new DefaultItemAnimator());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
