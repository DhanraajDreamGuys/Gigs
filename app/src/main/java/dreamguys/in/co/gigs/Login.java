package dreamguys.in.co.gigs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTAcceptBuyRequest;
import dreamguys.in.co.gigs.Model.POSTLogin;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/24/2017.
 */

public class Login extends AppCompatActivity {

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private EditText editEmail;
    private EditText editPassword;
    private final HashMap<String, String> loginData = new HashMap<String, String>();
    private CustomProgressDialog mCustomProgressDialog;
    private Toolbar mToolbar;
    private HashMap<String, String> postPush = new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        OneSignal.startInit(this).init();

        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();

        editEmail.addTextChangedListener(new LoginTextWatcher(editEmail));
        editPassword.addTextChangedListener(new LoginTextWatcher(editPassword));

    }

    private void initLayouts() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_username);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        editEmail = (EditText) findViewById(R.id.input_username);
        editPassword = (EditText) findViewById(R.id.input_password);

        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
    }

    public void forgotpasswordHere(View view) {
        startActivity(new Intent(this, ForgotPassword.class));
        clearFocus();
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, Register.class));
        clearFocus();
    }

    private boolean validateUsername() {
        String email = editEmail.getText().toString().trim();

        if (email.isEmpty()) {
            textInputLayoutEmail.setError(getString(R.string.err_msg_uname));
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
        } else {
            textInputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void goToHome(View view) {

        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        loginData.put("username", editEmail.getText().toString());
        loginData.put("password", editPassword.getText().toString());

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postLogin();
        } else {
            showToast();
        }
    }


    private class LoginTextWatcher implements TextWatcher {

        private View view;

        private LoginTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.input_username) {
                validateUsername();
            } else if (view.getId() == R.id.input_password) {
                validatePassword();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (view.getId() == R.id.input_username) {
                if (editEmail.getText().toString().isEmpty()) {
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutEmail.requestFocus();
                    textInputLayoutEmail.setError(null);
                }
            } else if (view.getId() == R.id.input_password) {
                if (editPassword.getText().toString().isEmpty()) {
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutPassword.requestFocus();
                    textInputLayoutPassword.setError(null);
                }
            }
        }
    }


    private void postLogin() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postLogin(loginData).enqueue(new Callback<POSTLogin>() {
            @Override
            public void onResponse(Call<POSTLogin> call, Response<POSTLogin> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        POSTLogin.Datum data = response.body().getData().get(0);
                        SessionHandler.getInstance().save(Login.this, Constants.USER_NAME, data.getUsername());
                        SessionHandler.getInstance().save(Login.this, Constants.USER_ID, data.getUserid());
                        SessionHandler.getInstance().save(Login.this, Constants.EMAIL_ID, data.getEmail());

                        new BackGroundPushAPiCall().execute();

                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                } else {
                    try {
                        Log.i("ERROR_LOGIN", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mCustomProgressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<POSTLogin> call, Throwable t) {
                mCustomProgressDialog.dismiss();
                Log.i("TAG", t.getMessage());
            }
        });
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void clearFocus() {
        editEmail.getText().clear();
        editPassword.getText().clear();
    }


    @SuppressLint("StaticFieldLeak")
    private class BackGroundPushAPiCall extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            postPush.put("user_id", SessionHandler.getInstance().get(getApplicationContext(), Constants.USER_ID));
            postPush.put("device_id", SessionHandler.getInstance().get(getApplicationContext(), Constants.NOTIFICATION_IDS));
            postPush.put("device", "Android");
            if (NetworkChangeReceiver.isConnected()) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                apiInterface.postPushDetails(postPush).enqueue(new Callback<POSTAcceptBuyRequest>() {
                    @Override
                    public void onResponse(Call<POSTAcceptBuyRequest> call, Response<POSTAcceptBuyRequest> response) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals(200)) {
                                Log.i("TAG", response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<POSTAcceptBuyRequest> call, Throwable t) {
                        Log.i("TAG", t.getMessage());
                    }
                });
            }

            return null;
        }
    }
}
