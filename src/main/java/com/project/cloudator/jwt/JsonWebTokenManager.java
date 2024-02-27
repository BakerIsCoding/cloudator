package com.project.cloudator.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JsonWebTokenManager {
    // Asegúrate de usar una clave secreta segura y única para tu aplicación
    private final String SECRET_KEY = "Marc_Eduardo_Hector_INIT_KEY_K@1wZt7!8hY$Vn3#Fx@G*qNcY%J^6&SbPmD+9OeLtF-Ku3CjI;RbQ<zE#8uVQwG9*hXmZnAs!3pBvMc2_LxU,oD~6Ik!7P.4W";
    private final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    /**
     * Crea un token JWT con un payload básico.
     *
     * @param subject El sujeto para quien el token es válido.
     * @return Un token JWT como String.
     */
    public String createToken(String subject, Integer id, String issuer) {
        return JWT.create()
                .withSubject(subject)
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hora de validez
                .withIssuer(issuer)
                .sign(ALGORITHM);
    }

}
