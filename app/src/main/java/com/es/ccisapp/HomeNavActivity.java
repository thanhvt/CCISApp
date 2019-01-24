package com.es.ccisapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.es.model.Bill_TaxInvoice;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeNavActivity extends AppCompatActivity { // implements NavigationView.OnNavigationItemSelectedListener  implements View.OnTouchListener
    public static final String TAG = "HomeNavActivity msg: ";
    Bill_TaxInvoice taxInvoice;
    GestureDetector gestureDetector;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    List<Bill_TaxInvoice> lstTaxInvoice = new ArrayList<>();
    int INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gestureDetector = new GestureDetector(new SwipeDetector());
//        gestureDetector=new GestureDetector(this,new OnSwipeListener(){
//
//            @Override
//            public boolean onSwipe(Direction direction) {
//                if (direction==Direction.up){
//                    //do your stuff
//                    Log.d(TAG, "onSwipe: up");
//
//                }
//
//                if (direction==Direction.down){
//                    //do your stuff
//                    Log.d(TAG, "onSwipe: down");
//                }
//                return true;
//            }
//
//
//        });
//        someLayout.setOnTouchListener(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        Utils.startFragment(getSupportFragmentManager(), TaxInvoiceDetailFragment.newInstance("ONE"));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        taxInvoice =
                (Bill_TaxInvoice) bundle.getSerializable("TAX");

        lstTaxInvoice = (ArrayList<Bill_TaxInvoice>) bundle.getSerializable("ALL");
        INDEX = lstTaxInvoice.indexOf(taxInvoice);
        for (int i = 0; i < lstTaxInvoice.size(); i++) {
            Bill_TaxInvoice x = lstTaxInvoice.get(i);
            if (x.getTaxInvoiceId() == taxInvoice.getTaxInvoiceId()) {
                INDEX = i;
                break;
            }
        }

        Log.e("taxInvoice", lstTaxInvoice.size() + " " + INDEX);
        switchFragment(buildFragment_TaxInvoiceDetail(), "TAX");
        Toasty.info(getApplicationContext(), "Vuốt màn hình sang trái hoặc phải để thao tác với khách hàng khác !", Toasty.LENGTH_LONG, true).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    Utils.startFragment(getSupportFragmentManager(), TaxInvoiceDetailFragment.newInstance("ONE"));
                    switchFragment(buildFragment_TaxInvoiceDetail(), "TAX");
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(buildFragment_AdjustInformations(), "ADJ");
                    return true;
                case R.id.navigation_notifications:
//                    Utils.startFragment(getSupportFragmentManager(), TaxInvoiceDetailFragment.newInstance("ONE"));
                    switchFragment(new NanFragment(), "NAN");
                    return true;
            }
            return false;
        }
    };

    private void switchFragment(Fragment pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_body, pos, tag)
                .commit();
    }

    private AdjustInformationsFragment buildFragment_AdjustInformations() {
        AdjustInformationsFragment fragment = new AdjustInformationsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TAX", taxInvoice);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TaxInvoiceDetailFragment buildFragment_TaxInvoiceDetail() {
        TaxInvoiceDetailFragment fragment = new TaxInvoiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TAX", taxInvoice);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                x1 = event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = event.getX();
//                float deltaX = x2 - x1;
//                if (Math.abs(deltaX) > MIN_DISTANCE)
//                {
//                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
//                }
//                else
//                {
//                    // consider as something else - a screen tap for example
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }


    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            TaxInvoiceDetailFragment myFragment = (TaxInvoiceDetailFragment) getSupportFragmentManager().findFragmentByTag("TAX");
            Fragment f = getVisibleFragment();
            if (f.getTag().equals("TAX")) {
//            if (myFragment != null && myFragment.isVisible()) {
                // add your code here
                // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
                // then dismiss the swipe.
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;

                // Swipe from left to right.
                // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
                // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
                if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (INDEX > 0) {
                        INDEX--;
                        taxInvoice = lstTaxInvoice.get(INDEX);
                        switchFragment(buildFragment_TaxInvoiceDetail(), "TAX");
                    }
                    Log.d(TAG, "onSwipe: left2right " + INDEX);
                    return true;
                } else {
                    if (INDEX < lstTaxInvoice.size() - 1) {
                        INDEX++;
                        taxInvoice = lstTaxInvoice.get(INDEX);
                        switchFragment(buildFragment_TaxInvoiceDetail(), "TAX");
                    }
                    Log.d(TAG, "onSwipe: right2left " + INDEX);

                }
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                // If the gestureDetector handles the event, a swipe has been
                // executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = HomeNavActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
//        return true;
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_down) {
//            // Handle the camera action
//        } else if (id == R.id.nav_up) {
//
//        } else if (id == R.id.nav_duyet) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
