package dev.bti.achieverfarm.logging.service;

import dev.bti.achieverfarm.logging.models.Log;
import dev.bti.achieverfarm.logging.models.LogCommand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<Log, String>, LogRepositoryCustom {
    List<Log> findLogsByCommand(LogCommand logCommand);

    List<Log> findLogsByTimestampAfter(LocalDateTime timestamp);
}
