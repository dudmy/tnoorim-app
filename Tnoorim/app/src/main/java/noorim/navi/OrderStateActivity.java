package noorim.navi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;

import noorim.server.SocketThread;
import noorim.list.OrderStateAdapter;
import noorim.list.OrderStateData;
import noorim.tnoorim.LoadingActivity;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-03-18.
 *
 * 주문 내역 액티비티
 */
public class OrderStateActivity extends ActionBarActivity {

    public static ActionBarActivity ORDERSTATE;

    private PinnedSectionListView listView;
    private ArrayList<OrderStateData> arrayList;
    public OrderStateAdapter adapter;

    public String tempDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_orderstate);

        ORDERSTATE = OrderStateActivity.this;

        listView = (PinnedSectionListView) findViewById(R.id.plv_orderstate);
        arrayList = new ArrayList<OrderStateData>();
        adapter = new OrderStateAdapter(this, arrayList);

        // 액티비티 실행 시 소켓 통신
        SocketThread.socketIO.emit("list_orderstate", LoadingActivity.PhoneNum);
    }

    public void setListAdapter() {
        listView.setAdapter(adapter);
    }

}
