package dev.bti.achieverfarm.services.terminal;

import dev.bti.achieverfarm.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    @Autowired
    private LogService logService;

    public void parse(String command) {
        // log
    }

}
