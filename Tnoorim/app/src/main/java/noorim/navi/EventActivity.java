package noorim.navi;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import noorim.server.SocketThread;
import noorim.list.EventAdapter;
import noorim.list.EventData;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-03-18.
 *
 * 이벤트 액티비티
 */
public class EventActivity extends ActionBarActivity {

    public static ActionBarActivity EVENT;

    private ExpandingListView listView;
    private ArrayList<EventData> arrayList;
    public EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_event);

        EVENT = EventActivity.this;

        listView = (ExpandingListView) findViewById(R.id.lv_event);
        arrayList = new ArrayList<EventData>();
        adapter = new EventAdapter(this, arrayList);

        // 액티비티 실행 시 소켓 통신
        SocketThread.socketIO.emit("list_event");
    }

    public void setListAdapter() {
        listView.setAdapter(adapter);
    }

}
