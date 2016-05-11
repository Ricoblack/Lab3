package it.polito.mad.insane.lab3.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import it.polito.mad.insane.lab3.Activities.RestaurantProfile;
import it.polito.mad.insane.lab3.Data.Dish;
import it.polito.mad.insane.lab3.R;

/**
 * Created by miche on 08/04/2016.
 */
public class DishesRecyclerAdapter extends RecyclerView.Adapter<DishesRecyclerAdapter.DishesViewHolder>
{
    private final Context context;
    private List<Dish> mData; // actual data to be displayed
    private int[] popupsVisibility; //per evitare problemi con le posizioni delle view, memorizzo qui il flag del popup, se visibile o meno
    private int[] selectedQuantities; //array che contiene le quantita' selezionate di ogni piatto del menu'
    private LayoutInflater mInflater;
    private  int reservationQty; // quantita' totale di item presenti nella prenotazione in esame
    private  double reservationPrice; //prezzo totale degli item presenti nella prenotazione in esame

    public DishesRecyclerAdapter(Context context, List<Dish> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;

        popupsVisibility = new int[data.size()];
        Arrays.fill(popupsVisibility, View.GONE); // all'inizio i popup sono tutti invisibili
        selectedQuantities = new int[data.size()];
        Arrays.fill(selectedQuantities, 0);

        reservationQty = 0;
        reservationPrice = 0;
    }

