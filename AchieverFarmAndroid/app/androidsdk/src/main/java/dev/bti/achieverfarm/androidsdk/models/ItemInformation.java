package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

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
public class ItemInformation implements Parcelable {
    public static final Creator<ItemInformation> CREATOR = new Creator<>() {
        @Override
        public ItemInformation createFromParcel(Parcel in) {
            return new ItemInformation(in);
        }

        @Override
        public ItemInformation[] newArray(int size) {
            return new ItemInformation[size];
        }
    };
    String name;
    String description;
    List<String> usesList;
    List<String> packaging;
    AdditionalInformation additionalInformation;
    String photoUrl;

    protected ItemInformation(Parcel in) {
        name = in.readString();
        description = in.readString();
        photoUrl = in.readString();
        usesList = in.createStringArrayList();
        packaging = in.createStringArrayList();
        additionalInformation = in.readParcelable(AdditionalInformation.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photoUrl);
        dest.writeStringList(usesList);
        dest.writeStringList(packaging);
        dest.writeParcelable(additionalInformation, flags);
    }
}
