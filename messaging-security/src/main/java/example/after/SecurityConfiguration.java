package example.after;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSocketSecurity
public class SecurityConfiguration {

	@Bean
	public AuthorizationManager<Message<?>> messageSecurity(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
		return messages
			.simpDestMatchers("/read").hasAuthority("read")
			.simpDestMatchers("/write").hasAuthority("write")
			.anyMessage().permitAll()
			.build();
	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
		UserDetails admin = User.withUsername("admin").password("{noop}password").authorities("read", "write").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}
