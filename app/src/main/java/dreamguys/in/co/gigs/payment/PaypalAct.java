package dreamguys.in.co.gigs.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;

import dreamguys.in.co.gigs.MainActivity;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.Model.POSTPaymentSuccess;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 11/16/2017.
 */

public class PaypalAct extends AppCompatActivity {
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "AfrBFVee3HnKZgWObWdp9kip7IQ3XzouSgzqkny3C_H2JFnhdePtuA9dWFKZJ9agd36GTxLKIA9ecPQz";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    POSTDetailGig.Gigs_details mGigs_details;
    String id = "", transaction_id = "";
    HashMap<String, String> postDetails = new HashMap<String, String>();
    CustomProgressDialog mCustomProgressDialog;
    LinearLayout llPayPal;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
           /* // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("dreamguys")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));*/

    PayPalPayment thingToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        llPayPal = (LinearLayout) findViewById(R.id.ll_thanks_order);
        mCustomProgressDialog = new CustomProgressDialog(this);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


        String title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        int cost = getIntent().getIntExtra("total_cost", 0);
        mGigs_details = (POSTDetailGig.Gigs_details) getIntent().getSerializableExtra("bundle");

        thingToBuy = new PayPalPayment(new BigDecimal(cost), "USD",
                title, PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intentPay = new Intent(PaypalAct.this,
                PaymentActivity.class);

        intentPay.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intentPay, REQUEST_CODE_PAYMENT);

    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PaypalAct.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));

                        transaction_id = confirm.toJSONObject().getJSONObject("response").get("id").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
                finish();
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void takeToHome(View view) {

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            if (!transaction_id.isEmpty() && id != null) {
                postDetails.put("paypal_uid", transaction_id);
                postDetails.put("item_number", id);

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                apiInterface.postPaymentSuccess(postDetails).enqueue(new Callback<POSTPaymentSuccess>() {
                    @Override
                    public void onResponse(Call<POSTPaymentSuccess> call, Response<POSTPaymentSuccess> response) {
                        mCustomProgressDialog.dismiss();
                        if (response.body() != null) {
                            if (response.body().getCode().equals(200)) {
                                Toast.makeText(PaypalAct.this, "Thank you for your order....", Toast.LENGTH_SHORT).show();
                                Intent callHome = new Intent(PaypalAct.this, MainActivity.class);
                                callHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(callHome);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<POSTPaymentSuccess> call, Throwable t) {
                        Log.i("TAG", t.getMessage());
                        mCustomProgressDialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
