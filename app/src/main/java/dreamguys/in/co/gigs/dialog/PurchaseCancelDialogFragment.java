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
import android.widget.Toast;

import java.util.HashMap;

import dreamguys.in.co.gigs.Model.POSTCancelGigs;
import dreamguys.in.co.gigs.R;
import dreamguys.in.co.gigs.fragment.MyPurchasesFragment;
import dreamguys.in.co.gigs.interfaces.CancelRequestDialog;
import dreamguys.in.co.gigs.interfaces.UpdateRequestData;
import dreamguys.in.co.gigs.network.ApiClient;
import dreamguys.in.co.gigs.network.ApiInterface;
import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.CustomProgressDialog;
import dreamguys.in.co.gigs.utils.SessionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad on 11/8/2017.
 */

public class PurchaseCancelDialogFragment extends DialogFragment implements CancelRequestDialog {

    private EditText editReason, editPaypalEID;
    private Button btnUpdate;
    String product_id = "";
    private HashMap<String, String> postCancelData = new HashMap<String, String>();
    private Context mContext;
    private CustomProgressDialog mCustomProgressDialog;
    private static UpdateRequestData callbacks;

    public PurchaseCancelDialogFragment(Context mContext) {
        this.mContext = mContext;
        mCustomProgressDialog = new CustomProgressDialog(mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cancel_dialog_fragment, container,
                false);
        getDialog().setTitle("Cancel your Order");
        getDialog().setCancelable(false);

        /*ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);*/

        editReason = (EditText) rootView.findViewById(R.id.input_reason);
        editPaypalEID = (EditText) rootView.findViewById(R.id.input_paypal_email);
        btnUpdate = (Button) rootView.findViewById(R.id.btn_update_request);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editReason.getText().toString().isEmpty()) {
                    editReason.setError("Enter reason");
                    editReason.requestFocus();
                    return;
                }
                if (editPaypalEID.getText().toString().isEmpty()) {
                    editPaypalEID.setError("Enter paypalID");
                    editPaypalEID.requestFocus();
                    return;
                }
                postCancelData.put("order_id", product_id);
                postCancelData.put("cancel_reason", editReason.getText().toString());
                postCancelData.put("paypal_email", editPaypalEID.getText().toString());
                postCancelData.put("user_id", SessionHandler.getInstance().get(mContext, Constants.USER_ID));
                postCancelData.put("time_zone", SessionHandler.getInstance().get(mContext, Constants.TIMEZONE_ID));

                if (NetworkChangeReceiver.isConnected()) {
                    mCustomProgressDialog.showDialog();
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    apiInterface.purchaseCancelGigs(postCancelData).enqueue(new Callback<POSTCancelGigs>() {
                        @Override
                        public void onResponse(Call<POSTCancelGigs> call, Response<POSTCancelGigs> response) {
                            if (response.body().getCode().equals(200)) {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                callbacks.updateRequestData(product_id);
                            } else {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            mCustomProgressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<POSTCancelGigs> call, Throwable t) {
                            Log.i("TAG", t.getMessage());
                            mCustomProgressDialog.dismiss();
                        }
                    });
                }


                getDialog().dismiss();

            }
        });

        return rootView;
    }


    @Override
    public void cancelRequestData(String product_id) {
        this.product_id = product_id;
    }

    public static void callDialgFrag(MyPurchasesFragment mMyPurchasesFragment) {
        callbacks = mMyPurchasesFragment;
    }
}
