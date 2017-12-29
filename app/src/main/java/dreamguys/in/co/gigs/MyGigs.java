package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

import dreamguys.in.co.gigs.Model.GETMyGigs;
import dreamguys.in.co.gigs.adapter.MyGigsListAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 06-11-2017.
 */

public class MyGigs extends AppCompatActivity {

    GridView gridGigsList;
    private MyGigsListAdapter myGigsListAdapter;
    private Toolbar toolbar;
    private CustomProgressDialog mCustomProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details_seller_gigs);
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        gridGigsList = (GridView) findViewById(R.id.customGridview);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.my_gigs));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mCustomProgressDialog = new CustomProgressDialog(this);
        getMyGigs();
    }

    private void getMyGigs() {
        mCustomProgressDialog.showDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getMyGigs(SessionHandler.getInstance().get(this, Constants.USER_ID)).enqueue(new Callback<GETMyGigs>() {
            @Override
            public void onResponse(Call<GETMyGigs> call, Response<GETMyGigs> response) {
                if (response.body().getCode().equals(200)) {
                    myGigsListAdapter = new MyGigsListAdapter(MyGigs.this, response.body().getData());
                    gridGigsList.setAdapter(myGigsListAdapter);
                    mCustomProgressDialog.dismiss();
                } else {
                    mCustomProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GETMyGigs> call, Throwable t) {
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
}
