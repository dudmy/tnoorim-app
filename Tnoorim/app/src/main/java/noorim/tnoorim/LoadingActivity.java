package noorim.tnoorim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import noorim.server.SocketThread;

/**
 * Created by YuJin on 2015-03-25.
 *
 * 로딩 액티비티
 */
public class LoadingActivity extends Activity {

    // 원래 로딩 액티비티 변수
    private Intent intent;
    private TelephonyManager tmgr;
    public static String PhoneNum;

    // GPS 액티비티 변수
    double x, y;
    public String address;
    LocationManager locationManager;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    private SocketThread socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 소켓 스레드 실행
        socket = new SocketThread();
        socket.start();

        // 기기의 휴대폰 번호 받기
        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneNum = tmgr.getLine1Number();
        if(PhoneNum!=null && PhoneNum.equals("")){
            PhoneNum = PhoneNum.substring(PhoneNum.length()-10,PhoneNum.length());
            PhoneNum = "0"+PhoneNum;
        }

        /*
        // 기기의 RegId 받기
        GcmModule _gcmModule = new GcmModule();
        GcmEvent onGcmEvent = new GcmEvent() {
            @Override
            public void OnGooglePlayServicesNotAvailable(int resultCode) {
                final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, LoadingActivity.this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    finish();
                }
            }
        };
        Context applicationContext = getApplicationContext();
        _gcmModule.setGcmListener(onGcmEvent);
        _gcmModule.initGcm(applicationContext);
        */

        // GPS 이용해 현재 위치 받기
        address = null;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable(){
            @Override
            public void run() {
                // GPS 관련 서비스 저장
                String context = Context.LOCATION_SERVICE;
                LocationManager locationManager = (LocationManager)getSystemService(context);

                // GPS 체크 관련 구문
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    alertCheckGPS(); // GPS 체크 관련 함수 호출
                } else {
                    if(!gps_enabled && !network_enabled) {
                        Handler hd = new Handler();
                        hd.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                // GPS 관련 서비스 저장
                                String context = Context.LOCATION_SERVICE;
                                LocationManager locationManager = (LocationManager)getSystemService(context);

                                // GPS 체크 관련 구문
                                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    alertCheckGPS();
                                } else {
                                    intent = new Intent(getApplicationContext(), LoadingActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, 1000);
                        Toast.makeText(getApplicationContext(), "nothing is enabled", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), AreaActivity.class);
                        startActivity(intent);
                    }
                    if(gps_enabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
                    }
                    if(network_enabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                    }
                }
            }
        }, 1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            SocketThread.socketIO.emit("delete_cart", PhoneNum);
        }catch(Exception e) {

        }
    }

    // GPS 이용
    LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            x = location.getLatitude();
            y = location.getLongitude();
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);

            setAddress(x, y);
            Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();

            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("area", address);
            startActivity(intent);
            finish();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    // 네트워크 이용
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            x = location.getLatitude();
            y = location.getLongitude();
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);

            setAddress(x, y);
            Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();

            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("area", address);
            startActivity(intent);
            finish();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    // 위도, 경도로 주소 구하기
    public void setAddress(double x, double y) {
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);

        try {
            List<Address> list = geocoder.getFromLocation(x, y, 1);
            StringBuilder sb = new StringBuilder();

            if(null!= list && list.size() > 0){
                Address addr = list.get(0);
                sb.append(addr.getLocality());
                address = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GPS 설정 팝업창
    private void alertCheckGPS(){
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("위치 서비스 설정")
                .setContentText("위치 정보 사용 설정이 꺼져있습니다.\n설정으로 이동하시겠습니까?")
                .setCancelText("취소")
                .setConfirmText("확인")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        intent = new Intent(getApplicationContext(), AreaActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        intent = new Intent(getApplicationContext(), LoadingActivity.class);
                        startActivity(intent);
                        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

}
