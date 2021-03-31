package com.example.ucdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountFragment extends Fragment {

    public static final int MANAGE_ADDRESS = 1;
    private Button viewAllAddressBtn,signOutBtn;
    private CircleImageView profileView, currentOrderImage;
    private TextView name, email, tvCurrentOrderStatus;
    private LinearLayout layoutContainer, recentOrdersContainer;
    private Dialog loadingDialog;
    private ImageView orderIndicator, confirmedIndicator, cookedIndicator, completedIndicator;
    private ProgressBar order_confirm_progress, confirm_cook_progress, cook_deliver_progress;
    private TextView yourRecentOrderstitle;
    private TextView addressName,address,pincode;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        //////////////Loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.show();
        //////////////Loading dialog

        profileView = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        layoutContainer = view.findViewById(R.id.layout_container);
        currentOrderImage = view.findViewById(R.id.current_order_image);
        tvCurrentOrderStatus = view.findViewById(R.id.tv_current_order_status);
        orderIndicator = view.findViewById(R.id.ordered_indicator);
        confirmedIndicator = view.findViewById(R.id.confirmed_indicator);
        cookedIndicator = view.findViewById(R.id.cooked_indicator);
        completedIndicator = view.findViewById(R.id.delivered_indicator);
        order_confirm_progress = view.findViewById(R.id.ordered_confirmed_progress);
        confirm_cook_progress = view.findViewById(R.id.packed_cooked_progress);
        cook_deliver_progress = view.findViewById(R.id.cooked_delivered_progress);
        yourRecentOrderstitle = view.findViewById(R.id.your_recent_orders_title);
        recentOrdersContainer = view.findViewById(R.id.recent_orders_container);
        addressName = view.findViewById(R.id.address_full_name);
        address = view.findViewById(R.id.address_in_my_addresses);
        pincode = view.findViewById(R.id.address_pincode_in_my_address);
        signOutBtn = view.findViewById(R.id.sign_out_btn_in_my_addresses);

        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);
        if (!DBqueries.profile.equals("")) {
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.my_account)).into(profileView);
        }

        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (MyOrderItemModel orderItemModel : DBqueries.myOrderItemModelList) {
                    if (!orderItemModel.isCancellationRequested()) {
                        if (!orderItemModel.getOrderStatus().equals("Completed") && !orderItemModel.getOrderStatus().equals("Cancelled")) {
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_big)).into(currentOrderImage);
                            tvCurrentOrderStatus.setText(orderItemModel.getOrderStatus());

                            switch (orderItemModel.getOrderStatus()) {
                                case "Ordered":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    break;
                                case "Confirmed":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    order_confirm_progress.setProgress(100);
                                    break;
                                case "Cooked":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    order_confirm_progress.setProgress(100);
                                    confirm_cook_progress.setProgress(100);
                                    break;
                                case "Out For Cooking":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    confirmedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    cookedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    completedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    order_confirm_progress.setProgress(100);
                                    confirm_cook_progress.setProgress(100);
                                    cook_deliver_progress.setProgress(100);
                                    break;
                            }

                        }
                    }
                }
                int i = 0;
                for (MyOrderItemModel myOrderItemModel : DBqueries.myOrderItemModelList) {
                    if (i < 4) {
                        if (myOrderItemModel.getOrderStatus().equals("Completed")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_big)).into((CircleImageView) recentOrdersContainer.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    yourRecentOrderstitle.setText("No recent Orders.");
                }
                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if(DBqueries.addressesModelList.size() == 0){
                            addressName.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        } else {
                            String nametext,mobileNo;
                            nametext = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getName();
                            mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getMobileNo();
                            if(DBqueries.addressesModelList.get(DBqueries.selectedaddress).getAlternateMobileNo().equals("")) {
                                addressName.setText(name + " - " + mobileNo);
                            } else {
                                addressName.setText(name + " - " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedaddress).getAlternateMobileNo());
                            }
                            String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getFlatNo();
                            String locality = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getLocality();
                            String landmark = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getLandmark();
                            String city = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getCity();
                            String state = DBqueries.addressesModelList.get(DBqueries.selectedaddress).getState();

                            if(landmark.equals("")){
                                address.setText(flatNo + " "+ locality + " "+ city +" "+ state);
                            } else {
                                address.setText(flatNo + " "+ locality +" "+ landmark +" "+ city +" "+ state);
                            }

                            pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedaddress).getPincode());
                        }
                    }
                });
                DBqueries.loadAddresses(getContext(),loadingDialog,false);
            }
        });

        DBqueries.loadOrders(getContext(), null, loadingDialog);

        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressesIntent = new Intent(getContext(), MyAddressesActivity.class);
                myAddressesIntent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myAddressesIntent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerIntent = new Intent(getContext(), UserLoginActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        return view;
    }
}