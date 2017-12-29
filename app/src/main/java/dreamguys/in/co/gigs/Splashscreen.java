package dreamguys.in.co.gigs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.TimeZone;

import dreamguys.in.co.gigs.Model.GETCountry;
import dreamguys.in.co.gigs.Model.GETProfession;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 10/24/2017.
 */

public class Splashscreen extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {
    private Gson gson;
    private AVLoadingIndicatorView aviLoadingView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
        SessionHandler.getInstance().save(Splashscreen.this, Constants.TIMEZONE_ID, tz.getID());

        aviLoadingView = (AVLoadingIndicatorView) findViewById(R.id.avi_progress_bar);


        gson = new Gson();
        if (SessionHandler.getInstance().get(Splashscreen.this, Constants.COUNTRY_JSON) != null) {
            GETCountry[] getCountryLists = gson.fromJson(SessionHandler.getInstance().get(Splashscreen.this, Constants.COUNTRY_JSON), GETCountry[].class);

            if (!(getCountryLists.length > 0)) {
                return;
            }
            if (!SessionHandler.getInstance().getBoolean(this, Constants.IS_WELCOME_FIRST_TIME)) {
                startActivity(new Intent(this, Introscreen.class));
                finish();
            } else if (SessionHandler.getInstance().getBoolean(this, Constants.IS_WELCOME_FIRST_TIME) &&
                    SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }
        } else {
            if (NetworkChangeReceiver.isConnected()) {
                aviLoadingView.show();
                getCountryAPI();
            } else {
                showToast();
            }
        }
    }

    private void getCountryAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getCountry().enqueue(new Callback<List<GETCountry>>() {
            @Override
            public void onResponse(Call<List<GETCountry>> call, Response<List<GETCountry>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        String json = gson.toJson(response.body());
                        SessionHandler.getInstance().save(Splashscreen.this, Constants.COUNTRY_JSON, json);
                        getProfession();
                    } else {
                        Toast.makeText(Splashscreen.this, "some error occurred..", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GETCountry>> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    private void getProfession() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getProfession().enqueue(new Callback<List<GETProfession>>() {
            @Override
            public void onResponse(Call<List<GETProfession>> call, Response<List<GETProfession>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        String json = gson.toJson(response.body());
                        SessionHandler.getInstance().save(Splashscreen.this, Constants.PROFESSION, json);
                        startActivity(new Intent(Splashscreen.this, Introscreen.class));
                        finish();
                    } else {
                        Toast.makeText(Splashscreen.this, "some error occurred..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("ERROR_PROFESSSION", response.errorBody().toString());
                }
                aviLoadingView.hide();
            }

            @Override
            public void onFailure(Call<List<GETProfession>> call, Throwable t) {
                Log.i("TAG", t.getMessage());
                aviLoadingView.hide();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (SessionHandler.getInstance().get(Splashscreen.this, Constants.COUNTRY_JSON) != null) {
                GETCountry[] getCountryLists = gson.fromJson(SessionHandler.getInstance().get(Splashscreen.this, Constants.COUNTRY_JSON), GETCountry[].class);

                if (!(getCountryLists.length > 0)) {
                    return;
                }
                startActivity(new Intent(this, Introscreen.class));
                finish();
            } else {
                aviLoadingView.show();
                getCountryAPI();
            }
        } else {
            showToast();
        }

    }

}
