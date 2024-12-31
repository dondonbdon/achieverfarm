package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import dev.bti.achieverfarm.androidsdk.enums.SellingType;
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
public class PricingInfo implements Parcelable {
    public static final Creator<PricingInfo> CREATOR = new Creator<>() {
        @Override
        public PricingInfo createFromParcel(Parcel in) {
            return new PricingInfo(in);
        }

        @Override
        public PricingInfo[] newArray(int size) {
            return new PricingInfo[size];
        }
    };

    BigDecimal usdPrice;
    List<Currency> conversionRates;
    Double max;
    SellingType sellingType;

    protected PricingInfo(Parcel in) {
        usdPrice = new BigDecimal(in.readString());

        conversionRates = in.createTypedArrayList(Currency.CREATOR);

        if (in.readByte() == 0) {
            max = null;
        } else {
            max = in.readDouble();
        }

        sellingType = SellingType.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(usdPrice.toString());
        dest.writeTypedList(conversionRates);

        if (max == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(max);
        }

        dest.writeString(sellingType.name());
    }
}
