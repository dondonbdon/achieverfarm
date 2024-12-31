package dev.bti.achieverfarm;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dev.bti.achieverfarm.adapter.CartItemAdapter;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.enums.PaymentType;
import dev.bti.achieverfarm.androidsdk.models.Order;
import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.common.Credentials;

public class OrderActivity extends AppCompatActivity {

    TextView orderId, orderStatus, orderCollection, orderTrack, orderPaymentMethod, orderTotal,
            orderViewAllItems, orderDate;
    RecyclerView orderItemRecycler;
    ConstraintLayout orderCallUs;

    Order order;
    CartItemAdapter cartItemAdapter;

    Boolean viewAllItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initUi();
        initData();
        initRecycler();
        initInfo();
        initClicks();
    }

    private void initUi() {
        orderId = findViewById(R.id.orderId);
        orderStatus = findViewById(R.id.orderStatus);
        orderCollection = findViewById(R.id.orderCollection);
        orderTrack = findViewById(R.id.orderTrack);
        orderPaymentMethod = findViewById(R.id.orderPaymentMethod);
        orderTotal = findViewById(R.id.orderTotal);
        orderViewAllItems = findViewById(R.id.orderViewAllItems);
        orderCallUs = findViewById(R.id.orderCallUs);
        orderDate = findViewById(R.id.orderDate);
        orderItemRecycler = findViewById(R.id.orderItemRecycler);
    }

    private void initData() {
        order = getIntent().getParcelableExtra("order");
    }

    private void initRecycler() {
        cartItemAdapter = new CartItemAdapter(this);
        orderItemRecycler.setAdapter(cartItemAdapter);
    }

    private void initInfo() {
        orderId.setText(order.getId());
        orderCollection.setText(order.getPickupDetails().getPickupLocation().getName());
        orderTrack.setText(String.format(Locale.getDefault(), "%s: %s", ContextCompat.getString(this, R.string.get_directions), order.getPickupDetails().getPickupLocation().getAddress()));
        orderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getPaymentDetails().getAmount()));
        orderStatus.setText(order.getStatus().toString());
        if (Objects.requireNonNull(order.getPaymentDetails().getPaymentType()) == PaymentType.COD) {
            orderPaymentMethod.setText(R.string.cash_on_delivery);
        }

        switch (order.getStatus()) {
            case PREPARING -> {
                orderStatus.setText(R.string.preparing_order);
                orderStatus.setTextColor(Color.parseColor("#FFD057"));
            }
            case READY -> {
                orderStatus.setText(R.string.ready_order);
                orderStatus.setTextColor(Color.parseColor("#77AF00"));
            }
            case COMPLETE -> {
                orderStatus.setText(R.string.completed_order);
                orderStatus.setTextColor(Color.parseColor("#046C00"));
            }
        }

        Date orderDateValue = Date.from(order.getTimestamp());
        Calendar orderCalendar = Calendar.getInstance();
        orderCalendar.setTime(orderDateValue);

        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dateString;

        if (orderCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && orderCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            dateString = String.format(Locale.getDefault(), "Today at %s", timeFormat.format(orderDateValue));
        } else if (orderCalendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && orderCalendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
            dateString = String.format(Locale.getDefault(), "Tomorrow at %s", timeFormat.format(orderDateValue));
        } else {
            dateString = String.format(Locale.getDefault(), "%s, %s", dateFormat.format(orderDateValue), timeFormat.format(orderDateValue));
        }
        orderDate.setText(dateString);

        order.getItems().forEach((id, quantity) -> {
            if (Application.getItemCache().containsKey(id)) {
                cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
            } else {
                SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper()
                        .getItem(id).addOnSuccessListener(response -> {
                            cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
                            Application.getItemCache().put(id, response.getData());
                        }).addOnFailureListener(e -> Log.e("ORDER", e.getMessage(), e)).execute();
            }
        });
    }

    private void initClicks() {
        cartItemAdapter.updateLimit(2);

        orderViewAllItems.setOnClickListener(v -> {
            if (!viewAllItems) {
                cartItemAdapter.updateLimit(CartItemAdapter.MAX);
                orderViewAllItems.setText(String.format(Locale.getDefault(), "Hide items%s", ""));
            } else {
                cartItemAdapter.updateLimit(2);
                orderViewAllItems.setText(String.format(Locale.getDefault(), "View all items%s", ""));
            }

            viewAllItems = !viewAllItems;
        });

        orderCallUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+263 788519464"));
            startActivity(intent);
        });

        orderTrack.setOnClickListener(v -> Common.openGoogleMaps(getApplicationContext(),
                order.getPickupDetails().getPickupLocation()));
    }

}