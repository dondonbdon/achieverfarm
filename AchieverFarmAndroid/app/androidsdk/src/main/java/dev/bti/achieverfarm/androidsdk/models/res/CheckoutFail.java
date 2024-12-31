package dev.bti.achieverfarm.androidsdk.models.res;

import java.util.List;

import dev.bti.achieverfarm.androidsdk.models.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckoutFail {

    String msg;
    List<Stock> notAvailable;
}
