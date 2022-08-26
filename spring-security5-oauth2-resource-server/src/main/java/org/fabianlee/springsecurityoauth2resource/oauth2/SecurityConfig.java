/**
 * For Spring Security 5.7.1+ OR Spring Boot 2.7.0+, WebSecurityConfigurerAdapter is DEPRECATED !!!!
 * Configure through component based security configuration as shown here
 * 
 * https://www.springcloud.io/post/2022-02/spring-security-deprecate-websecurityconfigureradapter
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 */
package org.fabianlee.springsecurityoauth2resource.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/", "/info", "/infojson", "/testjwt", "login**").permitAll()
				.anyRequest().authenticated();
		
		// add custom JWT converter that puts both SCOPE_ and ROLE_ into authorities
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()));
		
		return http.build();

	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
				.addMapping("/**")
				.allowedOrigins("http://localhost:8080")
				.allowCredentials(true)
				.allowedHeaders("Authorization","WWW-Authenticate") // is not sending to client at all!!!! in older Filter style only had 'Authorization' 
				.exposedHeaders("WWW-Authenticate","Content-Type")
				.allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
			}
		};
	}

	//https://stackoverflow.com/questions/58205510/spring-security-mapping-oauth2-claims-with-roles-to-secure-resource-server-endp
	private JwtAuthenticationConverter jwtAuthenticationConverter() {

		// use converter that puts both SCOPE_ and ROLE_ into authorities
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter());
		return jwtAuthenticationConverter;
	}

	// https://github.com/spring-projects/spring-security/issues/10439 (jzheaux Josh
	// Cummings)
	public DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter() {
		JwtGrantedAuthoritiesConverter scp = new JwtGrantedAuthoritiesConverter();
		// these are defaults and do not need to be set
		// scp.setAuthoritiesClaimName("scp");
		// scp.setAuthorityPrefix("SCOPE_");
		JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
		roles.setAuthoritiesClaimName("group");
		roles.setAuthorityPrefix("ROLE_");
		return new DelegatingJwtGrantedAuthoritiesConverter(scp, roles);
	}

} // class