package tacos.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

/*
        deprecated
           WebSecurityConfigurerAdapter

 */

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('USER')")
                .antMatchers("/", "/**")
                .access("permitAll")
                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        ArrayList<UserDetails> userList = new ArrayList<>();
        UserDetails user = User.withUsername("user1")
                .password("{noop}password")
                .roles("USER")
                .build();
        userList.add(user);

        user = User.withUsername("user2")
                .password("{noop}password")
                .roles("USER")
                .build();
        userList.add(user);

        return new InMemoryUserDetailsManager(userList);

    }
}








//        UserDetails user = User.withDefaultPasswordEncoder()
//        authBuilder.inMemoryAuthentication()
//                .withUser("user1")
//                .password("{noop}password1")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("user2")
//                .password("{noop}password1")
//                .authorities("ROLE_USER");