package it.polito.mad.insane.lab3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.mad.insane.lab3.adapters.MySpinnerAdapterHome;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.data.Restaurant;
import it.polito.mad.insane.lab3.adapters.RestaurantsRecyclerAdapter;
import it.polito.mad.insane.lab3.dBHandlers.RestaurateurJsonManager;
import it.polito.mad.insane.lab3.animations.SlideInOutLeftItemAnimator;

public class HomeConsumer extends AppCompatActivity {

    private static RestaurateurJsonManager manager = null;
    static final String PREF_NAME = "myPref";private SharedPreferences mPrefs = null;
    private List<Restaurant> listaFiltrata;
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
                    listaFiltrata = manager.getFilteredRestaurants(newText);
                    setUpRestaurantsRecycler(listaFiltrata);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) setUpRestaurantsRecycler(manager.getRestaurants());
                else {
                    listaFiltrata = manager.getFilteredRestaurants(newText);
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
                    setUpRestaurantsRecycler(manager.getOrderedRestaurants(dSpinner.getSelectedItem().toString(), listaFiltrata));
                }
            }
        });

        //set up ordering spinner
        setUpSpinner();  //fixme: succede che quando l'activity  è chiamata con extra devo solo inizializzare lo spinner,altrimenti già ordino per distanza/score

        //'clear filter
        this.mPrefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        if (mPrefs!=null) {
            SharedPreferences.Editor editor = this.mPrefs.edit();
            editor.clear();
            editor.commit();
        }
        // set up clean Recycler
        setUpRestaurantsRecycler(manager.getRestaurants());

        /*Intent intent = getIntent();
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
        }*/


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
    protected void onResume()
    {
        super.onResume();
        this.mPrefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        if(this.mPrefs != null)
        {
            //If coming from filter activity...
            listaFiltrata = manager.getAdvancedFilteredRestaurants(this.mPrefs.getString("distanceValue",""),this.mPrefs
                    .getString("priceValue",""),this.mPrefs.getString("typeValue",""),this.mPrefs.getString("timeValue",""));

            setUpRestaurantsRecycler(listaFiltrata);


        }else
        {
            //No filtering needed
            setUpRestaurantsRecycler(manager.getRestaurants());
        }

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

    private void setUpSpinner(){
        //TODO togliere order by e lasciare solo le due scelte distance e score nello spinner della home page
        final Spinner dSpinner = (Spinner) findViewById(R.id.orderSpinner);
        List<String> orderings = new ArrayList<>();
        Resources res = getResources();
        String[] dStrings = res.getStringArray(R.array.order_array);
        Collections.addAll(orderings, dStrings);
        MySpinnerAdapterHome dAdapter = new MySpinnerAdapterHome(HomeConsumer.this, R.layout.support_simple_spinner_dropdown_item,
                orderings, res);
        dAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dSpinner.setAdapter(dAdapter);

        dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if(position==1|| position==2){
                  //  setUpRestaurantsRecycler(manager.getOrderedRestaurants(dSpinner.getSelectedItem().toString()));
                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        dSpinner.setSelection(2);

    }
}
