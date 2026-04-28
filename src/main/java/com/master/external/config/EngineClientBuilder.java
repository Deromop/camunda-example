package com.master.external.config;

import org.camunda.community.rest.client.api.ExternalTaskApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineClientBuilder {

  @Bean
  ExternalTaskApi externalTaskApi() {
    return new ExternalTaskApi();
  }



}
