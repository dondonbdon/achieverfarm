package dev.bti.achieverfarm;

import static dev.bti.achieverfarm.common.Common.applyMultipleColorSpans;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import dev.bti.achieverfarm.adapter.CartItemAdapter;
import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.enums.PaymentType;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.LatLng;
import dev.bti.achieverfarm.androidsdk.models.PaymentDetails;
import dev.bti.achieverfarm.androidsdk.models.PickupDetails;
import dev.bti.achieverfarm.androidsdk.models.PickupLocation;
import dev.bti.achieverfarm.androidsdk.models.req.Checkout;
import dev.bti.achieverfarm.common.Credentials;
import dev.bti.achieverfarm.util.PaymentMethodDialog;

public class CheckoutActivity extends AppCompatActivity {

    private static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    final ZoneId zoneId = ZoneId.systemDefault();
    TextView checkoutCollectionPoint, checkoutCollectionDate, checkoutCollectionTime,
            checkoutViewCart, checkoutCompleteOrder, checkoutTotal, checkoutTax,
            checkoutGrandTotal, checkoutAddPromoCode, checkoutPaymentMethod;
    RecyclerView checkoutItemRecycler;
    CartItemAdapter cartItemAdapter;
    Boolean viewAllItems = false;
    Checkout checkout;
    Cart cart;
    LocalDateTime currentDateTime = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initUi();
        initData();
        initInfo();
        initCheckout();
        initAdapter();
        initRecyclers();
        initListeners();
    }

    void initUi() {
        checkoutCollectionPoint = findViewById(R.id.checkoutCollectionPoint);
        checkoutCollectionDate = findViewById(R.id.checkoutCollectionDate);
        checkoutCollectionTime = findViewById(R.id.checkoutCollectionTime);
        checkoutViewCart = findViewById(R.id.checkoutViewCart);
        checkoutCompleteOrder = findViewById(R.id.checkoutCompleteOrder);
        checkoutPaymentMethod = findViewById(R.id.checkoutPaymentMethod);
        checkoutTotal = findViewById(R.id.checkoutTotal);
        checkoutTax = findViewById(R.id.checkoutTax);
        checkoutGrandTotal = findViewById(R.id.checkoutGrandTotal);
        checkoutAddPromoCode = findViewById(R.id.checkoutAddPromoCode);
        checkoutItemRecycler = findViewById(R.id.checkoutItemRecycler);
    }

    void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            cart = getIntent().getParcelableExtra("cart", Cart.class);
        } else {
            cart = getIntent().getParcelableExtra("cart");
        }

        if (cart == null) {
            cart = Application.getGlobalCartHelper().getGlobalCart();
        }
    }

    void initListeners() {
        checkoutCollectionPoint.setOnClickListener(v -> {
            // TODO: OPEN COLLECTION POINTS DIALOG
        });

        checkoutCollectionDate.setOnClickListener(v -> showDatePickerDialog(checkoutCollectionDate));
        checkoutCollectionTime.setOnClickListener(v -> showTimePickerDialog(checkoutCollectionTime));

        cartItemAdapter.updateLimit(2);

        checkoutViewCart.setOnClickListener(v -> {
            if (!viewAllItems) {
                cartItemAdapter.updateLimit(CartItemAdapter.MAX);
                checkoutViewCart.setText(String.format(Locale.getDefault(), "Hide items%s", ""));
            } else {
                cartItemAdapter.updateLimit(2);
                checkoutViewCart.setText(String.format(Locale.getDefault(), "View all items%s", ""));
            }

            viewAllItems = !viewAllItems;
        });

        checkoutAddPromoCode.setOnClickListener(v -> {
            // TODO: OPEN PROMO CODE FRAGMENT
        });

        checkoutPaymentMethod.setOnClickListener(v -> {
            showPaymentMethodDialog();
            // checkoutPaymentMethod.setText(String.format(Locale.getDefault(), "Cash On Pick Up %s", ""));
        });

        checkoutCompleteOrder.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }
            checkoutCompleteOrder.setEnabled(false);
            checkout();
        });
    }

    void initCheckout() {
        checkout = new Checkout();

        PaymentDetails paymentDetails = new PaymentDetails();
        PickupDetails pickupDetails = new PickupDetails();
        PickupLocation pickupLocation = new PickupLocation();

        paymentDetails.setPaymentType(PaymentType.COD);
        paymentDetails.setAmount(cart.getTotal().multiply(BigDecimal.valueOf(1.1)));

        pickupLocation.setName("High Achievers Coach International Academy");
        pickupLocation.setAddress("801 Helvetia Drive, Borrowdale, Harare");
        pickupLocation.setLatLng(LatLng.of(-17.739829164184464, 31.095055277897767));

        pickupDetails.setPickupLocation(pickupLocation);

        Map<String, Integer> itemCounts = cart.getItemCounts();

        checkout.setPaymentDetails(paymentDetails);
        checkout.setPickupDetails(pickupDetails);
        checkout.setItems(itemCounts);
        checkout.setQuantity(cart.getQuantity());
    }

    void initAdapter() {
        cartItemAdapter = new CartItemAdapter(this);
        checkoutItemRecycler.setAdapter(cartItemAdapter);
    }

    void initRecyclers() {
        Cart cart = Application.getGlobalCartHelper().getGlobalCart();

        cart.getItemCounts().forEach((id, quantity) -> {
            if (Application.getItemCache().containsKey(id)) {
                cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
            } else {
                SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getItemHelper()
                        .getItem(id).addOnSuccessListener(response -> {
                            cartItemAdapter.addItem(Application.getItemCache().get(id), quantity);
                            Application.getItemCache().put(id, response.getData());
                        }).addOnFailureListener(e -> Log.e("ORDER", Objects.requireNonNull(e.getMessage()))).execute();
            }
        });
    }

    void checkout() {
        Log.d("Checkout", checkout.toString());
        SDKHelpers.GetInstance(Credentials.GetInstance().TOKEN).getOrderHelper()
                .checkout(Credentials.GetInstance().UID, checkout)
                .addOnSuccessListener(response -> {
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    intent.putExtra("order", response.getData());
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e -> {
                    checkoutCompleteOrder.setEnabled(true);

                    Log.e("ORDER", e.getMessage(), e);
                }).execute();
    }

    boolean validate() {
        if (checkout.getPickupDetails().getPickupTime() == null) {
            Toast.makeText(this, "Please select a date & time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void showDatePickerDialog(TextView textView) {
        Calendar now = Calendar.getInstance();

        long today = now.getTimeInMillis();
        long twoWeeksFromNow = today + 14 * DAY_IN_MILLIS;

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(today);
        constraintsBuilder.setEnd(twoWeeksFromNow);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Collection Date")
                .setSelection(today)
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedDate = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            applyMultipleColorSpans(getApplicationContext(), textView,
                    Pair.of("Date ", false),
                    Pair.of(selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")), true));

            onDateSet(selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    public void showTimePickerDialog(TextView textView) {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);

        int defaultHour = Math.max(currentHour, 10);
        int defaultMinute = 0;

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("Select Collection Time")
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(defaultHour)
                .setMinute(defaultMinute)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            if (selectedHour < 10 || selectedHour > 17 || (selectedHour == 17 && selectedMinute > 0)) {
                Toast.makeText(this, "Please select a time between 10:00 AM and 5:00 PM", Toast.LENGTH_SHORT).show();
                return;
            }

            applyMultipleColorSpans(getApplicationContext(), textView,
                    Pair.of("Time ", false),
                    Pair.of(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute), true));

            onTimeSet(selectedHour, selectedMinute);
        });

        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }

    public void onDateSet(int year, int month, int dayOfMonth) {
        LocalDate newDate = LocalDate.of(year, month + 1, dayOfMonth);
        currentDateTime = LocalDateTime.of(newDate, currentDateTime.toLocalTime());
        updateInstant();
    }

    public void onTimeSet(int hourOfDay, int minute) {
        LocalTime newTime = LocalTime.of(hourOfDay, minute);
        currentDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), newTime);
        updateInstant();
    }

    private void updateInstant() {
        Instant instant = currentDateTime.atZone(zoneId).toInstant();
        checkout.getPickupDetails().setPickupTime(instant);
    }


    void initInfo() {
        BigDecimal tax = BigDecimal.valueOf(0.1).multiply(cart.getTotal());
        checkoutTotal.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotal()));
        checkoutTax.setText(String.format(Locale.getDefault(), "$%.2f", tax));
        checkoutGrandTotal.setText(String.format(Locale.getDefault(), "$%.2f", tax.add(cart.getTotal())));
    }

    private void showPaymentMethodDialog() {


        // Show dialog
        PaymentMethodDialog dialog = new PaymentMethodDialog(
                this,
                "Select Payment Methods",
                PaymentType.valuesString(),
                List.of(PaymentType.valuesString().get(0)),
                selectedMethods -> {
                    checkout.getPaymentDetails().setPaymentType(PaymentType.valueOf(selectedMethods.get(0)));
                    Toast.makeText(this, "Selected: " + selectedMethods, Toast.LENGTH_LONG).show();
                }
        );
        dialog.show();
    }
}