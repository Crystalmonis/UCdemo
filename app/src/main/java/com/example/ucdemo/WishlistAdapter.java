package com.example.ucdemo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    public List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist ) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        String title=wishlistModelList.get(position).getProductTitle();
        String resource=wishlistModelList.get(position).getProductImage();
        long freeCoupons=wishlistModelList.get(position).getFreeCoupons();
        String rating=wishlistModelList.get(position).getRating();
        long totalRatings=wishlistModelList.get(position).getTotalRatings();
        String productPrice=wishlistModelList.get(position).getProductPrice();
        String cuttedPrice=wishlistModelList.get(position).getCuttedPrice();
        Boolean paymentMethod=wishlistModelList.get(position).isCOD();
        holder.setData(resource,title,freeCoupons,rating,totalRatings,productPrice,cuttedPrice,paymentMethod,position);


        if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImage;
        private final TextView producttitle;
        private final TextView freeCoupons;
        private final ImageView couponIcon;
        private final TextView rating;
        private final TextView totalRatings;
        private final View priceCut;
        private final TextView productPrice;
        private final TextView cuttedPrice;
        private final TextView paymentMethod;
        private final ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_in_wishlist);
            producttitle = itemView.findViewById(R.id.product_title_in_wishlist);
            freeCoupons = itemView.findViewById(R.id.free_coupons_in_wishlist);
            couponIcon = itemView.findViewById(R.id.coupon_icon_in_wishlist);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.total_ratings_in_wishlist);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price_in_wishist);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_in_wishlist);
            paymentMethod = itemView.findViewById(R.id.payment_method_in_wishlist);
            deleteBtn = itemView.findViewById(R.id.delete_btn_in_wishlist);
        }

        private void setData(String resource,String title, long freeCouponsNo, String averageRate,long totalRatingsNo, String price, String cuttedPriceValue, boolean COD, int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.place_holder_big)).into(productImage);
            producttitle.setText(title);
            if(freeCouponsNo != 0){
                couponIcon.setVisibility(View.VISIBLE);
                if(freeCouponsNo == 1) {
                    freeCoupons.setText("Free " + freeCouponsNo + " Coupon");
                } else {
                    freeCoupons.setText("Free " + freeCouponsNo + " Coupons");
                }
            } else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
            }
            rating.setText(averageRate);
            totalRatings.setText("(" +totalRatingsNo + ")ratings ");
            productPrice.setText("Rs. " +price + " /-");
            cuttedPrice.setText("Rs. " +cuttedPriceValue + " /-");
            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);
            } else {
                paymentMethod.setVisibility(View.INVISIBLE);
            }
            if(wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBtn.setEnabled(false);
                    DBqueries.removeFromWishlist(index,itemView.getContext());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}
