package dev.bti.achieverfarm.services.auth;

import dev.bti.achieverfarm.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
