package dreamguys.in.co.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTForgotPassword;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/24/2017.
 */

public class ForgotPassword extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail;
    private EditText editEmail;
    private CustomProgressDialog mCustomProgressDialog;
    private final HashMap<String, String> forgotData = new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();
        editEmail.addTextChangedListener(new ForgetPasswordTextWatcher(editEmail));
    }

    private void initLayouts() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        editEmail = (EditText) findViewById(R.id.input_email);
    }

    public void goToLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }

    public void submitForgot(View view) {
        if (!validateEmail()) {
            return;
        }
        forgotData.put("forget_email", editEmail.getText().toString());

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postForgotPassword();
        } else {
            showToast();
        }
    }

    private void postForgotPassword() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postForgot(forgotData).enqueue(new Callback<POSTForgotPassword>() {
            @Override
            public void onResponse(Call<POSTForgotPassword> call, Response<POSTForgotPassword> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Toast.makeText(ForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_FORGOT", response.errorBody().toString());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTForgotPassword> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });

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

    private class ForgetPasswordTextWatcher implements TextWatcher {

        private View view;

        private ForgetPasswordTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.input_email) {
                validateEmail();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (view.getId() == R.id.input_email) {
                if (editEmail.getText().toString().isEmpty()) {
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutEmail.requestFocus();
                    textInputLayoutEmail.setError(null);
                }
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }
}
