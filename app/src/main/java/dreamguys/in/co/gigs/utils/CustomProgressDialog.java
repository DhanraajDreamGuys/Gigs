package dreamguys.in.co.gigs.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import dreamguys.in.co.gigs.R;


/**
 * Created by tech on 6/29/2016.
 */
public class CustomProgressDialog extends Dialog {

    private final Context context;
    private Dialog dialog;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }



    public void showDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_layout);
        AVLoadingIndicatorView progressBar = (AVLoadingIndicatorView) dialog.findViewById(R.id.progress);
        dialog.setCancelable(false);
        dialog.show();
        int divierId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(divierId);
        if (divider != null) {
            divider.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
