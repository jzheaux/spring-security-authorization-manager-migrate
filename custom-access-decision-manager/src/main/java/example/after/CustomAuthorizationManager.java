package example.after;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	private final AuthorizationManager<HttpServletRequest> authorizationManager;
	private final AuthorizationManager<HttpServletRequest> admin = (supplier, object) ->
			new AuthorizationDecision("admin".equals(supplier.get().getName()));

	public CustomAuthorizationManager(RequestMatcherDelegatingAuthorizationManager authorizationManager) {
		this.authorizationManager = (supplier, object) -> {
			AuthorizationDecision decision = authorizationManager.check(supplier, object);
			if (decision != null && decision.isGranted()) {
				return decision;
			}
			return this.admin.check(supplier, object);
		};
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
		return this.authorizationManager.check(supplier, object.getRequest());
	}

}