    /**
     * Method called when a ViewHolder Object is created
     * The returned object contains a reference to a view representing the bare structure of the item
     * @param parent
     * @param viewType
     */
    @Override
    public DishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create the view starting from XML layout file
        View view = this.mInflater.inflate(R.layout.dish_list_item, parent, false);
        DishesViewHolder result =  new DishesViewHolder(view);
        return result;
    }

    /**
     * Method called after onCreateViewHolder() and it fetches data from the model and properly sets view accordingly
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final DishesViewHolder holder, int position)
    {
        Dish currentObj = this.mData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount()
    {
        return this.mData.size();
    }

    public List<Dish> getmData(){
        return mData;
    }

    public int getReservationQty(){
        return reservationQty;
    }

    public double getReservationPrice(){
        return reservationPrice;
    }

    public int[] getSelectedQuantities(){
        return selectedQuantities;
    }

    public class DishesViewHolder extends RecyclerView.ViewHolder
    {
//        private ImageView dishPhoto;
        private TextView dishID;
        private TextView dishName;
        private TextView dishDesc;
        private TextView dishPrice;
        private TextView dishAvailability;
        private Button minusButton;
        private Button plusButton;
        private TextView selectedQuantity;
        private TextView selectedPrice;
        private LinearLayout selectionLayout;
        private LinearLayout mainLayout;
        private View separator;
        private ImageView expandArrow;
//        private CheckBox selection;

        public DishesViewHolder(View itemView)
        {
            super(itemView);
//            this.dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            this.dishID = (TextView) itemView.findViewById(R.id.dish_ID);
            this.dishName = (TextView) itemView.findViewById(R.id.dish_name);
            this.dishDesc =  (TextView) itemView.findViewById(R.id.dish_description);
            this.dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            this.dishAvailability = (TextView) itemView.findViewById(R.id.dish_availability);
            this.minusButton = (Button) itemView.findViewById(R.id.dish_minus_button);
            this.plusButton = (Button) itemView.findViewById(R.id.dish_plus_button);
            this.selectedQuantity = (TextView) itemView.findViewById(R.id.dish_selected_quantity);
            this.selectedPrice = (TextView) itemView.findViewById(R.id.dish_selected_price);
            this.selectionLayout = (LinearLayout) itemView.findViewById(R.id.add_dish_popup) ;
            this.mainLayout = (LinearLayout) itemView.findViewById(R.id.cardView_main_layout);
            this.separator = itemView.findViewById(R.id.cardView_separator);
            this.expandArrow = (ImageView) itemView.findViewById(R.id.expand_arrow);
        }

        public void setData(Dish current, int position )
        {
            final int pos = position;
            DecimalFormat df = new DecimalFormat("0.00");
            this.dishID.setText(current.getID());
            this.dishName.setText(current.getName());
            this.dishDesc.setText(current.getDescription());
            this.dishPrice.setText(MessageFormat.format("{0}€", String.valueOf(df.format(current.getPrice()))));
            if(current.getAvailability_qty() == 0) {
                this.dishAvailability.setVisibility(View.VISIBLE);
                //TODO rendere non espandibile la cardView (ad es. settare popupsVisibility a -1)
            }
                                                                                          // mostrare solo quando non e' disponibile

            this.selectionLayout.setVisibility(popupsVisibility[position]); //layout del popup
            this.separator.setVisibility(popupsVisibility[position]); //layout della linea separatrice
            this.selectedQuantity.setText(String.valueOf(selectedQuantities[position]));
            df = new DecimalFormat("0.00");
            this.selectedPrice.setText(MessageFormat.format("{0}€",
                    String.valueOf(df.format(selectedQuantities[position] * mData.get(position).getPrice()))));

            this.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupsVisibility[pos] == View.GONE) { // al click se il popup è invisibile lo faccio apparire...
                        selectionLayout.setVisibility(View.VISIBLE);
                        separator.setVisibility(View.VISIBLE);
                        expandArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_black_24dp));
                        popupsVisibility[pos] = View.VISIBLE;
                    }
                    else { //... se e' visibile lo nascondo
                        selectionLayout.setVisibility(View.GONE);
                        separator.setVisibility(View.INVISIBLE);
                        expandArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp));
                        popupsVisibility[pos] = View.GONE;
                    }
                }
            });

            this.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedQuantities[pos] != 0) { // TODO se clicco di nuovo sul menu' nascondo il popup o non faccio nulla?
                        selectedQuantities[pos]--;
                        selectedQuantity.setText(String.valueOf(selectedQuantities[pos]));
                        DecimalFormat df = new DecimalFormat("0.00");
                        selectedPrice.setText(MessageFormat.format("{0}€",
                                String.valueOf(df.format(selectedQuantities[pos] * mData.get(pos).getPrice()))));

                        reservationPrice -= mData.get(pos).getPrice(); //decremento il prezzo totale della prenotazione
                        reservationQty--; //decremento la quantita' di item della prenotazione

                        TextView tv = (TextView) ((RestaurantProfile) context).findViewById(R.id.show_reservation_button);
                        if (tv != null){ // FIXME aggiustare il layout e conseguentemente questa stampa
                            if(reservationQty != 0)
                                tv.setText(String.format("GO TO CART           %d items - %s€", reservationQty, reservationPrice));
                            else
                                tv.setText(R.string.empty_cart);
                        }
                    }
                }
            });

            this.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedQuantities[pos] != 10) {
                        selectedQuantities[pos]++;
                        selectedQuantity.setText(String.valueOf(selectedQuantities[pos]));
                        DecimalFormat df = new DecimalFormat("0.00");
                        selectedPrice.setText(MessageFormat.format("{0}€",
                                String.valueOf(df.format(selectedQuantities[pos] * mData.get(pos).getPrice()))));

                        reservationPrice += mData.get(pos).getPrice(); //incremento il prezzo totale della prenotazione
                        reservationQty++; //incremento la quantita' di item della prenotazione

                        TextView tv = (TextView) ((RestaurantProfile) context).findViewById(R.id.show_reservation_button);
                        if (tv != null){ //FIXME come per il minusButton
                            if(reservationQty != 0)
                                tv.setText(String.format("GO TO CART           %d items - %s€", reservationQty, reservationPrice));
                            else
                                tv.setText(R.string.empty_cart);
                        }
                    }
                }
            });
        }
    }
}