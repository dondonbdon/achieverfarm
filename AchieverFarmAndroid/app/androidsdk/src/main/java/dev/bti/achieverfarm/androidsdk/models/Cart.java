package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cart implements Parcelable {
    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
    String id;
    String userId;
    Integer quantity;
    Map<String, Integer> itemCounts;
    String name;
    Instant createdTimestamp, lastUpdatedTimestamp;
    BigDecimal total;

    protected Cart(Parcel in) {
        id = in.readString();
        userId = in.readString();
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        int itemCountsSize = in.readInt();
        if (itemCountsSize > 0) {
            itemCounts = new HashMap<>();
            for (int i = 0; i < itemCountsSize; i++) {
                String key = in.readString();
                int value = in.readInt();
                itemCounts.put(key, value);
            }
        }
        name = in.readString();
        createdTimestamp = in.readByte() == 0 ? null : Instant.ofEpochMilli(in.readLong());
        lastUpdatedTimestamp = in.readByte() == 0 ? null : Instant.ofEpochMilli(in.readLong());
        total = (BigDecimal) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        if (itemCounts == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(itemCounts.size());
            for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeInt(entry.getValue());
            }
        }
        dest.writeString(name);
        if (createdTimestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(createdTimestamp.toEpochMilli());
        }
        if (lastUpdatedTimestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastUpdatedTimestamp.toEpochMilli());
        }
        dest.writeSerializable(total);
    }
}
