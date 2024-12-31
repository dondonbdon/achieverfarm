package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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
public class PickupLocation implements Parcelable {
    public static final Creator<PickupLocation> CREATOR = new Creator<>() {
        @Override
        public PickupLocation createFromParcel(Parcel in) {
            return new PickupLocation(in);
        }

        @Override
        public PickupLocation[] newArray(int size) {
            return new PickupLocation[size];
        }
    };
    String id;
    String name;
    String address;
    LatLng latLng;

    protected PickupLocation(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeParcelable(latLng, flags);
    }
}
