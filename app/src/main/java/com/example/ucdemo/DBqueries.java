package com.example.ucdemo;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();


    public static void loadCategories(RecyclerView categoryRecyclerView, Context context) {
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                        }
                        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                        categoryRecyclerView.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                    }
                    else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
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
                            lists.get(index).add(new HomePageModel(2, queryDocumentSnapshot.get("layout_title").toString(),queryDocumentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));

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
}
