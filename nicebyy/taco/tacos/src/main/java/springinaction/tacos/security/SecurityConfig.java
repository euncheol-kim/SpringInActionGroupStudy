package springinaction.tacos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * 사용자스토어 커스터마이징
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/design", "/orders")
                    .hasRole("USER")
                    .antMatchers("/", "/**").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
//                    .defaultSuccessUrl("/design",true)
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                .and()
                    .csrf().disable();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    /**
     * LDAP 기반 사용자 스토어
     */
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0})")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .contextSource()
                .root("dc=taco,dc=com")
                .ldif("classpath:users.ldif")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasscode");
    }
     */

    /**
     * JDBC 기반 사용자 스토어
     */
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("""
                        select username, password, enabled 
                        from users 
                        where username=?
                        """)
                .authoritiesByUsernameQuery("""
                        select username, authority 
                        from authorities 
                        where username=?
                        """)
                .passwordEncoder(new NoEncodingPasswordEncoder());
    }
     */

    /**
     * 인메모리 기반 사옹자 스토어
     */
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user1")
                .password("{noop}password1")
                .authorities("ROLE_USER")
                .and()
                .withUser("user2")
                .password("{noop}password2")
                .authorities("ROLE_USER");
    }

     */

}
