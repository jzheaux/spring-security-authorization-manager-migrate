package example.after;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

	@Bean
	CustomAuthorizationManager authorizationManager(HandlerMappingIntrospector introspector) {
		return new CustomAuthorizationManager(RequestMatcherDelegatingAuthorizationManager.builder()
				.add(new MvcRequestMatcher(introspector, "/read"), AuthorityAuthorizationManager.hasAuthority("read"))
				.add(new MvcRequestMatcher(introspector, "/write"), AuthorityAuthorizationManager.hasAuthority("write"))
				.add(AnyRequestMatcher.INSTANCE, AuthenticatedAuthorizationManager.authenticated())
				.build());
	}

	@Bean
	SecurityFilterChain web(HttpSecurity http, CustomAuthorizationManager access) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests.anyRequest().access(access))
			.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
		UserDetails admin = User.withUsername("admin").password("{noop}password").authorities("read", "write").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}
