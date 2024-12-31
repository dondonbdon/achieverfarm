package dev.bti.achieverfarm.models;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.models.req.UserReq;

import dev.bti.achieverfarm.models.res.UserRes;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String fullName;
    String email;
    String phone;
    String password;
    Boolean verified;
    List<Instant> logins;
    List<Instant> updates;
    Integer failedAttempts;
    Boolean lockedMaxAttempts;
    Instant lockedFailedAttemptsTimestampEnd;
    List<String> tokens;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();


    public User(UserReq userReq) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        this.id = Common.generateRandomId();
        this.fullName = userReq.getFullName();
        this.email = userReq.getEmail();
        this.phone = userReq.getPhone();
        this.password = encoder.encode(userReq.getPassword());
        this.verified = userReq.getVerified();
        this.logins = Collections.singletonList(Instant.now());
        this.updates = Collections.singletonList(Instant.now());
        this.failedAttempts = Constants.Auth.MAX_ATTEMPTS;
        this.lockedMaxAttempts = false;
        this.tokens = Collections.singletonList(Common.Jwt.createToken(this.getId(), userReq.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER"));
        this.roles = Collections.singleton(userReq.getIsAdmin() ? "ADMIN" : "USER");
    }

    public void update(UserReq userReq) {
        this.setFullName(userReq.getFullName());
        this.setPhone(  userReq.getPhone());
        this.setVerified( userReq.getVerified());
        this.getUpdates().add(Instant.now());
        this.setEmail(userReq.getEmail());
    }

    public UserRes map() {
        UserRes userRes = new UserRes();
        userRes.setId(getId());
        userRes.setPhone(getPhone());
        userRes.setEmail(getEmail());
        userRes.setLogins(getLogins());
        userRes.setUpdates(getUpdates());
        userRes.setVerified(getVerified());

        return userRes;
    }
}
