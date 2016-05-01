package it.polito.mad.insane.lab3;

import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

public class RestaurantProfile extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static boolean expandable = true;
    private ReviewsRecyclerAdapter reviewsAdapter = null;
    static private RestaurateurJsonManager manager = null;

    //FIXME: come recuperiamo id ristorante? sharedPreferences?
    //private static String restaurantId = null;
    private static String restaurantId="001";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //FIXME: va messo qui o nel caricamento del layout dell'apposito fragment?
        setupReviewsRecyclerView();

//        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.scrollView);
//        scrollView.setFillViewport (true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.restaurant_tabs);
        // Attach the view pager to the tab strip
        if (tabsStrip != null) {
            tabsStrip.setViewPager(mViewPager);
        }
    }

    private void setupReviewsRecyclerView() {
        manager = RestaurateurJsonManager.getInstance(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        //TODO recuperare id ristorante
        reviewsAdapter = new ReviewsRecyclerAdapter(this, manager.getRestaurant(restaurantId).getReviews());
        if(recyclerView != null){
            recyclerView.setAdapter(reviewsAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            // Return the number of total pages.
            return this.PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position) {
                case 0:
                    return getString(R.string.title_tab_menu);
                case 1:
                    return getString(R.string.title_tab_info);
                case 2:
                    return getString(R.string.title_tab_reviews);
                default:
                    return null;
            }
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
                    textView = (TextView) rootView.findViewById(R.id.tab2_section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    return rootView;
                case 3:
                    rootView = reviewsLayout(inflater, container);
                    return rootView;
                default:
                    return null;
            }
        }

        private View reviewsLayout(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

            final TextView textView = (TextView) rootView.findViewById(R.id.review_extendable_text);
            final TextView btnSeeMore = (TextView) rootView.findViewById(R.id.review_btn_see_more);

            if (btnSeeMore != null) {
                btnSeeMore.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!expandable) {
                            expandable = true;
                            ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", 3);
                            animation.setDuration(200).start();
                            btnSeeMore.setText(getString(R.string.see_more));
                        } else {
                            expandable = false;
                            ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", 40);
                            animation.setDuration(200).start();
                            btnSeeMore.setText(R.string.see_less);
                        }

                    }
                });
            }

            //TODO: implementare algoritmo per il calcolo del punteggio del ristorante
            List<Review> reviews = manager.getRestaurant(restaurantId).getReviews();

            //FIXME: calcolare il punteggio ogni volta che si aggiunge una recensione invece che tutto in una volta quando serve?

            return rootView;
        }
    }


}
