package dreamguys.in.co.gigs.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user5 on 06-01-2017.
 */
public class SessionHandler {
    private static final SessionHandler ourInstance = new SessionHandler();

    public static SessionHandler getInstance() {
        return ourInstance;
    }

    private SessionHandler() {
    }


    public SharedPreferences sharedPreferencesInstance(Context mContext) {
        return mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE);
    }


    public void save(Context mContext, String key, String value) {
        mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }



    public void saveInt(Context mContext, String key, int value) {
        mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public void saveBoolean(Context mContext, String key, boolean value) {
        mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(Context mContext, String key) {
        return mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public void remove(Context mContext, String key) {
        mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public void removeAll(Context mContext) {
        mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).edit().clear().commit();
    }

    public String get(Context mContext, String key) {
        return mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).getString(key, null);
    }

    public int getInt(Context mContext, String key) {
        return mContext.getSharedPreferences("GIGS", Context.MODE_PRIVATE).getInt(key, 0);
    }

}
