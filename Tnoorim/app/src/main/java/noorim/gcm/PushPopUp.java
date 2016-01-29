package noorim.gcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import noorim.server.SocketThread;
import noorim.tnoorim.PayOptionActivity;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-05-30.
 *
 * 쿠폰 이벤트 알림 액티비티
 */
public class PushPopUp extends Activity {

    private String cafe_name;
    private String menu_name;
    private TelephonyManager tmgr;
    public static String PhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneNum = tmgr.getLine1Number();
        if(PhoneNum!=null && PhoneNum.equals("")){
            PhoneNum = PhoneNum.substring(PhoneNum.length()-10,PhoneNum.length());
            PhoneNum = "0"+PhoneNum;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD // 잠금 화면 위에 뜨게하기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // 화면 깨우기

        //사용자가 화면을 끄지않는한 꺼지지않게 유지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cafe_name = getIntent().getStringExtra("cafe_name");
        menu_name = getIntent().getStringExtra("menu_name");
        pushPopUp(cafe_name, menu_name);
    }

    /**
     * 푸시 알람이 오면 화면 중앙에 나타나는 다이얼로그
     * @param cafe_name : 푸시를 보낸 카페명
     * @param menu_name : 사용자가 가장 많이 주문한 메뉴명
     */
    private void pushPopUp(final String cafe_name, final String menu_name){

        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        if(menu_name.equals("골드키위 주스")){
            dialog.setCustomImage(R.drawable.gold);
        }
        else if(menu_name.equals("유자 블렌디드 주스")){
            dialog.setCustomImage(R.drawable.yuja);
        }
        else if(menu_name.equals("카푸치노 ICE")){
            dialog.setCustomImage(R.drawable.cafu);
        }
        else if(menu_name.equals("카페 라떼 ICE")){
            dialog.setCustomImage(R.drawable.latte);
        }

        dialog.setTitleText("T.noorim (카페를 누리다)")
                .setContentText(cafe_name + "에서 " + menu_name +" 쿠폰을 보냈습니다.\n지금 사용하시겠습니까?")
                .setCancelText("취소")
                .setConfirmText("확인")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        dialog.dismiss();
                        finish();
                        SocketThread.socketIO.emit("delete_cart", PhoneNum);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        dialog.dismiss();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PayOptionActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("beacon_event", "beacon");
                        intent.putExtra("cafe_name", cafe_name);

                        startActivity(intent);
                    }
                })
                .show();
    }
}
