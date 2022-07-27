package example.before;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
public class SecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").withSockJS();
	}

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				.simpDestMatchers("/read").hasAuthority("read")
				.simpDestMatchers("/write").hasAuthority("write")
				.anyMessage().authenticated();
	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withUsername("user").password("{noop}password").authorities("read").build();
		UserDetails admin = User.withUsername("admin").password("{noop}password").authorities("read", "write").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}
