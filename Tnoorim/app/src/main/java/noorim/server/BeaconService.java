package noorim.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

import java.util.Timer;
import java.util.TimerTask;

import noorim.gcm.GcmEvent;
import noorim.gcm.GcmModule;

/**
 * Created by YuJin on 2015-05-05.
 *
 * 주변에 있는 카페의 비콘을 스캔하는 서비스
 */
public class BeaconService extends Service {

    private SocketThread socketThread;
    private Timer timer;
    private TimerTask scanStart;

    private CentralManager centralManager;

    private TelephonyManager tmgr;
    public static String PhoneNum;

    @Override
    public void onCreate() {
        super.onCreate();

        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneNum = tmgr.getLine1Number();
        if(PhoneNum!=null && PhoneNum.equals("")){
            PhoneNum = PhoneNum.substring(PhoneNum.length()-10,PhoneNum.length());
            PhoneNum = "0"+PhoneNum;
        }

        centralManager = CentralManager.getInstance();
        centralManager.init(getApplicationContext());

        socketThread = new SocketThread();
        socketThread.start();

        timer = new Timer();
        doTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 비콘 스캔 시작
        timer.schedule(scanStart, 0, 3000);

        Toast.makeText(getApplicationContext(), "T.noorim 서비스 시작", Toast.LENGTH_SHORT).show();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
        scanStart.cancel();

        Toast.makeText(getApplicationContext(), "T.noorim 서비스 종료", Toast.LENGTH_SHORT).show();
    }

    public void doTask() {
        scanStart = new TimerTask() {
            @Override
            public void run() {
                Log.i("@@@@@ myTask ", " Start @@@@@");

                centralManager.setPeripheralScanListener(new PeripheralScanListener() {
                    @Override
                    public void onPeripheralScan(Central central, final Peripheral peripheral) {
                        Log.i("##### "+peripheral.getBDAddress(), "  | ##### " +peripheral.getDistance());
                        SharedPreferences prefs = getApplication().getSharedPreferences("Key", Context.MODE_MULTI_PROCESS);
                        if(prefs.getBoolean("option1", false) && (peripheral.getDistance()<=0.01)){
                            push_method(peripheral.getBDAddress());
                        }

                        else if(peripheral.getDistance()>=1.0){
                            socketThread.socketIO.emit("delete_beacon", PhoneNum, peripheral.getBDAddress());
                        }
                    }
                });
                centralManager.startScanning();
            }
        };
    }


    private void push_method(String bd_address){
        GcmModule _gcmModule = new GcmModule();
        GcmEvent onGcmEvent = new GcmEvent() {
            @Override
            public void OnGooglePlayServicesNotAvailable(int resultCode) {}
        };
        Context applicationContext = getApplicationContext();
        _gcmModule.setGcmListener(onGcmEvent);
        _gcmModule.initGcm(applicationContext);
        socketThread.socketIO.emit("beacon_event", bd_address, PhoneNum, _gcmModule._regId);
    }

}
