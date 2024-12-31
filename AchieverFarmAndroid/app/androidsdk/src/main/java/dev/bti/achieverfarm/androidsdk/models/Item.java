package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import dev.bti.achieverfarm.androidsdk.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item implements Parcelable {
    public static final Creator<Item> CREATOR = new Creator<>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    String id;
    PricingInfo pricingInfo;
    ItemInformation itemInformation;
    ItemType type;
    String stockId;


    protected Item(Parcel in) {
        id = in.readString();
        pricingInfo = in.readParcelable(PricingInfo.class.getClassLoader());
        itemInformation = in.readParcelable(ItemInformation.class.getClassLoader());
        type = ItemType.valueOf(in.readString());
        stockId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(pricingInfo, flags);
        dest.writeParcelable(itemInformation, flags);
        dest.writeString(type.name());
        dest.writeString(stockId);
    }
}
