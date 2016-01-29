package noorim.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;

import noorim.navi.ExpandingLayout;
import noorim.tnoorim.LoadingActivity;
import noorim.tnoorim.MainActivity;
import noorim.tnoorim.MenuActivity;
import noorim.tnoorim.R;

/**
 * Created by 쟈 on 2015-03-19.
 */
public class MenuAdapter extends ArrayAdapter<MenuData> {

    @SuppressWarnings("unused")
    private Context context;
    ArrayList<MenuData> arrData;
    private LayoutInflater inflater;

    private String[] size = {"Regular", "Large"};

    public MenuAdapter(Context c, ArrayList<MenuData> arr){
        super(c, 0, arr);
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount(){
        return arrData.size();
    }

    @Override
    public MenuData getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MenuData object = arrData.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_menu, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout2));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        final TextView menu_name = (TextView)convertView.findViewById(R.id.menu_name);
        menu_name.setText(object.getMenu_name());

        final TextView price = (TextView)convertView.findViewById(R.id.price);
        price.setText("￦ " + object.getPrice());

        TextView info = (TextView)convertView.findViewById(R.id.info);
        info.setText(object.getInfo());

        ImageView menu_img = (ImageView)convertView.findViewById(R.id.menu_img);
        Glide.with(context).load(context.getString(R.string.ip_address)+object.getMenu_img()).into(menu_img);


        final TextView bt_num = (TextView)convertView.findViewById(R.id.bt_num);
        Button bt_plus = (Button)convertView.findViewById(R.id.bt_plus);
        bt_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                int num = Integer.parseInt(bt_num.getText().toString());
                if(num<9)
                    bt_num.setText(String.valueOf(++num));
            }
        });

        Button bt_minus = (Button)convertView.findViewById(R.id.bt_minus);
        bt_minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                int num = Integer.parseInt(bt_num.getText().toString());
                if(num>0)
                    bt_num.setText(String.valueOf(--num));
            }
        });

        final com.rey.material.widget.Spinner spinner_size = (com.rey.material.widget.Spinner)convertView.findViewById(R.id.spinner_size);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.row_spn, size);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spinner_size.setAdapter(adapter);


        Button put = (Button)convertView.findViewById(R.id.put);
        put.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                int num = Integer.parseInt(bt_num.getText().toString());
                int spinner_pos = spinner_size.getSelectedItemPosition();
                int price=0;
                for(int i=0; i<num; i++){
                    if(spinner_pos==0)
                        MenuActivity.cart.add(new PayOrderData(LoadingActivity.PhoneNum, MainActivity.select_cafe[0], MainActivity.select_cafe[1],
                            menu_name.getText().toString(), object.getPrice(), "regular", 0));
                    else
                        MenuActivity.cart.add(new PayOrderData(LoadingActivity.PhoneNum, MainActivity.select_cafe[0], MainActivity.select_cafe[1],
                                menu_name.getText().toString(), object.getPrice(), "large", 0));
                }
                for(int i=0; i<MenuActivity.cart.size(); i++)
                    price += MenuActivity.cart.get(i).getPrice();


                SnackBar mSnackBar;
                mSnackBar = MenuActivity.getSnackBar();

                mSnackBar.text("결제금액 " + price + "원");
                if(mSnackBar.getState() == SnackBar.STATE_SHOWED) {
                    mSnackBar.text("결제금액 " + price + "원");
                }
                else{
                    mSnackBar.text("결제금액 " + price + "원");
                    mSnackBar.applyStyle(R.style.StyleSnackBar).show();
                }
            }
        });

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout = (ExpandingLayout)convertView.findViewById(R.id
                .expanding_layout2);
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
