package example.before;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {
		http
			.authorizeRequests((requests) -> requests
				.mvcMatchers("/read").hasAuthority("read")
				.mvcMatchers("/write").hasAuthority("write")
				.anyRequest().authenticated()
			)
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
