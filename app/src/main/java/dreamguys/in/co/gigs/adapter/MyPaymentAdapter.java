package dreamguys.in.co.gigs.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTAcceptBuyRequest;
import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.Model.POSTPurchaseSeeFedBck;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.dialog.PurchaseCancelDialogFragment;
import dreamguys.in.co.gigs.dialog.SaleStatusDialog;
import dreamguys.in.co.gigs.dialog.SalesSeeFeedbackDialog;
import dreamguys.in.co.gigs.interfaces.UpdateRequestData;
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

public class MyPaymentAdapter extends RecyclerView.Adapter<MyPaymentAdapter.MyViewHolder> {
    private Context mContext;
    private List<POSTMyActivity.My_payment> data;
    private HashMap<String, String> postDetails;
    private PurchaseCancelDialogFragment mPurchaseCancelDialogFragment;
    private FragmentManager fm;
    private UpdateRequestData callbacks;
    private SalesSeeFeedbackDialog mSalesSeeFeedbackDialog;
    private HashMap<String, String> postGigsdetails;
    private CustomProgressDialog mCustomProgressDialog;
    private SaleStatusDialog mSaleStatusDialog;
    private TextView currentAmount;
    private int VIEW_NO = 0;
    String WalletBalance;
    int value;


    public MyPaymentAdapter(Context mContext, List<POSTMyActivity.My_payment> data, TextView currentAmount, String WalletBalance, FragmentManager fm) {
        this.mContext = mContext;
        this.data = data;
        this.currentAmount = currentAmount;
        this.fm = fm;
        this.WalletBalance = WalletBalance;
        postGigsdetails = new HashMap<String, String>();
        mCustomProgressDialog = new CustomProgressDialog(mContext);
        mPurchaseCancelDialogFragment = new PurchaseCancelDialogFragment(mContext);
        mSalesSeeFeedbackDialog = new SalesSeeFeedbackDialog(mContext);
        mSaleStatusDialog = new SaleStatusDialog(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_payment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(mContext).load(Constants.BASE_URL + data.get(position).getGig_image_thumb()).into(holder.gigsImages);
        holder.sellerName.setText("Seller Name: " + data.get(position).getBuyer_name());
        holder.gigsDDate.setText("Delivery Date: " + data.get(position).getDelivery_date());
        holder.gigsOrderId.setText("Order Id: " + data.get(position).getOrder_id());
        holder.gigsTitle.setText(data.get(position).getTitle());
        holder.postedDate.setText(data.get(position).getCreated_date());
        holder.gigsRate.setText(Constants.DOLLAR_SIGN + data.get(position).getAmount());
        /*value = Integer.parseInt(WalletBalance) - Integer.parseInt(data.get(position).getAmount());*/
        if (data.get(position).getWithdraw_val().equalsIgnoreCase("1")) {
            holder.withdrawAmout.setText(data.get(position).getWithdraw_message());
            /*currentAmount.setText(String.valueOf(value));*/

        } else if (data.get(position).getWithdraw_val().equalsIgnoreCase("2")) {
            holder.withdrawAmout.setText(data.get(position).getWithdraw_message());
            /*currentAmount.setText("Available Funds: " + String.valueOf(value));*/
            /*currentAmount.setText("Available Funds: " + Constants.DOLLAR_SIGN + "0");*/
        } else {
            holder.withdrawAmout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    postGigsdetails.put("order_id", data.get(position).getOrder_id());


                    if (NetworkChangeReceiver.isConnected()) {
                        mCustomProgressDialog.showDialog();
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        apiInterface.postWithdrawRequest(postGigsdetails).enqueue(new Callback<POSTAcceptBuyRequest>() {
                            @Override
                            public void onResponse(Call<POSTAcceptBuyRequest> call, Response<POSTAcceptBuyRequest> response) {
                                if (response.body().getCode().equals(200)) {
                                    /*holder.withdrawAmout.setText("Request Sent");*/
                                    data.get(position).setWithdraw_val("1");
                                    data.get(position).setWithdraw_message("Request Send");
                                    notifyItemChanged(position);

                                    Utils.toastMessage(mContext, response.body().getMessage());
                                } else {
                                    Utils.toastMessage(mContext, response.body().getMessage());
                                }
                                mCustomProgressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<POSTAcceptBuyRequest> call, Throwable t) {
                                Log.i("TAG", t.getMessage());
                                mCustomProgressDialog.dismiss();
                            }
                        });


                    } else {
                        Utils.toastMessage(mContext, mContext.getString(R.string.err_internet_connection));
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView gigsTitle;
        final TextView postedDate;
        final TextView gigsOrderId;
        final TextView gigsDDate;
        final TextView sellerName;
        final TextView pending;
        final TextView cancel;
        final TextView newStatus;
        final TextView gigsRate;
        final ImageView gigsImages;
        final Button withdrawAmout;

        MyViewHolder(View itemView) {
            super(itemView);
            gigsTitle = (TextView) itemView.findViewById(R.id.tv_gigs_title);
            postedDate = (TextView) itemView.findViewById(R.id.posted_dated);
            pending = (TextView) itemView.findViewById(R.id.tv_pending);
            cancel = (TextView) itemView.findViewById(R.id.tv_cancel);
            newStatus = (TextView) itemView.findViewById(R.id.tv_new_status);
            gigsImages = (ImageView) itemView.findViewById(R.id.iv_gigs_images);
            sellerName = (TextView) itemView.findViewById(R.id.sellerName);
            gigsOrderId = (TextView) itemView.findViewById(R.id.order_id);
            gigsDDate = (TextView) itemView.findViewById(R.id.delivery_date);
            gigsRate = (TextView) itemView.findViewById(R.id.tv_gigs_rate);
            withdrawAmout = (Button) itemView.findViewById(R.id.amount_request);


        }
    }


    private void callSeeFedBck(final POSTMyActivity.My_sale my_purchase) {
        postGigsdetails.put("from_user_id", my_purchase.getUser_id());
        postGigsdetails.put("to_user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
        postGigsdetails.put("gig_id", my_purchase.getGigs_id());
        postGigsdetails.put("order_id", my_purchase.getOrder_id());
        postGigsdetails.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));

        if (NetworkChangeReceiver.isConnected()) {
            mCustomProgressDialog.showDialog();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.postpurchsseFedBck(postGigsdetails).enqueue(new Callback<POSTPurchaseSeeFedBck>() {
                @Override
                public void onResponse(Call<POSTPurchaseSeeFedBck> call, Response<POSTPurchaseSeeFedBck> response) {

                    if (response.body().getCode().equals(200)) {
                        if (response.body().getData().getUser_feed().size() > 0) {
                            if (mSalesSeeFeedbackDialog != null) {
                                mSalesSeeFeedbackDialog.setData(my_purchase, 1, response.body().getData().getUser_feed());
                                mSalesSeeFeedbackDialog.show(fm, "see feedback");
                            }
                        } else {
                            Utils.toastMessage(mContext, "No Feedback Found");
                        }

                    } else {
                        Utils.toastMessage(mContext, response.body().getMessage());
                    }

                    mCustomProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<POSTPurchaseSeeFedBck> call, Throwable t) {
                    mCustomProgressDialog.dismiss();
                    Log.i("TAG", t.getMessage());
                }
            });


        } else {
            Utils.toastMessage(mContext, mContext.getString(R.string.err_internet_connection));
        }

    }

}
