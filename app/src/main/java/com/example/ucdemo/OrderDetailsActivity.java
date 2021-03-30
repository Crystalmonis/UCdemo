package com.example.ucdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private TextView title, price, quantity;
    private ImageView productImage, orderedIndicator, confirmedIndicator, cookedIndicator, deliveredIndicator;
    private ProgressBar order_confirm_progress, confirm_cook_progress, cook_deliver_progress;
    private TextView orderedTitle, confirmedTitle, cookedTitle, deliveredTitle;
    private TextView orderedDate, confirmedDate, cookedDate, deliveredDate;
    private TextView orderedBody, confirmedBody, cookedBody, deliveredBody;
    private LinearLayout rateNowContainer;
    private int rating;
    private TextView fullName,address,pincode;
    private TextView totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount;
    private Button changeOrAddAddressesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("Position", -1);
        MyOrderItemModel model = DBqueries.myOrderItemModelList.get(position);

        title = findViewById(R.id.product_title_in_order_details);
        price = findViewById(R.id.product_price_in_order_details);
        quantity = findViewById(R.id.product_quantity_in_order_details);

        productImage = findViewById(R.id.product_image_in_order_details);

        orderedIndicator = findViewById(R.id.ordered_indicator);
        confirmedIndicator = findViewById(R.id.confirmed_indicator);
        cookedIndicator = findViewById(R.id.cooked_indicator);
        deliveredIndicator = findViewById(R.id.ready_to_eat_indicator);

        order_confirm_progress = findViewById(R.id.ordered_confirmed_progress);
        confirm_cook_progress = findViewById(R.id.confirmed_cooked_progress);
        cook_deliver_progress = findViewById(R.id.cooked_ready_progress);

        orderedTitle = findViewById(R.id.ordered_title);
        confirmedTitle = findViewById(R.id.confirmed_title);
        cookedTitle = findViewById(R.id.cooking_title);
        deliveredTitle = findViewById(R.id.ready_to_eat_title);

        orderedDate = findViewById(R.id.ordered_date);
        confirmedDate = findViewById(R.id.confirmed_date);
        cookedDate = findViewById(R.id.cooking_date);
        deliveredDate = findViewById(R.id.ready_to_eat_date);

        orderedBody = findViewById(R.id.ordered_body);
        confirmedBody = findViewById(R.id.confirmed_body);
        cookedBody = findViewById(R.id.cooking_body);
        deliveredBody = findViewById(R.id.ready_to_eat_body);

        rateNowContainer = findViewById(R.id.rate_now_container);

        fullName = findViewById(R.id.fullname);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode_in_add_address);
        changeOrAddAddressesBtn = findViewById(R.id.change_or_add_address_btn);

        totalItems = findViewById(R.id.total_items);
        totalItemsPrice = findViewById(R.id.total_items_price);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        savedAmount = findViewById(R.id.saved_amount);

        title.setText(model.getProductTitle());
        if (!model.getDiscountedPrice().equals("")) {
            price.setText("Rs." + model.getDiscountedPrice() + "/-");
        } else {
            price.setText("Rs." + model.getProductPrice() + "/-");
        }
        quantity.setText("Qty : " + String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);

        switch (model.getOrderStatus()) {
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                order_confirm_progress.setVisibility(View.GONE);
                confirm_cook_progress.setVisibility(View.GONE);
                cook_deliver_progress.setVisibility(View.GONE);

                confirmedIndicator.setVisibility(View.GONE);
                confirmedBody.setVisibility(View.GONE);
                confirmedDate.setVisibility(View.GONE);
                confirmedTitle.setVisibility(View.GONE);

                cookedIndicator.setVisibility(View.GONE);
                cookedBody.setVisibility(View.GONE);
                cookedDate.setVisibility(View.GONE);
                cookedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);

                changeOrAddAddressesBtn.setVisibility(View.GONE);
                break;
            case "Confirmed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                confirmedDate.setText(String.valueOf(model.getConfirmedDate()));

                order_confirm_progress.setProgress(100);

                confirm_cook_progress.setVisibility(View.GONE);
                cook_deliver_progress.setVisibility(View.GONE);

                cookedIndicator.setVisibility(View.GONE);
                cookedBody.setVisibility(View.GONE);
                cookedDate.setVisibility(View.GONE);
                cookedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);

                changeOrAddAddressesBtn.setVisibility(View.GONE);

                break;
            case "Cooked":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                confirmedDate.setText(String.valueOf(model.getConfirmedDate()));

                cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                cookedDate.setText(String.valueOf(model.getCookedDate()));

                order_confirm_progress.setProgress(100);
                confirm_cook_progress.setProgress(100);

                cook_deliver_progress.setVisibility(View.GONE);


                deliveredIndicator.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);

                changeOrAddAddressesBtn.setVisibility(View.GONE);

                break;
            case "Completed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(model.getOrderedDate()));

                confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                confirmedDate.setText(String.valueOf(model.getConfirmedDate()));

                cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                cookedDate.setText(String.valueOf(model.getCookedDate()));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(model.getCompletedDate()));

                order_confirm_progress.setProgress(100);
                confirm_cook_progress.setProgress(100);
                cook_deliver_progress.setProgress(100);

                changeOrAddAddressesBtn.setVisibility(View.GONE);

                break;
            case "Cancelled":

                if(model.getConfirmedDate().after(model.getOrderedDate())){

                    if(model.getCookedDate().after(model.getConfirmedDate())){

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(model.getOrderedDate()));

                        confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        confirmedDate.setText(String.valueOf(model.getConfirmedDate()));

                        cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        cookedDate.setText(String.valueOf(model.getCookedDate()));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        deliveredDate.setText(String.valueOf(model.getCancelledDate()));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your booking has been cancelled");

                        order_confirm_progress.setProgress(100);
                        confirm_cook_progress.setProgress(100);
                        cook_deliver_progress.setProgress(100);

                        changeOrAddAddressesBtn.setVisibility(View.GONE);

                    } else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(model.getOrderedDate()));

                        confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        confirmedDate.setText(String.valueOf(model.getConfirmedDate()));

                        cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        cookedDate.setText(String.valueOf(model.getCancelledDate()));
                        cookedTitle.setText("Cancelled");
                        cookedBody.setText("Your booking has been cancelled");

                        order_confirm_progress.setProgress(100);
                        confirm_cook_progress.setProgress(100);

                        cook_deliver_progress.setVisibility(View.GONE);


                        deliveredIndicator.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);

                        changeOrAddAddressesBtn.setVisibility(View.GONE);
                    }

                } else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderedDate.setText(String.valueOf(model.getOrderedDate()));

                    confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    confirmedDate.setText(String.valueOf(model.getCancelledDate()));
                    confirmedTitle.setText("Cancelled");
                    confirmedBody.setText("Your booking has been cancelled");

                    order_confirm_progress.setProgress(100);

                    confirm_cook_progress.setVisibility(View.GONE);
                    cook_deliver_progress.setVisibility(View.GONE);

                    cookedIndicator.setVisibility(View.GONE);
                    cookedBody.setVisibility(View.GONE);
                    cookedDate.setVisibility(View.GONE);
                    cookedTitle.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);

                    changeOrAddAddressesBtn.setVisibility(View.GONE);
                }

                break;

        }

        //////////Rating Layout
        rating = model.getRating();
        setRating(rating);
        for(int x = 0; x < rateNowContainer.getChildCount(); x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starPosition);
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document("Z4BAs5R2DaEqkfabbXpS");
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                            if(rating != 0){
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;
                                Long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;
                                transaction.update(documentReference,starPosition + 1 +"_star",increase);
                                transaction.update(documentReference,rating + 1 + "_star",decrease);
                            } else{
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;
                                transaction.update(documentReference,starPosition + 1 + "_star",increase);
                            }

                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object object) {
                            Map<String, Object> myRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(model.getProductId())) {
                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(model.getProductId()), (long) starPosition + 1);
                            } else {
                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), model.getProductId());
                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DBqueries.myOrderItemModelList.get(position).setRating(starPosition);
                                        if (DBqueries.myRatedIds.contains(model.getProductId())) {
                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(model.getProductId()), Long.parseLong(String.valueOf(starPosition + 1)));
                                        } else {
                                            DBqueries.myRatedIds.add(model.getProductId());
                                            DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition + 1)));
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OrderDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        //////////Rating Layout

        fullName.setText(model.getFullName());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());

        totalItems.setText("Price(" + model.getProductQuantity() + " items)");

        Long totalItemsPriceValue;

        if(model.getDiscountedPrice().equals("")){
            totalItemsPriceValue = model.getProductQuantity()*Long.valueOf(model.getProductPrice());
            totalItemsPrice.setText("Rs." + totalItemsPriceValue + "/-");
        } else {
            totalItemsPriceValue = model.getProductQuantity()*Long.valueOf(model.getDiscountedPrice());
            totalItemsPrice.setText("Rs." + totalItemsPriceValue + "/-");
        }
        if(model.getDeliveryPrice().equals("FREE")){
            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText(totalItemsPrice.getText());
        } else {
            deliveryPrice.setText("Rs." + model.getDeliveryPrice() + "/-");
            totalAmount.setText("Rs." + (totalItemsPriceValue + Long.valueOf(model.getDeliveryPrice())) +"/-");
        }
        if(!model.getCuttedPrice().equals("")){
            if(!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice()))+" on this order");
            } else {
                savedAmount.setText("You saved Rs."+ model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProductPrice()))+" on this order");
            }
        } else {
            if(!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ model.getProductQuantity()*(Long.valueOf(model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice()))+" on this order");
            } else {
                savedAmount.setText("You saved Rs.0/- on this order");
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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