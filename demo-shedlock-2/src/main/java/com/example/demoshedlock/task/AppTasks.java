package com.example.demoshedlock.task;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html
@Component
@Slf4j
public class AppTasks {

    @Value("${app.node}")
    private String appNode;

    @Scheduled(cron = "0/10 * * * * *")
    @SchedulerLock(name = "printTheDateTask", lockAtMostFor = "30s", lockAtLeastFor = "30s")
    public void printTheDateTask() {
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        log.info("Running task at [{}] from node [{}]", ZonedDateTime.now(), appNode);
    }
}
