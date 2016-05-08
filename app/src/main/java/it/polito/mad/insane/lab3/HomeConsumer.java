package it.polito.mad.insane.lab3;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

public class HomeConsumer extends AppCompatActivity {

    private static RestaurateurJsonManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeConsumer.manager = RestaurateurJsonManager.getInstance(this);
        setContentView(R.layout.activity_home_consumer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView sv = (SearchView) findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
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



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras==null){
            //No filtering needed
            setUpRestaurantsRecycler(manager.getRestaurants());

        }
        else {
            //If coming from filter activity...
            List<Restaurant> listaFiltrata = manager.getAdvancedFilteredRestaurants(extras.getString("distanceValue"),extras
            .getString("priceValue"),extras.getString("typeValue"),extras.getString("timeValue"));

            setUpRestaurantsRecycler(listaFiltrata);
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

        finish();



        return super.onOptionsItemSelected(item);
    }

    private void setUpRestaurantsRecycler(List<Restaurant> restaurants)
    {
        RecyclerView rV = (RecyclerView) findViewById(R.id.RestaurateurRecyclerView);

        RestaurantsRecyclerAdapter adapter = new RestaurantsRecyclerAdapter(this, restaurants);
        rV.setAdapter(adapter);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,2);
        rV.setLayoutManager(mGridLayoutManager);

        // Set animation
        RecyclerView.ItemAnimator ia = new DefaultItemAnimator();  // If you don't apply other animations it uses the default one
        rV.setItemAnimator(ia);
    }
}
