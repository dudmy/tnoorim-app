package noorim.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YuJin on 2015-05-05.
 */
public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
            // 앱이 설치되었을 때
            Intent i = new Intent(context, BeaconService.class);
            context.startService(i);

        }

         else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)){
            // 앱이 업데이트 되었을 때
            Intent i = new Intent(context, BeaconService.class);
            context.startService(i);
        }

    }

}
