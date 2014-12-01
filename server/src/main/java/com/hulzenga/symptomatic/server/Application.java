package com.hulzenga.symptomatic.server;

import com.fasterxml.jackson.databind.Module;
import com.hulzenga.symptomatic.common.java.json.SymptomaticModule;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;


/**
 * Created by jouke on 11/4/14.
 */
@EnableAutoConfiguration
@Configuration()
@EnableWebMvc
@ComponentScan
@EntityScan("com.hulzenga.symptomatic.common.java.model")
public class Application {

  private final int port = 8080;

  public static void main(String[] args) {

    SpringApplication app = new SpringApplication(Application.class);

    app.setShowBanner(true);

    ConfigurableApplicationContext context = app.run(args);
  }

//  @Bean
//  EmbeddedServletContainerCustomizer containerCustomizer(
//      @Value("${keystore.file:src/main/resources/private/keystore}") String keystoreFile,
//      @Value("${keystore.pass:changeit}") final String keystorePass) throws Exception {
//
//    final String absoluteKeystoreFile = new File(keystoreFile).getAbsolutePath();
//
//    return new EmbeddedServletContainerCustomizer() {
//
//      @Override
//      public void customize(ConfigurableEmbeddedServletContainer container) {
//        TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
//        tomcat.addConnectorCustomizers(
//            new TomcatConnectorCustomizer() {
//              @Override
//              public void customize(Connector connector) {
//                connector.setPort(8443);
//                connector.setSecure(true);
//                connector.setScheme("https");
//
//                Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
//                proto.setSSLEnabled(true);
//                proto.setKeystoreFile(absoluteKeystoreFile);
//                proto.setKeystorePass(keystorePass);
//                proto.setKeystoreType("JKS");
//                proto.setKeyAlias("tomcat");
//              }
//            });
//      }
//    };
//  }

  @Bean
  public Module customJacksonModule() {
    return new SymptomaticModule();
  }
}
