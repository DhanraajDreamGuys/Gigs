package dreamguys.in.co.gigs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTBuyNow;
import dreamguys.in.co.gigs.Model.POSTDetailGig;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.payment.PaypalAct;
import dreamguys.in.co.gigs.payment.StripePay;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartModule extends AppCompatActivity {
    View HeaderLayout, FooterLayout;
    ListView CartLayout;
    int totalCost;
    POSTDetailGig.Gigs_details mGigs_details;
    List<POSTDetailGig.Gigs_details> fillGigs = new ArrayList<POSTDetailGig.Gigs_details>();
    private String SUPERFAST_DESC, SUPERFAST_DAYS, SUPERFAST_CHARGES, deliveryDays;
    List<POSTDetailGig.Extra_gig> order_gigs;
    RadioGroup choosePaymentGateway;
    String paymentType = "", fastDeliveryStatus = "", extraGigs = "";
    private HashMap<String, String> postGigsDetails = new HashMap<String, String>();
    Gson mGson;
    CustomProgressDialog mCustomProgressDialog;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_module);
        fillGigs.clear();
        mGigs_details = (POSTDetailGig.Gigs_details) getIntent().getSerializableExtra("Gigs_Detail");
        mCustomProgressDialog = new CustomProgressDialog(this);
        mGson = new Gson();

        Bundle bundle = this.getIntent().getExtras();
        SUPERFAST_CHARGES = getIntent().getExtras().getString(Constants.SUPERFAST_CHARGES);
        SUPERFAST_DAYS = getIntent().getExtras().getString(Constants.SUPERFAST_DAYS);
        SUPERFAST_DESC = getIntent().getExtras().getString(Constants.SUPERFAST_DELIVERY_DESC);
        order_gigs = (List<POSTDetailGig.Extra_gig>) bundle.getSerializable("order_gigs");
        deliveryDays = getIntent().getExtras().getString("delivery_days");

        if (mGigs_details != null) {
            fillGigs.add(mGigs_details);
        }
        totalCost = getIntent().getIntExtra("total_cost", 0);
        CartLayout = (ListView) findViewById(R.id.lv_cart);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        HeaderLayout = getLayoutInflater().inflate(R.layout.include_header_cart, null);
        FooterLayout = getLayoutInflater().inflate(R.layout.include_footer_cart, null);

        choosePaymentGateway = (RadioGroup) FooterLayout.findViewById(R.id.rg_payment_type);

        /*int defaultId = choosePaymentGateway.getCheckedRadioButtonId();
        RadioButton defaultRB = (RadioButton) findViewById(defaultId);
        paymentType = defaultRB.getText().toString();*/

        choosePaymentGateway.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                paymentType = rb.getText().toString();
            }
        });


        CartLayout.addHeaderView(HeaderLayout);

        if (fillGigs.size() > 0) {
            CartAdapter aCartAdapter = new CartAdapter(this, fillGigs);
            CartLayout.setAdapter(aCartAdapter);
        }
        CartLayout.addFooterView(FooterLayout);

    }

    public void buyNow(View view) {
        if (SessionHandler.getInstance().get(this, Constants.USER_ID) != null) {
            if (!paymentType.isEmpty() && paymentType != null) {
                switch (paymentType) {
                    case "Paypal":
                        buyingGigsHere();
                        break;
                    case "Stripe":
                        buyingGigsHere();

                        break;
                }
            } else {
                Toast.makeText(this, "Please choose the payment type...", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please login and buy your item.", Toast.LENGTH_SHORT).show();
        }
    }

    private void buyingGigsHere() {
        postGigsDetails.put("gig_id", mGigs_details.getId());
        postGigsDetails.put("seller_id", mGigs_details.getUser_id());
        postGigsDetails.put("gig_rate", "" + totalCost);
        postGigsDetails.put("buyer_id", SessionHandler.getInstance().get(this, Constants.USER_ID));
        postGigsDetails.put("super_fast_delivery", fastDeliveryStatus);
        if (!deliveryDays.isEmpty()) {
            postGigsDetails.put("total_delivery_days", deliveryDays);
        } else {
            postGigsDetails.put("total_delivery_days", "");
        }
        postGigsDetails.put("options", extraGigs);
        postGigsDetails.put("source", paymentType.toLowerCase());

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.postBuyingGigsdetails(postGigsDetails).enqueue(new Callback<POSTBuyNow>() {
                @Override
                public void onResponse(Call<POSTBuyNow> call, Response<POSTBuyNow> response) {


                    switch (paymentType) {
                        case "Paypal":
                            if (response.body().getCode().equals(200)) {
                                Intent intentPayAct = new Intent(CartModule.this, PaypalAct.class);
                                intentPayAct.putExtra("total_cost", totalCost);
                                intentPayAct.putExtra("title", mGigs_details.getTitle());
                                intentPayAct.putExtra("id", response.body().getData().getGig_order_id().toString());
                                intentPayAct.putExtra("bundle", mGigs_details);
                                startActivity(intentPayAct);
                            }
                            break;
                        case "Stripe":
                            Intent intentPayAct = new Intent(CartModule.this, StripePay.class);
                            intentPayAct.putExtra("total_cost", totalCost);
                            intentPayAct.putExtra("title", mGigs_details.getTitle());
                            intentPayAct.putExtra("id", response.body().getData().getGig_order_id().toString());
                            intentPayAct.putExtra("bundle", mGigs_details);
                            startActivity(intentPayAct);
                            break;
                    }

                    mCustomProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<POSTBuyNow> call, Throwable t) {
                    mCustomProgressDialog.dismiss();
                    Log.i("TAG", t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
        }


    }

    private class CartAdapter extends BaseAdapter {
        Context mContext;
        List<POSTDetailGig.Gigs_details> mGigs_details;
        LayoutInflater mInflater;

        CartAdapter(Context mContext, List<POSTDetailGig.Gigs_details> mGigs_details) {
            this.mContext = mContext;
            this.mGigs_details = mGigs_details;
            mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mGigs_details.size();
        }

        @Override
        public POSTDetailGig.Gigs_details getItem(int position) {
            return mGigs_details.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View convertView = view;
            ViewHolder mViewHolder;

            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.adapter_cart, null);
                mViewHolder.itemNo = (TextView) convertView.findViewById(R.id.item_no);
                mViewHolder.productName = (TextView) convertView.findViewById(R.id.product_name);
                mViewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
                mViewHolder.gigsPrices = (TextView) convertView.findViewById(R.id.gigs_price);
                mViewHolder.totalcost = (TextView) convertView.findViewById(R.id.total_cost);
                mViewHolder.fastextraNames = (TextView) convertView.findViewById(R.id.extras_name);
                mViewHolder.fastextraQuantity = (TextView) convertView.findViewById(R.id.extra_quantity);
                mViewHolder.fastextraCost = (TextView) convertView.findViewById(R.id.extra_cost);
                mViewHolder.llExtrasGigs = (LinearLayout) convertView.findViewById(R.id.ll_extra_gigs);
                mViewHolder.ll_dynamic_extras_gigs = (LinearLayout) convertView.findViewById(R.id.ll_dynamic_extras_gigs);

                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.itemNo.setText("1");
            mViewHolder.productName.setText(getItem(position).getTitle());
            mViewHolder.quantity.setText("1");
            mViewHolder.gigsPrices.setText("$ " + getItem(position).getGig_price());

            if (order_gigs.size() > 0) {
                for (int i = 0; i < order_gigs.size(); i++) {
                    View dynamicview = getLayoutInflater().inflate(R.layout.include_extras_gigs, null);
                    TextView extraNames = (TextView) dynamicview.findViewById(R.id.super_fast_extras_name);
                    TextView extraQuantity = (TextView) dynamicview.findViewById(R.id.super_fast_extra_quantity);
                    TextView extraCost = (TextView) dynamicview.findViewById(R.id.super_fast_extra_cost);
                    extraNames.setText(order_gigs.get(i).getExtra_gigs());
                    extraQuantity.setText("1");
                    extraCost.setText("$ " + order_gigs.get(i).getExtra_gigs_amount());
                    mViewHolder.ll_dynamic_extras_gigs.addView(dynamicview);
                    mViewHolder.ll_dynamic_extras_gigs.invalidate();
                }

                extraGigs = mGson.toJson(order_gigs);

            } else {
                mViewHolder.ll_dynamic_extras_gigs.setVisibility(View.GONE);
            }

            if (SUPERFAST_CHARGES != null && SUPERFAST_DAYS != null && SUPERFAST_DESC != null) {
                if (!SUPERFAST_CHARGES.isEmpty() && !SUPERFAST_DAYS.isEmpty() && !SUPERFAST_DESC.isEmpty()) {
                    mViewHolder.fastextraNames.setText(SUPERFAST_DESC);
                    mViewHolder.fastextraQuantity.setText("1");
                    mViewHolder.fastextraCost.setText("$ " + SUPERFAST_CHARGES);
                    fastDeliveryStatus = "1";
                } else {
                    mViewHolder.llExtrasGigs.setVisibility(View.GONE);
                    fastDeliveryStatus = "0";
                }
            } else {
                mViewHolder.llExtrasGigs.setVisibility(View.GONE);
                fastDeliveryStatus = "0";
            }


            mViewHolder.totalcost.setText("$ " + totalCost);
            return convertView;
        }

        class ViewHolder {
            TextView itemNo, productName, quantity, totalcost, gigsPrices, fastextraNames, fastextraQuantity, fastextraCost;
            LinearLayout llExtrasGigs, ll_dynamic_extras_gigs;
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
