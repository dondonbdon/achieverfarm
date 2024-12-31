package dev.bti.achieverfarm.scheduled;

import dev.bti.achieverfarm.logging.models.Log;
import dev.bti.achieverfarm.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogScheduler {

    @Autowired
    LogService logService;

    @Scheduled(fixedRate = 30_000)
    public void checkForNewLogs() {
        List<Log> newLogs = logService.getNewLogs();
        newLogs.forEach((log) -> logService.OnNewLog(log));
    }
}

