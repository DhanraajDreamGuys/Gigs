package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTLastVisitedGigs;
import dreamguys.in.co.gigs.adapter.LastVisitedGigsAdapter;
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

public class LastVisitedGigs extends AppCompatActivity {
    RecyclerView mFavRecycler;
    LastVisitedGigsAdapter aLastVisitedGigsAdapter;
    CustomProgressDialog mCustomProgressDialog;
    HashMap<String, String> postUserdetails = new HashMap<String, String>();
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_visited_gigs);
        mCustomProgressDialog = new CustomProgressDialog(this);
        mFavRecycler = (RecyclerView) findViewById(R.id.rcv_last_visited_gigs);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(LastVisitedGigs.this, LinearLayoutManager.VERTICAL, false);
        mFavRecycler.setLayoutManager(horizontalLayoutManagaer);

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postUserdetails.put("user_id", SessionHandler.getInstance().get(LastVisitedGigs.this, Constants.USER_ID));
            getUserFavGigs();
        } else {
            Utils.toastMessage(LastVisitedGigs.this, getString(R.string.err_internet_connection));
        }
    }

    private void getUserFavGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.getLastVisitedGigs(postUserdetails).enqueue(new Callback<POSTLastVisitedGigs>() {
            @Override
            public void onResponse(Call<POSTLastVisitedGigs> call, Response<POSTLastVisitedGigs> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {
                        aLastVisitedGigsAdapter = new LastVisitedGigsAdapter(LastVisitedGigs.this, response.body().getData());
                        mFavRecycler.setAdapter(aLastVisitedGigsAdapter);
                    } else {
                        Utils.toastMessage(LastVisitedGigs.this, response.body().getMessage());
                    }
                } else {
                    Utils.toastMessage(LastVisitedGigs.this, response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTLastVisitedGigs> call, Throwable t) {

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
}


