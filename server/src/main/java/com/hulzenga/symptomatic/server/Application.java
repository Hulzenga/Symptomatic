package com.hulzenga.symptomatic.server;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;
import com.hulzenga.symptomatic.common.java.json.serializer.SymptomStateMapSerializer;

import org.h2.server.web.DbStarter;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;


/**
 * Created by jouke on 11/4/14.
 */
@EnableAutoConfiguration
@Configuration()
@EnableWebMvc //todo remove don't think needed
@ComponentScan
@EntityScan("com.hulzenga.symptomatic.common.java.model")
public class Application {

  private final int port = 8080;

  public static void main(String[] args) {

    SpringApplication app = new SpringApplication(Application.class);

    app.setShowBanner(true);

    ConfigurableApplicationContext context = app.run(args);
  }


  @Bean
  public ServletRegistrationBean console() {
    DbStarter starter = new DbStarter();
    starter.getConnection();

    return new ServletRegistrationBean(new WebServlet(), "/console");
  }

  @Bean
  public Module customJacksonModule() {
    return new SymptomaticModule();
  }
}
