package it.polito.mad.insane.lab3;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Renato on 28/04/2016.
 */
public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsViewHolder> {

    private List<Review> mData; //actual data to be displayed
    private LayoutInflater mInflater;

    public ReviewsRecyclerAdapter(Context context, List<Review> data){
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
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create the view starting from XML layout file
        View view = this.mInflater.inflate(R.layout.review_list_item,parent, false);

        ReviewsViewHolder result = new ReviewsViewHolder(view); // create the holder
        return result;
    }


    /**
     * Method called after onCreateViewHolder() and it fetches data from the model and properly sets view accordingly
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Review currentObj = this.mData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{

        private View cardView;
        private ImageView userPic;
        private TextView userName;
        private ImageView[] stars;
        private TextView title;
        private TextView expandableText;
        private TextView date;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            this.cardView = itemView;  // non dovrebbe servire
//            this.userPic = (ImageView) itemView.findViewById(R.id.user_pic);
            this.userName = (TextView) itemView.findViewById(R.id.review_user_name);
            this.stars = new ImageView[5];
            this.stars[0] = (ImageView) itemView.findViewById(R.id.star_one);
            this.stars[1] = (ImageView) itemView.findViewById(R.id.star_two);
            this.stars[2] = (ImageView) itemView.findViewById(R.id.star_three);
            this.stars[3] = (ImageView) itemView.findViewById(R.id.star_four);
            this.stars[4] = (ImageView) itemView.findViewById(R.id.star_five);
            this.title = (TextView) itemView.findViewById(R.id.review_title);
            this.expandableText = (TextView) itemView.findViewById(R.id.review_extendable_text);
            this.date = (TextView) itemView.findViewById(R.id.review_date);
            //TODO: add the userPic instead of the left drawable of userName in the cardView
        }

        public void setData(Review current, int position){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.date.setText(dateFormat.format(current.getDate()));
            this.expandableText.setText(current.getText());
            this.title.setText(current.getTitle());
            //TODO: controllare come e' gestita la cosa nel DB
//            this.userName.setText(users.getUser(current.getUserID())); /**da implementare**/
            String imgPath = current.getPhotoPath();
            //FIXME capire bene la gestione delle immagini
            if(imgPath != null)
                this.userPic.setImageURI(Uri.parse(imgPath));
            double score = roundToHalf(current.getFinalScore());

            //TODO: da testare per bene
            if (score == 0.0)
                score = 0.5;
            for(int i = stars.length; i >= 1; i--){
                double i2 = (double) i;
                if (i2 == score)
                    break;
                if(i2 - 0.5 == score){
                    //TODO: immagine mezza stella Federico
//                        this.stars[i].setImageDrawable(R.drawable.half_star);
                        break;
                }
                this.stars[i].setVisibility(View.INVISIBLE);
            }
        }

        public double roundToHalf(double d) {
            return Math.round(d * 2) / 2.0;
        }
    }
}
