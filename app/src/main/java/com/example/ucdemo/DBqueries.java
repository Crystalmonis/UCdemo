package com.example.ucdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static void loadCategories(RecyclerView categoryRecyclerView, Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                        }
                        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                        categoryRecyclerView.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void loadFragmentData(final RecyclerView homePageRecyclerView, Context context, final int index, String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if ((long) queryDocumentSnapshot.get("view_type") == 0) {
                                List<SliderModel> sliderModelList = new ArrayList<>();
                                long no_of_banners = (long) queryDocumentSnapshot.get("no_of_banners");
                                for (long x = 1; x < no_of_banners + 1; x++) {
                                    sliderModelList.add(new SliderModel(queryDocumentSnapshot.get("banner_" + x).toString()
                                            , queryDocumentSnapshot.get("banner_" + x + "_background").toString()));
                                }
                                lists.get(index).add(new HomePageModel(0, sliderModelList));
                            } else if ((long) queryDocumentSnapshot.get("view_type") == 1) {
                                lists.get(index).add(new HomePageModel(1, queryDocumentSnapshot.get("strip_ad_banner").toString(),
                                        queryDocumentSnapshot.get("background").toString()));
                            } else if ((long) queryDocumentSnapshot.get("view_type") == 2) {
                                List<WishlistModel> viewAllProductList = new ArrayList<>();
                                List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                long no_of_products = (long) queryDocumentSnapshot.get("no_of_products");
                                for (long x = 1; x < no_of_products + 1; x++) {
                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                            queryDocumentSnapshot.get("product_ID_" + x).toString()
                                            , queryDocumentSnapshot.get("product_image_" + x).toString()
                                            , queryDocumentSnapshot.get("product_title_" + x).toString()
                                            , queryDocumentSnapshot.get("product_subtitle_" + x).toString()
                                            , queryDocumentSnapshot.get("product_price_" + x).toString()
                                    ));
                                    viewAllProductList.add(new WishlistModel(
                                            queryDocumentSnapshot.get("product_ID_" +x).toString(),
                                            queryDocumentSnapshot.get("product_image_" + x).toString()
                                            , queryDocumentSnapshot.get("product_full_title_" + x).toString()
                                            , (long) queryDocumentSnapshot.get("free_coupons_" + x)
                                            , queryDocumentSnapshot.get("average_rating_" + x).toString()
                                            , (long) queryDocumentSnapshot.get("total_ratings_" + x)
                                            , queryDocumentSnapshot.get("product_price_" + x).toString()
                                            , queryDocumentSnapshot.get("cutted_price_" + x).toString()
                                            , (boolean) queryDocumentSnapshot.get("COD_" + x)

                                    ));
                                }
                                lists.get(index).add(new HomePageModel(2, queryDocumentSnapshot.get("layout_title").toString(), queryDocumentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductList));

                            } else if ((long) queryDocumentSnapshot.get("view_type") == 3) {
                                List<HorizontalProductScrollModel> gridLayout = new ArrayList<>();
                                long no_of_products = (long) queryDocumentSnapshot.get("no_of_products");
                                for (long x = 1; x < no_of_products + 1; x++) {
                                    gridLayout.add(new HorizontalProductScrollModel(
                                            queryDocumentSnapshot.get("product_ID_" + x).toString()
                                            , queryDocumentSnapshot.get("product_image_" + x).toString()
                                            , queryDocumentSnapshot.get("product_title_" + x).toString()
                                            , queryDocumentSnapshot.get("product_subtitle_" + x).toString()
                                            , queryDocumentSnapshot.get("product_price_" + x).toString()
                                    ));
                                }
                                lists.get(index).add(new HomePageModel(3, queryDocumentSnapshot.get("layout_title").toString(), queryDocumentSnapshot.get("layout_background").toString(), gridLayout));
                            }

                        }
                        HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                        homePageRecyclerView.setAdapter(homePageAdapter);
                        homePageAdapter.notifyDataSetChanged();
                        HomeFragment.swipeRefreshLayout.setRefreshing(false);
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public static void loadWishlist(Context context, Dialog dialog, final boolean loadProductData) {
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x=0; x<(long)task.getResult().get("list_size"); x++) {
                        wishList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBqueries.wishList.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                            }
                        } else {
                            if (ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }

                        if (loadProductData) {
                            wishlistModelList.clear();
                            String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishlistModelList.add(new WishlistModel(
                                                productId,
                                                task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , (long) task.getResult().get("free_coupons")
                                                , task.getResult().get("average_rating").toString()
                                                , (long) task.getResult().get("total_ratings")
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("cutted_price").toString()
                                                , (boolean) task.getResult().get("COD")

                                        ));
                                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishlist(int index, final Context context) {
        wishList.remove(index);
        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishlist.put("product_ID_" + x, wishList.get(x));
        }
        updateWishlist.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").set(updateWishlist)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (wishlistModelList.size() != 0) {
                                wishlistModelList.remove(index);
                                MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                            }
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
//                        if (ProductDetailsActivity.addToWishlistBtn != null) {
//                            ProductDetailsActivity.addToWishlistBtn.setEnabled(true);
//                        }
                        ProductDetailsActivity.running_wishlist_query = false;
                    }
                });
    }

    public static void loadRatingList(final Context context){
        myRatedIds.clear();
        myRating.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(long x = 0; x < (long)task.getResult().get("list_size"); x++){
                        myRatedIds.add(task.getResult().get("product_ID_" +x).toString());
                        myRating.add((long)task.getResult().get("rating_"+x));

                        if(task.getResult().get("product_ID_" +x).toString().equals(ProductDetailsActivity.productID) && ProductDetailsActivity.rateNowContainer != null){
                            ProductDetailsActivity.setRating(Integer.parseInt(String.valueOf((long)task.getResult().get("rating_"+x))) - 1);
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishList.clear();
        wishlistModelList.clear();
    }

}