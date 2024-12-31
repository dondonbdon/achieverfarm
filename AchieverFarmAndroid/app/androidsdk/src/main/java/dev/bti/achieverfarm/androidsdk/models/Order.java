package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import dev.bti.achieverfarm.androidsdk.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order implements Parcelable {

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
    String id;
    String userId;
    PaymentDetails paymentDetails;
    PickupDetails pickupDetails;
    Map<String, Integer> items;
    Instant timestamp;
    OrderStatus status;
    Integer quantity;

    protected Order(Parcel in) {
        id = in.readString();
        userId = in.readString();

        paymentDetails = in.readParcelable(PaymentDetails.class.getClassLoader());

        pickupDetails = in.readParcelable(PickupDetails.class.getClassLoader());

        int mapSize = in.readInt();
        if (mapSize > 0) {
            items = new HashMap<>();
            for (int i = 0; i < mapSize; i++) {
                String key = in.readString();
                Integer value = (in.readByte() == 0) ? null : in.readInt();
                items.put(key, value);
            }
        }

        long timestampMillis = in.readLong();
        timestamp = (timestampMillis != -1) ? Instant.ofEpochMilli(timestampMillis) : null;

        status = (OrderStatus) in.readSerializable();

        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeParcelable(paymentDetails, flags);
        dest.writeParcelable(pickupDetails, flags);

        if (items == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(items.size());
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                dest.writeString(entry.getKey());
                if (entry.getValue() == null) {
                    dest.writeByte((byte) 0);
                } else {
                    dest.writeByte((byte) 1);
                    dest.writeInt(entry.getValue());
                }
            }
        }

        if (timestamp == null) {
            dest.writeLong(-1);
        } else {
            dest.writeLong(timestamp.toEpochMilli());
        }

        dest.writeSerializable(status);

        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
    }

    public String getSummary() {
        return "Achiever Farm Produce";
    }
}
