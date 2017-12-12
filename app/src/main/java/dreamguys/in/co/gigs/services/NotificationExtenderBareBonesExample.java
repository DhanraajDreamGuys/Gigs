package dreamguys.in.co.gigs.services;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

public class NotificationExtenderBareBonesExample extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {


       /* JSONObject data = receivedResult.payload.additionalData;

        if (data != null) {
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_MyUpdate);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra(EXTRA_KEY_UPDATE, data.toString());
            intentUpdate.putExtra(MESSAGE, receivedResult.payload.body);
            sendBroadcast(intentUpdate);
        }*/

        // Return true to stop the notification from displaying.
        return false;
    }
}