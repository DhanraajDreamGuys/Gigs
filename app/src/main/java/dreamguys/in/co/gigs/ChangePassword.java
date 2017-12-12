package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTChangePassword;
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
 * Created by Prasad on 10/25/2017.
 */

public class ChangePassword extends AppCompatActivity {

    private TextInputLayout textInputLayoutCPassword;
    private TextInputLayout textInputLayoutNPassword;
    private TextInputLayout textInputLayoutRPassword;
    private EditText editCPassword;
    private EditText editNPassword;
    private EditText editRPassword;
    private Toolbar mToolbar;
    private CustomProgressDialog mCustomProgressDialog;
    private final HashMap<String, String> changePasswordData = new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();


    }

    private void initLayouts() {
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);

        textInputLayoutCPassword = (TextInputLayout) findViewById(R.id.input_layout_cpassword);
        textInputLayoutNPassword = (TextInputLayout) findViewById(R.id.input_layout_npassword);
        textInputLayoutRPassword = (TextInputLayout) findViewById(R.id.input_layout_rpassword);

        editCPassword = (EditText) findViewById(R.id.input_cpassword);
        editNPassword = (EditText) findViewById(R.id.input_npassword);
        editRPassword = (EditText) findViewById(R.id.input_rpassword);

    }

    private boolean validateBothPassword() {
        if (!editNPassword.getText().toString().trim().equalsIgnoreCase(editRPassword.getText().toString().trim())) {
            textInputLayoutNPassword.setError(getString(R.string.err_msg_cpassword));
            textInputLayoutRPassword.setError(getString(R.string.err_msg_cpassword));
            requestFocus(editNPassword);
            requestFocus(editRPassword);
            return false;
        } else {
            textInputLayoutRPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (editCPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutCPassword.setError(getString(R.string.err_msg_cupassword));
            requestFocus(editCPassword);
            return false;
        } else {
            textInputLayoutCPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNPassword() {
        if (editNPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutNPassword.setError(getString(R.string.err_msg_npassword));
            requestFocus(editNPassword);
            return false;
        } else {
            textInputLayoutNPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRPassword() {
        if (editRPassword.getText().toString().trim().isEmpty()) {
            textInputLayoutRPassword.setError(getString(R.string.err_msg_rpassword));
            requestFocus(editRPassword);
            return false;
        } else {
            textInputLayoutRPassword.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void changePassword(View view) {
        if (!validatePassword()) {
            return;
        }
        if (!validateNPassword()) {
            return;
        }
        if (!validateRPassword()) {
            return;
        }

        if (!validateBothPassword()) {
            return;
        }
        changePasswordData.put("current_password", editCPassword.getText().toString());
        changePasswordData.put("new_password", editNPassword.getText().toString());
        changePasswordData.put("id", SessionHandler.getInstance().get(this, Constants.USER_ID));

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            postChangePassword();
        } else {
            showToast();
        }
    }

    private void postChangePassword() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postChangePassword(changePasswordData).enqueue(new Callback<POSTChangePassword>() {
            @Override
            public void onResponse(Call<POSTChangePassword> call, Response<POSTChangePassword> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Toast.makeText(ChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_CHANGE_PASSWORD", response.errorBody().toString());
                }
                mCustomProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<POSTChangePassword> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                mCustomProgressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }
}
