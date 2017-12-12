package dreamguys.in.co.gigs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import dreamguys.in.co.gigs.Model.AddExtraGigs;
import dreamguys.in.co.gigs.Model.GETCategory;
import dreamguys.in.co.gigs.Model.POSTCreategigs;
import dreamguys.in.co.gigs.Model.POSTEditGigs;
import dreamguys.in.co.gigs.Model.POSTExtraGigs;
import dreamguys.in.co.gigs.Model.POSTSubCategory;
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
 * Created by Prasad on 11/6/2017.
 */

public class EditGigs extends AppCompatActivity {

    private EditText titleGigs, gigsDeliverDay, gigsCosts, gigsDesc,
            gigsFastextras, gigsFastextrasCost, gigsFastextrasDay,
            gigsRequirement, extrasTitle, extrasCost, extrasDay;
    private int count;
    private POSTExtraGigs mPOSTExtraGigs = new POSTExtraGigs();
    List<POSTExtraGigs> arrExtrasGigs = new ArrayList<POSTExtraGigs>();
    List<AddExtraGigs> arrAddExtrasGigs = new ArrayList<AddExtraGigs>();
    private String gigs_id;

    private LinearLayout llMoreGigs;
    private ImageView closeMoreExtras, doneExtras;

    private Spinner spinCategory, spinSubCategory;

    private TextView addMoreGigs;

    private Toolbar mToolbar;

    private String category_id = "", fastExtras = "", fastExtrastitle = "", fastExtrasCost = "", fastExtrasDay = "";

