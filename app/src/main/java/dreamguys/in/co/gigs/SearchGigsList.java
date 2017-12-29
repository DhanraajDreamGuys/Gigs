package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTSearchGigs;
import dreamguys.in.co.gigs.adapter.SearchGigsListAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 27-12-2017.
 */

public class SearchGigsList extends AppCompatActivity {


    SearchGigsListAdapter mSearchGigsListAdapter;
    private CustomProgressDialog mCustomProgressDialog;
    private Toolbar mToolbar;
    private HashMap<String, String> postDetails = new HashMap<String, String>();
    private GridView gridGigsList;
    String cat_id, title, state, country;
    private TextView inputNoGigs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gigs);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        gridGigsList = (GridView) findViewById(R.id.gv_gigs_list);
        mCustomProgressDialog = new CustomProgressDialog(this);
        inputNoGigs = (TextView) findViewById(R.id.tv_nogigs);
     /*   cat_id = getIntent().getStringExtra("cat_id");
        title = getIntent().getStringExtra("title");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");*/

        postDetails.put("title", getIntent().getStringExtra("title"));
        postDetails.put("state", getIntent().getStringExtra("state"));
        postDetails.put("country", getIntent().getStringExtra("country"));
        postDetails.put("category_id", getIntent().getStringExtra("cat_id"));

        postSearchGigs();
    }


    public void postSearchGigs() {
        mCustomProgressDialog.showDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postSearchGigs(postDetails).enqueue(new Callback<POSTSearchGigs>() {
            @Override
            public void onResponse(Call<POSTSearchGigs> call, Response<POSTSearchGigs> response) {
                if (response.body().getCode().equals(200)) {
                    mSearchGigsListAdapter = new SearchGigsListAdapter(SearchGigsList.this, response.body().getData());
                    gridGigsList.setAdapter(mSearchGigsListAdapter);
                    mCustomProgressDialog.dismiss();
                } else {
                    gridGigsList.setVisibility(View.GONE);
                    inputNoGigs.setVisibility(View.VISIBLE);
                    mCustomProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<POSTSearchGigs> call, Throwable t) {
                gridGigsList.setVisibility(View.GONE);
                inputNoGigs.setVisibility(View.VISIBLE);
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
