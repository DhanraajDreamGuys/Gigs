package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.GETAllGigs;
import dreamguys.in.co.gigs.adapter.AllGigsListsAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/26/2017.
 */

public class GigsLists extends AppCompatActivity {

    private GridView gridGigsList;
    private AllGigsListsAdapter aAllGigsListsAdapter;
    private CustomProgressDialog mCustomProgressDialog;
    private String user_id = "", cat_id = "", sub_cat_id = "";
    private Toolbar toolbar;
    private HashMap<String, String> postDetails = new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gigs);


        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();


        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            getGigs();
        } else {
            showToast();
        }

    }

    private void getGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (SessionHandler.getInstance().get(GigsLists.this, Constants.USER_ID) != null) {
            user_id = SessionHandler.getInstance().get(GigsLists.this, Constants.USER_ID);
        } else {
            user_id = "";
        }

        if (getIntent().getStringExtra(Constants.CAT_ID) != null) {
            cat_id = getIntent().getStringExtra(Constants.CAT_ID);
        } else {
            cat_id = "";
        }


        if (getIntent().getStringExtra(Constants.SUB_CAT_ID) != null) {
            sub_cat_id = getIntent().getStringExtra(Constants.SUB_CAT_ID);
        } else {
            sub_cat_id = "";
        }

        if (!cat_id.isEmpty() &&
                !sub_cat_id.isEmpty()
                && !user_id.isEmpty()) {
            postDetails.put("category_id", cat_id);
            postDetails.put("sub_category_id", sub_cat_id);
            postDetails.put("user_id", user_id);
            postDetails.put("services", "ALL");
        } else if (!cat_id.isEmpty() && !user_id.isEmpty()) {
            postDetails.put("category_id", cat_id);
            postDetails.put("user_id", user_id);
            postDetails.put("services", "ALL");
        } else if (!user_id.isEmpty()) {
            postDetails.put("user_id", user_id);
            postDetails.put("services", "ALL");
        } else if (!cat_id.isEmpty() && !sub_cat_id.isEmpty()) {
            postDetails.put("category_id", cat_id);
            postDetails.put("sub_category_id", sub_cat_id);
            postDetails.put("services", "ALL");
        } else if (!cat_id.isEmpty()) {
            postDetails.put("category_id", cat_id);
            postDetails.put("services", "ALL");
        } else {
            postDetails.put("services", "ALL");
        }

        apiInterface.getUserGigs(postDetails).enqueue(new Callback<GETAllGigs>() {
            @Override
            public void onResponse(Call<GETAllGigs> call, Response<GETAllGigs> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {
                        aAllGigsListsAdapter = new AllGigsListsAdapter(GigsLists.this, response.body().getData());
                        gridGigsList.setAdapter(aAllGigsListsAdapter);
                    } else {
                        Toast.makeText(GigsLists.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GigsLists.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GETAllGigs> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });


    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void initLayouts() {
        gridGigsList = (GridView) findViewById(R.id.gv_gigs_list);

        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
