package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Currency implements Parcelable {
    public static final Creator<Currency> CREATOR = new Creator<>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
    String name;
    Double rateToUSD;

    protected Currency(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            rateToUSD = null;
        } else {
            rateToUSD = in.readDouble();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        if (rateToUSD == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rateToUSD);
        }
    }
}
