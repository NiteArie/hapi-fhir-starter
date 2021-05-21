package com.heathcare.lab.hapistarter;

import com.heathcare.lab.hapistarter.entity.PatientEntity;
import com.heathcare.lab.hapistarter.servlet.FHIRRestfulServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackageClasses = PatientEntity.class)
@ServletComponentScan
public class HapiStarterApplication {
  public static void main(String[] args) {
    SpringApplication.run(HapiStarterApplication.class, args);
  }

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public ServletRegistrationBean ServletRegistrationBean() {
    ServletRegistrationBean registration= new ServletRegistrationBean(new FHIRRestfulServer(applicationContext),"/r4-postgres/*");
    registration.setName("FhirServlet");
    return registration;
  }
}
