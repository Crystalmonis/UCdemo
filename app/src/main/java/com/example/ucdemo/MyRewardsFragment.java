package com.example.ucdemo;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyRewardsFragment extends Fragment {

    private RecyclerView rewardsRecyclerView;
    private Dialog loadingDialog;
    public static MyRewardsAdapter myRewardsAdapter;

    public MyRewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);

        //////////////Loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.show();
        //////////////Loading dialog

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(linearLayoutManager);

        myRewardsAdapter=new MyRewardsAdapter(DBqueries.rewardModelList,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);

        if(DBqueries.rewardModelList.size() == 0){
            DBqueries.loadRewards(getContext(),loadingDialog);
        }else {
            loadingDialog.dismiss();
        }

        myRewardsAdapter.notifyDataSetChanged();
        return view;
    }
}