package de.codecentric.hc.track.jwt;

import de.codecentric.hc.track.habits.validation.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * An implementation of a strategy interface for resolving HTTP headers and converting them into a user ID in
 * the context of a given request.
 * <br>
 * <br>
 * If {@link HttpHeaders#AUTHORIZATION} is present, user ID is extracted from potentially existing JWT.
 */
@Component
public class AuthHeaderArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UserId.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && isBearerToken(authorizationHeader)) {
            String token = extractTokenFrom(authorizationHeader);
            Jwt decodedJwt = jwtDecoder.decode(token);
            return getUserIdOf(decodedJwt);
        } else return null;
    }

    private boolean isBearerToken(String authorizationHeader) {
        return authorizationHeader.startsWith("Bearer ");
    }

    private String extractTokenFrom(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private String getUserIdOf(Jwt decodedJwt) {
        return decodedJwt.getClaimAsString("sub");
    }
}
