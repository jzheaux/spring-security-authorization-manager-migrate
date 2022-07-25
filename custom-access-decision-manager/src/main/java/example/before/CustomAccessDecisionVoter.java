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
			return ACCESS_ABSTAIN;
		}
		if (!"admin".equals(authentication.getName())) {
			return ACCESS_ABSTAIN;
		}
		return ACCESS_GRANTED;
	}
}
