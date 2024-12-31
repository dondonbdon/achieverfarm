package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.Instant;

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
public class PickupDetails implements Parcelable {
    public static final Creator<PickupDetails> CREATOR = new Creator<>() {
        @Override
        public PickupDetails createFromParcel(Parcel in) {
            return new PickupDetails(in);
        }

        @Override
        public PickupDetails[] newArray(int size) {
            return new PickupDetails[size];
        }
    };
    Instant pickupTime;
    PickupLocation pickupLocation;

    protected PickupDetails(Parcel in) {
        long epochMillis = in.readLong();
        pickupTime = (epochMillis == -1) ? null : Instant.ofEpochMilli(epochMillis);

        pickupLocation = in.readParcelable(PickupLocation.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(pickupTime != null ? pickupTime.toEpochMilli() : -1);
        dest.writeParcelable(pickupLocation, flags);
    }
}
