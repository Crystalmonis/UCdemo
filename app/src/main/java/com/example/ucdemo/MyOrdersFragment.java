package com.example.ucdemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {

    private RecyclerView myOrdersRecyclerView;
    public static MyOrderAdapter myOrderAdapter;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);



        myOrderAdapter = new MyOrderAdapter(DBqueries.myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);

        if(DBqueries.myOrderItemModelList.size() == 0){
            DBqueries.loadOrders(getContext(),myOrderAdapter);
        }

        return view;

    }
}