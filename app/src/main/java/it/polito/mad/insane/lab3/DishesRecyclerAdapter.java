package it.polito.mad.insane.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by miche on 08/04/2016.
 */
public class DishesRecyclerAdapter extends RecyclerView.Adapter<DishesRecyclerAdapter.DishesViewHolder>
{
    private final Context context;
    private List<Dish> mData; // actual data to be displayed
    private int[] popupsVisibility;
    private int[] selectedQuantities;
    private LayoutInflater mInflater;
//    private boolean cardView_clickable;
    private  int reservationQty;
    private  double reservationPrice;
    private List<Dish> reservationList;

    public DishesRecyclerAdapter(Context context, List<Dish> data, boolean clickable)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;

        popupsVisibility = new int[data.size()];
        Arrays.fill(popupsVisibility, View.GONE);
        selectedQuantities = new int[data.size()];
        Arrays.fill(selectedQuantities, 0);

        reservationQty = 0;
        reservationPrice = 0;
        reservationList = new ArrayList<>();
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

    public class DishesViewHolder extends RecyclerView.ViewHolder
    {
        private View cardView;
        private ImageView dishPhoto;
        private TextView dishID;
        private TextView dishName;
        private TextView dishDesc;
        private TextView dishPrice;
        private TextView dishAvailabQty;
        private Button minusButton;
        private Button plusButton;
        private TextView selectedQuantity;
        private TextView selectedPrice;
        private LinearLayout selectionLayout;
        private LinearLayout mainLayout;
        private View separator;
//        private CheckBox selection;

        public DishesViewHolder(View itemView)
        {
            super(itemView);
            this.cardView = itemView;
            this.dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            this.dishID = (TextView) itemView.findViewById(R.id.dish_ID);
            this.dishName = (TextView) itemView.findViewById(R.id.dish_name);
            this.dishDesc =  (TextView) itemView.findViewById(R.id.dish_description);
            this.dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            this.dishAvailabQty = (TextView) itemView.findViewById(R.id.dish_availab_qty);
            this.minusButton = (Button) itemView.findViewById(R.id.dish_minus_button);
            this.plusButton = (Button) itemView.findViewById(R.id.dish_plus_button);
            this.selectedQuantity = (TextView) itemView.findViewById(R.id.dish_selected_quantity);
            this.selectedPrice = (TextView) itemView.findViewById(R.id.dish_selected_price);
            this.selectionLayout = (LinearLayout) itemView.findViewById(R.id.add_dish_popup) ;
            this.mainLayout = (LinearLayout) itemView.findViewById(R.id.cardView_main_layout);
            this.separator = itemView.findViewById(R.id.cardView_separator);
        }

        public void setData(Dish current, int position )
        {
            final int pos = position;
            this.dishID.setText(current.getID());
            this.dishName.setText(current.getName());
            this.dishDesc.setText(current.getDescription());
            this.dishPrice.setText(Double.toString(current.getPrice()));
            this.dishAvailabQty.setText(Integer.toString(current.getAvailability_qty()));

            this.selectionLayout.setVisibility(popupsVisibility[position]);
            this.separator.setVisibility(popupsVisibility[position]);
            this.selectedQuantity.setText(String.valueOf(selectedQuantities[position]));
            this.selectedPrice.setText(String.valueOf(selectedQuantities[position] * mData.get(position).getPrice()));

            this.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(popupsVisibility[pos] == View.GONE) {
                        selectionLayout.setVisibility(View.VISIBLE);
                        separator.setVisibility(View.VISIBLE);
                        popupsVisibility[pos] = View.VISIBLE;
                    }
                    else {
                        selectionLayout.setVisibility(View.GONE);
                        separator.setVisibility(View.INVISIBLE);
                        popupsVisibility[pos] = View.GONE;
                    }
                }
            });

            this.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedQuantities[pos] != 0) {
                        selectedQuantities[pos]--;
                        selectedQuantity.setText(String.valueOf(selectedQuantities[pos]));
                        selectedPrice.setText(String.format("%s€",
                                String.valueOf(selectedQuantities[pos] * mData.get(pos).getPrice())));

//                        reservationPrice -= mData.get(pos).getPrice();
                    }
                }
            });

            this.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedQuantities[pos] != 10) {
                        selectedQuantities[pos]++;
                        selectedQuantity.setText(String.valueOf(selectedQuantities[pos]));
                        selectedPrice.setText(String.format("%s€",
                                String.valueOf(selectedQuantities[pos] * mData.get(pos).getPrice())));

//                        reservationPrice += mData.get(pos).getPrice();

                    }
                }
            });


//            String imgPath = current.getPhotoPath();
//            if(imgPath != null)
//                this.dishPhoto.setImageURI(Uri.parse(imgPath));
        }
    }
}


//CODICE VECCHIO NON DOVREBBE SERVIRE MA NON SI SA MAI
//        holder.selection.setTag(mData.get(position));

//        holder.selection.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                final CheckBox cb = (CheckBox) v;
////                Dish dish = (Dish) cb.getTag();
//                if (cb.isChecked()) {
//                    holder.selection.setChecked(true);
//                    mData.get(pos).setSelected(true);
//                    RestaurantProfile activity = (RestaurantProfile) context;
//
//                    nSelected++;
//                    reservationPrice += mData.get(pos).getPrice();
//
//                    activity.editFooter(nSelected, reservationPrice);
//                } else {
//                    holder.selection.setChecked(false);
//                    mData.get(pos).setSelected(false);
//                    RestaurantProfile activity = (RestaurantProfile) context;
//
//                    nSelected--;
//                    reservationPrice -= mData.get(pos).getPrice();
//
//                    activity.editFooter(nSelected, reservationPrice);
//                }

//                dish.setSelected(cb.isChecked());
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                if(cb.isChecked()){
//
//                    builder.setTitle(new StringBuilder().append(mData.get(holder.getAdapterPosition()).getName()).append(" ").
//                            append(mData.get(holder.getAdapterPosition()).getPrice()).toString());
//                    builder.setPositiveButton(R.string.add_dialog_button, new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int id)
//                        {
//
//                        }
//                    });
//                    builder.setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            holder.selection.setChecked(false);
//                            mData.get(holder.getAdapterPosition()).setSelected(false);
//                        }
//                    });
//                    // Create the AlertDialog
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//                else{
//
//                    holder.selection.setChecked(false);
//                    mData.get(holder.getAdapterPosition()).setSelected(false);
//                    RestaurantProfile activity = (RestaurantProfile) context;
//
//                    nSelected--;
//                    reservationPrice -= mData.get(holder.getAdapterPosition()).getPrice();
//
//                    activity.editFooter(nSelected, reservationPrice);
//                }
//            }
//        }
//            }
//        });