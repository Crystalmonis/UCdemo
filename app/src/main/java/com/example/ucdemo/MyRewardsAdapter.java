package com.example.ucdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private final List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
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
        holder.setData(type, validity, body,upperLimit,lowerLimit,discORamt);

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

        private void setData(String type, Date validity, String body, String upperLimit, String lowerLimit, String discORamt) {

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
                        ProductDetailsActivity.couponTitle.setText(type);
                        ProductDetailsActivity.couponBody.setText(body);
                        ProductDetailsActivity.couponExpiryDate.setText(simpleDateFormat.format(validity));
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
