package noorim.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import noorim.tnoorim.R;

/**
 * Created by 쟈 on 2015-05-05.
 */
public class NaviAdapter extends BaseAdapter {

    private Context context;
    ArrayList<String> arrData;
    private LayoutInflater inflater;

    public NaviAdapter(Context c, ArrayList<String> arr){
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_navi, parent, false);
        }

        TextView navi_title = (TextView)convertView.findViewById(R.id.navi_title);
        navi_title.setText(arrData.get(position));

        return convertView;
    }

}