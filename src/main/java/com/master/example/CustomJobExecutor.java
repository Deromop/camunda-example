package com.master.example;

import java.util.List;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;

public class CustomJobExecutor extends JobExecutor {

  @Override
  protected void startExecutingJobs() {

  }

  @Override
  protected void stopExecutingJobs() {

  }

  @Override
  public void executeJobs(List<String> list, ProcessEngineImpl processEngine) {

  }
}