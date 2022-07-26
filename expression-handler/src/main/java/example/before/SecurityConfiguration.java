package example.before;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {
		http
			.authorizeRequests((requests) -> requests.anyRequest().authenticated())
			.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl impl = new RoleHierarchyImpl();
		impl.setHierarchy("write > read");
		return impl;
	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
		UserDetails admin = User.withUsername("admin").password("{noop}password").authorities("write").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}
