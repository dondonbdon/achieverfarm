package dev.bti.achieverfarm.androidsdk.enums;

import androidx.annotation.NonNull;

public enum WhereGrown {
    SHED_NET, GREEN_HOUSE, OPEN_FIELD;


    @NonNull
    @Override
    public String toString() {
        return switch (this) {
            case SHED_NET -> "Shed Net";
            case GREEN_HOUSE -> "Green House";
            case OPEN_FIELD -> "Open Field";
        };
    }
}
