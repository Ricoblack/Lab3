package it.polito.mad.insane.lab3.Adapters;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad.insane.lab3.R;

/**
 * Created by Federico on 11/05/2016.
 */
public class MySpinnerAdapterHome extends ArrayAdapter<String> {
    Context contextSpinner;
    List<String> choices = new ArrayList<>();
    Resources resSpinnerAdapter;


    public MySpinnerAdapterHome(Context context, int resource, List objects, Resources res)
    {
        super(context, resource, objects);
        contextSpinner = context;
        choices = (List<String>)objects;
        this.resSpinnerAdapter = res;
    }

    @Override
    public boolean isEnabled(int position){
        return position != 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {

        if(pos == 0) {

            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner_home, prnt,
                    false);

            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.text_spinner_home);
            main_text.setText(choices.get(pos));

            return mySpinner;
        }else{
            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner_home, prnt,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_spinner_home);
            main_text.setText(choices.get(pos));

            return mySpinner;
        }
    }
    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        if(position == 0) {

            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner_home, parent,
                    false);

            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.text_spinner_home);
            main_text.setText(choices.get(position));
            main_text.setTextColor(resSpinnerAdapter.getColor(R.color.colorPrimary));
            main_text.setPadding(10,10,10,10);


            return mySpinner;
        }else{
            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner_home, parent,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_spinner_home);
            main_text.setText(choices.get(position));
            main_text.setTextColor(resSpinnerAdapter.getColor(R.color.black));
            main_text.setPadding(10,10,10,10);


            return mySpinner;
        }
    }
}
