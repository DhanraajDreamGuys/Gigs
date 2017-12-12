package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTFavGigs;
import dreamguys.in.co.gigs.adapter.FavouriteAdapter;
import dreamguys.in.co.gigs.fragment.HomeFragment;
import dreamguys.in.co.gigs.interfaces.GetfavRemovedGigIdenitifer;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 11/4/2017.
 */

public class Favourites extends AppCompatActivity implements GetfavRemovedGigIdenitifer{
    RecyclerView mFavRecycler;
    FavouriteAdapter aFavouriteAdapter;
    CustomProgressDialog mCustomProgressDialog;
    HashMap<String, String> postUserdetails = new HashMap<String, String>();
    Toolbar mToolbar;
    HomeFragment mHomeFragment;
    boolean gigsRemovedBool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        mCustomProgressDialog = new CustomProgressDialog(this);
        mFavRecycler = (RecyclerView) findViewById(R.id.rcv_favourites);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(Favourites.this, LinearLayoutManager.VERTICAL, false);
        mFavRecycler.setLayoutManager(horizontalLayoutManagaer);

        mHomeFragment = new HomeFragment();
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postUserdetails.put("user_id", SessionHandler.getInstance().get(Favourites.this, Constants.USER_ID));
            getUserFavGigs();
        } else {
            Utils.toastMessage(Favourites.this, getString(R.string.err_internet_connection));
        }
    }

    private void getUserFavGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getFavLists(postUserdetails).enqueue(new Callback<POSTFavGigs>() {
            @Override
            public void onResponse(Call<POSTFavGigs> call, Response<POSTFavGigs> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {
                        aFavouriteAdapter = new FavouriteAdapter(Favourites.this, response.body().getData());
                        mFavRecycler.setAdapter(aFavouriteAdapter);
                    } else {
                        Utils.toastMessage(Favourites.this, getString(R.string.no_fav_item_found));
                    }
                } else {
                    Utils.toastMessage(Favourites.this, response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTFavGigs> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getFavRemGigIds(boolean removed) {
        this.gigsRemovedBool = removed;
    }
}


