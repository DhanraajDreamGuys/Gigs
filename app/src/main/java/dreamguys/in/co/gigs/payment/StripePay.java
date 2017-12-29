package dreamguys.in.co.gigs.payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.BuildConfig;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

import dreamguys.in.co.gigs.Model.POSTPaymentSuccess;
import dreamguys.in.co.gigs.MyActivity;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripePay extends AppCompatActivity {

    String title;
    int total_cost;
    CustomProgressDialog mCustomProgressDialog;
    Button pay;
    HashMap<String, String> postDetails = new HashMap<String, String>();
    String transaction_id = "", id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_pay);


        pay = (Button) findViewById(R.id.submitButton);


        mCustomProgressDialog = new CustomProgressDialog(this);
        total_cost = getIntent().getIntExtra("total_cost", 0);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        pay.setText("Pay " + "$ " + total_cost);

    }

    public void submitCard(View view) {

        mCustomProgressDialog.showDialog();

        // TODO: replace with your own test key
        final String publishableApiKey = BuildConfig.DEBUG ?
                "pk_test_6pRNASCoBOKtIshFeQd4XMUh" :
                getString(R.string.com_stripe_publishable_key);

        TextView cardNumberField = (TextView) findViewById(R.id.cardNumber);
        TextView monthField = (TextView) findViewById(R.id.month);
        TextView yearField = (TextView) findViewById(R.id.year);
        TextView cvcField = (TextView) findViewById(R.id.cvc);

        Card card = new Card(cardNumberField.getText().toString(),
                Integer.valueOf(monthField.getText().toString()),
                Integer.valueOf(yearField.getText().toString()),
                cvcField.getText().toString());
        card.setCurrency("USD");


        Stripe stripe = new Stripe();
        stripe.createToken(card, publishableApiKey, new TokenCallback() {
            public void onSuccess(Token token) {
                mCustomProgressDialog.dismiss();
                com.stripe.Stripe.apiKey = "sk_test_jZ7lXkDEyr1YKcxwVCrkcq7P";

                total_cost = total_cost * 100;

                // Charge the user's card:
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("amount", total_cost);
                params.put("currency", "USD");
                params.put("description", title);
                params.put("source", token.getId());

                try {
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here
                        Charge charge = Charge.create(params);

                        if (charge != null) {
                            transaction_id = charge.getId();
                            if (charge.getStatus().equalsIgnoreCase("succeeded")) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StripePay.this);
                                mBuilder.setMessage("Thank you for your Order..");
                                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

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
                                                                Intent callHome = new Intent(StripePay.this, MyActivity.class);
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
                                            Toast.makeText(StripePay.this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });
                                mBuilder.show();
                            }
                        }
                    }
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                }

            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }


}
