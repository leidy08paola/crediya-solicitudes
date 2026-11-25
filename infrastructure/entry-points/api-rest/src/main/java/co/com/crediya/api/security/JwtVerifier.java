package co.com.crediya.api.security;

import co.com.crediya.model.user.Role;
import co.com.crediya.model.usuario.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtVerifier {

    @Value("${jwt.secret}")
    private String secret;

    public AuthenticatedUser verify(String authorization) {
        String token = extractBearerToken(authorization);
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Token inválido");
        }
        if (!signatureIsValid(parts)) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Firma de token inválida");
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        return buildAuthenticatedUser(payload, token);
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Token no proporcionado");
        }
        return authorization.substring("Bearer ".length()).trim();
    }

    private boolean signatureIsValid(String[] parts) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] expected = mac.doFinal((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));
            byte[] received = Base64.getUrlDecoder().decode(parts[2]);
            return MessageDigest.isEqual(expected, received);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Error validando la firma del token", ex);
            return false;
        }
    }

    private AuthenticatedUser buildAuthenticatedUser(String payload, String token) {
        Long exp = extractLongClaim(payload, "exp");
        if (exp == null || exp < System.currentTimeMillis() / 1000) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Token expirado o sin exp");
        }
        String sub = extractStringClaim(payload, "sub");
        if (sub == null) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Token sin claim sub");
        }
        String rol = extractStringClaim(payload, "rol");
        String doc = extractStringClaim(payload, "doc");
        try {
            return new AuthenticatedUser(sub, rol != null ? Role.valueOf(rol) : null, doc, token);
        } catch (IllegalArgumentException ex) {
            throw new AuthException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Claim rol inválido en token");
        }
    }

    private String extractStringClaim(String payload, String claim) {
        Matcher matcher = Pattern.compile("\"" + claim + "\":\\s*\"([^\"]+)\"").matcher(payload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long extractLongClaim(String payload, String claim) {
        Matcher matcher = Pattern.compile("\"" + claim + "\":\\s*(\\d+)").matcher(payload);
        if (!matcher.find()) {
            return null;
        }
        try {
            return Long.parseLong(matcher.group(1));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
