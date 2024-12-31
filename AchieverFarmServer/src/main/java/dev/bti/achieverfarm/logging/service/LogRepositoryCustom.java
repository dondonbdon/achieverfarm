package dev.bti.achieverfarm.logging.service;

import dev.bti.achieverfarm.logging.models.Log;
import dev.bti.achieverfarm.logging.models.LogCommand;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepositoryCustom {
    List<Log> findLogsByCommand(LogCommand logCommand);
    public List<Log> findLogsByTimestampAfter(LocalDateTime timestamp);
}
