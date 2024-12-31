package dev.bti.achieverfarm.logging.service;

import dev.bti.achieverfarm.logging.enums.LogType;
import dev.bti.achieverfarm.logging.models.Log;
import dev.bti.achieverfarm.logging.models.LogCommand;
import dev.bti.achieverfarm.logging.models.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class LogRepositoryCustomImpl implements LogRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Log> findLogsByCommand(LogCommand logCommand) {
        Query query = new Query();


        if (logCommand.getEnableAllLogs() != null && !logCommand.getEnableAllLogs()) {
            if (logCommand.getEnableOrderLogs() != null && logCommand.getEnableOrderLogs()) {
                query.addCriteria(Criteria.where("type").is(LogType.ODR));
            }
            if (logCommand.getEnableInventoryLogs() != null && logCommand.getEnableInventoryLogs()) {
                query.addCriteria(Criteria.where("type").is(LogType.INV));
            }
            if (logCommand.getEnableAuthLogs() != null && logCommand.getEnableAuthLogs()) {
                query.addCriteria(Criteria.where("type").is(LogType.USR));
            }
        }

        if (logCommand.getStart() != null) {
            query.addCriteria(Criteria.where("timestamp").gte(Instant.parse(logCommand.getStart())));
        }
        if (logCommand.getStop() != null) {
            query.addCriteria(Criteria.where("timestamp").lte(Instant.parse(logCommand.getStop())));
        }
        if (logCommand.getLevel() != null) {
            query.addCriteria(Criteria.where("level").is(logCommand.getLevel()));
        }
        if (logCommand.getSystemOnlyLogs() != null && logCommand.getSystemOnlyLogs()) {
            query.addCriteria(Criteria.where("commanderName").is("SYS"));
        }

        if (logCommand.getCount() != null) {
            query.limit(logCommand.getCount());
        }

        return mongoTemplate.find(query, Log.class);
    }

    @Override
    public List<Log> findLogsByTimestampAfter(LocalDateTime timestamp) {
        return mongoTemplate.query(Log.class)
                .matching(query(Criteria.where("timestamp").gt(timestamp)))
                .all();
    }
}


