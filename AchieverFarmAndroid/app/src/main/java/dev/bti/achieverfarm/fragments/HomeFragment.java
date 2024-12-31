package dev.bti.achieverfarm.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dev.bti.achieverfarm.Application;
import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.adapter.HomeListAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.common.Credentials;

public class HomeFragment extends Fragment {

    ConstraintLayout homeShopByFresh, homeShopByDried;
    RecyclerView homeRecycler;
    HomeListAdapter homeListAdapter;
    SwipeRefreshLayout homeSwipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initUi(rootView);
        initClicks();
        fetchData();
        initRecycler();
        initListener();
        return rootView;
    }

    public void initUi(View rootView) {
        homeShopByFresh = rootView.findViewById(R.id.homeShopByFresh);
        homeShopByDried = rootView.findViewById(R.id.homeShopByDried);
        homeRecycler = rootView.findViewById(R.id.homeRecycler);
        homeSwipeRefresh = rootView.findViewById(R.id.homeSwipeRefresh);
    }

    public void initClicks() {
        homeShopByFresh.setOnClickListener(v -> {

        });

        homeShopByDried.setOnClickListener(v -> {

        });
    }

    public void fetchData() {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper().getItemsRecommendations().addOnSuccessListener(response -> {
            homeListAdapter.addNewSection("Our Recommendations", response.getData());
            Application.appendItemCache(response.getData());
        }).addOnFailureListener(e -> Log.e("Our Recommendations", e.getMessage(), e)).execute();

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper().getItemsOrderedBefore().addOnSuccessListener(response -> {
            homeListAdapter.addNewSection("Ordered Before", response.getData());
            Application.appendItemCache(response.getData());
        }).addOnFailureListener(e -> Log.e("Ordered Before", e.getMessage(), e)).execute();
    }

    public void initRecycler() {
        homeListAdapter = new HomeListAdapter(requireActivity());
        homeRecycler.setAdapter(homeListAdapter);
    }

    public void initListener() {
        homeSwipeRefresh.setOnRefreshListener(() -> {
            homeListAdapter.setReset(true);
            SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper().getItemsRecommendations().addOnSuccessListener(response -> {
                homeListAdapter.addNewSection("Our Recommendations", response.getData());
                Application.appendItemCache(response.getData());
            }).addOnFailureListener(e -> Log.e("Our Recommendations", e.getMessage(), e)).execute();

            SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper().getItemsOrderedBefore().addOnSuccessListener(response -> {
                homeListAdapter.addNewSection("Ordered Before", response.getData());
                Application.appendItemCache(response.getData());
                homeSwipeRefresh.setRefreshing(false);
            }).addOnFailureListener(e -> Log.e("Ordered Before", e.getMessage(), e)).execute();
        });
    }
}