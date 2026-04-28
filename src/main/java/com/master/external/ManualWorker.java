package com.master.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.camunda.community.rest.client.api.ExternalTaskApi;
import org.camunda.community.rest.client.dto.CompleteExternalTaskDto;
import org.camunda.community.rest.client.dto.ExternalTaskFailureDto;
import org.camunda.community.rest.client.dto.FetchExternalTaskTopicDto;
import org.camunda.community.rest.client.dto.FetchExternalTasksDto;
import org.camunda.community.rest.client.dto.LockedExternalTaskDto;
import org.camunda.community.rest.client.invoker.ApiException;
import org.springframework.stereotype.Service;

@Service
public class ManualWorker {

  private final ExternalTaskApi externalTaskApi;
  private final String TOPIC_NAME = "ManualTopic";
  private final long LOCK_DURATION = 10000;
  private final long TIME_TILL_NEXT_POLL = 5000;
  private final int retries = 3;


  public ManualWorker(ExternalTaskApi externalTaskApi) {
    this.externalTaskApi = externalTaskApi;
  }

  public void execute() throws ApiException {
    FetchExternalTasksDto fetchExternalTasksDto = getFetchExternalTasksDto();
    List<LockedExternalTaskDto> tasks = externalTaskApi.fetchAndLock(fetchExternalTasksDto);
    if (tasks == null || tasks.isEmpty()) {
      return;
    }
    LockedExternalTaskDto task = tasks.getFirst();
    try {
      System.out.println("Task: " + task.getId());
      externalTaskApi.completeExternalTaskResource(task.getId(), getCompleteExternalTaskDto());
    } catch (Exception e) {
      externalTaskApi.handleFailure(task.getId(), getExternalTaskFailureDto(task));
    }
  }

  private ExternalTaskFailureDto getExternalTaskFailureDto(LockedExternalTaskDto task){
    ExternalTaskFailureDto externalTaskFailureDto = new ExternalTaskFailureDto();
    externalTaskFailureDto.setRetries(getRetries(task));
    externalTaskFailureDto.setRetryTimeout(TIME_TILL_NEXT_POLL);
    externalTaskFailureDto.setErrorMessage("Error");
    return externalTaskFailureDto;
  }

  private CompleteExternalTaskDto getCompleteExternalTaskDto(){
    CompleteExternalTaskDto completeExternalTaskDto = new CompleteExternalTaskDto();
    completeExternalTaskDto.setWorkerId(this.getClass().getSimpleName());
    completeExternalTaskDto.setVariables(new HashMap<>());
    return completeExternalTaskDto;
  }

  private FetchExternalTasksDto getFetchExternalTasksDto(){
    FetchExternalTasksDto fetchExternalTasksDto = new FetchExternalTasksDto();
    fetchExternalTasksDto.setMaxTasks(1);
    fetchExternalTasksDto.setWorkerId(this.getClass().getSimpleName());
    FetchExternalTaskTopicDto topicDto = new FetchExternalTaskTopicDto();
    topicDto.setTopicName(TOPIC_NAME);
    topicDto.setLockDuration(LOCK_DURATION);
    fetchExternalTasksDto.setTopics(Collections.singletonList(topicDto));
    return fetchExternalTasksDto;
  }

  private int getRetries(LockedExternalTaskDto task) {
    if (task == null || task.getRetries() == null) {
      return retries;
    }
    return task.getRetries() - 1;
  }

}
