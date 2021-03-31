package com.example.ucdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserMainPage extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;
    private static final int HomeFragment = 0, CART_FRAGMENT = 1, ORDERS_FRAGMENT = 2, WISHLIST_FRAGMENT = 3, REWARDS_FRAGMENT = 4, MYACCOUNT_FRAGMENT = 5;
    private int currentFragment = -1;
    private ImageView actionbarLogo;
    private Window window;
    private Toolbar toolbar;
    public static DrawerLayout drawer;
    public static boolean showCart = false;
    public static boolean resetMainActivity = false;
    public static Activity mainActivity;
    ActionBarDrawerToggle toggle;
    private Dialog signInDialog;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    private CircleImageView profileView;
    private TextView fullname, email;
    private ImageView addProfileIcon;

    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);
        toolbar = findViewById(R.id.toolbar);
        actionbarLogo = findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_my_mall,
                R.id.nav_my_orders, R.id.nav_my_rewards, R.id.nav_my_cart,
                R.id.nav_my_wishlist, R.id.nav_my_account, R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);

        profileView = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.main_fullname);
        email = navigationView.getHeaderView(0).findViewById(R.id.main_email);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);

        if (showCart) {
            mainActivity = this;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HomeFragment);
        }


        signInDialog = new Dialog(UserMainPage.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        Intent registerIntent = new Intent(UserMainPage.this, UserLoginActivity.class);

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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            MenuItem menuItem;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                menuItem = item;
                if (currentUser != null) {
                    drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                            int id = menuItem.getItemId();
                            if (id == R.id.nav_my_mall) {
                                actionbarLogo.setVisibility(View.VISIBLE);
                                invalidateOptionsMenu();
                                setFragment(new HomeFragment(), HomeFragment);
                                //navigationView.getMenu().getItem(0).setChecked(true);
                            } else if (id == R.id.nav_my_orders) {
                                gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                            } else if (id == R.id.nav_my_rewards) {
                                gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                            } else if (id == R.id.nav_my_cart) {
                                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                            } else if (id == R.id.nav_my_wishlist) {
                                gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                            } else if (id == R.id.nav_my_account) {
                                gotoFragment("My Account", new MyAccountFragment(), MYACCOUNT_FRAGMENT);
                            } else if (id == R.id.nav_sign_out) {
                                FirebaseAuth.getInstance().signOut();
                                DBqueries.clearData();
                                Intent registerIntent = new Intent(UserMainPage.this, UserLoginActivity.class);
                                startActivity(registerIntent);
                                finish();
                            }
                            drawer.removeDrawerListener(this);
                        }
                    });
                    return true;
                } else {
                    signInDialog.show();
                    return false;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            DBqueries.checkNotifications(false);
            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBqueries.fullname = task.getResult().getString("fullname");
                            DBqueries.email = task.getResult().getString("email");
                            DBqueries.profile = task.getResult().getString("profile");

                            fullname.setText(DBqueries.fullname);
                            email.setText(DBqueries.email);
                            if (DBqueries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(UserMainPage.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.place_holder_big)).into(profileView);
                            }

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(UserMainPage.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                fullname.setText(DBqueries.fullname);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.drawable.my_account);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(UserMainPage.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.place_holder_big)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (resetMainActivity) {
            resetMainActivity = false;
            actionbarLogo.setVisibility(View.VISIBLE);
            setFragment(new HomeFragment(), HomeFragment);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBqueries.checkNotifications(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HomeFragment) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    mainActivity = null;
                    showCart = false;
                    finish();

                } else {
                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HomeFragment);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment == HomeFragment) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.user_main_page, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart_black);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser != null) {
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(UserMainPage.this, new Dialog(UserMainPage.this), false, badgeCount, new TextView(UserMainPage.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartList.size() < 99) {
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
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    }
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            Intent notificationIntent = new Intent(this,NotificationActivity.class);
            startActivity(notificationIntent);
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                mainActivity = null;
                showCart = false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(scrollFlags);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {

            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#5b04b1"));
                toolbar.setBackgroundColor(Color.parseColor("#5b04b1"));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.user_theme));
                toolbar.setBackgroundColor(getResources().getColor(R.color.user_theme));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }
}