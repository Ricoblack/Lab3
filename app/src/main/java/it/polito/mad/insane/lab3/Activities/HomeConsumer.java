package it.polito.mad.insane.lab3.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.mad.insane.lab3.Adapters.MySpinnerAdapterHome;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.Data.Restaurant;
import it.polito.mad.insane.lab3.Adapters.RestaurantsRecyclerAdapter;
import it.polito.mad.insane.lab3.DBHandlers.RestaurateurJsonManager;
import it.polito.mad.insane.lab3.Animations.SlideInOutLeftItemAnimator;

public class HomeConsumer extends AppCompatActivity {

    private static RestaurateurJsonManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeConsumer.manager = RestaurateurJsonManager.getInstance(this);
        setContentView(R.layout.activity_home_consumer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SearchView sv = (SearchView) findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                if(newText.equals("")) setUpRestaurantsRecycler(manager.getRestaurants());
                else {
                    List<Restaurant> listaFiltrata = manager.getFilteredRestaurants(newText);
                    setUpRestaurantsRecycler(listaFiltrata);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) setUpRestaurantsRecycler(manager.getRestaurants());
                else {
                    List<Restaurant> listaFiltrata = manager.getFilteredRestaurants(newText);
                    setUpRestaurantsRecycler(listaFiltrata);
                }

                return true;
            }
        });

        sv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sv.setIconified(false);
            }
        });

        AppCompatButton applyButton=(AppCompatButton) findViewById(R.id.applyOrdering);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner dSpinner = (Spinner) findViewById(R.id.orderSpinner);
                if(dSpinner.getSelectedItemPosition()==0) {
                    Toast.makeText(v.getContext(),"Seleziona un ordinamento",Toast.LENGTH_SHORT).show();
                }
                else {
                    setUpRestaurantsRecycler(manager.getOrderedRestaurants(dSpinner.getSelectedItem().toString()));
                }
            }
        });


        //TODO togliere order by e lasciare solo le due scelte distance e score
        final Spinner dSpinner = (Spinner) findViewById(R.id.orderSpinner);
        List<String> orderings = new ArrayList<>();
        Resources res = getResources();
        String[] dStrings = res.getStringArray(R.array.order_array);
        Collections.addAll(orderings, dStrings);
        MySpinnerAdapterHome dAdapter = new MySpinnerAdapterHome(HomeConsumer.this, R.layout.support_simple_spinner_dropdown_item,
                orderings, res);
        dAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dSpinner.setAdapter(dAdapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras==null){
            //No filtering needed
            setUpRestaurantsRecycler(manager.getRestaurants());

        }
        else {
            //If coming from filter activity...
            if (extras.getString("distanceValue") != null) {
                List<Restaurant> listaFiltrata = manager.getAdvancedFilteredRestaurants(extras.getString("distanceValue"),extras
                        .getString("priceValue"),extras.getString("typeValue"),extras.getString("timeValue"));

                setUpRestaurantsRecycler(listaFiltrata);
            }
            else {
                setUpRestaurantsRecycler(manager.getRestaurants());
            }
        }


        // Fix Portrait Mode
        if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL ||
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_consumer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_filtro) {
            Intent i = new Intent(this,FilterActivity.class);
            startActivity(i);
        }
        if(id == R.id.activity_reservations){
            Intent i = new Intent(this,MyReservationsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpRestaurantsRecycler(List<Restaurant> restaurants)
    {
        //TODO nel caso in cui l'app venga utilizzati su cell con schermo piccolo utilizzare il linearLayout, nel caso di schermi grandi(10 pollici non 7) utilizzare griglia a 3
        RecyclerView rV = (RecyclerView) findViewById(R.id.RestaurateurRecyclerView);

        RestaurantsRecyclerAdapter adapter = new RestaurantsRecyclerAdapter(this, restaurants);
        rV.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,2);
        rV.setLayoutManager(mGridLayoutManager);

        // Set animation
        RecyclerView.ItemAnimator ia = new SlideInOutLeftItemAnimator(rV);  // try animator //FIXME: doesn't work
        rV.setItemAnimator(ia);
    }
}