    private CustomProgressDialog mCustomProgressDialog;
    private HashMap<String, String> postGigsDetails = new HashMap<String, String>();
    private HashMap<String, String> postCategoryDetails = new HashMap<String, String>();
    ArrayList<String> subCategoryArray = new ArrayList<String>();
    ArrayList<String> categoryArray = new ArrayList<String>();
    private HashMap<String, String> postgigsDetails = new HashMap<String, String>();
    private Gson mGson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gigs);
        mCustomProgressDialog = new CustomProgressDialog(this);
        mGson = new Gson();

        if (getIntent().getStringExtra(Constants.GIGS_ID)!=null) {
            gigs_id = getIntent().getStringExtra(Constants.GIGS_ID);
        } else {
            gigs_id = "";
        }

        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);

        titleGigs = (EditText) findViewById(R.id.input_title_gigs);
        gigsDeliverDay = (EditText) findViewById(R.id.input_deliver_day);
        gigsCosts = (EditText) findViewById(R.id.input_gig_cost);
        gigsDesc = (EditText) findViewById(R.id.input_desc);
        extrasCost = (EditText) findViewById(R.id.input_extras_cost);
        extrasDay = (EditText) findViewById(R.id.input_extras_day);
        gigsFastextras = (EditText) findViewById(input_fast_extras);
        gigsFastextrasCost = (EditText) findViewById(R.id.input_fast_extras_cost);
        gigsFastextrasDay = (EditText) findViewById(R.id.input_fast_extras_day);
        gigsRequirement = (EditText) findViewById(R.id.input_requirement);
        addMoreGigs = (TextView) findViewById(R.id.tv_add_more_items);
        llMoreGigs = (LinearLayout) findViewById(R.id.ll_extras);
        spinCategory = (Spinner) findViewById(R.id.spinner_category);
        spinSubCategory = (Spinner) findViewById(R.id.spinner_sub_category);


        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                postGigsDetails.put("user_id", SessionHandler.getInstance().get(this, Constants.USER_ID));
                postGigsDetails.put("gig_id", "1");
            }

            getEditGigs();
        } else {
            Utils.toastMessage(EditGigs.this, getString(R.string.err_internet_connection));
        }

        if (gigsDeliverDay.getText().toString().isEmpty()) {
            handleFastExtrasFocus(false);
        } else {
            handleFastExtrasFocus(true);
        }

        gigsDeliverDay.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    handleFastExtrasFocus(true);
                    fastExtras = "yes";
                } else {
                    handleFastExtrasFocus(false);
                    fastExtras = "no";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                if (s.length() != 0) {
                    handleFastExtrasFocus(true);
                    fastExtras = "yes";
                } else {
                    handleFastExtrasFocus(false);
                    fastExtras = "no";
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        gigsFastextrasDay.addTextChangedListener(new TextWatcher() {

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
        });


        addMoreGigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 9) {
                    LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final LinearLayout LnrChildLayout = (LinearLayout) mInflater
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
                } else {
                    addMoreGigs.setVisibility(View.GONE);
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


    }

    private void getEditGigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.editGigs(postGigsDetails).enqueue(new Callback<POSTEditGigs>() {
            @Override
            public void onResponse(Call<POSTEditGigs> call, Response<POSTEditGigs> response) {
                if (response.body().getCode().equals(200)) {
                    if (response.body().getData().size() > 0) {

                        POSTEditGigs.Datum data = response.body().getData().get(0);
                        titleGigs.setText(data.getGig_details().getTitle());
                        gigsDeliverDay.setText(data.getGig_details().getDelivering_time());
                        gigsCosts.setText(data.getGig_details().getGig_price());
                        gigsDesc.setText(data.getGig_details().getGig_details());
                        getCategoryList(data.getGig_details());

                        if (!data.getGig_details().getSub_category_id().equalsIgnoreCase("0")) {
                            spinSubCategory.setVisibility(View.GONE);

                        }

                        fastExtras = data.getGig_details().getSuper_fast_charges();
                        gigsFastextras.setText(data.getGig_details().getSuper_fast_delivery_desc());
                        gigsFastextrasCost.setText(data.getGig_details().getSuper_fast_charges());
                        if (!data.getGig_details().getSuper_fast_delivery_date().equalsIgnoreCase("0")) {
                            gigsFastextrasDay.setText(data.getGig_details().getSuper_fast_delivery_date());
                        } else {
                            gigsFastextrasDay.setText("");
                        }
                        if (!data.getGig_details().getSuper_fast_charges().equalsIgnoreCase("0")) {
                            gigsFastextrasCost.setText(data.getGig_details().getSuper_fast_charges());
                        } else {
                            gigsFastextrasCost.setText("");
                        }

                        gigsRequirement.setText(data.getGig_details().getRequirements());
                        if (data.getExtra_gigs().size() > 0) {
                            for (int i = 0; i < data.getExtra_gigs().size(); i++) {
                                POSTEditGigs.Extra_gig extra_gig_detail = data.getExtra_gigs().get(i);
                                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final LinearLayout LnrChildLayout = (LinearLayout) mInflater
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

                                extrasTitle.setText(extra_gig_detail.getExtra_gigs());
                                extrasCost.setText(extra_gig_detail.getExtra_gigs_amount());
                                extrasDay.setText(extra_gig_detail.getExtra_gigs_delivery());

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

                        }
                    } else {
                        Toast.makeText(EditGigs.this, "No data found...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditGigs.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTEditGigs> call, Throwable t) {
                mCustomProgressDialog.dismiss();
                Log.i("TAG", t.getMessage());
            }
        });
    }


    private void getCategoryList(final POSTEditGigs.Gig_details gig_details) {
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
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditGigs.this,
                                android.R.layout.simple_spinner_item, categoryArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinCategory.setAdapter(adapter);

                        if (gig_details != null) {
                            for (int i = 0; i < response.body().getPrimary().size(); i++) {
                                if (gig_details.getCategory_id().equalsIgnoreCase(response.body().getPrimary().get(i).getCid())) {
                                    spinCategory.setSelection(i);
                                    category_id = response.body().getPrimary().get(i).getCid();
                                }

                            }
                        }


                        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                spinSubCategory.clearFocus();
                                if (categoryArray.get(position).equalsIgnoreCase(response.body().getPrimary().get(position).getName())) {
                                    category_id = response.body().getPrimary().get(position).getCid();
                                    if (!category_id.isEmpty()) {
                                        postCategoryDetails.put("category_id", category_id);
                                        if (SessionHandler.getInstance().get(EditGigs.this, Constants.USER_ID) != null) {
                                            postCategoryDetails.put("user_id", SessionHandler.getInstance().get(EditGigs.this, Constants.USER_ID));
                                        }
                                        postCategoryDetails.put("services", "All");
                                        if (response.body().getPrimary().get(position).getSubcategory().equalsIgnoreCase("1")) {
                                            spinSubCategory.setVisibility(View.VISIBLE);
                                            getSubCategory(gig_details);
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
                        Utils.toastMessage(EditGigs.this, response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(EditGigs.this, response.body().getMessage());
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

    private void getSubCategory(final POSTEditGigs.Gig_details gig_details) {
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
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditGigs.this,
                                android.R.layout.simple_spinner_item, subCategoryArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinSubCategory.setAdapter(adapter);

                        if (gig_details != null) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                if (gig_details.getCategory_id().equalsIgnoreCase(response.body().getData().get(i).getCid())) {
                                    spinSubCategory.setSelection(i);
                                    category_id = response.body().getData().get(i).getCid();
                                }

                            }
                        }

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
                        Utils.toastMessage(EditGigs.this, response.body().getMessage());
                    }

                } else {
                    Utils.toastMessage(EditGigs.this, response.body().getMessage());
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

    public void EditGigs(View view) {
        if (titleGigs.getText().toString().isEmpty()) {
            titleGigs.setError(getString(R.string.err_gigs_title));
            return;
        }

        if (gigsDeliverDay.getText().toString().isEmpty()) {
            gigsDeliverDay.setError(getString(R.string.err_gigs_delivery_day));
            return;
        }
        if (gigsCosts.getText().toString().isEmpty()) {
            gigsCosts.setError(getString(R.string.err_gigs_costs));
            return;
        }
        if (gigsDesc.getText().toString().isEmpty()) {
            gigsDesc.setError(getString(R.string.err_gigs_desc));
            return;
        }

        if (gigsRequirement.getText().toString().isEmpty()) {
            gigsRequirement.setError(getString(R.string.err_gigs_require));
            return;
        }


        if (gigsFastextras.getText().toString().isEmpty() &&
                gigsFastextrasCost.getText().toString().isEmpty() &&
                gigsFastextrasDay.getText().toString().isEmpty()) {
            fastExtras = "No";
            fastExtrasCost = "";
            fastExtrasDay = "";
            fastExtrastitle = "";
        } else {
            fastExtras = "Yes";
            if (!gigsFastextrasDay.getText().toString().isEmpty() && !gigsFastextrasCost.getText().toString().isEmpty() &&
                    !gigsFastextras.getText().toString().isEmpty()
                    ) {
                fastExtrasCost = gigsFastextrasCost.getText().toString();
                fastExtrasDay = gigsFastextrasDay.getText().toString();
                fastExtrastitle = gigsFastextras.getText().toString();

            } else {
                Toast.makeText(this, "Missing fields in Fast extras options...", Toast.LENGTH_SHORT).show();
            }
        }


        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postgigsDetails.put("gig_id", "1");
            postgigsDetails.put("user_id", SessionHandler.getInstance().get(this, Constants.USER_ID));
            postgigsDetails.put("title", titleGigs.getText().toString());
            postgigsDetails.put("delivering_time", gigsDeliverDay.getText().toString());
            postgigsDetails.put("gig_price", gigsCosts.getText().toString());
            postgigsDetails.put("image", "noimage.jpg");
            postgigsDetails.put("gig_tags", "");
            postgigsDetails.put("category_id", category_id);
            postgigsDetails.put("gig_details", gigsDesc.getText().toString());
            postgigsDetails.put("super_fast_delivery", fastExtras);
            postgigsDetails.put("super_fast_delivery_desc", fastExtrastitle);
            postgigsDetails.put("super_fast_delivery_date", fastExtrasDay);
            postgigsDetails.put("super_fast_charges", fastExtrasCost);
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
            postgigsDetails.put("extra_gigs", jsonString);

            creategigs();
        } else {
            Utils.toastMessage(this, getString(R.string.err_internet_connection));
        }
    }

    private void creategigs() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.updateGigs(postgigsDetails).enqueue(new Callback<POSTCreategigs>() {
            @Override
            public void onResponse(Call<POSTCreategigs> call, Response<POSTCreategigs> response) {
                if (response.body().getCode().equals(200)) {
                    Toast.makeText(EditGigs.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditGigs.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
