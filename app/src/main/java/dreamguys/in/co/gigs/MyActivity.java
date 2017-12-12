package dreamguys.in.co.gigs;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.fragment.MyPaymentFragment;
import dreamguys.in.co.gigs.fragment.MyPurchasesFragment;
import dreamguys.in.co.gigs.fragment.MySalesFragment;
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
 * Created by Prasad on 11/7/2017.
 */

public class MyActivity extends AppCompatActivity {

    CustomProgressDialog mCustomProgressDialog;
    MyPurchasesFragment purchasesFragment;
    MySalesFragment salesFragment;
    MyPaymentFragment paymentFragment;
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mCustomProgressDialog = new CustomProgressDialog(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }


        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            getMyActivity();
        } else {
            Utils.toastMessage(this, getString(R.string.err_internet_connection));
        }


    }

    private void getMyActivity() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
            apiInterface.getMyActivity(SessionHandler.getInstance().get(this, Constants.USER_ID)).enqueue(new Callback<POSTMyActivity>() {
                @Override
                public void onResponse(Call<POSTMyActivity> call, Response<POSTMyActivity> response) {
                    if (response.body().getCode().equals(200)) {
                        if (response.body().getData().getMy_purchases().size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(Constants.MY_PURARRAY_KEY, (ArrayList<? extends Parcelable>) response.body().getData().getMy_purchases());
                            purchasesFragment = new MyPurchasesFragment();
                            purchasesFragment.setArguments(bundle);
                        }else{
                            purchasesFragment = new MyPurchasesFragment();
                        }

                        if (response.body().getData().getMy_sale().size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(Constants.MY_SALEARRAY_KEY, (ArrayList<? extends Parcelable>) response.body().getData().getMy_sale());
                            salesFragment = new MySalesFragment();
                            salesFragment.setArguments(bundle);
                        }else{
                            salesFragment = new MySalesFragment();
                        }

                        if (response.body().getData().getMy_payments().size() > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.WALLET_BALANCE, response.body().getData().getWallet_balance());
                            bundle.putParcelableArrayList(Constants.MY_PAYMENTARRAY_KEY, (ArrayList<? extends Parcelable>) response.body().getData().getMy_payments());
                            paymentFragment = new MyPaymentFragment();
                            paymentFragment.setArguments(bundle);
                        }else{
                            paymentFragment = new MyPaymentFragment();
                        }
                        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
                        tabLayout.setupWithViewPager(viewPager);
                    } else {
                        Toast.makeText(MyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    mCustomProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<POSTMyActivity> call, Throwable t) {
                    mCustomProgressDialog.dismiss();
                    Log.i("TAG", t.getMessage());
                }
            });
        }

    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                if (purchasesFragment != null)
                    return purchasesFragment;
            } else if (position == 1) {
                if (salesFragment != null)
                    return salesFragment;
            } else if (position == 2) {
                if (paymentFragment != null)
                    return paymentFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Purchases";
                case 1:
                    return "Sales";
                case 2:
                    return "Payment";
            }
            return null;
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
