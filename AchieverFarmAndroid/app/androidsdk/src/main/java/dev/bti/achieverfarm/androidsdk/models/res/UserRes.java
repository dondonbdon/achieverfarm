package dev.bti.achieverfarm.androidsdk.models.res;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRes {

    String id;
    String fullName;
    String email;
    String phone;
    Boolean verified;
    List<Instant> logins;
    List<Instant> updates;
}
