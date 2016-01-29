package noorim.list;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import java.util.ArrayList;

import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-04-01.
 */
public class OrderStateAdapter extends ArrayAdapter<OrderStateData>
        implements PinnedSectionListAdapter {

    private Context context;
    private ArrayList<OrderStateData> arrData;
    private LayoutInflater inflater;

    public OrderStateAdapter(Context c, ArrayList<OrderStateData> arr){
        super(c, 0, arr);
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount(){
        return arrData.size();
    }

    @Override
    public OrderStateData getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        OrderStateData object = arrData.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_orderstate, parent, false);
        }

        TextView cafe_name = (TextView)convertView.findViewById(R.id.orderstate_cafe_name);
        cafe_name.setText(object.getCafe_name());

        TextView cafe_branch = (TextView)convertView.findViewById(R.id.orderstate_cafe_branch);
        cafe_branch.setText(object.getCafe_branch());

        TextView menu_name = (TextView)convertView.findViewById(R.id.orderstate_menu_name);
        menu_name.setText(object.getMenu_name());

        TextView buy_option = (TextView)convertView.findViewById(R.id.buy_option);
        buy_option.setText(object.getBuy_option());


        if (object.getType() == 1) {
            cafe_name.setBackgroundColor(Color.parseColor("#dddddd"));
            //cafe_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            convertView.setBackgroundColor(Color.parseColor("#dddddd"));
        }

        return convertView;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return arrData.get(position).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }


}

