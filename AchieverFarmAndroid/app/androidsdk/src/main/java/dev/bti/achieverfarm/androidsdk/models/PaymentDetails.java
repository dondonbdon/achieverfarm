package dev.bti.achieverfarm.androidsdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

import dev.bti.achieverfarm.androidsdk.enums.PaymentType;
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
public class PaymentDetails implements Parcelable {
    public static final Creator<PaymentDetails> CREATOR = new Creator<>() {
        @Override
        public PaymentDetails createFromParcel(Parcel in) {
            return new PaymentDetails(in);
        }

        @Override
        public PaymentDetails[] newArray(int size) {
            return new PaymentDetails[size];
        }
    };
    PaymentType paymentType;
    BigDecimal amount;

    protected PaymentDetails(Parcel in) {
        paymentType = (PaymentType) in.readSerializable();

        String amountStr = in.readString();
        amount = (amountStr != null) ? new BigDecimal(amountStr) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(paymentType);
        dest.writeString(amount != null ? amount.toPlainString() : null);
    }
}
