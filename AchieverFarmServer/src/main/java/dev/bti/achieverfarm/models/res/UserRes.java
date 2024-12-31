package dev.bti.achieverfarm.models.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

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
