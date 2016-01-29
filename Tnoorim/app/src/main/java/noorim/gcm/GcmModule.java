package noorim.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by YuJin on 2015-04-22.
 */
public class GcmModule {

    // for GCM
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private GoogleCloudMessaging _gcm;
    public static String _regId;
    private String GCM_SHARED_PREFERENCE_NAME = "default";

    private GcmEvent _callbackGcm = null;

    public void setGcmListener(GcmEvent l)
    {
        _callbackGcm = l;
    }

    // 프로젝트 번호 등록
    private static final String SENDER_ID = "?";

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(GCM_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     * @return registration ID, or empty string if there is no existing registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        // Registration not found
        if (registrationId.isEmpty())
            return "";

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        // App version changed
        if (registeredVersion != currentVersion)
            return "";

        return registrationId;
    }

    // gcm 서버에 접속해서 registration id 발급 받는다
    private void registerInBackground(final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (_gcm == null)
                        _gcm = GoogleCloudMessaging.getInstance(context);
                    _regId = _gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + _regId;

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, _regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    // Saving regId on app version
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    public void initGcm(Context context)  {
        // google play service 사용 가능 여부 판단
        if (checkPlayServices(context)) {
            _gcm = GoogleCloudMessaging.getInstance(context);
            _regId = getRegistrationId(context);

            if (TextUtils.isEmpty(_regId))
                registerInBackground(context);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog
     * that allows users to download the APK from the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if( _callbackGcm != null )
                _callbackGcm.OnGooglePlayServicesNotAvailable(resultCode);
            return false;
        }
        return true;
    }

}
