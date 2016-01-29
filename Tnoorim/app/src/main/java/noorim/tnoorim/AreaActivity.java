package noorim.tnoorim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import noorim.server.BackPressClose;

/**
 * Created by YuJin on 2015-03-25.
 *
 * 지역 리스트 액티비티
 */
public class AreaActivity extends ActionBarActivity {

    // 타이틀
    String[] area1 = {"서울", "인천", "경기"};

    // 타이틀의 하위항목
    String[][] area2 = {
            {"성북구", "동대문구", "강북구"},
            {"부평구", "남동구", "중구"},
            {"일산", "수원", "의정부"}
    };

    private BackPressClose backPressClose;

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        backPressClose = new BackPressClose(this);

        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for(int i = 0; i < area1.length; i++) {
            GroupItem item = new GroupItem();

            item.title = area1[i];

            for(int j = 0; j < area2[i].length; j++) {
                ChildItem child = new ChildItem();
                child.title = area2[i][j];

                item.items.add(child);
            }

            items.add(item);
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.elv_animated);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

         // 이벤트 처리
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                intent.putExtra("area", "성북구");
                                break;
                            case 1:
                                intent.putExtra("area", "동대문구");
                                break;
                            case 2:
                                intent.putExtra("area", "영등포구");
                                break;
                            case 3:
                                intent.putExtra("area", "강북구");
                                break;
                        }
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });

    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
    }

    private static class ChildHolder {
        TextView title;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_area_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.tv_area2);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.list_area_group, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.tv_area1);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    // 뒤로가기 두 번 누를 시 호출
    @Override
    public void onBackPressed() {
        backPressClose.BackClose();
    }

}