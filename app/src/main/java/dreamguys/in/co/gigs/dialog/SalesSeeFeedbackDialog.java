package dreamguys.in.co.gigs.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import dreamguys.in.co.gigs.Model.POSTLeaveFeedback;
import dreamguys.in.co.gigs.Model.POSTMyActivity;
import dreamguys.in.co.gigs.Model.POSTPurchaseSeeFedBck;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.fragment.MyPurchasesFragment;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.CircleTransform;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import dreamguys.in.co.gigs.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 11/9/2017.
 */

public class SalesSeeFeedbackDialog extends DialogFragment {

    View inflateSeeFeBack;
    EditText editComment;
    TextView fromChatName, toChatName, fromComments, toComments, fromTime, toTime;
    RatingBar rateUsStars, fromRating, toRating;
    Button btnSendFeedback;
    Context mContext;
    HashMap<String, String> postGigsdetails = new HashMap<String, String>();
    CustomProgressDialog mCustomProgressDialog;
    POSTMyActivity.My_sale my_purchase;
    static MyPurchasesFragment callbacks;
    ImageView fromImage, toImage;
    List<POSTPurchaseSeeFedBck.User_feed> user_feed;
    int type;

    public SalesSeeFeedbackDialog(Context mContext) {
        this.mContext = mContext;
        mCustomProgressDialog = new CustomProgressDialog(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (my_purchase.getFeedback_val().equals(1)) {
            inflateSeeFeBack = inflater.inflate(R.layout.dialog_leave_feedback, null);
            inflateSeeFeBack.setPadding(10, 10, 10, 10);
            editComment = (EditText) inflateSeeFeBack.findViewById(R.id.et_comments);
            rateUsStars = (RatingBar) inflateSeeFeBack.findViewById(R.id.rb_comments);
            btnSendFeedback = (Button) inflateSeeFeBack.findViewById(R.id.btn_send_feeedback);
            btnSendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editComment.getText().toString().isEmpty()) {
                        return;
                    }

                    if (rateUsStars.getRating() == 0) {
                        return;
                    }

                    if (type == 1) {
                        postGigsdetails.put("from_user_id", my_purchase.getUser_id());
                        postGigsdetails.put("to_user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                        postGigsdetails.put("gig_id", my_purchase.getGigs_id());
                        postGigsdetails.put("order_id", my_purchase.getOrder_id());
                        postGigsdetails.put("comment", editComment.getText().toString());
                        postGigsdetails.put("rating", String.valueOf(rateUsStars.getRating()));
                        postGigsdetails.put("time_zone", SessionHandler.getInstance().get(mContext, Constants.TIMEZONE_ID));
                        postGigsdetails.put("type", String.valueOf(type));
                    } else {
                        postGigsdetails.put("from_user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                        postGigsdetails.put("to_user_id", my_purchase.getUser_id());
                        postGigsdetails.put("gig_id", my_purchase.getGigs_id());
                        postGigsdetails.put("order_id", my_purchase.getOrder_id());
                        postGigsdetails.put("comment", editComment.getText().toString());
                        postGigsdetails.put("rating", String.valueOf(rateUsStars.getRating()));
                        postGigsdetails.put("time_zone", SessionHandler.getInstance().get(mContext, Constants.TIMEZONE_ID));
                        postGigsdetails.put("type", String.valueOf(type));
                    }

                    if (NetworkChangeReceiver.isConnected()) {
                        mCustomProgressDialog.showDialog();
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        apiInterface.postLeaveFeedback(postGigsdetails).enqueue(new Callback<POSTLeaveFeedback>() {
                            @Override
                            public void onResponse(Call<POSTLeaveFeedback> call, Response<POSTLeaveFeedback> response) {

                                if (response.body().getCode().equals(200)) {
                                    Utils.toastMessage(mContext, response.body().getMessage());
                                    callbacks.UpdateFeedbackRequest(my_purchase.getOrder_id());
                                    getDialog().dismiss();
                                } else {
                                    Utils.toastMessage(mContext, response.body().getMessage());
                                }

                                mCustomProgressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<POSTLeaveFeedback> call, Throwable t) {
                                Log.i("TAG", t.getMessage());
                                mCustomProgressDialog.dismiss();
                            }
                        });


                    } else {
                        Utils.toastMessage(mContext, getString(R.string.err_internet_connection));
                    }

                }
            });

        } else {
            inflateSeeFeBack = inflater.inflate(R.layout.dialog_see_feedback, null);

            inflateSeeFeBack.setPadding(10, 10, 10, 10);
            fromChatName = (TextView) inflateSeeFeBack.findViewById(R.id.from_chat_name);
            toChatName = (TextView) inflateSeeFeBack.findViewById(R.id.to_chat_name);
            fromComments = (TextView) inflateSeeFeBack.findViewById(R.id.from_chat_msg);
            toComments = (TextView) inflateSeeFeBack.findViewById(R.id.to_chat_msg);
            fromTime = (TextView) inflateSeeFeBack.findViewById(R.id.from_chat_date);
            toTime = (TextView) inflateSeeFeBack.findViewById(R.id.to_chat_date);
            toRating = (RatingBar) inflateSeeFeBack.findViewById(R.id.to_rb_comments);
            fromRating = (RatingBar) inflateSeeFeBack.findViewById(R.id.from_rb_comments);
            fromImage = (ImageView) inflateSeeFeBack.findViewById(R.id.iv_from_prf_image);
            toImage = (ImageView) inflateSeeFeBack.findViewById(R.id.iv_to_prf_image);

            fromChatName.setText(user_feed.get(0).getFb_user_name());
            fromComments.setText(user_feed.get(0).getFb_user_comment());
            fromRating.setRating(Float.parseFloat(user_feed.get(0).getFb_user_rating()));
            fromTime.setText(user_feed.get(0).getFb_user_time());
            Picasso.with(mContext).load(user_feed.get(0).getFb_user_img()).placeholder(R.drawable.no_image).transform(new CircleTransform()).into(fromImage);

            toChatName.setText(user_feed.get(1).getFb_user_name());
            toComments.setText(user_feed.get(1).getFb_user_comment());
            toRating.setRating(Float.parseFloat(user_feed.get(1).getFb_user_rating()));
            toTime.setText(user_feed.get(1).getFb_user_time());
            Picasso.with(mContext).load(user_feed.get(1).getFb_user_img()).placeholder(R.drawable.no_image).transform(new CircleTransform()).into(toImage);

        }

        return inflateSeeFeBack;
    }

    public void setData(POSTMyActivity.My_sale my_purchase, int type, List<POSTPurchaseSeeFedBck.User_feed> user_feed) {
        this.my_purchase = my_purchase;
        this.user_feed = user_feed;
        this.type = type;
    }

    public static void callDialgFrag(MyPurchasesFragment mMyPurchasesFragment) {
        callbacks = mMyPurchasesFragment;
    }
}
