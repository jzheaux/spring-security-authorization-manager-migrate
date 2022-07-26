package example.before;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain web(HttpSecurity http, CustomAccessDecisionVoter voter) throws Exception {
		http
			.authorizeRequests((requests) -> requests
				.mvcMatchers("/read").hasAuthority("read")
				.mvcMatchers("/write").hasAuthority("write")
				.anyRequest().authenticated()
				.accessDecisionManager(new AffirmativeBased(Arrays.asList(voter, new WebExpressionVoter())))
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
