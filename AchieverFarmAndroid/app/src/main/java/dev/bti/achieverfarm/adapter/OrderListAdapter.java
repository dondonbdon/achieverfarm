package dev.bti.achieverfarm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    Context context;
    List<Order> orders = new ArrayList<>();

    public OrderListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.format_order_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        holder.listName.setText(order.getSummary());
        holder.listDate.setText(formatter.format(order.getTimestamp()));
        holder.listAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getPaymentDetails().getAmount()));
        holder.listQuantity.setText(String.format(Locale.getDefault(), "%d items", order.getQuantity()));

        holder.listHolder.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra("order", order);
            context.startActivity(intent);
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

        LinearLayoutCompat listHolder;
        TextView listName, listDate, listAmount, listQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            init(itemView);
        }

        void init(View itemView) {
            initUi(itemView);
        }

        void initUi(View itemView) {
            listHolder = itemView.findViewById(R.id.listHolder);
            listName = itemView.findViewById(R.id.listName);
            listDate = itemView.findViewById(R.id.listDate);
            listAmount = itemView.findViewById(R.id.listAmount);
            listQuantity = itemView.findViewById(R.id.listQuantity);
        }
    }
}
