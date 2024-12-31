package dev.bti.achieverfarm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import dev.bti.achieverfarm.OrderActivity;
import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.models.Order;


public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.ViewHolder> {

    FragmentActivity activity;
    List<Order> orders = new ArrayList<>();

    public CurrentOrderAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CurrentOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.format_current_order, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        holder.listName.setText(order.getSummary());
        holder.listDate.setText(formatter.format(order.getTimestamp()));
        holder.listAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getPaymentDetails().getAmount()));
        holder.listQuantity.setText(String.format(Locale.getDefault(), "%d items", order.getQuantity()));

        holder.ordersTrack.setOnClickListener(v -> {
            Intent intent = new Intent(activity, OrderActivity.class);
            intent.putExtra("order", order);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setList(List<Order> newOrders) {
        Map<String, Order> newOrderMap = newOrders.stream()
                .collect(Collectors.toMap(Order::getId, order -> order));

        for (int i = orders.size() - 1; i >= 0; i--) {
            Order currentOrder = orders.get(i);
            if (!newOrderMap.containsKey(currentOrder.getId())) {
                orders.remove(i);
                notifyItemRemoved(i);
            }
        }

        for (Order newOrder : newOrders) {
            int index = findOrderIndexById(newOrder.getId());
            if (index == -1) {
                orders.add(newOrder);
                notifyItemInserted(orders.size() - 1);
            } else {
                orders.set(index, newOrder);
                notifyItemChanged(index);
            }
        }
    }

    private int findOrderIndexById(String orderId) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(orderId)) {
                return i;
            }
        }
        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout currentOrderHolder;
        TextView ordersTrack, listName, listAmount, listDate, listQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            currentOrderHolder = itemView.findViewById(R.id.currentOrderHolder);
            ordersTrack = itemView.findViewById(R.id.ordersTrack);
            listName = itemView.findViewById(R.id.listName);
            listAmount = itemView.findViewById(R.id.listAmount);
            listDate = itemView.findViewById(R.id.listDate);
            listQuantity = itemView.findViewById(R.id.listQuantity);
        }
    }

}
