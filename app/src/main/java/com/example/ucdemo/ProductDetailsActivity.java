package com.example.ucdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ucdemo.UserMainPage.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailsActivity;

    public static boolean fromSearch = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private String productOriginalPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;
    private LinearLayout couponRedemptionLayout;
    private Button couponRedeemBtn;


    private TextView rewardTitle;
    private TextView rewardBody;

    ////////////Product Description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private TextView productOnlyDescriptionBody;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    ///////////Product Description

    //////Ratings Layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //////Ratings Layout


    private Button buyNowBtn;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton addToWishlistBtn;

    //////////Coupon Dialog
    private TextView couponTitle;
    private TextView couponBody;
    private TextView couponExpiryDate;
    private RecyclerView couponsRecyclerView;
    private LinearLayout selectedCoupons;
    private TextView discountedPrice;
    private TextView originalPrice;
    //////////Coupon Dialog

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    private DocumentSnapshot documentSnapshot;
    private TextView badgeCount;
    private boolean inStock = false;

    FirebaseFirestore firebaseFirestore;
    public static String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tabLayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        couponRedeemBtn = findViewById(R.id.coupon_redemption_btn);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.rewards_body);
        productDetailsTabsContainer = findViewById(R.id.product_detail_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rating_progress_bar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        couponRedemptionLayout = findViewById(R.id.coupon_redemption_layout);

        initialRating = -1;

        //////////////Loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.show();
        //////////////Loading dialog

        ///////////////Coupon dialog
        Dialog checkCouponPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toggle_recycler_view);
        couponsRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerView);
        selectedCoupons = checkCouponPriceDialog.findViewById(R.id.selected_coupon);
        couponTitle = checkCouponPriceDialog.findViewById(R.id.coupon_title_in_rewards_item);
        couponBody = checkCouponPriceDialog.findViewById(R.id.coupon_body_in_rewards_item);
        couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.coupon_validity_in_rewards_item);

        originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponsRecyclerView.setLayoutManager(layoutManager);

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }

        });
        ////////////////Coupon Dialog

        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (long x = 1; x <= (long) documentSnapshot.get("no_of_product_images"); x++) {
                                            productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                        }
                                        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                        productImagesViewPager.setAdapter(productImagesAdapter);

                                        productTitle.setText(documentSnapshot.get("product_title").toString());
                                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                        totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                        productPrice.setText("Rs. " + documentSnapshot.get("product_price").toString() + " /-");

                                        ///////////////for coupon dialog
                                        originalPrice.setText(productPrice.getText());
                                        productOriginalPrice = documentSnapshot.get("product_price").toString();
                                        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList, true,couponsRecyclerView,selectedCoupons,productOriginalPrice,couponTitle,couponExpiryDate,couponBody,discountedPrice);
                                        couponsRecyclerView.setAdapter(myRewardsAdapter);
                                        myRewardsAdapter.notifyDataSetChanged();
                                        ///////////////for coupon dialog

                                        cuttedPrice.setText("Rs. " + documentSnapshot.get("cutted_price").toString() + " /-");
                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codIndicator.setVisibility(View.VISIBLE);
                                            tvCodIndicator.setVisibility(View.VISIBLE);
                                        } else {
                                            codIndicator.setVisibility(View.INVISIBLE);
                                            tvCodIndicator.setVisibility(View.INVISIBLE);
                                        }
                                        rewardTitle.setText((long) documentSnapshot.get("free_coupons") + documentSnapshot.get("free_coupon_title").toString());
                                        rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());

                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                            productDetailsOnlyContainer.setVisibility(View.GONE);
                                            productDescription = documentSnapshot.get("product_description").toString();
                                            productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                for (long y = 1; y <= (long) documentSnapshot.get("spec_title_" + x + "_total_fields"); y++) {
                                                    productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                                                }
                                            }
                                        } else {
                                            productDetailsTabsContainer.setVisibility(View.GONE);
                                            productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                            productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                                        }

                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");

                                        for (int x = 0; x < 5; x++) {
                                            TextView ratings = (TextView) ratingsNoContainer.getChildAt(x);
                                            ratings.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                        }
                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                        averageRating.setText(documentSnapshot.get("average_rating").toString());
                                        productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                                        if (currentUser != null) {
                                            if (DBqueries.myRating.size() == 0) {
                                                DBqueries.loadRatingList(ProductDetailsActivity.this);
                                            }
                                            if (DBqueries.cartList.size() == 0) {
                                                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount, new TextView(ProductDetailsActivity.this));
                                            }
                                            if (DBqueries.wishList.size() == 0) {
                                                DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);
                                            }
                                            if (DBqueries.rewardModelList.size() == 0) {
                                                DBqueries.loadRewards(ProductDetailsActivity.this, loadingDialog,false);
                                            }
                                            if(DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0){
                                                loadingDialog.dismiss();
                                            }
                                        } else {
                                            loadingDialog.dismiss();
                                        }

                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            int index = DBqueries.myRatedIds.indexOf(productID);
                                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                            setRating(initialRating);
                                        }

                                        if (DBqueries.cartList.contains(productID)) {
                                            ALREADY_ADDED_TO_CART = true;
                                        } else {
                                            ALREADY_ADDED_TO_CART = false;
                                        }

                                        if (DBqueries.wishList.contains(productID)) {
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        } else {
                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }

                                        if (task.getResult().getDocuments().size() < (long)documentSnapshot.get("stock_quantity")) {

                                            inStock = true;
                                            buyNowBtn.setVisibility(View.VISIBLE);

                                            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (currentUser == null) {
                                                        signInDialog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(ProductDetailsActivity.this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            if (DBqueries.cartItemModelList.size() != 0) {
                                                                                DBqueries.cartItemModelList.add(0,new CartItemModel(
                                                                                        documentSnapshot.getBoolean("COD")
                                                                                        ,CartItemModel.CART_ITEM
                                                                                        , productID
                                                                                        , documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        ,inStock
                                                                                        ,(long)documentSnapshot.get("max-quantity")
                                                                                        ,(long) documentSnapshot.get("stock_quantity")

                                                                                ));
                                                                            }

                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartList.add(productID);
                                                                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            inStock = false;
                                            buyNowBtn.setVisibility(View.GONE);
                                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                            outOfStock.setText("Not available");
                                            outOfStock.setTextColor(getResources().getColor(R.color.user_theme));
                                            outOfStock.setCompoundDrawables(null,null,null,null);
                                        }

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {

                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishlist(index, ProductDetailsActivity.this);
                            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel(
                                                    productID,
                                                    documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupons")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_ratings")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    ,inStock
                                            ));
                                        }

                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to Wishlist Successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        running_wishlist_query = false;
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });
                        }
                    }
                }
            }
        });

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //////////Rating Layout
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;

                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                        } else {
                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating us!!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratings = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratings.getText().toString()));
                                                        }

                                                        initialRating = starPosition;
                                                        averageRating.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                            int index = DBqueries.wishList.indexOf(productID);
                                                            DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatings.getText().toString()));

                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                }
            });
        }
        //////////Rating Layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(
                            documentSnapshot.getBoolean("COD")
                            ,CartItemModel.CART_ITEM
                            , productID
                            , documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupons")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) 0
                            , (long) documentSnapshot.get("offers_applied")
                            ,inStock
                            ,(long) documentSnapshot.get("max-quantity")
                            ,(long) documentSnapshot.get("stock_quantity")

                    ));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this, loadingDialog,true);
                    } else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });





        couponRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCouponPriceDialog.show();
            }
        });
        ////////////////Coupon Dialog

        ///////////////Signin Dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(ProductDetailsActivity.this, UserLoginActivity.class);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLoginActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                startActivity(registerIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLoginActivity.disableCloseBtn = true;
                signInDialog.dismiss();
                startActivity(registerIntent);
            }
        });
        //////////////Signin Dialog


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);
        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBqueries.rewardModelList.size() == 0) {
                DBqueries.loadRewards(ProductDetailsActivity.this, loadingDialog,false);
            }
            if(DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0){
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.red));
        } else {
            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();
    }

    private void showDialogRecyclerView() {
        if (couponsRecyclerView.getVisibility() == View.GONE) {
            couponsRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupons.setVisibility(View.GONE);
        } else {
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupons.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart_black);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if(currentUser != null){
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount, new TextView(ProductDetailsActivity.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if(DBqueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        Intent cartIntent = new Intent(ProductDetailsActivity.this, UserMainPage.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailsActivity = null;
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            if(fromSearch){
                finish();
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, UserMainPage.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}