package dreamguys.in.co.gigs;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.GETCountry;
import dreamguys.in.co.gigs.Model.GETState;
import dreamguys.in.co.gigs.Model.POSTRegister;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/24/2017.
 */

public class Register extends AppCompatActivity {

    private Button buttonRegister;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutRPassword;
    private EditText editName;
    private EditText editEmail;
    private EditText editUserName;
    private EditText editPassword;
    private EditText editRPassword;
    private Spinner spinCountry;
    private Spinner spinState;
    private String country = "";
    private String state = "";
    private Gson gson;
    private final List<String> addCountryLists = new ArrayList<String>();
    private final List<String> addStateLists = new ArrayList<String>();
    private List<GETState> stateData;
    private GETCountry[] getCountryLists;
    private final HashMap<String, String> registerData = new HashMap<String, String>();
    private CustomProgressDialog mCustomProgressDialog;
    private Toolbar mToolbar;
    CheckBox mCheckBox;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap thumbnail;
    private String userChoosenTask;
    FileOutputStream fo;
    ByteArrayOutputStream bytes;
    String encodeImage;
    MultipartBody.Part body;
    RequestBody requestFile;
    private ImageView ivImage;
    RequestBody email = null, username = null, password = null, states = null, countries = null, fullname = null, user_timezone = null, user_image = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gson = new Gson();
        mCustomProgressDialog = new CustomProgressDialog(this);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        initLayouts();

        editName.addTextChangedListener(new RegisterTextWatcher(editName));
        editEmail.addTextChangedListener(new RegisterTextWatcher(editEmail));
        editUserName.addTextChangedListener(new RegisterTextWatcher(editUserName));
        editPassword.addTextChangedListener(new RegisterTextWatcher(editPassword));
        editRPassword.addTextChangedListener(new RegisterTextWatcher(editRPassword));

        /*if (ContextCompat.checkSelfPermission(Register.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }*/

