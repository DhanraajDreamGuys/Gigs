package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.GETAllGigs;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.adapter.AllGigsListsAdapter;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuyFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link BuyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GridView gridGigsWidgets;
    private View inflateGigsLayouts;
    private CustomProgressDialog mCustomProgressDialog;
    private String user_id = "";
    private HashMap<String, String> postDetails = new HashMap<String, String>();
    private AllGigsListsAdapter aAllGigsListsAdapter;

    public BuyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFragment newInstance(String param1, String param2) {
        BuyFragment fragment = new BuyFragment();
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
        // Inflate the layout for this fragment

        inflateGigsLayouts = inflater.inflate(R.layout.fragment_buy, container, false);
        mCustomProgressDialog = new CustomProgressDialog(getActivity());

        gridGigsWidgets = (GridView) inflateGigsLayouts.findViewById(R.id.gv_gigs_list);
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            getGigs();
        } else {
            Utils.toastMessage(getActivity(), getString(R.string.err_internet_connection));
        }


        return inflateGigsLayouts;
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

    private void getGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (SessionHandler.getInstance().get(getActivity(), Constants.USER_ID) != null) {
            user_id = SessionHandler.getInstance().get(getActivity(), Constants.USER_ID);
        } else {
            user_id = "";
        }
        if (!user_id.isEmpty()) {
            postDetails.put("user_id", user_id);
            postDetails.put("services", "ALL");
        } else {
            postDetails.put("services", "ALL");
        }

        apiInterface.getUserGigs(postDetails).enqueue(new Callback<GETAllGigs>() {
            @Override
            public void onResponse(Call<GETAllGigs> call, Response<GETAllGigs> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        if (response.body().getData().size() > 0) {
                            aAllGigsListsAdapter = new AllGigsListsAdapter(getActivity(), response.body().getData());
                            gridGigsWidgets.setAdapter(aAllGigsListsAdapter);
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_ALL_GIGS", response.errorBody().toString());
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
}
