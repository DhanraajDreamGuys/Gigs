package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.GETCountry;
import dreamguys.in.co.gigs.Model.GETProfession;
import dreamguys.in.co.gigs.Model.GETState;
import dreamguys.in.co.gigs.Model.POSTPaypalSettings;
import dreamguys.in.co.gigs.Model.POSTViewProfile;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.MultiSpinner;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/26/2017.
 */

public class UpdateProfile extends AppCompatActivity {
    private final HashMap<String, String> updateProfiles = new HashMap<String, String>();
    private final AlertDialog alert = null;
    private Spinner spinCountry;
    private Spinner spinState;
    private Spinner spinProfession;
    private MultiSpinner spinSpeaks;
    private Gson gson;
    private GETCountry[] getCountryLists;
    private GETProfession[] getProfession;
    private final List<String> addCountryLists = new ArrayList<String>();
    private final List<String> addProfessionLists = new ArrayList<String>();
    private final List<String> addStateLists = new ArrayList<String>();
    private final List<String> addlanguageLists = new ArrayList<String>();
    private String country = "";
    private String state = "";
    private String profession = "";
    private String languages = "";
    private String state_name = "";
    private EditText editPhone;
    private EditText editName;
    private EditText editAddress;
    private EditText editCity;
    private EditText editZipCode;
    private EditText editSuggestions;
    private CustomProgressDialog mCustomProgressDialog;
    private HashMap<String, String> getProfiles = new HashMap<String, String>();
    private int VIEW_TYPE = 0;
    private Toolbar tb_toolbar;
    private TextInputLayout inputLayoutPhone, inputLayoutCity, inputLayoutZipcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        gson = new Gson();
        mCustomProgressDialog = new CustomProgressDialog(this);

