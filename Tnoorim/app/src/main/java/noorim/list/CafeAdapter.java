package noorim.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import noorim.tnoorim.MainActivity;
import noorim.tnoorim.MenuActivity;
import noorim.tnoorim.R;

/**
 * Created by 쟈 on 2015-03-19.
 */
public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> {

    ArrayList<CafeData> arrData;
    private static Context mContext;
    private static ImageView cafe_img;
    private CafeData object;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cafe_name;
        TextView cafe_branch;
        TextView time;
        TextView cafe_tel;

        public ViewHolder(View v) {
            super(v);

            mContext = v.getContext();
            cafe_branch = (TextView) v.findViewById(R.id.cafe_branch);
            cafe_name = (TextView) v.findViewById(R.id.cafe_name);
            time = (TextView) v.findViewById(R.id.time);
            cafe_tel = (TextView) v.findViewById(R.id.cafe_tel);
            cafe_img = (ImageView)v.findViewById(R.id.cafe_img);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), MenuActivity.class);
                    intent.putExtra("cafe_name", cafe_name.getText());

                    MainActivity.select_cafe[0] = cafe_name.getText().toString();
                    MainActivity.select_cafe[1] = cafe_branch.getText().toString();

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CafeAdapter(ArrayList<CafeData> arr) {
        this.arrData = arr;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CafeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cafe, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        object = arrData.get(position);

        holder.cafe_name.setText(object.getCafe_name());
        holder.cafe_branch.setText(object.getCafe_branch());
        holder.time.setText("Time. " + object.getTime());
        holder.cafe_tel.setText("Tel. " + object.getCafe_tel());

        Glide.with(mContext).load(mContext.getString(R.string.ip_address)+object.getCafe_img()).into(cafe_img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrData.size();
    }

}
