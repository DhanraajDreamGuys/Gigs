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

import dreamguys.in.co.gigs.Model.POSTPaypalSettings;
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
 * Created by Prasad on 10/25/2017.
 */

public class PaypalSettings extends AppCompatActivity {
    private TextInputLayout inputLayoutEmail;
    private EditText editEmail;
    private Toolbar mToolbar;
    private CustomProgressDialog mCustomProgressDialog;
    private final HashMap<String, String> paypalSetttings = new HashMap<String, String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_settings);
        mCustomProgressDialog = new CustomProgressDialog(this);
        initLayouts();

    }

    private void initLayouts() {
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        editEmail = (EditText) findViewById(R.id.input_email);
    }

    public void submitPaypal(View view) {
        if (!validateEmail()) {
            return;
        }

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();

            paypalSetttings.put("paypal_email", editEmail.getText().toString());
            paypalSetttings.put("user_id", SessionHandler.getInstance().get(this, Constants.USER_ID));

            postPaypalSettings();
        } else {
            showToast();
        }

        Toast.makeText(this, "Paypal email setting is prefect!...", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void postPaypalSettings() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.postPaypalSettings(paypalSetttings).enqueue(new Callback<POSTPaypalSettings>() {
            @Override
            public void onResponse(Call<POSTPaypalSettings> call, Response<POSTPaypalSettings> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Toast.makeText(PaypalSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PaypalSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_PAYPAL_SETTINGS", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<POSTPaypalSettings> call, Throwable t) {
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateEmail() {
        String email = editEmail.getText().toString().trim();

        if (email.isEmpty() || !Utils.isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(editEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
