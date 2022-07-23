package example.before;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, FilterInvocation object, Collection collection) {
		if (authentication == null) {
			return mapToVote(false);
		}
		if (object.getRequestUrl().equals("/read")) {
			return mapToVote(authentication.getAuthorities().contains(new SimpleGrantedAuthority("read")));
		}
		if (object.getRequestUrl().equals("/write")) {
			return mapToVote(authentication.getAuthorities().contains(new SimpleGrantedAuthority("write")));
		}
		return mapToVote(authentication.isAuthenticated() && !this.trustResolver.isAnonymous(authentication));
	}

	private int mapToVote(Boolean granted) {
		if (granted == null) {
			return ACCESS_ABSTAIN;
		}
		if (granted) {
			return ACCESS_GRANTED;
		}
		return ACCESS_DENIED;
	}

}
