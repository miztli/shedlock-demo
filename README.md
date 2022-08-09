# ShedLock + SpringBoot Integration

---

### What is ShedLock

- Distributed lock that makes sure that your scheduled tasks are executed at most once at the same time.

### What ShedLock is not

- It's not a distributed scheduler
- It’s not a task executor, meaning that if a task is being executed in another node, task gets simply skipped (not
  queued).

### Constraints

- Works only with environments that share DB
- Works with (Providers): Mongo, Redis, Hazelcast, ZooKeeper and anything with a JDBC driver.
- ShedLock assumes application server clocks' are synchronized
- You have to set lockAtMostFor to a value which is much longer than normal execution time. If the task takes longer
  than lockAtMostFor the resulting behavior may be unpredictable (more than one process will effectively hold the lock)
  . **Recommendation:** Consider adding a timeout for the executed task.

### Implementation details

##### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>net.javacrumbs.shedlock</groupId>
        <artifactId>shedlock-spring</artifactId>
        <version>4.39.0</version>
    </dependency>
    <dependency>
        <groupId>net.javacrumbs.shedlock</groupId>
        <artifactId>shedlock-provider-jdbc-template</artifactId>
        <version>4.39.0</version>
    </dependency>
</dependencies>
```

##### Enable locking mechanism 

`@EnableSchedulerLock(defaultLockAtMostFor = "10m")`

##### Annotate your scheduled tasks

`@SchedulerLock(name = "printTheDateTask", lockAtMostFor = "30s", lockAtLeastFor = "30s")`

##### Create DB table

[script](./script.sql)

### Glossary

- `lockAtMostFor` attribute: Specifies how long the lock should be kept in case the executing node dies. This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes. You have to set lockAtMostFor to a value which is much longer than normal execution time. If the task takes longer than lockAtMostFor the resulting behavior may be unpredictable (more than one process will effectively hold the lock). If you do not specify lockAtMostFor in @SchedulerLock default value from @EnableSchedulerLock will be used.

- `lockAtLeastFor` attribute: Specifies minimum amount of time for which the lock should be kept. Its main purpose is to prevent execution from multiple nodes in case of really short tasks and clock difference between the nodes.

##### References

[Shedlock project](https://github.com/lukas-krecan/ShedLock)

### Alternatives

- Quartz