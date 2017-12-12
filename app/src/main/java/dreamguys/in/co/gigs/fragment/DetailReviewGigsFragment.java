package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTSellerReviews;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.DetailGigsReviewAdapter;
import dreamguys.in.co.gigs.adapter.SellerReviewsGigsAdpater;
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
 * Created by user5 on 06-11-2017.
 */

public class DetailReviewGigsFragment extends Fragment {

    View view;
    HashMap<String, String> postGigDetails = new HashMap<String, String>();
    HashMap<String, String> postSellerReviews = new HashMap<String, String>();
    Context mContext;
    DetailGigsReviewAdapter detailGigsReviewAdapter;
    SellerReviewsGigsAdpater sellerReviewsGigsAdpater;
    String gigs_id;
    private ListView mDetailGigsReview;
    CustomProgressDialog mCustomProgressDialog;

    public DetailReviewGigsFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_gigs_user_review, container, false);
        mCustomProgressDialog = new CustomProgressDialog(mContext);
        gigs_id = SessionHandler.getInstance().get(mContext, Constants.GIGS_ID);
        mDetailGigsReview = (ListView) view.findViewById(R.id.lv_review_gigs);
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            if (SessionHandler.getInstance().get(mContext, Constants.USER_ID) != null) {
                postGigDetails.put("userid", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                postSellerReviews.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
            } else {
                postGigDetails.put("userid", "");
            }
            postGigDetails.put("gig_id", gigs_id);
            getSellerReviews();

        } else {
            Utils.toastMessage(mContext, getString(R.string.err_internet_connection));
        }

        return view;
    }


    private void getSellerReviews() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getSellerReviews(postSellerReviews).enqueue(new Callback<POSTSellerReviews>() {
            @Override
            public void onResponse(Call<POSTSellerReviews> call, Response<POSTSellerReviews> response) {
                if (response.body().getData().size() > 0) {
                    sellerReviewsGigsAdpater = new SellerReviewsGigsAdpater(mContext, response.body().getData());
                    mDetailGigsReview.setAdapter(sellerReviewsGigsAdpater);
                    mCustomProgressDialog.dismiss();
                    Utils.getListViewSize(mDetailGigsReview);
                }
            }

            @Override
            public void onFailure(Call<POSTSellerReviews> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }


}
