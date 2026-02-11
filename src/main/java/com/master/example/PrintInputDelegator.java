package com.master.example;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class PrintInputDelegator implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    String input = (String) delegateExecution.getVariable("userinput");
    System.out.println(input);
  }
}




