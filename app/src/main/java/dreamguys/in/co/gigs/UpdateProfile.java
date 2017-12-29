package dreamguys.in.co.gigs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
import dreamguys.in.co.gigs.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public static StringBuilder stringBuilder;
    Toolbar mToolbar;
    RequestBody user_id = null, user_contact = null, user_zip = null, user_city = null, user_addr = null, user_desc = null, country_id = null, state_id = null, professions = null, user_name = null, language = null;
    MultipartBody.Part body;
    RequestBody requestFile;
    private ImageView ivImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap thumbnail;
    private String userChoosenTask;
    FileOutputStream fo;
    ByteArrayOutputStream bytes;
    String encodeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        gson = new Gson();
        mCustomProgressDialog = new CustomProgressDialog(this);

        initLayouts();
        editPhone.addTextChangedListener(new UpdateProfileTextWatcher(editPhone));
        editCity.addTextChangedListener(new UpdateProfileTextWatcher(editCity));
        editZipCode.addTextChangedListener(new UpdateProfileTextWatcher(editZipCode));

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
                                country = getCountryLists[position].getId();
                                getStateAPI();
                            }
                            VIEW_TYPE = 1;
                        } else {
                            country = getCountryLists[position].getId();
                            getStateAPI();
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
            spinSpeaks.setItems(addlanguageLists, "Select Language", new MultiSpinner.MultiSpinnerListener() {
                @Override
                public void onItemsSelected(boolean[] selected) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            languages = sb.append(addlanguageLists.get(i)).append(",").toString();
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
                        stringBuilder = new StringBuilder();
                        if (stringBuilder.length() < 0) {
                            languages = "";
                        } else {
                            String data = response.body().getData().get(0).getLang_speaks();

                            if (!data.isEmpty()) {
                                String[] items = data.split(",");
                                for (String item : items) {
                                    stringBuilder.append(item);
                                    stringBuilder.append(",");
                                }

                                spinSpeaks.setUpdateItems(addlanguageLists, stringBuilder.toString(), new MultiSpinner.MultiSpinnerListener() {
                                    @Override
                                    public void onItemsSelected(boolean[] selected) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < selected.length; i++) {
                                            if (selected[i]) {
                                                languages = sb.append(addlanguageLists.get(i)).append(",").toString();
                                            }
                                        }
                                    }
                                });
                            } else {
                                spinSpeaks.setItems(addlanguageLists, "Select Language", new MultiSpinner.MultiSpinnerListener() {
                                    @Override
                                    public void onItemsSelected(boolean[] selected) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < selected.length; i++) {
                                            if (selected[i]) {
                                                languages = sb.append(addlanguageLists.get(i)).append(",").toString();
                                            }
                                        }
                                    }
                                });
                            }


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
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.getState(country).enqueue(new Callback<List<GETState>>() {
                @Override
                public void onResponse(Call<List<GETState>> call, final Response<List<GETState>> response) {
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

        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);

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
        inputLayoutCity = (TextInputLayout) findViewById(R.id.input_layout_city);
        inputLayoutZipcode = (TextInputLayout) findViewById(R.id.input_layout_zip_code);
        ivImage = (ImageView) findViewById(R.id.input_profile_picture);


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

    public void UpdatePicture(View view) {
        selectImage();
    }

    private boolean validateCity() {
        if (editCity.getText().toString().trim().isEmpty()) {
            inputLayoutCity.setError(getString(R.string.err_msg_city_name));
            requestFocus(editCity);
            return false;
        } else if (!editCity.getText().toString().matches(Constants.cityMatch)) {
            inputLayoutCity.setError(getString(R.string.err_msg_city));
            requestFocus(editCity);
            return false;
        } else {
            inputLayoutCity.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (editPhone.getText().toString().trim().isEmpty()) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone_no));
            requestFocus(editPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateZipCode() {
        if (editZipCode.getText().toString().trim().isEmpty()) {
            inputLayoutZipcode.setError(getString(R.string.err_msg_zipcode));
            requestFocus(editZipCode);
            return false;
        } else {
            inputLayoutZipcode.setErrorEnabled(false);
        }

        return true;
    }

    public void upadteProfile(View view) {

        if (!validatePhone()) {
            return;
        } else if (!validateCity()) {
            return;
        } else if (!validateZipCode()) {
            return;
        } else {
            mCustomProgressDialog.showDialog();
            user_id = RequestBody.create(MediaType.parse("text/plain"), SessionHandler.getInstance().get(UpdateProfile.this, Constants.USER_ID));
            user_contact = RequestBody.create(MediaType.parse("text/plain"), editPhone.getText().toString());
            user_zip = RequestBody.create(MediaType.parse("text/plain"), editZipCode.getText().toString());
            user_city = RequestBody.create(MediaType.parse("text/plain"), editCity.getText().toString());
            user_addr = RequestBody.create(MediaType.parse("text/plain"), editAddress.getText().toString());
            user_desc = RequestBody.create(MediaType.parse("text/plain"), editSuggestions.getText().toString());
            country_id = RequestBody.create(MediaType.parse("text/plain"), country);
            state_id = RequestBody.create(MediaType.parse("text/plain"), state);
            professions = RequestBody.create(MediaType.parse("text/plain"), profession);
            user_name = RequestBody.create(MediaType.parse("text/plain"), editName.getText().toString());
            language = RequestBody.create(MediaType.parse("text/plain"), languages);

            postUpdateProfile();

        }
    }


    private void postUpdateProfile() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postUpdateProfile(user_id, user_contact, user_zip, user_city, user_addr, user_desc, country_id, state_id, professions, user_name, language, body).enqueue(new Callback<POSTPaypalSettings>() {
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


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class UpdateProfileTextWatcher implements TextWatcher {
        private View view;

        private UpdateProfileTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.input_phone) {
                validatePhone();
            } else if (view.getId() == R.id.input_city) {
                validateCity();
            } else if (view.getId() == R.id.input_zip_code) {
                validateZipCode();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            /*if (editUserName.getText().toString().isEmpty()) {
                textInputLayoutUsername.setError(null);
                textInputLayoutUsername.setErrorEnabled(false);
            }*/
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(UpdateProfile.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        Uri selectedImage = data.getData();

        encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
        body = MultipartBody.Part.createFormData("profile_img", "image.jpg", requestFile);


        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        SessionHandler.getInstance().save(UpdateProfile.this, "BitmapPath", String.valueOf(picturePath));
        ivImage.setImageBitmap(thumbnail);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
        }
        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
        body = MultipartBody.Part.createFormData("profile_img", "image.jpg", requestFile);
        ivImage.setImageBitmap(thumbnail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
