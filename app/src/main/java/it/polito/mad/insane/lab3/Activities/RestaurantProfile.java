package it.polito.mad.insane.lab3.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.polito.mad.insane.lab3.data.Dish;
import it.polito.mad.insane.lab3.adapters.DishesRecyclerAdapter;
import it.polito.mad.insane.lab3.R;
import it.polito.mad.insane.lab3.data.Restaurant;
import it.polito.mad.insane.lab3.dBHandlers.RestaurateurJsonManager;
import it.polito.mad.insane.lab3.data.RestaurateurProfile;
import it.polito.mad.insane.lab3.data.Review;
import it.polito.mad.insane.lab3.adapters.ReviewsRecyclerAdapter;

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
    private static FloatingActionButton fab;
    private static RestaurateurJsonManager manager = null;
    private static String restaurantId;
    private static DishesRecyclerAdapter dishesAdapter = null;
    private static List<Dish> reservationList = null;

    public static Activity RestaurantProfileActivity = null; // attribute used to finish() the current activity from another activity

    @Override
    public void finish()
    {
        super.finish();
        RestaurantProfileActivity = null;
        clearStaticVariables();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        RestaurantProfile.RestaurantProfileActivity = this;

        setContentView(R.layout.activity_restaurant_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = RestaurateurJsonManager.getInstance(this);
        if(getIntent().getStringExtra("ID") != null) {
            restaurantId = getIntent().getStringExtra("ID");
            setTitle(manager.getRestaurant(restaurantId).getProfile().getRestaurantName());
        }

//        NestedScrollView scrollView = (NestedScrollView) findViewById (R.id.scrollView);
//        scrollView.setFillViewport (true);

        /** Create the adapter that will return a fragment for each of the three primary sections of the activity. **/

        // set button
        fab = (FloatingActionButton) findViewById(R.id.fab_cart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantProfile.this, MakeReservationActivity.class);
                Bundle bundle = new Bundle();
                if(dishesAdapter.getReservationQty() == 0)
                    Toast.makeText(RestaurantProfile.this, getResources().getString(R.string.cart_empty_alert), Toast.LENGTH_SHORT).show();
                else {
//                                bundle.putParcelableArrayList("reservationList", (ArrayList<? extends Parcelable>) reservationList);
                    bundle.putIntArray("selectedQuantities", dishesAdapter.getSelectedQuantities());
                    bundle.putString("ID", restaurantId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch(position)
                {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        fab.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.restaurant_tabs);
        // Attach the view pager to the tab strip
        if (tabsStrip != null) {
            tabsStrip.setViewPager(mViewPager);
            tabsStrip.setBackgroundColor(ContextCompat.getColor(RestaurantProfile.this, R.color.colorPrimary));
            tabsStrip.setTextColor(ContextCompat.getColor(RestaurantProfile.this, R.color.white));
            tabsStrip.setIndicatorColor(ContextCompat.getColor(RestaurantProfile.this, R.color.white));
        }
//        if(dishesAdapter != null)
//            editShowButton(dishesAdapter.getReservationQty(), dishesAdapter.getReservationPrice());

        // Fix Portrait Mode
        if( (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL ||
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dishesAdapter = null;
        reservationList = null;
        restaurantId = null;
    }

//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        if(dishesAdapter != null)
//            editShowButton(dishesAdapter.getReservationQty(), dishesAdapter.getReservationPrice());
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dishesAdapter != null)
            editShowButton(dishesAdapter.getReservationQty(), dishesAdapter.getReservationPrice());
    }

    public void editShowButton(int quantity, double price) {

        TextView tv = (TextView) findViewById(R.id.show_reservation_button);
        if (tv != null) {
            if (quantity != 0)
                tv.setText(String.format("%d "+getResources().getString(R.string.itemsFormat)+" - %s€", quantity, price));
            else
                tv.setText(R.string.empty_cart);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_restaurant_profile, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                MakeReservationActivity.clearStaticVariables();
                finish();
                return true;
        }

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public static void clearStaticVariables()
    {
        RestaurantProfile.dishesAdapter = null;
        RestaurantProfile.reservationList = null;
        RestaurantProfile.restaurantId = null;
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = menuLayout(inflater, container);
                    return rootView;
                case 2:
                    rootView = infoLayout(inflater, container);
                    return rootView;
                case 3:
                    rootView = reviewsLayout(inflater, container);
                    return rootView;
                default:
                    return null;
            }
        }


        private View infoLayout(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
//            TextView tv = (TextView) getActivity().findViewById(R.id.chart_selection);
//            tv.setVisibility(View.GONE);
            loadProfileData(rootView);

            return rootView;
        }

        private View menuLayout(LayoutInflater inflater, ViewGroup container)
        {
            View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
//            TextView tv = (TextView) getActivity().findViewById(R.id.chart_selection);
//            tv.setVisibility(View.VISIBLE);

            // take the list of dishes form manager
            manager = RestaurateurJsonManager.getInstance(getActivity());
            Restaurant restaurant = manager.getRestaurant(restaurantId);

            // set up dishesRecyclerView
            final RecyclerView rv = setupDishesRecyclerView(rootView, restaurant.getDishes());
            TextView tv = (TextView) rootView.findViewById((R.id.show_reservation_button));
            if(tv != null) {
                if(dishesAdapter != null)
                    editShowButton(dishesAdapter.getReservationQty(), dishesAdapter.getReservationPrice(), rootView);

//                tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), MakeReservationActivity.class);
//                        if (rv != null) {
//                            Bundle bundle = new Bundle();
//                            if(dishesAdapter.getReservationQty() == 0)
//                                Toast.makeText(getActivity(), "Cart must contain at least one dish", Toast.LENGTH_SHORT).show();
//                            else {
////                                bundle.putParcelableArrayList("reservationList", (ArrayList<? extends Parcelable>) reservationList);
//                                bundle.putIntArray("selectedQuantities", dishesAdapter.getSelectedQuantities());
//                                bundle.putString("ID", restaurantId);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                });
            }

            return rootView;
        }

        private View reviewsLayout(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);
//            TextView tv = (TextView) getActivity().findViewById(R.id.chart_selection);
//            tv.setVisibility(View.GONE);


            manager = RestaurateurJsonManager.getInstance(getActivity());

            Restaurant restaurant = manager.getRestaurant(restaurantId);

            setupReviewsRecyclerView(rootView, restaurant.getReviews());

            TextView tv = (TextView) rootView.findViewById(R.id.restaurant_final_score);
            DecimalFormat df = new DecimalFormat("0.0");
            tv.setText(df.format(restaurant.getAvgFinalScore()));

            tv = (TextView) rootView.findViewById(R.id.reviews_number);
            tv.setText(String.format(getResources().getString(R.string.reviewsFormat), restaurant.getReviews().size()));

            tv = (TextView) rootView.findViewById(R.id.score_1);
            df = new DecimalFormat("0.0");
            tv.setText(df.format(restaurant.getAvgScores()[0]));

            tv = (TextView) rootView.findViewById(R.id.score_2);
            df = new DecimalFormat("0.0");
            tv.setText(df.format(restaurant.getAvgScores()[1]));

            tv = (TextView) rootView.findViewById(R.id.score_3);
            df = new DecimalFormat("0.0");
            tv.setText(df.format(restaurant.getAvgScores()[2]));

            return rootView;
        }

        private void setupReviewsRecyclerView(View rootView, List<Review> reviews)
        {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recycler_view);
            RecyclerView.Adapter reviewsAdapter = new ReviewsRecyclerAdapter(getActivity(), reviews);
            if(recyclerView != null){
                recyclerView.setAdapter(reviewsAdapter);
                // set Layout Manager
                if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                {
                    // 10 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        // 2 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(mGridLayoutManager);
                    }else
                    {
                        // 3 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
                        recyclerView.setLayoutManager(mGridLayoutManager);
                    }

                } else if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                {
                    // 7 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        // 2 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(mGridLayoutManager);

                    }else
                    {
                        // 1 column
                        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
                        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                    }
                }else {
                    // small and normal screen
                    // 1 columns
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                }


                // set Animator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }

        private RecyclerView setupDishesRecyclerView(View rootView, List<Dish> dishes)
        {
            // set Adapter
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.MenuRecyclerView);
            if (recyclerView != null) {
                if(dishesAdapter == null)
                    dishesAdapter = new DishesRecyclerAdapter(getActivity(), dishes);
                recyclerView.setAdapter(dishesAdapter);
            }

            if (recyclerView != null)
            {
                recyclerView.setAdapter(dishesAdapter);

                // set Layout Manager
                if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                {
                    // 10 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        // 2 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(mGridLayoutManager);
                    }else
                    {
                        // 3 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
                        recyclerView.setLayoutManager(mGridLayoutManager);
                    }

                } else if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                {
                    // 7 inches
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        // 2 columns
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(mGridLayoutManager);

                    }else
                    {
                        // 1 column
                        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
                        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                    }
                }else {
                    // small and normal screen
                    // 1 columns
                    LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
                    mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                }


                // set Animator
                recyclerView.setItemAnimator(new DefaultItemAnimator()); // default animations
                return recyclerView;
            }

            return null;
        }

        public static void editShowButton(int quantity, double price, View rootView) {
//        if (tv == null)
            TextView tv = (TextView) rootView.findViewById(R.id.show_reservation_button);
            if (tv != null) {
                if(quantity != 0)
                    tv.setText(String.format("%d "+rootView.getResources().getString(R.string.itemsFormat)+" - %s€", quantity, price));
                else
                    tv.setText(R.string.empty_cart);

            }
        }

        private void loadProfileData(View rootView) {
            manager = RestaurateurJsonManager.getInstance(getActivity());
            RestaurateurProfile profile = manager.getRestaurant(restaurantId).getProfile();

            TextView tv;

            tv = (TextView) rootView.findViewById(R.id.editName);
            if (tv != null) {
                if(profile.getRestaurantName() != null)
                    tv.setText(profile.getRestaurantName());
            }
            tv = (TextView) rootView.findViewById(R.id.editAddress);
            if (tv != null) {
                if(profile.getAddress() != null)
                    tv.setText(profile.getAddress());
            }
            tv = (TextView) rootView.findViewById(R.id.editDescription);
            if (tv != null) {
                if(profile.getDescription() != null)
                    tv.setText(profile.getDescription());
            }
            tv = (TextView) rootView.findViewById(R.id.editTimeNotes);
            if (tv != null) {
                if(profile.getTimeInfo() != null)
                    tv.setText(profile.getTimeInfo());
            }
            tv = (TextView) rootView.findViewById(R.id.editPayment);
            if (tv != null) {
                if(profile.getPaymentMethod() != null)
                    tv.setText(profile.getPaymentMethod());
            }
            tv = (TextView) rootView.findViewById(R.id.editServices);
            if (tv != null) {
                if(profile.getAdditionalServices() != null)
                    tv.setText(profile.getAdditionalServices());
            }
            tv = (TextView) rootView.findViewById(R.id.university);
            if (tv != null) {
                if(profile.getNearbyUniversity() != null)
                    tv.setText(profile.getNearbyUniversity());
            }
            tv = (TextView) rootView.findViewById(R.id.cuisineType);
            if (tv != null) {
                if(profile.getCuisineType() != null)
                    tv.setText(profile.getCuisineType());
            }

            tv = (TextView) rootView.findViewById(R.id.openingHour_title);
            if(tv != null){
                Date date = profile.getOpeningHour();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                tv.setText(String.format("%s:%s", pad(hourOfDay), pad(minute)));
            }
            tv = (TextView) rootView.findViewById(R.id.closingHour_title);
            if(tv != null){
                Date date = profile.getClosingHour();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                tv.setText(String.format("%s:%s", pad(hourOfDay), pad(minute)));
            }
        }

        private String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }
    }




}
