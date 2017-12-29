package dreamguys.in.co.gigs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.MainActivity;
import dreamguys.in.co.gigs.Model.AddExtraGigs;
import dreamguys.in.co.gigs.Model.GETCategory;
import dreamguys.in.co.gigs.Model.POSTCreategigs;
import dreamguys.in.co.gigs.Model.POSTExtraGigs;
import dreamguys.in.co.gigs.Model.POSTSubCategory;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.InputFilterMinMax;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dreamguys.in.co.gigs.R.id.input_fast_extras;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellFragment.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View sellViewLayout;

    private EditText titleGigs, gigsDeliverDay, gigsCosts, gigsDesc,
            gigsFastextras, gigsFastextrasCost, gigsFastextrasDay,
            gigsRequirement, extrasTitle, extrasCost, extrasDay;
    private LinearLayout llMoreGigs;
    private ImageView closeMoreExtras, doneExtras;

    private Spinner spinCategory, spinSubCategory;

    private TextView addMoreGigs;

    private Button createGigs;
    private CustomProgressDialog mCustomProgressDialog;
    private OnFragmentInteractionListener mListener;

    private int count;
    private POSTExtraGigs mPOSTExtraGigs = new POSTExtraGigs();
    List<POSTExtraGigs> arrExtrasGigs = new ArrayList<POSTExtraGigs>();
    private HashMap<String, String> postgigsDetails = new HashMap<String, String>();
    private String fastExtras = "", category_id = "";
    private Gson mGson;
    ArrayList<String> subCategoryArray = new ArrayList<String>();
    ArrayList<String> categoryArray = new ArrayList<String>();
    private HashMap<String, String> postCategoryDetails = new HashMap<String, String>();
    List<AddExtraGigs> arrAddExtrasGigs = new ArrayList<AddExtraGigs>();
    Handler mHandler;
    LinearLayout LnrChildLayout;
    CheckBox mCheckBox;


    public SellFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellFragment newInstance(String param1, String param2) {
        SellFragment fragment = new SellFragment();
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
        mCustomProgressDialog = new CustomProgressDialog(getActivity());
        sellViewLayout = inflater.inflate(R.layout.fragment_sell, container, false);
        mGson = new Gson();

        mHandler = new Handler();

        titleGigs = (EditText) sellViewLayout.findViewById(R.id.input_title_gigs);
        gigsDeliverDay = (EditText) sellViewLayout.findViewById(R.id.input_deliver_day);
        gigsCosts = (EditText) sellViewLayout.findViewById(R.id.input_gig_cost);
        gigsDesc = (EditText) sellViewLayout.findViewById(R.id.input_desc);
        extrasCost = (EditText) sellViewLayout.findViewById(R.id.input_extras_cost);
        extrasDay = (EditText) sellViewLayout.findViewById(R.id.input_extras_day);
        gigsFastextras = (EditText) sellViewLayout.findViewById(input_fast_extras);
        gigsFastextrasCost = (EditText) sellViewLayout.findViewById(R.id.input_fast_extras_cost);
        gigsFastextrasDay = (EditText) sellViewLayout.findViewById(R.id.input_fast_extras_day);
        gigsRequirement = (EditText) sellViewLayout.findViewById(R.id.input_requirement);
        createGigs = (Button) sellViewLayout.findViewById(R.id.button_create_gigs);
        addMoreGigs = (TextView) sellViewLayout.findViewById(R.id.tv_add_more_items);
        llMoreGigs = (LinearLayout) sellViewLayout.findViewById(R.id.ll_extras);
        spinCategory = (Spinner) sellViewLayout.findViewById(R.id.spinner_category);
        spinSubCategory = (Spinner) sellViewLayout.findViewById(R.id.spinner_sub_category);
        mCheckBox = (CheckBox) sellViewLayout.findViewById(R.id.cb_agree_conditions);

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            getCategoryList();
        } else {
            Utils.toastMessage(getActivity(), getString(R.string.err_internet_connection));
        }


        if (gigsDeliverDay.getText().toString().isEmpty()) {
            handleFastExtrasFocus(false);
        } else {
            handleFastExtrasFocus(true);
        }

        gigsDeliverDay.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (!s.toString().isEmpty()) {
                    if (s.length() != 0) {
                        handleFastExtrasFocus(true);
                        fastExtras = "yes";
                        gigsFastextrasDay.setFilters(new InputFilter[]{new InputFilterMinMax("1", s.toString())});
                    } else {
                        handleFastExtrasFocus(false);
                        fastExtras = "no";
                        gigsFastextras.setText("");
                        gigsFastextrasCost.setText("");
                        gigsFastextrasDay.setText("");
                    }
                } else {
                    handleFastExtrasFocus(false);
                    fastExtras = "no";
                    gigsFastextras.setText("");
                    gigsFastextrasCost.setText("");
                    gigsFastextrasDay.setText("");
                }
            }
        });

       /* gigsFastextrasDay.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                gigsFastextrasDay.setFilters(new InputFilter[]{new InputFilterMinMax("1", gigsDeliverDay.getText().toString())});
            }
        });*/


        addMoreGigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llMoreGigs.indexOfChild(LnrChildLayout) <= 8) {
                    if (count <= 9) {
                        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LnrChildLayout = (LinearLayout) mInflater
                                .inflate(R.layout.activity_add_more_items, null);
                        LnrChildLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        llMoreGigs.addView(LnrChildLayout);
                        closeMoreExtras = (ImageView) LnrChildLayout.findViewById(R.id.iv_close);
                        extrasTitle = (EditText) LnrChildLayout.findViewById(R.id.input_sub_extras);
                        extrasCost = (EditText) LnrChildLayout.findViewById(R.id.input_sub_extras_cost);
                        extrasDay = (EditText) LnrChildLayout.findViewById(R.id.input_sub_extras_day);
                        doneExtras = (ImageView) LnrChildLayout.findViewById(R.id.iv_done);

                        doneExtras.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (LnrChildLayout != null) {
                                    if (!extrasDay.getText().toString().isEmpty() &&
                                            !extrasTitle.getText().toString().isEmpty() &&
                                            !extrasCost.getText().toString().isEmpty()) {
                                        mPOSTExtraGigs = new POSTExtraGigs();
                                        mPOSTExtraGigs.setExtra_gigs(extrasTitle.getText().toString());
                                        mPOSTExtraGigs.setExtra_gigs_amount(extrasCost.getText().toString());
                                        mPOSTExtraGigs.setExtra_gigs_delivery(extrasDay.getText().toString());
                                        count = llMoreGigs.indexOfChild(LnrChildLayout);
                                        mPOSTExtraGigs.setPosition(count);
                                        LnrChildLayout.setTag(count);
                                        arrExtrasGigs.add(count, mPOSTExtraGigs);
                                        closeMoreExtras.setVisibility(View.VISIBLE);
                                        doneExtras.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                        closeMoreExtras.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = Integer.parseInt(LnrChildLayout.getTag().toString());
                                for (int i = 0; i < arrExtrasGigs.size(); i++) {
                                    if (arrExtrasGigs.get(i).getPosition() == pos) {
                                        arrExtrasGigs.remove(i);
                                        llMoreGigs.removeViewAt(i);
                                    }
                                }
                                addMoreGigs.setVisibility(View.VISIBLE);
                                count--;
                            }
                        });
                    }
                } else {
                    /*addMoreGigs.setVisibility(View.GONE);*/
                    Toast.makeText(getActivity(), "Limit exceeded", Toast.LENGTH_SHORT).show();
                    addMoreGigs.setClickable(false);


                    closeMoreExtras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count--;
                            llMoreGigs.removeViewAt(count);
                            addMoreGigs.setVisibility(View.VISIBLE);
                        }
                    });

                }

            }
        });

        createGigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleGigs.getText().toString().isEmpty()) {
                    titleGigs.setError(getString(R.string.err_gigs_title));
                    titleGigs.requestFocus();
                    return;
                }

                if (gigsDeliverDay.getText().toString().isEmpty()) {
                    gigsDeliverDay.setError(getString(R.string.err_gigs_delivery_day));
                    gigsDeliverDay.requestFocus();
                    return;
                }
                if (gigsCosts.getText().toString().isEmpty()) {
                    gigsCosts.setError(getString(R.string.err_gigs_costs));
                    gigsCosts.requestFocus();
                    return;
                }
                if (gigsDesc.getText().toString().isEmpty()) {
                    gigsDesc.setError(getString(R.string.err_gigs_desc));
                    gigsDesc.requestFocus();
                    return;
                }
                if (gigsDesc.getText().toString().isEmpty()) {
                    gigsDesc.setError(getString(R.string.err_gigs_desc));
                    gigsDesc.requestFocus();
                    return;
                }

                if (gigsRequirement.getText().toString().isEmpty()) {
                    gigsRequirement.setError(getString(R.string.err_gigs_require));
                    gigsRequirement.requestFocus();
                    return;
                }

                if (gigsFastextras.getText().toString().isEmpty() &&
                        gigsFastextras.getText().toString().isEmpty() &&
                        gigsFastextras.getText().toString().isEmpty()) {

                    fastExtras = "No";
                } else {
                    fastExtras = "Yes";
                }

                if (!mCheckBox.isChecked()) {
                    Toast.makeText(getActivity(), "Please accept the terms & conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (NetworkChangeReceiver.isConnected()) {
                    mCustomProgressDialog.showDialog();
                    postgigsDetails.put("user_id", SessionHandler.getInstance().get(getActivity(), Constants.USER_ID));
                    postgigsDetails.put("title", titleGigs.getText().toString());
                    postgigsDetails.put("delivering_time", gigsDeliverDay.getText().toString());
                    postgigsDetails.put("gig_price", gigsCosts.getText().toString());
                    postgigsDetails.put("image", "noimage.jpg");
                    postgigsDetails.put("gig_tags", "");
                    postgigsDetails.put("category_id", category_id);
                    postgigsDetails.put("gig_details", gigsDesc.getText().toString());
                    postgigsDetails.put("super_fast_delivery", fastExtras);
                    postgigsDetails.put("super_fast_delivery_desc", gigsFastextras.getText().toString());
                    postgigsDetails.put("super_fast_delivery_date", gigsFastextrasDay.getText().toString());
                    postgigsDetails.put("super_fast_charges", gigsFastextrasCost.getText().toString());
                    postgigsDetails.put("requirements", gigsRequirement.getText().toString());
                    postgigsDetails.put("work_option", "1");
                    postgigsDetails.put("terms_conditions", "1");

                    if (arrExtrasGigs.size() > 0) {
                        for (int i = 0; i < arrExtrasGigs.size(); i++) {
                            AddExtraGigs mAddExtraGigs = new AddExtraGigs();
                            mAddExtraGigs.setExtra_gigs(arrExtrasGigs.get(i).getExtra_gigs());
                            mAddExtraGigs.setExtra_gigs_amount(arrExtrasGigs.get(i).getExtra_gigs_amount());
                            mAddExtraGigs.setExtra_gigs_delivery(arrExtrasGigs.get(i).getExtra_gigs_delivery());
                            arrAddExtrasGigs.add(mAddExtraGigs);
                        }
                    }

                    String jsonString = mGson.toJson(arrAddExtrasGigs);
                    Log.i("EXTRAS------> ", jsonString);
                    postgigsDetails.put("extra_gigs", jsonString);
                    postgigsDetails.put("time_zone", SessionHandler.getInstance().get(getActivity(), Constants.TIMEZONE_ID));

                    creategigs();
                } else {
                    Utils.toastMessage(getActivity(), getString(R.string.err_internet_connection));
                }
            }
        });


        return sellViewLayout;
    }

    public void termsOfConditions(View view) {
        Toast.makeText(getActivity(), "Terms and conditions..", Toast.LENGTH_SHORT).show();
    }

    private void getCategoryList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getCategories().enqueue(new Callback<GETCategory>() {
            @Override
            public void onResponse(Call<GETCategory> call, final Response<GETCategory> response) {

                if (response.body().getCode().equals(200)) {
                    if (response.body().getPrimary().size() > 0) {

                        for (int i = 0; i < response.body().getPrimary().size(); i++) {
                            if (!categoryArray.contains(response.body().getPrimary().get(i).getName())) {
                                categoryArray.add(response.body().getPrimary().get(i).getName());
                            }
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, categoryArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinCategory.setAdapter(adapter);

                        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                spinSubCategory.clearFocus();
                                if (categoryArray.get(position).equalsIgnoreCase(response.body().getPrimary().get(position).getName())) {
                                    category_id = response.body().getPrimary().get(position).getCid();
                                    if (!category_id.isEmpty()) {
                                        if (!category_id.isEmpty()) {
                                            postCategoryDetails.put("category_id", category_id);
                                        }
                                        if (SessionHandler.getInstance().get(getActivity(), Constants.USER_ID) != null) {
                                            postCategoryDetails.put("user_id", SessionHandler.getInstance().get(getActivity(), Constants.USER_ID));
                                        }
                                        postCategoryDetails.put("services", "All");
                                        if (response.body().getPrimary().get(position).getSubcategory().equalsIgnoreCase("1")) {
                                            spinSubCategory.setVisibility(View.VISIBLE);
                                            getSubCategory();
                                        } else {
                                            spinSubCategory.setVisibility(View.GONE);
                                        }

                                    }


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        Utils.toastMessage(getActivity(), response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(getActivity(), response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GETCategory> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }

    private void getSubCategory() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getSubCategory(postCategoryDetails).enqueue(new Callback<POSTSubCategory>() {
            @Override
            public void onResponse(Call<POSTSubCategory> call, final Response<POSTSubCategory> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (!subCategoryArray.contains(response.body().getData().get(i).getName())) {
                                subCategoryArray.add(response.body().getData().get(i).getName());
                            }
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, subCategoryArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinSubCategory.setAdapter(adapter);
                        spinSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                spinSubCategory.clearFocus();
                                if (subCategoryArray.get(position).equalsIgnoreCase(response.body().getData().get(position).getName())) {
                                    category_id = response.body().getData().get(position).getCid();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else {
                        Utils.toastMessage(getActivity(), response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(getActivity(), response.body().getMessage());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTSubCategory> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });

    }

    private void creategigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.createGigs(postgigsDetails).enqueue(new Callback<POSTCreategigs>() {
            @Override
            public void onResponse(Call<POSTCreategigs> call, Response<POSTCreategigs> response) {
                if (response.body().getCode().equals(200)) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).callHome();
                        }
                    }, 3000);
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTCreategigs> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });

    }

    private void handleFastExtrasFocus(boolean type) {
        gigsFastextras.setFocusable(type);
        gigsFastextras.setFocusableInTouchMode(type);
        gigsFastextras.setClickable(type);
        gigsFastextrasCost.setFocusable(type);
        gigsFastextrasCost.setFocusableInTouchMode(type);
        gigsFastextrasCost.setClickable(type);
        gigsFastextrasDay.setFocusable(type);
        gigsFastextrasDay.setFocusableInTouchMode(type);
        gigsFastextrasDay.setClickable(type);
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
