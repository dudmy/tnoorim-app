package noorim.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import noorim.navi.ExpandingLayout;
import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-04-01.
 */
public class EventAdapter extends ArrayAdapter<EventData> {


    private Context context;
    ArrayList<EventData> arrData;
    private LayoutInflater inflater;

    public EventAdapter(Context c, ArrayList<EventData> arr){
        super(c, 0, arr);
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return arrData.size();
    }

    @Override
    public EventData getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        EventData object = arrData.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_event, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        TextView event_name = (TextView)convertView.findViewById(R.id.event_name);
        event_name.setText(object.getEvent_name());

        TextView cafe_name = (TextView)convertView.findViewById(R.id.event_cafe_name);
        cafe_name.setText(object.getCafe_name() + " ");

        TextView event_info = (TextView)convertView.findViewById(R.id.event_info);
        event_info.setText(object.getEvent_info());

        TextView event_day = (TextView)convertView.findViewById(R.id.event_day);
        event_day.setText(object.getStartday() + " ~ " + object.getEndday());

        ImageView imgView = (ImageView)convertView.findViewById(R.id.event_cafe_img);
        if(arrData.get(position).getIs_use() == true) {
            imgView.setImageResource(R.drawable.event_end);
        } else {
            imgView.setImageResource(R.drawable.event_ing);
        }

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout = (ExpandingLayout)convertView.findViewById(R.id
                .expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

}
