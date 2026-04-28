package com.master.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.camunda.bpm.client.impl.EngineClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.topic.impl.dto.TopicRequestDto;
import org.springframework.stereotype.Service;

@Service
public class ManualWorker {

  private final EngineClient client;
  private final String TOPIC_NAME = "ManualTopic";
  private final long LOCK_DURATION = 10000;
  private final long TIME_TILL_NEXT_POLL = 5000;
  private final int retries = 3;


  public ManualWorker(EngineClient client) {
    this.client = client;
  }

  public void execute() {
    TopicRequestDto requestDto = new TopicRequestDto(TOPIC_NAME, LOCK_DURATION,
        Collections.emptyList(), null);
    List<ExternalTask> tasks = client.fetchAndLock(List.of(requestDto));
    if (tasks == null || tasks.isEmpty()) {
      return;
    }
    ExternalTask task = tasks.getFirst();
    try {
      System.out.println("Task: " + task.getId());
      client.complete(task.getId(), new HashMap<>(), new HashMap<>());
    } catch (Exception e) {
      client.failure(
          task.getId(),
          "Error during logging",
          "further information",
          getRetries(task),
          TIME_TILL_NEXT_POLL,
          new HashMap<>(),
          new HashMap<>());
    }
  }

  private int getRetries(ExternalTask task) {
    if (task.getRetries() == null) {
      return retries;
    }
    return task.getRetries() - 1;
  }

}