        if (SessionHandler.getInstance().get(Register.this, Constants.COUNTRY_JSON) != null) {

            getCountryLists = gson.fromJson(SessionHandler.getInstance().get(Register.this, Constants.COUNTRY_JSON), GETCountry[].class);

            for (GETCountry getCountryList : getCountryLists) {
                addCountryLists.add(getCountryList.getCountry());
            }
            if (addCountryLists.size() > 0) {
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this,
                        android.R.layout.simple_spinner_item, addCountryLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinCountry.setAdapter(adapter);


                spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        editName.clearFocus();
                        editEmail.clearFocus();
                        editUserName.clearFocus();
                        editPassword.clearFocus();
                        editRPassword.clearFocus();
                        spinState.clearFocus();
                        if (addCountryLists.get(position).equalsIgnoreCase(getCountryLists[position].getCountry())) {
                            country = getCountryLists[position].getId();
//                            Toast.makeText(Register.this, "ID: " + country, Toast.LENGTH_SHORT).show();
                            if (!country.isEmpty())
                                getStateAPI();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    private void getStateAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getState(country).enqueue(new Callback<List<GETState>>() {
            @Override
            public void onResponse(Call<List<GETState>> call, final Response<List<GETState>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        stateData = response.body();
                        addStateLists.clear();
                        for (GETState getState : response.body()) {
                            addStateLists.add(getState.getState_name());
                        }
                        if (addStateLists.size() > 0 && stateData.size() > 0) {
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this,
                                    android.R.layout.simple_spinner_item, addStateLists);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinState.setAdapter(adapter);

                            spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (addStateLists.get(position).equalsIgnoreCase(stateData.get(position).getState_name())) {
                                        state = stateData.get(position).getState_id();
//                                        Toast.makeText(Register.this, "ID: " + state, Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void initLayouts() {

        buttonRegister = (Button) findViewById(R.id.button_register);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        textInputLayoutRPassword = (TextInputLayout) findViewById(R.id.input_layout_rpassword);

        editName = (EditText) findViewById(R.id.input_name);
        editEmail = (EditText) findViewById(R.id.input_email);
        editUserName = (EditText) findViewById(R.id.input_username);
        editPassword = (EditText) findViewById(R.id.input_password);
        editRPassword = (EditText) findViewById(R.id.input_rpassword);

        spinCountry = (Spinner) findViewById(R.id.spinner_counrty);
        spinState = (Spinner) findViewById(R.id.spinner_state);

        mCheckBox = (CheckBox) findViewById(R.id.cb_agree_conditions);
        ivImage = (ImageView) findViewById(R.id.input_profile_picture);

    }


    public void registerHere(View view) {

        if (!validateName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validateRPassword()) {
            return;
        }
        /*if (!validateBothPassword()) {
            return;
        }*/
        if (!validateCountrySpin()) {
            return;
        }
        if (!validateStateSpin()) {
            return;
        }

        if (!mCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the terms & conditions", Toast.LENGTH_SHORT).show();
            return;
        }

       /* registerData.put("email", editEmail.getText().toString());
        registerData.put("username", editUserName.getText().toString());
        registerData.put("password", editPassword.getText().toString());
        registerData.put("state", state);
        registerData.put("country", country);
        registerData.put("fullname", editName.getText().toString());
        registerData.put("user_timezone", SessionHandler.getInstance().get(Register.this, Constants.TIMEZONE_ID));*/


        email = RequestBody.create(MediaType.parse("text/plain"), editEmail.getText().toString());
        username = RequestBody.create(MediaType.parse("text/plain"), editUserName.getText().toString());
        password = RequestBody.create(MediaType.parse("text/plain"), editPassword.getText().toString());
        states = RequestBody.create(MediaType.parse("text/plain"), state);
        countries = RequestBody.create(MediaType.parse("text/plain"), country);
        fullname = RequestBody.create(MediaType.parse("text/plain"), editName.getText().toString());
        user_timezone = RequestBody.create(MediaType.parse("text/plain"), SessionHandler.getInstance().get(Register.this, Constants.TIMEZONE_ID));

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postRegistration();
        } else {
            showToast();
        }
    }

    private void postRegistration() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postRegister(email, username, password, states, countries, fullname, user_timezone, body).enqueue(new Callback<POSTRegister>() {
            @Override
            public void onResponse(Call<POSTRegister> call, Response<POSTRegister> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.i("ERROR_REGISTER:", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTRegister> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });

    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        if (editName.getText().toString().trim().isEmpty()) {
            textInputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(editName);
            return false;
        } else {
            textInputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUsername() {
        if (editUserName.getText().toString().trim().isEmpty()) {
            textInputLayoutUsername.setError(getString(R.string.err_msg_uname));
            requestFocus(editUserName);
            return false;
        } else {
            textInputLayoutUsername.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = editEmail.getText().toString().trim();

        if (email.isEmpty() || !Utils.isValidEmail(email)) {
            textInputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(editEmail);
            return false;
        } else {
            textInputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (editPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(editPassword);
            return false;
        } else if (editPassword.getText().toString().length() < 8) {
            textInputLayoutPassword.setError(getString(R.string.err_password));
            requestFocus(editPassword);
            return false;
        } else if (!editPassword.getText().toString().matches(Constants.passwordMatch)) {
            textInputLayoutPassword.setError(getString(R.string.err_password));
            requestFocus(editPassword);
            return false;
        } else {
            textInputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRPassword() {
        if (editRPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutRPassword.setError(getString(R.string.err_msg_rpassword));
            requestFocus(editRPassword);
            return false;
        } else if (!editRPassword.getText().toString().matches(Constants.passwordMatch)) {
            textInputLayoutRPassword.setError(getString(R.string.err_password));
            requestFocus(editRPassword);
            return false;
        } else if (!editRPassword.getText().toString().trim().matches(editPassword.getText().toString().trim())) {
            textInputLayoutRPassword.setError(getString(R.string.err_msg_cpassword));
            requestFocus(editRPassword);
            return false;
        } else {
            textInputLayoutRPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateBothPassword() {
        if (!editPassword.getText().toString().trim().equalsIgnoreCase(editRPassword.getText().toString().trim())) {
            textInputLayoutPassword.setError(getString(R.string.err_msg_cpassword));
            textInputLayoutRPassword.setError(getString(R.string.err_msg_cpassword));
            requestFocus(editPassword);
            requestFocus(editRPassword);
            return false;
        } else {
            textInputLayoutRPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCountrySpin() {
        if (country.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_msg_country), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateStateSpin() {
        if (state.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_msg_state), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void SelectPicture(View view) {
        selectImage();
    }


    private class RegisterTextWatcher implements TextWatcher {
        private View view;

        private RegisterTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.input_name) {
                validateName();
            } else if (view.getId() == R.id.input_email) {
                validateEmail();
            } else if (view.getId() == R.id.input_username) {
                validateUsername();
            } else if (view.getId() == R.id.input_password) {
                validatePassword();
            } else if (view.getId() == R.id.input_rpassword) {
                validateRPassword();
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


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void loginHere(View view) {
        startActivity(new Intent(this, Login.class));
    }

    public void termsOfConditions(View view) {
        Toast.makeText(this, "Terms and conditions..", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(Register.this);
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
        SessionHandler.getInstance().save(Register.this, "BitmapPath", String.valueOf(picturePath));
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

}
