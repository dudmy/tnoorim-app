package noorim.server;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import noorim.list.CafeData;
import noorim.list.CouponData;
import noorim.list.EventData;
import noorim.list.MenuData;
import noorim.list.PayOrderData;
import noorim.list.OrderStateData;
import noorim.navi.CouponActivity;
import noorim.navi.EventActivity;
import noorim.navi.OrderStateActivity;
import noorim.tnoorim.MainActivity;
import noorim.tnoorim.MenuActivity;
import noorim.tnoorim.MenuFragment;
import noorim.tnoorim.PayOptionActivity;

/**
 * Created by YuJin on 2015-05-28.
 */
public class SocketThread  extends Thread {

    public static SocketIO socketIO;
    public static Handler handler = new Handler();

    public SocketCallback callback = new SocketCallback();

    public SocketThread() {}

    public void run() {
        try {
            socketIO = new SocketIO("?");
            socketIO.connect(callback);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static class SocketCallback implements IOCallback {

        @Override
        public void onDisconnect() {
            Log.i("----- socket : ", "Connection terminated -----");
        }

        public void onConnect() {
            Log.i("----- socket : ", "Connection established -----");
        }

        @Override
        public void onMessage(String s, IOAcknowledge ioAcknowledge) {
            Log.i("----- socket : ", "Server said: " + s + "-----");
        }

        @Override
        public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
            try {
                Log.i("----- socket : ", "Server said: " + jsonObject.toString(2) + " -----");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void on(String s, IOAcknowledge ioAcknowledge, final Object... objects) {

            final MainActivity subMAIN = (MainActivity) MainActivity.MAIN;
            final EventActivity subEVENT = (EventActivity) EventActivity.EVENT;
            final CouponActivity subCOUPON = (CouponActivity) CouponActivity.COUPON;
            final OrderStateActivity subORDERSTATE = (OrderStateActivity) OrderStateActivity.ORDERSTATE;
            final PayOptionActivity subPAYOPTION = (PayOptionActivity) PayOptionActivity.PAYOPTION;
            final MenuFragment subMENUFRAG;

            if ("list_cafe".equals(s) && objects.length > 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        subMAIN.arrayList.add(new CafeData(objects[0].toString(), objects[1].toString(),
                                objects[2].toString(), objects[3].toString(), objects[4].toString()));
                        subMAIN.setListAdapter();
                    }
                });
            }

            else if ("list_menu1".equals(s) && objects.length > 0) {
                if (objects[1].toString().equals("COFFEE")) {
                    subMENUFRAG = (MenuFragment) MenuActivity.MENUFRAG[1];
                } else if (objects[1].toString().equals("ICE COFFEE")) {
                    subMENUFRAG = (MenuFragment) MenuActivity.MENUFRAG[2];
                } else if (objects[1].toString().equals("NON-COFFEE")) {
                    subMENUFRAG = (MenuFragment) MenuActivity.MENUFRAG[3];
                } else {
                    subMENUFRAG = (MenuFragment) MenuActivity.MENUFRAG[4];
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        subMENUFRAG.adapter.add(new MenuData("[이벤트] "+objects[0].toString(), objects[1].toString(),
                                Integer.parseInt(objects[2].toString()), objects[3].toString(), objects[4].toString(), 400));
                        subMENUFRAG.setListAdapter();
                    }
                });
            }

            else if ("list_event".equals(s) && objects.length > 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        subEVENT.adapter.add(new EventData(objects[0].toString(), objects[1].toString(),
                                objects[2].toString(), objects[3].toString(), objects[4].toString(), false, 400));
                        subEVENT.setListAdapter();
                    }
                });
            }

            else if ("list_coupon".equals(s) && objects.length > 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        subCOUPON.arrayList.add(new CouponData(objects[0].toString(), Integer.parseInt(objects[1].toString())));
                        subCOUPON.setListAdapter();
                    }
                });
            }

            else if ("list_orderstate".equals(s) && objects.length > 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!subORDERSTATE.tempDate.equals(objects[3].toString())) {
                            subORDERSTATE.tempDate = objects[3].toString();
                            subORDERSTATE.adapter.add(new OrderStateData(objects[3].toString(), null, null, null, null, 1));
                        }
                        subORDERSTATE.adapter.add(new OrderStateData(objects[0].toString(), objects[1].toString(),
                                objects[2].toString(), objects[3].toString(), "(" + objects[4].toString() + ")", 0));
                        subORDERSTATE.setListAdapter();
                    }
                });
            }

            else if ("list_order".equals(s) && objects.length > 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        subPAYOPTION.adapter.add(new PayOrderData("", "", "", objects[0].toString(),
                                Integer.parseInt(objects[1].toString()), objects[2].toString(), 0));
                        subPAYOPTION.setListAdapter();
                    }
                });
            }

            else if ("beacon_event".equals(s) && objects.length > 0) {
                socketIO.emit("add_cart", objects[0].toString(), objects[1].toString(), objects[2].toString(), objects[3].toString(),
                        Integer.parseInt(objects[4].toString()), "regular", 0);
            }

        }

        @Override
        public void onError(SocketIOException e) {
            Log.i("----- socket : ", "Error occurred -----");
            e.printStackTrace();
        }

    }

}
