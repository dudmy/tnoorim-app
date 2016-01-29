package noorim.navi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import noorim.server.SocketThread;
import noorim.list.CouponAdapter;
import noorim.list.CouponData;
import noorim.tnoorim.LoadingActivity;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-03-18.
 *
 * 쿠폰 액티비티
 */
public class CouponActivity extends ActionBarActivity {

    public static ActionBarActivity COUPON;

    public ArrayList<CouponData> arrayList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_coupon);

        COUPON = CouponActivity.this;

        mRecyclerView = (RecyclerView) findViewById(R.id.lv_coupon);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        arrayList = new ArrayList<CouponData>();
        mAdapter = new CouponAdapter(arrayList);
        mRecyclerView.setAdapter(mAdapter);

        // 액티비티 실행 시 소켓 통신
        SocketThread.socketIO.emit("list_coupon", LoadingActivity.PhoneNum);
    }

    public void setListAdapter() {
        mRecyclerView.setAdapter(mAdapter);
    }

}
