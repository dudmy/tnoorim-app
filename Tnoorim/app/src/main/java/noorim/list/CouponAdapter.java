package noorim.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import noorim.tnoorim.R;

/**
 * Created by YuJin on 2015-04-09.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    ArrayList<CouponData> arrData;
    private static Context mContext;
    private static ImageView cafe_img;
    private CouponData object;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView cafe_name;
        ArrayList<ImageView> coupon_img;

        public ViewHolder(View v) {
            super(v);
            mContext = v.getContext();
            cafe_name = (TextView) v.findViewById(R.id.coupon_cafe_name);
            coupon_img = new ArrayList<ImageView>();
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img1));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img2));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img3));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img4));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img5));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img6));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img7));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img8));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img9));
            coupon_img.add((ImageView)v.findViewById(R.id.coupon_img10));
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CouponAdapter(ArrayList<CouponData> arr) {
        this.arrData = arr;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coupon, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        object = arrData.get(position);

        holder.cafe_name.setText(object.getCafe_name() + " 쿠폰");

        for(int i=0; i<arrData.get(position).getNum(); i++){
            holder.coupon_img.get(i).setImageResource(R.drawable.coupon_yes);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrData.size();
    }

}
