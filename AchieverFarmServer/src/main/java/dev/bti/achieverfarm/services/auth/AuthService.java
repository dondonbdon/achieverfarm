package dev.bti.achieverfarm.services.auth;

import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.common.Pair;
import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.logging.auth.Type;
import dev.bti.achieverfarm.logging.enums.Level;
import dev.bti.achieverfarm.logging.service.LogService;
import dev.bti.achieverfarm.models.User;
import dev.bti.achieverfarm.models.req.LoginReq;
import dev.bti.achieverfarm.models.req.UserReq;
import dev.bti.achieverfarm.scheduled.JwtBlacklistManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    @Autowired
    private AuthRepository repository;

    @Autowired
    private LogService logService;

    @Autowired
    private JwtBlacklistManager jwtBlacklistManager;

    public Pair<String, String> createUser(UserReq userReq) {
        User user = new User(userReq);
        repository.save(user);


        Common.Log.auth(logService, "", Level.I, user.getEmail(), Type.CREA);
        return new Pair<>(user.getId(), user.getTokens().getFirst());
    }

    public Pair<String, String> loginUser(LoginReq loginReq) throws AuthException {
        User user = repository.findByEmail(loginReq.getEmail()).orElseThrow(() -> {
            Common.Log.auth(logService, "", Level.E, loginReq.getEmail(), Type.USNF);
            return Constants.Throws.UserNotFound;
        });

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        if (user.getFailedAttempts() == 0) {
            user.setLockedMaxAttempts(true);
            user.setLockedFailedAttemptsTimestampEnd(Instant.now().plus(Duration.of(1, ChronoUnit.HOURS)));
            repository.save(user);
            Common.Log.auth(logService, "", Level.E, loginReq.getEmail(), Type.TMAT);
            throw Constants.Throws.MaxFailedAttempts;
        }

        if (encoder.matches(loginReq.getPassword(), user.getPassword())) {
            user.getTokens().clear();

            user.getLogins().add(Instant.now());
            user.setFailedAttempts(Constants.Auth.MAX_ATTEMPTS);

            String newToken = Common.Jwt.createToken(user.getId(), user.getRoles().stream()
                    .map(role -> "ROLE_" + role)
                    .toArray(String[]::new));
            user.getTokens().add(newToken);
            repository.save(user);

            Common.Log.auth(logService, "", Level.I, loginReq.getEmail(), Type.LGIN);
            return new Pair<>(user.getId(), newToken);
        }

        user.setFailedAttempts(user.getFailedAttempts() - 1);
        repository.save(user);
        Common.Log.auth(logService, "", Level.E, loginReq.getEmail(), Type.LGNF);
        throw Constants.Throws.IncorrectPassword;
    }

    public void invalidateToken(String token) {
        Common.Jwt.invalidate(jwtBlacklistManager, token);
    }
}
