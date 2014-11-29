package com.hulzenga.symptomatic.server.security;

import com.hulzenga.symptomatic.common.java.api.ServerSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Configuration
public class OAuth2SecurityConfiguration {

  private static final String AUTH_PATIENT = "patient";
  private static final String AUTH_DOCTOR = "doctor";

  @Configuration
  @EnableWebSecurity
  @Order(Ordered.LOWEST_PRECEDENCE)
  protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    protected void registerAuthentication(
        final AuthenticationManagerBuilder auth) throws Exception {

      auth.userDetailsService(userDetailsService);
    }
  }

  @Configuration
  @EnableResourceServer
  protected static class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

      http.csrf().disable();

      http
          .authorizeRequests()
          .antMatchers("/oauth/token").anonymous();

      http
          .authorizeRequests()
          .antMatchers("/patient/**").hasAuthority(AUTH_PATIENT);

      http
          .authorizeRequests()
          .antMatchers("/doctor/**").hasAuthority(AUTH_DOCTOR);
//      http
//          .authorizeRequests()
//          .antMatchers("/patient/**")
//          .hasAuthority(AUTH_PATIENT);

    }
  }

  @Configuration
  @EnableAuthorizationServer
  @Order(Ordered.LOWEST_PRECEDENCE + 100)
  protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    // Delegate the processing of Authentication requests to the framework
    @Autowired
    private AuthenticationManager authenticationManager;

    // A data structure used to store both a ClientDetailsService and a UserDetailsService
    private ClientAndUserDetailsService combinedService_;

    public OAuth2Config() throws Exception {

      ClientDetailsService csvc = new InMemoryClientDetailsServiceBuilder()
          .withClient(ServerSettings.PATIENT_CLIENT).secret(ServerSettings.PATIENT_CLIENT_SECRET)
          .authorizedGrantTypes("password")
          .authorities(AUTH_PATIENT)
          .scopes("check-in", "get_own_data")
          .accessTokenValiditySeconds(Integer.MAX_VALUE).and().build();

//      UserDetailsService svc = new InMemoryUserDetailsManager(
//          Arrays.asList(
//              User.create(User.UserType.PATIENT, "alice", "ecila", AUTH_DOCTOR),
//              User.create(User.UserType.PATIENT, "bob", "bob", AUTH_PATIENT)));

      UserDetailsService svc = new InMemoryUserDetailsManager(
          Arrays.asList(
              User2.create("admin", "pass", "ADMIN", "USER"),
              User2.create("user0", "pass", "USER")));

      // Since clients have to use BASIC authentication with the client's id/secret,
      // when sending a request for a password grant, we make each client a user
      // as well. When the BASIC authentication information is pulled from the
      // request, this combined UserDetailsService will authenticate that the
      // client is a valid "user".
      combinedService_ = new ClientAndUserDetailsService(csvc, svc);
    }

    /**
     * Return the list of trusted client information to anyone who asks for it.
     */
    @Bean
    public ClientDetailsService clientDetailsService() throws Exception {
      return combinedService_;
    }

    /**
     * Return all of our user information to anyone in the framework who requests it.
     */
    @Bean
    public UserDetailsService userDetailsService() {
      return combinedService_;
    }

    /**
     * This method tells our AuthorizationServerConfigurerAdapter to use the delegated AuthenticationManager
     * to process authentication requests.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
        throws Exception {
      endpoints.authenticationManager(authenticationManager);
    }

    /**
     * This method tells the AuthorizationServerConfigurerAdapter to use our self-defined client details service to
     * authenticate clients with.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
        throws Exception {

      clients.withClientDetails(clientDetailsService());
    }

  }
}
