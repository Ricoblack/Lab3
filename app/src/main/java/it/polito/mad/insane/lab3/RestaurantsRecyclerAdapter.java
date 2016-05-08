package it.polito.mad.insane.lab3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Federico on 03/05/2016.
 */
public class RestaurantsRecyclerAdapter extends RecyclerView.Adapter<RestaurantsRecyclerAdapter.RestaurantsViewHolder> {

    private List<Restaurant> mData;
    private LayoutInflater mInflater;

    //construttore
    public RestaurantsRecyclerAdapter(Context context, List<Restaurant> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * Method called when a ViewHolder Object is created
     * The returned object contains a reference to a view representing the bare structure of the item
     * @param parent
     * @param viewType
     */
    @Override
    public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create the view starting from XML layout file
        View view = this.mInflater.inflate(R.layout.restaurant_list_item,parent, false);

        RestaurantsViewHolder result = new RestaurantsViewHolder(view); // create the holder
        return result;
    }

    /**
     * Method called after onCreateViewHolder() and it fetches data from the model and properly sets view accordingly
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RestaurantsViewHolder holder, int position) {
        Restaurant currentObj = this.mData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        private View cardView;
        private TextView title;
        private TextView street;
        private String IDrestaurant;

        private android.view.View.OnClickListener cardViewListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(v.getContext(),"Cliccato sulla cardView", Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(),RestaurantProfile.class);
                i.putExtra("ID",IDrestaurant);
                v.getContext().startActivity(i);
            }
        };

        public RestaurantsViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView;
            this.title = (TextView) itemView.findViewById(R.id.restaurant_title);
            this.street = (TextView) itemView.findViewById(R.id.street_restaurant);

            // set the onClickListener to the View
            this.cardView.setOnClickListener(cardViewListener);

        }

        public void setData(Restaurant current, int position) {
            this.title.setText(current.getProfile().getRestaurantName());
            this.street.setText(current.getProfile().getAddress());
            this.IDrestaurant = current.getRestaurantID();
        }


    }
}