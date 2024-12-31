package dev.bti.achieverfarm.services.user;

import dev.bti.achieverfarm.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByLockedMaxAttempts(Boolean lockedMaxAttempts);

    Optional<User> findByEmail(String email);
}
