package dev.bti.achieverfarm.scheduled;


import dev.bti.achieverfarm.common.Constants;
import dev.bti.achieverfarm.models.User;

import dev.bti.achieverfarm.services.user.UserRepository;
import dev.bti.achieverfarm.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserMaxAttemptSchedule {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 60_000)
    public void runTask() {
        for (User user : userRepository.findByLockedMaxAttempts(true)) {
            if (Instant.now().isAfter(user.getLockedFailedAttemptsTimestampEnd())) {
                unlock(user);
            }
        }
    }

    private void unlock(User user) {
        user.setFailedAttempts(Constants.Auth.MAX_ATTEMPTS);
        user.setLockedFailedAttemptsTimestampEnd(null);
        user.setLockedMaxAttempts(false);
    }
}
