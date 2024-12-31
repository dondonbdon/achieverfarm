package dev.bti.achieverfarm.common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import dev.bti.achieverfarm.Application;
import dev.bti.achieverfarm.R;
import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.models.Cart;
import dev.bti.achieverfarm.androidsdk.models.Item;
import dev.bti.achieverfarm.androidsdk.models.PickupLocation;

public class Common {

    @SafeVarargs
    public static void applyMultipleColorSpans(Context context, TextView textView, Pair<String, Boolean>... parts) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

        Arrays.asList(parts).forEach((part) -> {
            int grey = ContextCompat.getColor(context, R.color.grey);
            int black_white = ContextCompat.getColor(context, R.color.black_white);

            if (part.getTwo()) {
                spannableBuilder.append(part.getOne(), new ForegroundColorSpan(black_white), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableBuilder.append(part.getOne(), new ForegroundColorSpan(grey), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        });

        textView.setText(spannableBuilder);
    }

    public static BigDecimal getTotal(Cart cart) {
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);

        idToItemMap(cart.getItemCounts()).forEach((item, quantity) ->
                total.set(total.get()
                        .add(item.getPricingInfo().getUsdPrice()
                                .multiply(BigDecimal.valueOf(quantity)))));

        return total.get();
    }

    public static Map<Item, Integer> idToItemMap(Map<String, Integer> itemCounts) {
        Map<Item, Integer> map = new HashMap<>();

        for (String id : itemCounts.keySet()) {
            map.put(Application.getItemCache().get(id), itemCounts.get(id));
        }

        return map;
    }

    public static void openGoogleMaps(Context context, PickupLocation pickupLocation) {
        String geoUri = String.format(Locale.getDefault(), "geo:%f,%f?q=%f,%f(%s)",
                pickupLocation.getLatLng().getLatitude(),
                pickupLocation.getLatLng().getLongitude(),
                pickupLocation.getLatLng().getLatitude(),
                pickupLocation.getLatLng().getLongitude(),
                Uri.encode(pickupLocation.getAddress()));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void showDatePickerDialog(TextView textView, Context context) {
        Calendar calendar = Calendar.getInstance();
        int yearCal = calendar.get(Calendar.YEAR);
        int monthCal = calendar.get(Calendar.MONTH);
        int dayCal = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Instant instant = convertToInstant(year, monthOfYear, dayOfMonth, -1, -1);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    String formattedInstant = localDateTime.format(formatter);
                    textView.setText(formattedInstant);
                },
                yearCal, monthCal, dayCal
        );

        datePickerDialog.show();
    }

    public static void showTimePickerDialog(TextView textView, Context context) {
        Calendar calendar = Calendar.getInstance();
        int hourCal = calendar.get(Calendar.YEAR);
        int minuteCal = calendar.get(Calendar.MONTH);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            Instant instant = convertToInstant(-1, -1, -1, hourOfDay, minute);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            String formattedInstant = localDateTime.format(formatter);
            textView.setText(formattedInstant);
        }, hourCal, minuteCal, true);

        timePickerDialog.show();
    }

    public static Instant convertToInstant(int year, int month, int day, int hour, int minute) {
        LocalDate localDate = LocalDate.of(year, month + 1, day);
        LocalTime localTime = LocalTime.MIDNIGHT;

        if (hour != -1 && minute != -1) {
            localTime = LocalTime.of(hour, minute);
        }

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}
