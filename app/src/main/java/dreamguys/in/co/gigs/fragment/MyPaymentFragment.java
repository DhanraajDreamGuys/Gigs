package dreamguys.in.co.gigs.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.MyApplication;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.MyPaymentAdapter;
import dreamguys.in.co.gigs.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPaymentFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link MyPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPaymentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View myPaymentViews;
    RecyclerView recyclerViewPurchase;
    //    SwipeRefreshLayout mSwipeRefreshLayout;
    MyPaymentAdapter aMyPaymentAdapter;
    TextView currentAmount, mNoDataFound;
    private BroadcastReceiver myBroadcastReceiver;
    Context mContext;


    public MyPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPaymentFragment newInstance(String param1, String param2) {
        MyPaymentFragment fragment = new MyPaymentFragment();
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
        myPaymentViews = inflater.inflate(R.layout.fragment_my_payment, container, false);
        recyclerViewPurchase = (RecyclerView) myPaymentViews.findViewById(R.id.rv_purchase_recyclerview);
        currentAmount = (TextView) myPaymentViews.findViewById(R.id.tv_current_bal);
        mNoDataFound = (TextView) myPaymentViews.findViewById(R.id.no_data_found);

        PaymentUpdate();
        IntentFilter intentFilter = new IntentFilter(MyApplication.ACTION_MyUpdate);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.registerReceiver(myBroadcastReceiver, intentFilter);


        if (getArguments() != null) {
            List<POSTMyActivity.My_payment> my_payment_array = getArguments().getParcelableArrayList(Constants.MY_PAYMENTARRAY_KEY);
            if (my_payment_array != null) {
                if (my_payment_array.size() > 0) {
                    mNoDataFound.setVisibility(View.GONE);
                    recyclerViewPurchase.setVisibility(View.VISIBLE);
                    currentAmount.setVisibility(View.VISIBLE);
                    currentAmount.setText("Available Funds: " + Constants.DOLLAR_SIGN + getArguments().getString(Constants.WALLET_BALANCE));
                    aMyPaymentAdapter = new MyPaymentAdapter(getActivity(), my_payment_array, currentAmount, getArguments().getString(Constants.WALLET_BALANCE), getChildFragmentManager());
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewPurchase.setLayoutManager(horizontalLayoutManagaer);
                    recyclerViewPurchase.setHasFixedSize(true);
                    recyclerViewPurchase.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPurchase.setAdapter(aMyPaymentAdapter);
                } else {
                    mNoDataFound.setVisibility(View.VISIBLE);
                    recyclerViewPurchase.setVisibility(View.GONE);
                    currentAmount.setVisibility(View.GONE);

                }
            }

        } else {
            mNoDataFound.setVisibility(View.VISIBLE);
            recyclerViewPurchase.setVisibility(View.GONE);
            currentAmount.setVisibility(View.GONE);
        }

        return myPaymentViews;
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
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (myBroadcastReceiver != null) {
            mContext.unregisterReceiver(myBroadcastReceiver);
        }
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myBroadcastReceiver != null) {
            mContext.unregisterReceiver(myBroadcastReceiver);
        }
    }

    public void PaymentUpdate() {
        myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra(MyApplication.EXTRA_KEY_UPDATE);
                if (!result.isEmpty()) {

                    try {
                        JSONObject mJSONObject = new JSONObject(result);
                        currentAmount.setText("Available Funds: " + mJSONObject.getString("balance"));
                        Log.i("TAG JSONRESULT ---->", mJSONObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    /*@Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MyApplication.ACTION_MyUpdate);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(myBroadcastReceiver, intentFilter);
    }*/

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
