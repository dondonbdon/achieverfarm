package dev.bti.achieverfarm.logging.service;

import dev.bti.achieverfarm.logging.interfaces.LogListener;
import dev.bti.achieverfarm.logging.models.Log;
import dev.bti.achieverfarm.logging.models.LogCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogService implements LogListener {

    @Autowired
    private LogRepository logRepository;

    private LocalDateTime lastCheckedTime;
    private final Map<SseEmitter, LogCommand> emitters = new ConcurrentHashMap<>();

    public LogService() {
        this.lastCheckedTime = LocalDateTime.now().minusMinutes(1);
    }

    public void push(Log log) {
        logRepository.save(log);
    }

    public List<Log> getAll(LogCommand logCommand) {
        return logRepository.findLogsByCommand(logCommand);
    }

    public List<Log> getNewLogs() {
        List<Log> newLogs = logRepository.findLogsByTimestampAfter(lastCheckedTime);
        updateLastCheckedTime();
        return newLogs;
    }

    public void updateLastCheckedTime() {
        this.lastCheckedTime = LocalDateTime.now();
    }

    public SseEmitter addEmitter(LogCommand logCommand) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(emitter, logCommand);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        return emitter;
    }

    @Async("taskExecutor")
    @Override
    public void OnNewLog(Log log) {
        emitters.forEach(((sseEmitter, logCommand) -> {
            if (log.matches(logCommand)) {
                try {
                    sseEmitter.send(SseEmitter.event().name("log").data(log));
                } catch (IOException e) {
                    sseEmitter.complete();
                    emitters.remove(sseEmitter);
                }
            }
        }));
    }
}
