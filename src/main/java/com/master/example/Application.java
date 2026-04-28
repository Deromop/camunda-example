package com.master.example;

import com.master.external.ManualWorker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = "com.master")
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @Profile("MANUAL_WORKER")
  public CommandLineRunner commandLineRunner(ManualWorker worker, ApplicationContext ctx) {

    return args -> {
      final int[] exitCode = {0};
      try {
        worker.execute();
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        exitCode[0] = 1;
      }
      SpringApplication.exit(ctx, () -> exitCode[0]);
    };
  }

}