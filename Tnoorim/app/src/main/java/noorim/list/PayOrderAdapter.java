package noorim.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import noorim.tnoorim.R;

/**
 * Created by 쟈 on 2015-03-19.
 */
public class PayOrderAdapter extends ArrayAdapter<PayOrderData> {

    @SuppressWarnings("unused")
    private Context context;
    ArrayList<PayOrderData> arrData;
    private LayoutInflater inflater;

    public PayOrderAdapter(Context c, ArrayList<PayOrderData> arr){
        super(c, 0, arr);
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return arrData.size();
    }

    @Override
    public PayOrderData getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_pay_menu, parent, false);
        }

        TextView menu_name = (TextView)convertView.findViewById(R.id.pay_menu_name);
        menu_name.setText(arrData.get(position).getMenu_name());

        TextView price = (TextView)convertView.findViewById(R.id.pay_price);
        price.setText(arrData.get(position).getPrice() + "원");

        TextView option = (TextView)convertView.findViewById(R.id.pay_option);
        option.setText(arrData.get(position).getSize());

        return convertView;
    }

}
