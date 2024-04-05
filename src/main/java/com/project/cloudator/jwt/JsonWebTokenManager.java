package com.project.cloudator.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.project.cloudator.service.SecurityService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class JsonWebTokenManager {

    @Value("${secretencryptor}")
    private String SECRET_KEY_ENCRYPTOR;

    @Value("${secretkey}")
    private String SECRET_KEY;
    private Algorithm ALGORITHM;

    @PostConstruct
    public void init() {
        ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    }

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

    public ArrayList<String> decodeJwt(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer("FILESERVER")
                    .build();

            // Verificar y decodificar el token
            DecodedJWT jwt = verifier.verify(token);

            ArrayList<String> arrayItems = new ArrayList<String>();

            SecurityService securityService = new SecurityService(SECRET_KEY_ENCRYPTOR);

            String decryptedFileName = securityService.decryptData(jwt.getClaim("filename").asString());
            String decryptedFileType = securityService.decryptData(jwt.getClaim("filetype").asString());
            String decryptedFileRoute = securityService.decryptData(jwt.getClaim("fileroute").asString());
            String decryptedFileDate = securityService.decryptData(jwt.getClaim("filedate").asString());
            String decryptedFileSize = securityService.decryptData(jwt.getClaim("filesize").asString());
            String decryptedOwner = securityService.decryptData(jwt.getClaim("owner").asString());
            String decryptedIsPublic = securityService.decryptData(jwt.getClaim("ispublic").asString());
            String decryptedUrl = securityService.decryptData(jwt.getClaim("url").asString());

            arrayItems.add(decryptedFileName);
            arrayItems.add(decryptedFileType);
            arrayItems.add(decryptedFileRoute);
            arrayItems.add(decryptedFileDate);
            arrayItems.add(decryptedFileSize);
            arrayItems.add(decryptedOwner);
            arrayItems.add(decryptedIsPublic);
            arrayItems.add(decryptedUrl);

            return arrayItems;
        } catch (JWTVerificationException exception) {
            System.out.println("Token inválido: " + exception.getMessage());
            System.out.println("Token inválido: " + token);
        }
        return null;
    }

}
