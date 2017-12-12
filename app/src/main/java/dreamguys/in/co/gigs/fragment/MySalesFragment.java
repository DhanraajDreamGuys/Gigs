package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.MySalesAdapter;
import dreamguys.in.co.gigs.dialog.SaleStatusDialog;
import dreamguys.in.co.gigs.interfaces.UpdateSalesData;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MySalesFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link MySalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySalesFragment extends Fragment implements UpdateSalesData {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View purchaseFragment;
    MySalesAdapter aMySalesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerViewPurchase;
    List<POSTMyActivity.My_sale> my_sale_array;
    TextView mNoDataFound;

    public MySalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySalesFragment newInstance(String param1, String param2) {
        MySalesFragment fragment = new MySalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        purchaseFragment = inflater.inflate(R.layout.fragment_my_sales, container, false);
        // Inflate the layout for this fragment
        recyclerViewPurchase = (RecyclerView) purchaseFragment.findViewById(R.id.rv_purchase_recyclerview);
        mNoDataFound = (TextView) purchaseFragment.findViewById(R.id.no_data_found);


        if (getArguments() != null) {
            my_sale_array = getArguments().getParcelableArrayList(Constants.MY_SALEARRAY_KEY);
            SaleStatusDialog.callDialgFrag(this);
            if (my_sale_array != null) {
                if (my_sale_array.size() > 0) {
                    mNoDataFound.setVisibility(View.GONE);
                    recyclerViewPurchase.setVisibility(View.VISIBLE);
                    aMySalesAdapter = new MySalesAdapter(getActivity(), my_sale_array, getChildFragmentManager());
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewPurchase.setLayoutManager(horizontalLayoutManagaer);
                    recyclerViewPurchase.setHasFixedSize(true);
                    recyclerViewPurchase.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPurchase.setAdapter(aMySalesAdapter);
                } else {
                    mNoDataFound.setVisibility(View.VISIBLE);
                    recyclerViewPurchase.setVisibility(View.GONE);
                }
            }
        } else {
            mNoDataFound.setVisibility(View.VISIBLE);
            recyclerViewPurchase.setVisibility(View.GONE);
        }

        return purchaseFragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void updateRequestData() {
        getSaleData();
    }

    private void getSaleData() {
//        mSwipeRefreshLayout.setRefreshing(true);
        recyclerViewPurchase.removeAllViews();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (SessionHandler.getInstance().get(getActivity(), Constants.USER_ID) != null) {
            apiInterface.getMyActivity(SessionHandler.getInstance().get(getActivity(), Constants.USER_ID)).enqueue(new Callback<POSTMyActivity>() {
                @Override
                public void onResponse(Call<POSTMyActivity> call, Response<POSTMyActivity> response) {
//                    mSwipeRefreshLayout.setRefreshing(false);
                    if (response.body().getCode().equals(200)) {
                        if (response.body().getData().getMy_sale().size() > 0) {
                            aMySalesAdapter = new MySalesAdapter(getActivity(), response.body().getData().getMy_sale(), getChildFragmentManager());
                            recyclerViewPurchase.setAdapter(aMySalesAdapter);
                        }

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<POSTMyActivity> call, Throwable t) {
                    Log.i("TAG", t.getMessage());
//                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    /**
     * This interfaces must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
