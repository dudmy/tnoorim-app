package noorim.tnoorim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import noorim.server.SocketThread;
import noorim.list.PayOrderData;
import noorim.list.PayOrderAdapter;
import noorim.navi.OrderStateActivity;

/**
 * Created by YuJin on 2015-03-28.
 *
 * 결제 액티비티
 */
public class PayOptionActivity extends ActionBarActivity {

    public static ActionBarActivity PAYOPTION;
    private MenuActivity subMenu = (MenuActivity)MenuActivity.MENU;

    public static com.rey.material.widget.RadioButton rbMobile;
    public static com.rey.material.widget.RadioButton rbCoupon;
    private com.rey.material.widget.CheckBox cbAgreeAll;
    private com.rey.material.widget.CheckBox cbAgree1;
    private com.rey.material.widget.CheckBox cbAgree2;
    private com.rey.material.widget.CheckBox cbAgree3;

    private Button btnPay;
    private TextView sumPrice;
    public int couponNum;

    private ListView listView;
    private ArrayList<PayOrderData> arrayList;
    public PayOrderAdapter adapter;

    private TelephonyManager tmgr;
    public static String PhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_option);

        PAYOPTION = PayOptionActivity.this;

        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneNum = tmgr.getLine1Number();
        if(PhoneNum!=null && PhoneNum.equals("")){
            PhoneNum = PhoneNum.substring(PhoneNum.length()-10,PhoneNum.length());
            PhoneNum = "0"+PhoneNum;
        }

        rbMobile = (com.rey.material.widget.RadioButton) findViewById(R.id.rb_pay_mobile);
        rbCoupon = (com.rey.material.widget.RadioButton) findViewById(R.id.rb_pay_coupon);
        cbAgreeAll = (com.rey.material.widget.CheckBox) findViewById(R.id.cb_agree_all);
        cbAgree1 = (com.rey.material.widget.CheckBox) findViewById(R.id.cb_agree1);
        cbAgree2 = (com.rey.material.widget.CheckBox) findViewById(R.id.cb_agree2);
        cbAgree3 = (com.rey.material.widget.CheckBox) findViewById(R.id.cb_agree3);
        sumPrice = (TextView)findViewById(R.id.tv_price);
        btnPay = (Button) findViewById(R.id.btn_pay);

        listView = (ListView) findViewById(R.id.lv_pay_menu);
        arrayList = new ArrayList<PayOrderData>();
        adapter = new PayOrderAdapter(this, arrayList);

        // 결제하기 버튼
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAgree1.isChecked() && cbAgree2.isChecked() && cbAgree3.isChecked()) {
                    if (rbMobile.isChecked() || rbCoupon.isChecked()) {
                        alertCheckPAY();
                    } else {
                        Toast.makeText(getBaseContext(), "결제 수단을 선택하세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "동의가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 모두 동의 체크박스
        cbAgreeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbAgreeAll.isChecked()) {
                    cbAgree1.setChecked(true);
                    cbAgree2.setChecked(true);
                    cbAgree3.setChecked(true);
                } else {
                    cbAgree1.setChecked(false);
                    cbAgree2.setChecked(false);
                    cbAgree3.setChecked(false);
                }
            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rbMobile.setChecked(rbMobile == buttonView);
                    rbCoupon.setChecked(rbCoupon == buttonView);
                }
            }
        };
        rbMobile.setOnCheckedChangeListener(listener);
        rbCoupon.setOnCheckedChangeListener(listener);

        // 액티비티 실행 시 소켓 통신
        Intent gintent = getIntent();
        if(gintent.getStringExtra("beacon_event").equals("beacon")){
            SocketThread.socketIO.emit("list_order", PhoneNum);
            rbCoupon.setEnabled(false);
        } else {
            SocketThread.socketIO.emit("list_order", PhoneNum);
            SocketThread.socketIO.emit("count_coupon", MainActivity.select_cafe[0], PhoneNum);
        }

    }

    public void setListAdapter() {
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);

        int cal = 0;
        for (int i=0; i<arrayList.size(); i++)
            cal = cal + arrayList.get(i).getPrice();
        sumPrice.setText("총 결제금액 : " + String.valueOf(cal)+"원");
    }

    public void setCoupon() {
        rbCoupon.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketThread.socketIO.emit("delete_cart", PhoneNum);
    }

    private void alertCheckPAY(){
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("꼭 읽어주세요!")
                .setContentText("결제 후에는 주문취소를 할 수 없습니다.")
                .setCancelText("주문취소")
                .setConfirmText("결제하기")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("주문취소")
                                .setContentText("주문을 취소하셨습니다.")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        dialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("주문완료")
                                .setContentText("음료가 완성되면 알림이 옵니다.")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        // reuse previous dialog instance, keep widget user
                                        Intent gintent = getIntent();
                                        if (gintent.getStringExtra("beacon_event").equals("beacon")) {
                                            SocketThread.socketIO.emit("buy_by_mobile", PhoneNum, gintent.getStringExtra("cafe_name"), couponNum);
                                        } else {
                                            if (PayOptionActivity.rbMobile.isChecked()) {
                                                SocketThread.socketIO.emit("buy_by_mobile", PhoneNum, MainActivity.select_cafe[0], couponNum);
                                            } else {
                                                SocketThread.socketIO.emit("buy_by_coupon", PhoneNum, MainActivity.select_cafe[0]);
                                            }
                                        }

                                        // 제작 완료시 푸시를 위해 DB에 regid 저장
                                        SharedPreferences prefs = getSharedPreferences("default", MODE_PRIVATE);
                                        String registrationId = prefs.getString("registration_id", "");
                                        SocketThread.socketIO.emit("set_regid", PhoneNum, registrationId);

                                        dialog.dismiss();
                                        finish();
                                        if (!gintent.getStringExtra("beacon_event").equals("beacon")) {
                                            subMenu.finish();
                                        }

                                        Intent intent = new Intent(getApplicationContext(), OrderStateActivity.class);
                                        startActivity(intent);

                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }

    // 스크롤뷰 안에 리스트뷰의 높이가 재대로 출력되지 않음. 리스트의 높이를 직접 계산하여 넣어주는 방법
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) { return; }

        int totalHeight = 0;
        for(int i=0; i<listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
