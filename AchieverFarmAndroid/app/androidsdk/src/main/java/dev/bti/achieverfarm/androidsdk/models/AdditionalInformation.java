package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import dev.bti.achieverfarm.androidsdk.common.Pair;
import dev.bti.achieverfarm.androidsdk.common.Triple;
import dev.bti.achieverfarm.androidsdk.enums.WhereGrown;
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
public class AdditionalInformation implements Parcelable {

    public static final Creator<AdditionalInformation> CREATOR = new Creator<>() {
        @Override
        public AdditionalInformation createFromParcel(Parcel in) {
            return new AdditionalInformation(in);
        }

        @Override
        public AdditionalInformation[] newArray(int size) {
            return new AdditionalInformation[size];
        }
    };
    Pair<String, String> origin;
    Pair<String, String> variety;
    Triple<Integer, Integer, ChronoUnit> growLength;
    Triple<Integer, Integer, ChronoUnit> avgShelfLife;
    WhereGrown whereGrown;

    protected AdditionalInformation(Parcel in) {
        origin = new Pair<>(in.readString(), in.readString());
        variety = new Pair<>(in.readString(), in.readString());
        growLength = new Triple<>(in.readInt(), in.readInt(), ChronoUnit.valueOf(in.readString()));
        avgShelfLife = new Triple<>(in.readInt(), in.readInt(), ChronoUnit.valueOf(in.readString()));
        whereGrown = WhereGrown.valueOf(in.readString());
    }

    public BigDecimal avgShelfLifeVal() {
        BigDecimal lower = new BigDecimal(getAvgShelfLife().getOne());
        BigDecimal upper = new BigDecimal(getAvgShelfLife().getTwo());
        return upper.add(lower).divide(BigDecimal.valueOf(2), 1, RoundingMode.UP);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(origin.getOne());
        dest.writeString(origin.getTwo());

        dest.writeString(variety.getOne());
        dest.writeString(variety.getTwo());

        dest.writeInt(growLength.getOne());
        dest.writeInt(growLength.getTwo());
        dest.writeString(growLength.getThree().name());

        dest.writeInt(avgShelfLife.getOne());
        dest.writeInt(avgShelfLife.getTwo());
        dest.writeString(avgShelfLife.getThree().name());

        dest.writeString(whereGrown.name());
    }
}
