package com.heathcare.lab.hapistarter;

import com.heathcare.lab.hapistarter.domain.entities.PatientEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@EntityScan(basePackageClasses = PatientEntity.class)
@ServletComponentScan
public class HapiStarterApplication {

  public static void main(String[] args) {
    SpringApplication.run(HapiStarterApplication.class, args);
  }

}
