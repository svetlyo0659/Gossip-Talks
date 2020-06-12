
package bg.codeacademy.spring.gossiptalks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{


  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    http
        .authorizeRequests()
        // allow users to register
        .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
        // all other API are authenticated
        .antMatchers("/api/v1/**").authenticated()
        .and()
        .httpBasic();

    http.csrf().disable();
    http.headers().frameOptions().disable();
  }


  @Bean
  PasswordEncoder passwordEncoder()
  {
    return new BCryptPasswordEncoder();
  }

}
