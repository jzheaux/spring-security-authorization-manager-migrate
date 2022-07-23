package example.after;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	private final Map<RequestMatcher, AuthorizationManager<RequestAuthorizationContext>> matchers = new LinkedHashMap<>();

	public CustomAuthorizationManager() {
		this.matchers.put(new AntPathRequestMatcher("/read"), AuthorityAuthorizationManager.hasAuthority("read"));
		this.matchers.put(new AntPathRequestMatcher("/write"), AuthorityAuthorizationManager.hasAuthority("write"));
		this.matchers.put(AnyRequestMatcher.INSTANCE, AuthenticatedAuthorizationManager.authenticated());
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
		for (RequestMatcher matcher : this.matchers.keySet()) {
			if (matcher.matches(object.getRequest())) {
				return this.matchers.get(matcher).check(supplier, object);
			}
		}
		return new AuthorizationDecision(false);
	}

}
