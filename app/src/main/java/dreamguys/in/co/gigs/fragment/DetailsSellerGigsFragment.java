package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dreamguys.in.co.gigs.Model.GETMyGigs;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.MyGigsListAdapter;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomGridView;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user5 on 06-11-2017.
 */

public class DetailsSellerGigsFragment extends Fragment {


    View view;
    CustomGridView gridGigsList;
    private MyGigsListAdapter myGigsListAdapter;
    private Toolbar toolbar;
    Context mContext;

    public DetailsSellerGigsFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details_seller_gigs, container, false);
        gridGigsList = (CustomGridView) view.findViewById(R.id.customGridview);
      /*  toolbar = (Toolbar) view.findViewById(R.id.tb_toolbar);
        toolbar.setVisibility(View.GONE);*/
        getMyGigs();
        return view;
    }

    private void getMyGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getMyGigs(SessionHandler.getInstance().get(mContext, Constants.USER_ID)).enqueue(new Callback<GETMyGigs>() {
            @Override
            public void onResponse(Call<GETMyGigs> call, Response<GETMyGigs> response) {
                if (response.body().getCode().equals(200)) {
                    myGigsListAdapter = new MyGigsListAdapter(mContext, response.body().getData());
                    gridGigsList.setAdapter(myGigsListAdapter);
                    Utils.getGridViewSize(gridGigsList);
                }
            }

            @Override
            public void onFailure(Call<GETMyGigs> call, Throwable t) {

            }
        });
    }

}
