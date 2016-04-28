package it.polito.mad.insane.lab3;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

public class HomeConsumer extends AppCompatActivity {

    private static boolean expandable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_consumer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        final TextView textView = (TextView) findViewById(R.id.home_extendable_text);
        final Button btnSeeMore = (Button) findViewById(R.id.home_btn_see_more);

//        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(expandable) {
//                    expandable = false;
//                    if (textView.getLineCount() > 4) {
////                        if (btnSeeMore != null) {
////                            btnSeeMore.setVisibility(View.VISIBLE);
////                        }
//                        ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", 4);
//                        animation.setDuration(0).start();
//                    }
//                }
//            }
//        });

        if (btnSeeMore != null) {
            btnSeeMore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (!expandable) {
                        expandable = true;
                        ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", 4);
                        animation.setDuration(200).start();
                        btnSeeMore.setText("SEE MORE");
//                        btnSeeMore.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_collapse));
                    } else {
                        expandable = false;
                        ObjectAnimator animation = ObjectAnimator.ofInt(textView, "maxLines", 40);
                        animation.setDuration(200).start();
                        btnSeeMore.setText("SEE LESS");
//                        btnSeeMore.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_expand));
                    }

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_consumer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if(id == R.id.activity_renato_michele)
        {
            Intent intent = new Intent(HomeConsumer.this, RestaurantProfile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
