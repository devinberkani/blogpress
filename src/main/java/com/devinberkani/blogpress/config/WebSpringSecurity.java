package com.devinberkani.blogpress.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
     This code is defining a Spring Security configuration class for a web application.

    Here's a breakdown of what the code is doing:

    Defining a configuration class: The code defines a Spring configuration class called WebSpringSecurity. This class is annotated with @Configuration and @EnableWebSecurity, which enables Spring Security's web security support and provides integration with Spring MVC.

    Injecting user details service: The code injects a UserDetailsService object using the constructor injection.

    Defining a password encoder: The code defines a BCryptPasswordEncoder object as a PasswordEncoder bean using the @Bean annotation. This object is used to encode and decode passwords for user authentication.

    Configuring security filters: The code defines a SecurityFilterChain bean using the filterChain() method. This bean defines security filters for incoming requests.

    a. The method first disables CSRF protection using http.csrf().disable().

    b. The authorizeHttpRequests() method is used to define authorization rules for incoming requests. Requests to static resources under /css/ and all paths starting with /register/ and /post/ are permitted for all users. Requests to paths starting with /admin/ are required to be authenticated and must have the ADMIN or GUEST role. All other requests are required to be authenticated.

    c. The formLogin() method is used to define a form-based authentication mechanism for the application. The login page is set to /login, the default success URL is set to /admin/posts, and the login processing URL is set to /login.

    d. The logout() method is used to define a logout mechanism for the application. The logout request URL is set to /logout.

    e. The method returns the built SecurityFilterChain object.

    Configuring authentication: The code defines an AuthenticationManagerBuilder object using the configureGlobal() method. This object is used to configure user authentication.

    a. The userDetailsService() method is used to set the UserDetailsService object defined in step 2.

    b. The passwordEncoder() method is used to set the PasswordEncoder object defined in step 3.

    In summary, this code defines a Spring Security configuration class for a web application that includes authorization rules, a form-based authentication mechanism, and a logout mechanism. It also configures user authentication using a UserDetailsService object and a BCryptPasswordEncoder object.

*/

@Configuration
@EnableWebSecurity // used to enable spring security's web security support and provide the spring mvc integration
public class WebSpringSecurity {

    private final UserDetailsService userDetailsService;

    public WebSpringSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/register/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                .hasAnyRole("ADMIN", "GUEST")
                                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/post/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/page/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/**/comments")).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/posts")
                        .loginProcessingUrl("/login")
                        .permitAll()
                ).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
