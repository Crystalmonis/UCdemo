package com.example.ucdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private TextView title, price, quantity;
    private ImageView productImage, orderedIndicator, confirmedIndicator, cookedIndicator, deliveredIndicator;
    private ProgressBar order_confirm_progress, confirm_cook_progress, cook_deliver_progress;
    private TextView orderedTitle, confirmedTitle, cookedTitle, deliveredTitle;
    private TextView orderedDate, confirmedDate, cookedDate, deliveredDate;
    private TextView orderedBody, confirmedBody, cookedBody, deliveredBody;


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

        title.setText(model.getProductTitle());
        if (model.getDiscountedPrice() != null) {
            price.setText(model.getDiscountedPrice());
        } else {
            price.setText(model.getProductPrice());
        }
        quantity.setText(String.valueOf(model.getProductQuantity()));
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
                }

                break;

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
}