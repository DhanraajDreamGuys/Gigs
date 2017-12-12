package dreamguys.in.co.gigs.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTAcceptBuyRequest;
import dreamguys.in.co.gigs.R;
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
 * Created by Prasad on 11/15/2017.
 */

public class ChatCommentBox extends DialogFragment {

    private EditText commentBox;
    private Button sendButton;
    Context mContext;
    private HashMap<String, String> postBuyerChat = new HashMap<String, String>();
    private String seller_user_id = "";
    CustomProgressDialog mCustomProgressDialog;


    public ChatCommentBox(Context mContext, String seller_user_id) {
        this.mContext = mContext;
        this.seller_user_id = seller_user_id;
        mCustomProgressDialog = new CustomProgressDialog(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View chatCommentBox = inflater.inflate(R.layout.dialog_chat_comment_box, null);
        commentBox = chatCommentBox.findViewById(R.id.et_chat_comment_box);
        sendButton = chatCommentBox.findViewById(R.id.btn_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentBox.getText().toString().isEmpty()) {
                    return;
                }
                postBuyerChat.put("sell_gigs_userid", seller_user_id);
                postBuyerChat.put("chat_message_content", commentBox.getText().toString());
                postBuyerChat.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                if (NetworkChangeReceiver.isConnected()) {
                    mCustomProgressDialog.showDialog();
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    apiInterface.postMessage(postBuyerChat).enqueue(new Callback<POSTAcceptBuyRequest>() {
                        @Override
                        public void onResponse(Call<POSTAcceptBuyRequest> call, Response<POSTAcceptBuyRequest> response) {
                            if (response.body() != null) {
                                if (response.body().getCode().equals("200")) {
                                    Utils.toastMessage(mContext, response.body().getMessage());
                                } else {
                                    Utils.toastMessage(mContext, response.body().getMessage());
                                }
                            }
                            mCustomProgressDialog.dismiss();
                            getDialog().dismiss();
                        }

                        @Override
                        public void onFailure(Call<POSTAcceptBuyRequest> call, Throwable t) {
                            Log.i("TAG", t.getMessage());
                            mCustomProgressDialog.dismiss();
                        }
                    });
                } else {
                    Utils.toastMessage(mContext, getString(R.string.err_internet_connection));
                }

            }
        });


        return chatCommentBox;
    }
}
