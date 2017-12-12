package dreamguys.in.co.gigs;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import dreamguys.in.co.gigs.receiver.NetworkChangeReceiver;
import dreamguys.in.co.gigs.utils.Constants;
import dreamguys.in.co.gigs.utils.SessionHandler;

/**
 * Created by Prasad on 10/23/2017.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
    public static final String MESSAGE = "MESSAGE";
    public static final String ACTION_MyUpdate = "dreamguys.in.co.gigs.UPDATE";

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        if (SessionHandler.getInstance().get(getApplicationContext(), Constants.USER_ID) != null) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                })
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {
                        JSONObject data = notification.payload.additionalData;
                        if (data != null) {
                            Intent intentUpdate = new Intent();
                            intentUpdate.setAction(ACTION_MyUpdate);
                            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
                            intentUpdate.putExtra(EXTRA_KEY_UPDATE, data.toString());
                            intentUpdate.putExtra(MESSAGE, notification.payload.body);
                            sendBroadcast(intentUpdate);

                        }
                    }
                })
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.setSubscription(true);

        JSONObject tags = new JSONObject();
        try {
            if (SessionHandler.getInstance().get(getApplicationContext(), Constants.USER_ID) != null) {
                tags.put("user_id", SessionHandler.getInstance().get(getApplicationContext(), Constants.USER_ID));
                OneSignal.sendTags(tags);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveOnesignalId();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    private void saveOnesignalId() {

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(final String userId, String registrationId) {

                Log.i("onesignaal", "userId : " + userId);
                Log.i("onesignaal", "registrationId : " + registrationId);
                SessionHandler.getInstance().save(getApplicationContext(), Constants.NOTIFICATION_IDS, userId);
            }
        });

    }


}
