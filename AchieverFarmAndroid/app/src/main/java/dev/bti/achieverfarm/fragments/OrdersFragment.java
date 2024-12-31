package dev.bti.achieverfarm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.adapter.CurrentOrderAdapter;
import dev.bti.achieverfarm.adapter.OrderListAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.common.Credentials;

public class OrdersFragment extends Fragment {

    ConstraintLayout currentOrderHolder;
    RecyclerView savedCartsRecycler, currentOrdersRecycler;
    SwipeRefreshLayout ordersSwipeRefresh;

    OrderListAdapter orderListAdapter;
    CurrentOrderAdapter currentOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi(view);
        initRecyclers();
        initData();
        initListener();
    }

    private void initUi(View view) {
        currentOrderHolder = view.findViewById(R.id.currentOrderHolder);
        savedCartsRecycler = view.findViewById(R.id.savedCartsRecycler);
        currentOrdersRecycler = view.findViewById(R.id.currentOrdersRecycler);
        ordersSwipeRefresh = view.findViewById(R.id.ordersSwipeRefresh);
    }

    private void initRecyclers() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(currentOrdersRecycler);

        currentOrderAdapter = new CurrentOrderAdapter(requireActivity());
        currentOrdersRecycler.setAdapter(currentOrderAdapter);

        orderListAdapter = new OrderListAdapter(requireActivity());
        savedCartsRecycler.setAdapter(orderListAdapter);
    }

    private void initData() {
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getOrderHelper()
                .getActiveOrders(Credentials.GetInstance().UID)
                .addOnSuccessListener(response -> currentOrderAdapter.setList(response.getData())).execute();

        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN)
                .getOrderHelper()
                .getCompletedOrders(Credentials.GetInstance().UID)
                .addOnSuccessListener(response -> {
                    orderListAdapter.setList(response.getData());
                    ordersSwipeRefresh.setRefreshing(false);
                }).execute();
    }

    public void initListener() {
        ordersSwipeRefresh.setOnRefreshListener(this::initData);
    }
}