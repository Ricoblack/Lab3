package it.polito.mad.insane.lab3.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.mad.insane.lab3.Adapters.MySpinnerAdapterFilter;
import it.polito.mad.insane.lab3.R;

public class FilterActivity extends AppCompatActivity {

    // TODO: usare le shared preferences per salvare i filtri impostati e modificarli

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up spinners
        setUpSpinners();

        //set up on click listeners of the buttons
        AppCompatButton reset= (AppCompatButton) findViewById(R.id.resetButton);
        AppCompatButton apply=(AppCompatButton) findViewById(R.id.applyButton);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPressed(v);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApplyPressed(v);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onResetPressed(View v){
        this.setUpSpinners();
    }

    public void onApplyPressed(View v){
        //read values of spinners, launches home activity with extras, and finally finishes

        Spinner dSpinner = (Spinner) findViewById(R.id.distance_filter);
        Spinner pSpinner = (Spinner) findViewById(R.id.price_filter);
        Spinner tSpinner = (Spinner) findViewById(R.id.type_restaurant_filter);
        Spinner tiSpinner = (Spinner) findViewById(R.id.lunch_time_filter);

        String distanceValue="";
        String priceValue="";
        String typeValue="";
        String timeValue="";

        if(dSpinner.getSelectedItemPosition()!=0) distanceValue=dSpinner.getSelectedItem().toString();
        if(pSpinner.getSelectedItemPosition()!=0) priceValue=pSpinner.getSelectedItem().toString();
        if(tSpinner.getSelectedItemPosition()!=0) typeValue=tSpinner.getSelectedItem().toString();
        if(tiSpinner.getSelectedItemPosition()!=0) timeValue=tiSpinner.getSelectedItem().toString();

        Intent i = new Intent(this,HomeConsumer.class);
        i.putExtra("distanceValue",distanceValue);
        i.putExtra("priceValue",priceValue);
        i.putExtra("typeValue",typeValue);
        i.putExtra("timeValue",timeValue);
        startActivity(i);
        finish();

    }

    private void setUpSpinners() {

        final Spinner dSpinner = (Spinner) findViewById(R.id.distance_filter);
        List<String> distances = new ArrayList<>();
        Resources res = getResources();
        String[] dStrings = res.getStringArray(R.array.distance_array);
        Collections.addAll(distances, dStrings);
        MySpinnerAdapterFilter dAdapter = new MySpinnerAdapterFilter(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item,
                distances,res);
        dAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dSpinner.setAdapter(dAdapter);


        final Spinner pSpinner = (Spinner) findViewById(R.id.price_filter);
        List<String> prices = new ArrayList<>();
        Resources res2 = getResources();
        String[] pStrings = res2.getStringArray(R.array.price_array);
        Collections.addAll(prices, pStrings);
        MySpinnerAdapterFilter pAdapter = new MySpinnerAdapterFilter(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item,
                prices,res2);
        pAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        pSpinner.setAdapter(pAdapter);

        final Spinner tSpinner = (Spinner) findViewById(R.id.type_restaurant_filter);
        List<String> types = new ArrayList<>();
        Resources res3 = getResources();
        String[] tStrings = res3.getStringArray(R.array.type_array);
        Collections.addAll(types, tStrings);
        MySpinnerAdapterFilter tAdapter = new MySpinnerAdapterFilter(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item,
                types,res3);
        tAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tSpinner.setAdapter(tAdapter);

        final Spinner tiSpinner = (Spinner) findViewById(R.id.lunch_time_filter);
        List<String> times = new ArrayList<>();
        Resources res4 = getResources();
        String[] tiStrings = res4.getStringArray(R.array.time_array);
        Collections.addAll(times, tiStrings);
        MySpinnerAdapterFilter tiAdapter = new MySpinnerAdapterFilter(FilterActivity.this, R.layout.support_simple_spinner_dropdown_item,
                times,res4);
        tiAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tiSpinner.setAdapter(tiAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,HomeConsumer.class);
        startActivity(i);
    }
}
