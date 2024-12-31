package dev.bti.achieverfarm.models.res;

import dev.bti.achieverfarm.models.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckoutFail {

    String msg;
    List<Stock> notAvailable;
}
