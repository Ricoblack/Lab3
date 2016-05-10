package it.polito.mad.insane.lab3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renato on 08/04/2016.
 */
public class MySpinnerAdapter extends ArrayAdapter<String>
{
    Context contextSpinner;
    List<String> choices = new ArrayList<>();


    public MySpinnerAdapter(Context context, int resource, List objects)
    {
        super(context, resource, objects);
        contextSpinner = context;
        choices = (List<String>)objects;
    }

    @Override
    public boolean isEnabled(int position){
        return position != 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
//        View view = super.getDropDownView(position, convertView, parent);
//        TextView tv = (TextView) view;
//        if(position == 0){
//            // Set the hint text color gray
//            tv.setTextColor(Color.GRAY);
//        }
//        else
//            tv.setTextColor(Color.BLACK);
//
//        return view;

        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {

        //return getCustomView(pos, cnvtView, prnt);
        if(pos == 0) {

            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner, prnt,
                    false);

            ImageView image = (ImageView) mySpinner.findViewById(R.id.spinner_image);
            image.setVisibility(View.INVISIBLE);

            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.name_option);
            main_text.setText(choices.get(pos));

            return mySpinner;
        }else{
            LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
            View mySpinner = inflater.inflate(R.layout.custom_spinner, prnt,
                    false);

            TextView main_text = (TextView) mySpinner.findViewById(R.id.name_option);
            main_text.setText(choices.get(pos));

            return mySpinner;
        }
    }
    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {


            if(position == 0) {

                LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
                View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
                        false);

                ImageView image = (ImageView) mySpinner.findViewById(R.id.spinner_image);
                image.setVisibility(View.INVISIBLE);

                TextView main_text = (TextView) mySpinner
                        .findViewById(R.id.name_option);
                main_text.setText(choices.get(position));

                return mySpinner;
            }else{
                LayoutInflater inflater = LayoutInflater.from(this.contextSpinner);
                View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
                        false);

                ImageView image = (ImageView) mySpinner.findViewById(R.id.spinner_image);
                image.setVisibility(View.INVISIBLE);

                TextView main_text = (TextView) mySpinner.findViewById(R.id.name_option);
                main_text.setText(choices.get(position));

                return mySpinner;
            }


    }
}
