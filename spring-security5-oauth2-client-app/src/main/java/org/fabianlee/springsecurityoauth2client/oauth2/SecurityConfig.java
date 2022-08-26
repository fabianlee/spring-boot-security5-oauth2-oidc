/**
 * For Spring Security 5.7.1+ OR Spring Boot 2.7.0+, WebSecurityConfigurerAdapter is DEPRECATED !!!!
 *  Configure through component based security configuration as shown here
 * 
 * https://www.springcloud.io/post/2022-02/spring-security-deprecate-websecurityconfigureradapter
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
package org.fabianlee.springsecurityoauth2client.oauth2;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests ->
			{
				try {
					authorizeRequests
					.antMatchers("/","/index","/info","/infojson","/login/oauth2/code/**").permitAll()
					.anyRequest().authenticated()
					.and().oauth2Login()
						.defaultSuccessUrl("/",true)
						.redirectionEndpoint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			);
		return http.build();
	}


	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**","/images/**");
    }
	
}