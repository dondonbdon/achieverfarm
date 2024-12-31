package dev.bti.achieverfarm.androidsdk.models.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReq {
    String fullName;
    String email;
    String phone;
    String password;
    Boolean verified;
    Boolean isAdmin;
}
