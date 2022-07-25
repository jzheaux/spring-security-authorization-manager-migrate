package example.after;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	private final RequestMatcherDelegatingAuthorizationManager authorizationManager;
	private final AuthorizationManager<RequestAuthorizationContext> admin = (supplier, object) ->
			new AuthorizationDecision("admin".equals(supplier.get().getName()));

	public CustomAuthorizationManager(RequestMatcherDelegatingAuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
		AuthorizationDecision decision = this.authorizationManager.check(supplier, object.getRequest());
		if (decision != null && decision.isGranted()) {
			return decision;
		}
		return this.admin.check(supplier, object);
	}

}
