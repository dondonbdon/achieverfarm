package dev.bti.achieverfarm.logging.interfaces;

import dev.bti.achieverfarm.logging.models.Log;

public interface LogListener {
    void OnNewLog(Log logs);
}
