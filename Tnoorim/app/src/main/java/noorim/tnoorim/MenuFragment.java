package noorim.tnoorim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import noorim.server.SocketThread;
import noorim.list.MenuAdapter;
import noorim.list.MenuData;

/**
 * Created by 쟈 on 2015-03-27.
 *
 * 메뉴 액티비티에 대한 프래그먼트
 */
public class MenuFragment extends Fragment {

    private ExpandingListView2 listView;
    private ArrayList<MenuData> arrayList;
    public MenuAdapter adapter;

    public static View v;

    private static String[] MODE = {"ALL", "COFFEE", "ICE COFFEE", "NON-COFFEE", "OTHER"};
    private static int pos;

    public static MenuFragment newInstance(int position) {
        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();
        b.putString("mode", MODE[position]);
        f.setArguments(b);

        pos = position;
        MenuActivity.MENUFRAG[pos] = f;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_menu, container, false);

        listView = (ExpandingListView2) v.findViewById(R.id.lv_menu);
        arrayList = new ArrayList<MenuData>();
        adapter = new MenuAdapter(v.getContext(), arrayList);

        // 액티비티 실행 시 소켓 통신
        Intent gintent = getActivity().getIntent();
        Bundle gbundle = getArguments();
        SocketThread.socketIO.emit("list_menu", gintent.getStringExtra("cafe_name"), gbundle.getString("mode"));

        return v;
    }

    public void setListAdapter() {
        listView.setAdapter(adapter);
    }

}
