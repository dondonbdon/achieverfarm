package dev.bti.achieverfarm.exceptions.item;

import dev.bti.achieverfarm.common.Constants;
import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends ItemException {

    public ItemNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constants.Item._404);
    }
}
