package noorim.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import noorim.navi.EventActivity;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-04-22.
 */
public class GcmIntentService extends IntentService {

    public static int ex = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

          if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("error", "Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("error", "Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String t1 = intent.getStringExtra("title");
                String m1 = intent.getStringExtra("message");
                String cafe_name = intent.getStringExtra("cafe_name");
                String menu_name = intent.getStringExtra("menu_name");

                String title = "";
                String msg = "";

                if (t1 != null && !t1.equals("")) {
                    try {
                        title = URLDecoder.decode(t1, "EUC-KR");
                        msg = URLDecoder.decode(m1, "EUC-KR");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                // Post notification of received message.
                if (!title.equals("")) {

                    // 상단의 푸시 알림 메시지 보내기
                    sendNotification(title, msg);

                    // 사용자가 주문을 한 내역이 있을 경우 화면 중앙의 쿠폰 다이얼로그 띄우기
                    if(!title.equals("메뉴 제작완료") && !menu_name.equals("메뉴 없음")) {
                        Intent intent1 = new Intent(getApplicationContext(), PushPopUp.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("cafe_name", cafe_name);
                        intent1.putExtra("menu_name", menu_name);
                        startActivity(intent1);
                    }
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with a GCM message.
    private void sendNotification(String title, String msg) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), EventActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent contentIntent = PendingIntent.getActivity(this, uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.push_icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500});

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(ex++, mBuilder.build());
    }

}
