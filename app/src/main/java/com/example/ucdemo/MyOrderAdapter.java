package com.example.ucdemo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {

    private final List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder holder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
        //int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String orderStatus = myOrderItemModelList.get(position).getOrderStatus();
        Date date;
        switch (orderStatus){
            case "Ordered":
                date = myOrderItemModelList.get(position).getOrderedDate();
                break;
            case "Confirmed":
                date = myOrderItemModelList.get(position).getConfirmedDate();
                break;
            case "Cooked":
                date = myOrderItemModelList.get(position).getCookedDate();
                break;
            case "Completed":
                date = myOrderItemModelList.get(position).getCompletedDate();
                break;
            case "Cancelled":
                date = myOrderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = myOrderItemModelList.get(position).getCancelledDate();

        }
        holder.setData(resource,title,orderStatus,date);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private final ImageView productImage;
        private final ImageView orderIndicator;
        private final TextView productTitle;
        private final TextView deliveryStatus;
        private final LinearLayout rateNowContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_in_my_orders);
            productTitle = itemView.findViewById(R.id.product_title_in_my_orders);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext(),OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });
        }

        private void setData(String resource,String title, String orderStatus, Date date){
            Glide.with(itemView.getContext()).load(resource).into(productImage);
            productTitle.setText(title);
            if(orderStatus.equals("Cancelled")) {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));
            } else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }
            deliveryStatus.setText(orderStatus + String.valueOf(date));

            //////////Rating Layout
            //setRating(rating);
            for(int x = 0; x < rateNowContainer.getChildCount(); x++){
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRating(starPosition);
                    }
                });
            }
            //////////Rating Layout
        }

        private void setRating(int starPosition) {
            for(int x = 0; x < rateNowContainer.getChildCount(); x++){
                ImageView starButton = (ImageView)rateNowContainer.getChildAt(x);
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if(x <= starPosition){
                    starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }
    }
}
