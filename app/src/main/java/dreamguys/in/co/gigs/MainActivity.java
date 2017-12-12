package dreamguys.in.co.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;

import dreamguys.in.co.gigs.fragment.BuyFragment;
import dreamguys.in.co.gigs.fragment.ChatFragment;
import dreamguys.in.co.gigs.fragment.HomeFragment;
import dreamguys.in.co.gigs.fragment.SellFragment;
import dreamguys.in.co.gigs.fragment.SettingsFragment;
import dreamguys.in.co.gigs.interfaces.callnavigationHme;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, callnavigationHme {


    @Override
    public void callHome() {
        FragmentTransaction fragmentTransaction;
        Fragment fragment = null;
        fragment = new HomeFragment();
        removeHomeHighlight();
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }

    private ImageView ivHome;
    private ImageView ivExplore;
    private ImageView ivBuy;
    private ImageView ivChat;
    private ImageView ivSettings;
    private TextView txtHome;
    private TextView txtExplore;
    private TextView txtBuy;
    private TextView txtChat;
    private TextView txtSettings;
    private LinearLayout llHome;
    private LinearLayout llExplore;
    private LinearLayout llBuy;
    private LinearLayout llChat;
    private LinearLayout llSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // if savedInstanceState is null we do some cleanup
        if (savedInstanceState != null) {
            // cleanup any existing fragments in case we are in detailed mode
            getFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().
                    findFragmentById(R.id.fragment_frame_layout);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById).commit();
            }

        }

        initLayouts();

        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, fragment).commit();
        removeHomeHighlight();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setDefaultIcons();

    }


    @Override
    protected void onResume() {
        super.onResume();
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
    }

    private void setDefaultIcons() {
        txtExplore.setTextColor(getResources().getColor(android.R.color.black));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_black_24dp);
        txtBuy.setTextColor(getResources().getColor(android.R.color.black));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_black_24dp);
        txtChat.setTextColor(getResources().getColor(android.R.color.black));
        ivChat.setBackgroundResource(R.drawable.ic_chat_black_24dp);
        txtSettings.setTextColor(getResources().getColor(android.R.color.black));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_black_24dp);
        txtHome.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivHome.setBackgroundResource(R.drawable.ic_home_purple_24dp);
    }

    private void initLayouts() {
        ivHome = (ImageView) findViewById(R.id.iv_home);
        ivExplore = (ImageView) findViewById(R.id.iv_explore);
        ivBuy = (ImageView) findViewById(R.id.iv_cart);
        ivChat = (ImageView) findViewById(R.id.iv_chat);
        ivSettings = (ImageView) findViewById(R.id.iv_settings);

        txtHome = (TextView) findViewById(R.id.txt_home);
        txtExplore = (TextView) findViewById(R.id.txt_explore);
        txtBuy = (TextView) findViewById(R.id.txt_cart);
        txtChat = (TextView) findViewById(R.id.txt_chat);
        txtSettings = (TextView) findViewById(R.id.txt_settings);

        llHome = (LinearLayout) findViewById(R.id.ll_home);
        llExplore = (LinearLayout) findViewById(R.id.ll_explore);
        llBuy = (LinearLayout) findViewById(R.id.ll_buy);
        llChat = (LinearLayout) findViewById(R.id.ll_chat);
        llSettings = (LinearLayout) findViewById(R.id.ll_settings);

        llHome.setOnClickListener(this);
        llExplore.setOnClickListener(this);
        llBuy.setOnClickListener(this);
        llChat.setOnClickListener(this);
        llSettings.setOnClickListener(this);

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
//        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                startActivity(new Intent(this, Favourites.class));
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }
        } else if (id == R.id.nav_gallery) {
            if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                startActivity(new Intent(this, LastVisitedGigs.class));
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }
        }else if (id == R.id.nav_myactivity) {
            if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                startActivity(new Intent(this, MyActivity.class));
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction;
        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.ll_home:
                fragment = new HomeFragment();
                removeHomeHighlight();
                break;
            case R.id.ll_explore:
                if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                    this.getSupportActionBar().setTitle(getString(R.string.title_sell));
                    fragment = new SellFragment();
                } else {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
                removeExploreHighlight();
                break;
            case R.id.ll_buy:
                this.getSupportActionBar().setTitle(getString(R.string.title_buy));
                fragment = new BuyFragment();
                removeBuyHighlight();
                break;
            case R.id.ll_chat:
                if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                    this.getSupportActionBar().setTitle(getString(R.string.title_chat));
                    fragment = new ChatFragment();
                } else {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
                removeChatHighlight();
                break;
            case R.id.ll_settings:
                /*if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {

                } else {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }*/
                this.getSupportActionBar().setTitle(getString(R.string.title_settings));
                fragment = new SettingsFragment();
                removeSettingsHighlight();
                break;
        }
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }

    private void removeHomeHighlight() {
        this.getSupportActionBar().setTitle(getString(R.string.title_home));
        txtHome.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivHome.setBackgroundResource(R.drawable.ic_home_purple_24dp);
        txtExplore.setTextColor(getResources().getColor(android.R.color.black));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_black_24dp);
        txtBuy.setTextColor(getResources().getColor(android.R.color.black));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_black_24dp);
        txtChat.setTextColor(getResources().getColor(android.R.color.black));
        ivChat.setBackgroundResource(R.drawable.ic_chat_black_24dp);
        txtSettings.setTextColor(getResources().getColor(android.R.color.black));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_black_24dp);
    }

    private void removeExploreHighlight() {
        txtExplore.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_purple_24dp);
        txtHome.setTextColor(getResources().getColor(android.R.color.black));
        ivHome.setBackgroundResource(R.drawable.ic_home_black_24dp);
        txtBuy.setTextColor(getResources().getColor(android.R.color.black));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_black_24dp);
        txtChat.setTextColor(getResources().getColor(android.R.color.black));
        ivChat.setBackgroundResource(R.drawable.ic_chat_black_24dp);
        txtSettings.setTextColor(getResources().getColor(android.R.color.black));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_black_24dp);
    }

    private void removeBuyHighlight() {

        txtBuy.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_purple_24dp);
        txtExplore.setTextColor(getResources().getColor(android.R.color.black));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_black_24dp);
        txtHome.setTextColor(getResources().getColor(android.R.color.black));
        ivHome.setBackgroundResource(R.drawable.ic_home_black_24dp);
        txtChat.setTextColor(getResources().getColor(android.R.color.black));
        ivChat.setBackgroundResource(R.drawable.ic_chat_black_24dp);
        txtSettings.setTextColor(getResources().getColor(android.R.color.black));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_black_24dp);
    }

    private void removeChatHighlight() {

        txtChat.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivChat.setBackgroundResource(R.drawable.ic_chat_purple_24dp);
        txtExplore.setTextColor(getResources().getColor(android.R.color.black));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_black_24dp);
        txtBuy.setTextColor(getResources().getColor(android.R.color.black));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_black_24dp);
        txtHome.setTextColor(getResources().getColor(android.R.color.black));
        ivHome.setBackgroundResource(R.drawable.ic_home_black_24dp);
        txtSettings.setTextColor(getResources().getColor(android.R.color.black));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_black_24dp);
    }

    private void removeSettingsHighlight() {

        txtSettings.setTextColor(getResources().getColor(R.color.colorAppTheme));
        ivSettings.setBackgroundResource(R.drawable.ic_settings_purple_24dp);
        txtExplore.setTextColor(getResources().getColor(android.R.color.black));
        ivExplore.setBackgroundResource(R.drawable.ic_explore_black_24dp);
        txtBuy.setTextColor(getResources().getColor(android.R.color.black));
        ivBuy.setBackgroundResource(R.drawable.ic_add_shopping_cart_black_24dp);
        txtHome.setTextColor(getResources().getColor(android.R.color.black));
        ivHome.setBackgroundResource(R.drawable.ic_home_black_24dp);
        txtChat.setTextColor(getResources().getColor(android.R.color.black));
        ivChat.setBackgroundResource(R.drawable.ic_chat_black_24dp);
    }



}
