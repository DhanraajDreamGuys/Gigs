package dreamguys.in.co.gigs.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.Model.POSTPurchaseSeeFedBck;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.dialog.PurchaseCancelDialogFragment;
import dreamguys.in.co.gigs.dialog.PurchaseSeeFeedbackDialog;
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

public class MyPurchaseAdapter extends RecyclerView.Adapter<MyPurchaseAdapter.MyViewHolder> {
    private Context mContext;
    private List<POSTMyActivity.My_purchase> data;
    private HashMap<String, String> postGigsdetails;
    private PurchaseCancelDialogFragment mPurchaseCancelDialogFragment;
    private FragmentManager fm;
    private UpdateRequestData callbacks;
    private PurchaseSeeFeedbackDialog mPurchaseSeeFeedbackDialog;
    private CustomProgressDialog mCustomProgressDialog;


    public MyPurchaseAdapter(Context mContext, List<POSTMyActivity.My_purchase> data, FragmentManager fm) {
        this.mContext = mContext;
        this.data = data;
        postGigsdetails = new HashMap<String, String>();
        mCustomProgressDialog = new CustomProgressDialog(mContext);
        this.fm = fm;
        mPurchaseCancelDialogFragment = new PurchaseCancelDialogFragment(mContext);
        mPurchaseSeeFeedbackDialog = new PurchaseSeeFeedbackDialog(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_purchase_gigs, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(mContext).load(Constants.BASE_URL + data.get(position).getGig_image_thumb()).into(holder.gigsImages);
        holder.sellerName.setText("Seller Name: " + data.get(position).getSeller_name());
        holder.gigsDDate.setText("Delivery Date: " + data.get(position).getDelivery_date());
        holder.gigsOrderId.setText("Order Id: " + data.get(position).getOrder_id());
        holder.gigsTitle.setText(data.get(position).getTitle());
        holder.postedDate.setText(data.get(position).getCreated_date());
        holder.gigsRate.setText(data.get(position).getAmount());

        if (data.get(position).getFeedback_val().equals(0)) {
            holder.pending.setText(data.get(position).getFeedback());
        } else if (data.get(position).getFeedback_val().equals(1)) {
            holder.pending.setText(data.get(position).getFeedback());
            holder.pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPurchaseSeeFeedbackDialog != null) {
                        mPurchaseSeeFeedbackDialog.setData(data.get(position),1, null);
                        mPurchaseSeeFeedbackDialog.show(fm, "see feedback");
                    }
                }
            });

        } else {
            holder.pending.setText(data.get(position).getFeedback());
            holder.pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callSeeFedBck(data.get(position));
                }
            });
        }

        if (data.get(position).getOrder_cancel_val().equals(0)) {
            holder.cancel.setText(data.get(position).getOrder_cancel());
        } else if (data.get(position).getOrder_cancel_val().equals(1)) {
            holder.cancel.setText(data.get(position).getOrder_cancel());

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPurchaseCancelDialogFragment != null) {
                        mPurchaseCancelDialogFragment.cancelRequestData(data.get(position).getOrder_id());
                        mPurchaseCancelDialogFragment.show(fm, "cancel_dialog");
                    }
                }
            });
        } else {
            holder.cancel.setText(data.get(position).getOrder_cancel());


        }

        if (data.get(position).getStatus_msg_val().equals(0)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(1)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(2)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(3)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(4)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(5)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(6)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(7)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
        } else if (data.get(position).getStatus_msg_val().equals(8)) {
            holder.newStatus.setText(data.get(position).getOrder_status());
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
        }
    }


    private void callSeeFedBck(final POSTMyActivity.My_purchase my_purchase) {
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
                            if (mPurchaseSeeFeedbackDialog != null) {
                                mPurchaseSeeFeedbackDialog.setData(my_purchase, 1, response.body().getData().getUser_feed());
                                mPurchaseSeeFeedbackDialog.show(fm, "see feedback");
                            }
                           /* */

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
