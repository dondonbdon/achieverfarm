package dev.bti.achieverfarm.androidsdk.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Config {
    public static Config INSTANCE;
    String serverAddress = "10.0.2.2:7070";

    public static Config GetInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }
}
