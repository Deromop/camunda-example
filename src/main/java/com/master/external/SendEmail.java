package com.master.external;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

@ExternalTaskSubscription("SendEmail")
public class SendEmail implements ExternalTaskHandler {

  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    externalTask.getVariable("someVariable");

    externalTaskService.complete(externalTask);

  }
}