        initLayouts();


        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            getProfiles.put("user_id", SessionHandler.getInstance().get(UpdateProfile.this, Constants.USER_ID));
            postProfile();
        } else {
            showToast();
        }

        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset());
            if (obj.length() > 0) {
                for (int i = 0; i < obj.length(); i++) {
                    addlanguageLists.add(obj.getJSONObject(i).get("language").toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (SessionHandler.getInstance().get(UpdateProfile.this, Constants.COUNTRY_JSON) != null) {

            getCountryLists = gson.fromJson(SessionHandler.getInstance().get(UpdateProfile.this, Constants.COUNTRY_JSON), GETCountry[].class);
            getProfession = gson.fromJson(SessionHandler.getInstance().get(UpdateProfile.this, Constants.PROFESSION), GETProfession[].class);
            for (GETCountry getCountryList : getCountryLists) {
                addCountryLists.add(getCountryList.getCountry());
            }

            for (GETProfession getProfessionList : getProfession) {
                addProfessionLists.add(getProfessionList.getProfession_name());
            }

            if (addCountryLists.size() > 0) {
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfile.this,
                        android.R.layout.simple_spinner_item, addCountryLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCountry.setAdapter(adapter);


                spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinState.clearFocus();
                        if (!country.isEmpty() && VIEW_TYPE == 0) {
                            if (addCountryLists.get(position).equalsIgnoreCase(getCountryLists[position].getCountry())) {
                                getStateAPI();
                            }
                            VIEW_TYPE = 1;
                        } else {
                            getStateAPI();
                            country = getCountryLists[position].getId();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            if (addProfessionLists.size() > 0) {
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfile.this,
                        android.R.layout.simple_spinner_item, addProfessionLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinProfession.setAdapter(adapter);


                spinProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        profession = getProfession[position].getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }


        }


        if (addlanguageLists.size() > 0) {
            spinSpeaks.setItems(addlanguageLists, "Languages", new MultiSpinner.MultiSpinnerListener() {
                @Override
                public void onItemsSelected(boolean[] selected) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            languages = sb.append(addlanguageLists.get(i)).append(",").toString();
                        } else {
                            languages = "";
                        }
                    }
                }
            });
        }


    }

    private void postProfile() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postViewProfile(getProfiles).enqueue(new Callback<POSTViewProfile>() {
            @Override
            public void onResponse(Call<POSTViewProfile> call, Response<POSTViewProfile> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        mCustomProgressDialog.dismiss();
                        editPhone.setText(response.body().getData().get(0).getContact());
                        editAddress.setText(response.body().getData().get(0).getAddress());
                        editSuggestions.setText(response.body().getData().get(0).getDescription());
                        editZipCode.setText(response.body().getData().get(0).getZipcode());
                        editName.setText(response.body().getData().get(0).getFullname());
                        editCity.setText(response.body().getData().get(0).getCity());

                        for (int i = 0; i < getCountryLists.length; i++) {
                            if (getCountryLists[i].getId().equalsIgnoreCase(
                                    response.body().getData().get(0).getCountry()
                            )) {
                                spinCountry.setSelection(i);
                                country = getCountryLists[i].getId();
                            }
                        }

                        state = response.body().getData().get(0).getState();
                        state_name = response.body().getData().get(0).getState_name();

//                        spinState.setSelection(Integer.parseInt(response.body().getData().get(0).getState()));

                        if (response.body().getData().get(0).getLang_speaks().isEmpty()) {
                            spinSpeaks.setPrompt("Select Languages");
                        } else {
                            final StringBuilder spinnerBuffer = new StringBuilder();
                            String data = response.body().getData().get(0).getLang_speaks();
                            String[] items = data.split(",");
                            for (String item : items) {
                                spinnerBuffer.append(item);
                                spinnerBuffer.append(",");
                            }
                            languages = spinnerBuffer.toString().trim().substring(0, spinnerBuffer.toString().length() - 1);
                            spinSpeaks.setUpdateItems(addlanguageLists, spinnerBuffer.toString(), new MultiSpinner.MultiSpinnerListener() {
                                @Override
                                public void onItemsSelected(boolean[] selected) {
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < selected.length; i++) {
                                        if (selected[i]) {
                                            languages = sb.append(addlanguageLists.get(i)).append(",").toString();
                                        } else {
                                            languages = "";
                                        }
                                    }
                                }
                            });
                        }


                        for (int i = 0; i < getProfession.length; i++) {
                            if (getProfession[i].getId().equalsIgnoreCase(
                                    response.body().getData().get(0).getProfession()
                            )) {
                                spinProfession.setSelection(i);
                                profession = getProfession[i].getId();
                            }
                        }


                    } else {
                        Toast.makeText(UpdateProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    mCustomProgressDialog.dismiss();
                } else {
                    Log.i("ERROR_UPDATE_PROFILE", response.errorBody().toString());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTViewProfile> call, Throwable t) {
                mCustomProgressDialog.dismiss();
                Log.i("TAG", t.getMessage());
            }
        });
    }

    private void getStateAPI() {
        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.getState(country).enqueue(new Callback<List<GETState>>() {
                @Override
                public void onResponse(Call<List<GETState>> call, final Response<List<GETState>> response) {
                    mCustomProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().size() > 0) {
                            addStateLists.clear();
                            for (GETState getState : response.body()) {
                                addStateLists.add(getState.getState_name());
                            }

                            if (addStateLists.size() > 0 && response.body().size() > 0) {
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfile.this,
                                        android.R.layout.simple_spinner_item, addStateLists);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinState.setAdapter(adapter);


                                if (!state_name.isEmpty()) {
                                    for (int i = 0; i < addStateLists.size(); i++) {
                                        if (state_name.equalsIgnoreCase(addStateLists.get(i))) {
                                            spinState.setSelection(i);
                                        }

                                    }
                                }

                                spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (addStateLists.get(position).equalsIgnoreCase(response.body().get(position).getState_name())) {
                                            state = response.body().get(position).getState_id();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {


                                    }
                                });


                            }


                        }
                    }

                }

                @Override
                public void onFailure(Call<List<GETState>> call, Throwable t) {
                    Log.i("TAG", t.getMessage());
                    mCustomProgressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
        }


    }


    private void initLayouts() {

        tb_toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(tb_toolbar);

        spinCountry = (Spinner) findViewById(R.id.spinner_counrty);
        spinState = (Spinner) findViewById(R.id.spinner_state);
        spinProfession = (Spinner) findViewById(R.id.spinner_profession);
        spinSpeaks = (MultiSpinner) findViewById(R.id.spinner_speaks);

        editPhone = (EditText) findViewById(R.id.input_phone);
        editAddress = (EditText) findViewById(R.id.input_address_line);
        editCity = (EditText) findViewById(R.id.input_city);
        editSuggestions = (EditText) findViewById(R.id.input_suggestion_about_you);
        editZipCode = (EditText) findViewById(R.id.input_zip_code);
        editName = (EditText) findViewById(R.id.input_name);

        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phno);


    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("language.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void clickOpen(View view) {
        alert.show();

    }

    public void upadteProfile(View view) {

        if (!editPhone.getText().toString().matches(Constants.numberMatch)) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
        } else if (!editCity.getText().toString().matches(Constants.cityMatch)) {
            inputLayoutCity.setError(getString(R.string.err_msg_city));
        } else {
            if (NetworkChangeReceiver.isConnected()) {
                mCustomProgressDialog.showDialog();
                updateProfiles.put("user_id", SessionHandler.getInstance().get(UpdateProfile.this, Constants.USER_ID));
                updateProfiles.put("user_contact", editPhone.getText().toString());
                updateProfiles.put("user_zip", editZipCode.getText().toString());
                updateProfiles.put("user_city", editCity.getText().toString());
                updateProfiles.put("user_addr", editAddress.getText().toString());
                updateProfiles.put("user_desc", editSuggestions.getText().toString());
                updateProfiles.put("country_id", country);
                updateProfiles.put("state_id", state);
                updateProfiles.put("profession", profession);
                updateProfiles.put("user_name", editName.getText().toString());
                updateProfiles.put("language_tags", languages);
                postUpdateProfile();
            } else {

            }
        }
    }


    private void postUpdateProfile() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postUpdateProfile(updateProfiles).enqueue(new Callback<POSTPaypalSettings>() {
            @Override
            public void onResponse(Call<POSTPaypalSettings> call, Response<POSTPaypalSettings> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Toast.makeText(UpdateProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_UPDATE_PROFILE", response.errorBody().toString());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTPaypalSettings> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });

    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
