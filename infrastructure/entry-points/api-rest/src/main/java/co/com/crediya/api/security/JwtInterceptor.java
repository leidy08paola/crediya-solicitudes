package co.com.crediya.api.security;

import co.com.crediya.model.usuario.AuthenticatedUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String ATTRIBUTE_NAME = "authenticatedUser";

    private final JwtVerifier jwtVerifier;

    public JwtInterceptor(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        AuthenticatedUser authenticatedUser = jwtVerifier.verify(authorization);
        AuthenticatedUser withToken = new AuthenticatedUser(
                authenticatedUser.correoElectronico(),
                authenticatedUser.rol(),
                authenticatedUser.documentoIdentidad(),
                authorization);
        request.setAttribute(ATTRIBUTE_NAME, withToken);
        return true;
    }

    public static AuthenticatedUser getAuthenticatedUser(HttpServletRequest request) {
        return (AuthenticatedUser) request.getAttribute("authenticatedUser");
    }
}
