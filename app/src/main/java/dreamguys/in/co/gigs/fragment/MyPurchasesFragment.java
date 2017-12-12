package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.MyPurchaseAdapter;
import dreamguys.in.co.gigs.dialog.PurchaseCancelDialogFragment;
import dreamguys.in.co.gigs.dialog.PurchaseSeeFeedbackDialog;
import dreamguys.in.co.gigs.dialog.SalesSeeFeedbackDialog;
import dreamguys.in.co.gigs.interfaces.UpdateRequestData;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPurchasesFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link MyPurchasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPurchasesFragment extends Fragment implements UpdateRequestData {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    List<POSTMyActivity.My_purchase> myPurchaseArray;
    View purchaseFragment;

    RecyclerView recyclerViewPurchase;
    MyPurchaseAdapter aMyPurchaseAdapter;
//    SwipeRefreshLayout mSwipeRefreshLayout;

    MyPurchasesFragment mMyPurchasesFragment;

    CustomProgressDialog mCustomProgressDialog;
    TextView mNoDataFound;

    public MyPurchasesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPurchasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPurchasesFragment newInstance(String param1, String param2) {
        MyPurchasesFragment fragment = new MyPurchasesFragment();
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
//        setUserVisibleHint(true);
        mMyPurchasesFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        purchaseFragment = inflater.inflate(R.layout.fragment_my_purchases, container, false);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) purchaseFragment.findViewById(R.id.swipe_refresh_layout);
        recyclerViewPurchase = (RecyclerView) purchaseFragment.findViewById(R.id.rv_purchase_recyclerview);
        mNoDataFound = (TextView) purchaseFragment.findViewById(R.id.no_data_found);

        if (getArguments() != null) {
            myPurchaseArray = getArguments().getParcelableArrayList(Constants.MY_PURARRAY_KEY);
            PurchaseCancelDialogFragment.callDialgFrag(mMyPurchasesFragment);
            PurchaseSeeFeedbackDialog.callDialgFrag(mMyPurchasesFragment);
            SalesSeeFeedbackDialog.callDialgFrag(mMyPurchasesFragment);
            if (myPurchaseArray != null)
                if (myPurchaseArray.size() > 0) {
                    mNoDataFound.setVisibility(View.GONE);
                    recyclerViewPurchase.setVisibility(View.VISIBLE);
                    aMyPurchaseAdapter = new MyPurchaseAdapter(getActivity(), myPurchaseArray, getChildFragmentManager());
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewPurchase.setLayoutManager(horizontalLayoutManagaer);
                    recyclerViewPurchase.setHasFixedSize(true);
                    recyclerViewPurchase.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPurchase.setAdapter(aMyPurchaseAdapter);
                } else {
                    mNoDataFound.setVisibility(View.VISIBLE);
                    recyclerViewPurchase.setVisibility(View.GONE);
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
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void UpdateFeedbackRequest(String order_id) {
        for (int position = 0; position < myPurchaseArray.size(); position++) {
            if (myPurchaseArray.get(position).getOrder_id().equalsIgnoreCase(order_id)) {
                POSTMyActivity.My_purchase newModifiedItem = myPurchaseArray.get(position);
                newModifiedItem.setFeedback("See Feedback");
                newModifiedItem.setFeedback_val(2);
                myPurchaseArray.set(position, newModifiedItem);
                aMyPurchaseAdapter.notifyItemChanged(position);
            }
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

    @Override
    public void updateRequestData(String order_id) {
        for (int position = 0; position < myPurchaseArray.size(); position++) {
            if (myPurchaseArray.get(position).getOrder_id().equalsIgnoreCase(order_id)) {
                POSTMyActivity.My_purchase newModifiedItem = myPurchaseArray.get(position);
                newModifiedItem.setOrder_cancel_val(2);
                newModifiedItem.setOrder_cancel("Request send");
                myPurchaseArray.set(position, newModifiedItem);
                aMyPurchaseAdapter.notifyItemChanged(position);
            }
        }
    }


}
