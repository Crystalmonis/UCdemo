package com.example.ucdemo;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private final List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView couponsRecyclerView;
    private LinearLayout selectedCoupon;
    private String productOriginalPrice;
    private TextView selectedCouponTitle;
    private TextView selectedCouponExpiryDate;
    private TextView selectedCouponBody;
    private TextView discountedPrice;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView couponsRecyclerView, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerView = couponsRecyclerView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCouponTitle = couponTitle;
        this.selectedCouponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;
    }

    @NonNull
    @Override
    public MyRewardsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardsAdapter.Viewholder holder, int position) {
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCouponBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getDiscORamt();
        holder.setData(type,upperLimit,lowerLimit,discORamt,body,validity);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private final TextView couponTitle;
        private final TextView couponExpiryDate;
        private final TextView couponBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.coupon_title_in_rewards_item);
            couponExpiryDate = itemView.findViewById(R.id.coupon_validity_in_rewards_item);
            couponBody = itemView.findViewById(R.id.coupon_body_in_rewards_item);
        }

        private void setData(final String type, final String upperLimit, final String lowerLimit, final String discORamt, final String body, final Date validity) {

            if(type.equals("Discount")){
                couponTitle.setText(type);
            } else {
                couponTitle.setText("FLAT Rs."+discORamt+" OFF");
            }

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            couponExpiryDate.setText("Till "+simpleDateFormat.format(validity));

            couponBody.setText(body);

            if(useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals("Discount")) {
                            selectedCouponTitle.setText(type);
                        } else {
                            selectedCouponTitle.setText("FLAT Rs." + discORamt + " OFF");
                        }

                        selectedCouponExpiryDate.setText("till " + simpleDateFormat.format(validity));
                        selectedCouponBody.setText(body);

                        if(Long.valueOf(productOriginalPrice) > Long.valueOf(upperLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(lowerLimit)){
                            if(type.equals("Discount")){
                                Long discountAmount = Long.valueOf(productOriginalPrice)*Long.valueOf(discORamt)/100;
                                discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                            } else{
                                discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + "/-");
                            }

                        } else {
                            discountedPrice.setText("Invalid");
                            Toast.makeText(itemView.getContext(), "Sorry!Your order does not match the coupon terms", Toast.LENGTH_SHORT).show();

                        }


                        if (couponsRecyclerView.getVisibility() == View.GONE) {
                            couponsRecyclerView.setVisibility(View.VISIBLE);
                            selectedCoupon.setVisibility(View.GONE);
                        } else {
                            couponsRecyclerView.setVisibility(View.GONE);
                            selectedCoupon.setVisibility(View.VISIBLE);
                        }                    }
                });
            }
        }
    }
}
