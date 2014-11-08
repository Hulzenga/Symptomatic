package com.hulzenga.symptomatic.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Created by jouke on 11/4/14.
 */
@EnableAutoConfiguration
@Configuration()
@ComponentScan
@EntityScan("com.hulzenga.symptomatic.model")
public class Application {

  public static void main(String[] args) {

    ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

  }
}
