package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTGigsReview;
import dreamguys.in.co.gigs.adapter.GigsReviewAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 15-12-2017.
 */

public class UserReviews extends AppCompatActivity {


    ListView mViewReviews;
    GigsReviewAdapter mGigsReviewAdapter;
    HashMap<String, String> postSellerReviews = new HashMap<String, String>();
    CustomProgressDialog mCustomProgressDialog;
    Toolbar toolbar;
    private String Gig_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);
        Gig_id = getIntent().getStringExtra(Constants.GIGS_ID);
        mCustomProgressDialog = new CustomProgressDialog(UserReviews.this);
        mViewReviews = (ListView) findViewById(R.id.lv_review_list);
        postSellerReviews.put("gig_id", Gig_id);
        getSellerReviews();
    }

    private void getSellerReviews() {
        mCustomProgressDialog.showDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getGigsReviews(postSellerReviews).enqueue(new Callback<POSTGigsReview>() {
            @Override
            public void onResponse(Call<POSTGigsReview> call, Response<POSTGigsReview> response) {
                if (response.body().getData().size() > 0) {
                    mGigsReviewAdapter = new GigsReviewAdapter(UserReviews.this, response.body().getData());
                    mViewReviews.setAdapter(mGigsReviewAdapter);
                    mCustomProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<POSTGigsReview> call, Throwable t) {
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

}
