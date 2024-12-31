package dev.bti.achieverfarm.androidsdk.models;

import androidx.annotation.NonNull;

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
public class User {

    String id;
    String fullName;
    String email;
    String phone;
    String password;
    Boolean verified;
    List<Instant> logins;
    List<Instant> updates;

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'';
    }
}
