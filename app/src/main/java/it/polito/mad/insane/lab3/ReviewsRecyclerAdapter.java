package it.polito.mad.insane.lab3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Renato on 28/04/2016.
 */
public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsViewHolder> {

    private List<Review> mData; //actual data to be displayed
    private LayoutInflater mInflater;


    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        public ReviewsViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(Review current, int position){

        }
    }
}
